package jaxvanyang.poker.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;

public class Card extends Item {
    public enum Suit {
        HEART,
        DIAMOND,
        CLUB,
        SPADE
    }

    public enum Rank {
        ACE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING
    }

    public Card(Properties properties) {
        super(properties.stacksTo(1));
    }

    public ItemStack createCard(Suit suit, Rank rank) {
        ItemStack stack = new ItemStack(this);
        stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(getCardID(suit, rank)));
        return stack;
    }

    public static int getCardID(Suit suit, Rank rank) {
        return suit.ordinal() * 13 + rank.ordinal() + 1;
    }
}
