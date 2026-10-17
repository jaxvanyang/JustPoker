package jaxvanyang.poker.item;

import jaxvanyang.poker.platform.Services;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;

// Helper item with only color to render covered cards and decks, card data is stored in the entity
public class CardBack extends Item {
    public CardBack(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static ItemStack newStack(byte color) {
        ItemStack stack = new ItemStack(Services.PLATFORM.getCardBackItem());
        stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(color));
        return stack;
    }

    public static byte getColor(ItemStack stack) {
        return (byte) stack.get(DataComponents.CUSTOM_MODEL_DATA).value();
    }
}
