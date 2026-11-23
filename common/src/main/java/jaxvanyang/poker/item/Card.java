package jaxvanyang.poker.item;

import jaxvanyang.poker.entity.CardDeckEntity;
import jaxvanyang.poker.entity.CardEntity;
import jaxvanyang.poker.platform.Services;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

import static jaxvanyang.poker.Common.LORE_VERSION;
import static jaxvanyang.poker.Common.LORE_VERSION_KEY;

public class Card extends Item {
    public Card(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) {
            return;
        }

        CompoundTag tags = data.copyTag();
        if (!LORE_VERSION.equals(tags.getString(LORE_VERSION_KEY).orElse(""))) {
            stack.remove(DataComponents.LORE);
            tags.putString(LORE_VERSION_KEY, LORE_VERSION);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tags));
            setLore(stack, getColor(stack));
        }
    }

    public static ItemStack newStack(byte color, byte cardId) {
        ItemStack stack = new ItemStack(Services.PLATFORM.getCardItem());
        CompoundTag tags = new CompoundTag();
        tags.putByte("color", color);
        tags.putString(LORE_VERSION_KEY, LORE_VERSION);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tags));
        stack.set(
                DataComponents.CUSTOM_MODEL_DATA,
                new CustomModelData(List.of(), List.of(), List.of(String.valueOf(cardId)), List.of())
        );
        Component suit = Component.translatable("item.just_poker.suit." + get_suit(cardId));
        Component rank = Component.translatable("item.just_poker.rank." + get_rank(cardId));
        stack.set(DataComponents.ITEM_NAME, Component.translatable("item.just_poker.card_name", suit, rank));
        setLore(stack, color);

        return stack;
    }

    private static void setLore(ItemStack stack, byte color) {
        stack.set(
                DataComponents.LORE,
                new ItemLore(List.of(Component.translatable("item.just_poker.card_color." + color)
                                             .withColor(getColorCode(color))))
        );
    }

    public static byte getColor(ItemStack stack) {
        return stack.get(DataComponents.CUSTOM_DATA).copyTag().getByte("color").get();
    }

    public static int getColorCode(byte color) {
        return switch (color) {
            case 0 -> 0x555555; // black
            case 1 -> 0x5555ff; // blue
            case 2 -> 0xff55ff; // pink
            case 3 -> 0xff5555; // red
            default -> throw new IllegalArgumentException("Invalid card color: " + color);
        };
    }

    public static byte getCardId(ItemStack stack) {
        var data = stack.get(DataComponents.CUSTOM_MODEL_DATA).getString(0);
        assert data != null;
        return Byte.parseByte(data);
    }

    public static String get_suit(byte cardId) {
        return switch ((cardId - 1) / 13) {
            case 0 -> "heart";
            case 1 -> "diamond";
            case 2 -> "club";
            case 3 -> "spade";
            default -> throw new IllegalArgumentException("Invalid card id: " + cardId);
        };
    }

    public static String get_rank(byte cardId) {
        int rank = (cardId - 1) % 13 + 1;
        return switch (rank) {
            case 1 -> "ace";
            case 11 -> "jack";
            case 12 -> "queen";
            case 13 -> "king";
            default -> String.valueOf(rank);
        };
    }

    public static String get_suit_unicode(byte suit) {
        return switch (suit) {
            case 0 -> "♥";
            case 1 -> "♦";
            case 2 -> "♣";
            case 3 -> "♠";
            default -> throw new IllegalArgumentException("Invalid suit: " + suit);
        };
    }

    public static String get_rank_short(byte rank) {
        return switch (rank) {
            case 1 -> "A";
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            default -> String.valueOf(rank);
        };
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        ItemStack stack = context.getItemInHand();
        Entity entity = context.getPlayer().isCrouching() ? new CardDeckEntity(
                level,
                context.getClickLocation(),
                context.getRotation(),
                getColor(stack),
                new byte[]{getCardId(stack)}
        ) : new CardEntity(level, context.getClickLocation(), context.getRotation(), getColor(stack), getCardId(stack));
        level.addFreshEntity(entity);
        context.getItemInHand().shrink(1);

        return InteractionResult.SUCCESS;
    }
}
