package jaxvanyang.poker.item;

import jaxvanyang.poker.entity.CardDeckEntity;
import jaxvanyang.poker.platform.Services;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.Arrays;
import java.util.List;

import static jaxvanyang.poker.Common.*;

public class CardDeck extends Item {
    public static final int DECK_SIZE = 52;

    public CardDeck(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) {
            return;
        }

        CompoundTag tags = data.copyTag();
        String loreVersion = tags.getString(LORE_VERSION_KEY).orElse("");
        if (!LORE_VERSION.equals(loreVersion)) {
            LOG.warn("Updating lore for card deck item from version {} to {}", loreVersion, LORE_VERSION);
            stack.remove(DataComponents.LORE);
            tags.putString(LORE_VERSION_KEY, LORE_VERSION);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tags));
            setLore(stack, getDeck(stack));
        }
    }

    public static ItemStack newStack(byte color, byte[] deck) {
        assert deck.length != 0;
        ItemStack stack = new ItemStack(Services.PLATFORM.getCardDeckItem(color));
        // use duability to indicate the number of cards left in the deck
        stack.set(DataComponents.MAX_DAMAGE, DECK_SIZE);
        setDeck(stack, deck);

        return stack;
    }

    public static byte[] getDeck(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        byte[] deck;
        if (data == null) {
            deck = newSortedDeck();
        } else {
            deck = data.copyTag().getByteArray("deck").get();
        }

        return deck;
    }

    public static void setDeck(ItemStack stack, byte[] deck) {
        CompoundTag tags = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        tags.putByteArray("deck", deck);
        tags.putString(LORE_VERSION_KEY, LORE_VERSION);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tags));
        stack.setDamageValue(DECK_SIZE - deck.length);

        setLore(stack, deck);
    }

    private static void setLore(ItemStack stack, byte[] deck) {
        if (deck.length == DECK_SIZE) {
            stack.remove(DataComponents.LORE);
            return;
        }

        boolean[] missing = new boolean[DECK_SIZE + 1];
        Arrays.fill(missing, true);
        for (byte b : deck) {
            missing[b] = false;
        }
        List<Component> lore = new java.util.ArrayList<>(List.of(Component.translatable(
                "item.just_poker.card_deck.missing")));
        for (byte suit = 0; suit < 4; ++suit) {
            StringBuilder line = new StringBuilder();
            for (byte rank = 1; rank <= 13; ++rank) {
                byte cardId = (byte) (suit * 13 + rank);
                if (missing[cardId]) {
                    if (!line.isEmpty()) {
                        line.append(",");
                    }
                    line.append(Card.get_rank_short(rank));
                }
            }

            if (!line.isEmpty()) {
                lore.add(Component.literal(Card.get_suit_unicode(suit) + " " + line));
            }
        }
        stack.set(DataComponents.LORE, new ItemLore(lore));
    }

    public static byte[] newRandomDeck() {
        byte[] deck = newSortedDeck();
        // shuffle the deck
        for (int i = 0; i < DECK_SIZE; i++) {
            int j = (int) (Math.random() * DECK_SIZE);
            byte tmp = deck[i];
            deck[i] = deck[j];
            deck[j] = tmp;
        }
        return deck;
    }

    public static byte[] newSortedDeck() {
        byte[] deck = new byte[DECK_SIZE];
        for (byte i = 0; i < DECK_SIZE; i++) {
            deck[i] = (byte) (i + 1);
        }
        return deck;
    }

    public byte getColor() {
        String id = BuiltInRegistries.ITEM.getKey(this).toString();
        byte color = -1;
        if (id.contains("black")) {
            color = 0;
        } else if (id.contains("blue")) {
            color = 1;
        } else if (id.contains("pink")) {
            color = 2;
        } else if (id.contains("red")) {
            color = 3;
        }
        assert color != -1;

        return color;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        // TODO: only place on the ground
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        ItemStack stack = context.getItemInHand();
        byte color = getColor();
        byte[] deck = getDeck(stack);

        CardDeckEntity entity = new CardDeckEntity(
                level,
                context.getClickLocation(),
                context.getRotation(),
                color,
                deck
        );
        level.addFreshEntity(entity);
        context.getItemInHand().shrink(1);

        return InteractionResult.SUCCESS;
    }
}
