package jaxvanyang.poker.item;

import jaxvanyang.poker.entity.CardEntity;
import jaxvanyang.poker.platform.Services;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;


public class Card extends Item {
    public Card(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static ItemStack newStack(byte color, byte cardId) {
        ItemStack stack = new ItemStack(Services.PLATFORM.getCardItem());
        CompoundTag tags = new CompoundTag();
        tags.putByte("color", color);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tags));
        stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(cardId));

        return stack;
    }

    public static byte getColor(ItemStack stack) {
        return stack.get(DataComponents.CUSTOM_DATA).copyTag().getByte("color");
    }

    public static byte getCardId(ItemStack stack) {
        return (byte) stack.get(DataComponents.CUSTOM_MODEL_DATA).value();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        ItemStack stack = context.getItemInHand();
        CardEntity entity = new CardEntity(
                level,
                context.getClickLocation(),
                context.getRotation(),
                getColor(stack),
                getCardId(stack)
        );
        level.addFreshEntity(entity);
        context.getItemInHand().shrink(1);

        return InteractionResult.SUCCESS;
    }
}
