package jaxvanyang.poker.item;

import jaxvanyang.poker.entity.CardDeckEntity;
import jaxvanyang.poker.platform.Services;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.context.UseOnContext;

import java.util.Arrays;
import java.util.List;

public class CardDeck extends Item {
    public static final int DECK_SIZE = 52;

    public CardDeck(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static ItemStack newStack(byte color, byte[] deck) {
        assert deck.length != 0;
        var stack = new ItemStack(Services.PLATFORM.getCardDeckItem(color));
        // use duability to indicate the number of cards left in the deck
        stack.set(DataComponents.MAX_DAMAGE, DECK_SIZE);
        setDeck(stack, deck);

        return stack;
    }

    public static byte[] getDeck(ItemStack stack) {
        var data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) {
            return newSortedDeck();
        }

        return data.copyTag().getByteArray("deck").orElse(newSortedDeck());
    }

    public static void setDeck(ItemStack stack, byte[] deck) {
        var tags = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        tags.putByteArray("deck", deck);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tags));
        stack.setDamageValue(DECK_SIZE - deck.length);

        if (deck.length == DECK_SIZE) {
            stack.set(DataComponents.LORE, ItemLore.EMPTY);
            return;
        }

        var missing = new boolean[DECK_SIZE + 1];
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
        var deck = newSortedDeck();
        // shuffle the deck
        for (int i = 0; i < DECK_SIZE; i++) {
            int j = (int) (Math.random() * DECK_SIZE);
            var tmp = deck[i];
            deck[i] = deck[j];
            deck[j] = tmp;
        }
        return deck;
    }

    public static byte[] newSortedDeck() {
        var deck = new byte[DECK_SIZE];
        for (byte i = 0; i < DECK_SIZE; i++) {
            deck[i] = (byte) (i + 1);
        }
        return deck;
    }

    public byte getColor() {
        var id = BuiltInRegistries.ITEM.getKey(this).toString();
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
        var level = context.getLevel();

        // TODO: only place on the ground
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        var stack = context.getItemInHand();
        var color = getColor();
        var deck = getDeck(stack);
        var entity = new CardDeckEntity(
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
