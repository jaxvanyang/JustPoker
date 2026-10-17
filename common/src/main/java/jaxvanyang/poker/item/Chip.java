package jaxvanyang.poker.item;

import jaxvanyang.poker.entity.ChipEntity;
import jaxvanyang.poker.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class Chip extends Item {
    public Chip(Properties properties) {
        super(properties);
    }

    public static ItemStack newStack(byte color) {
        return new ItemStack(Services.PLATFORM.getChipItem(color));
    }

    public byte getColor() {
        String id = BuiltInRegistries.ITEM.getKey(this).toString();
        byte color = -1;
        if (id.contains("black")) {
            color = 0;
        } else if (id.contains("blue")) {
            color = 1;
        } else if (id.contains("green")) {
            color = 2;
        } else if (id.contains("red")) {
            color = 3;
        } else if (id.contains("white")) {
            color = 4;
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

        ChipEntity entity = new ChipEntity(
                level,
                context.getClickLocation(),
                context.getRotation(),
                color
        );
        level.addFreshEntity(entity);
        context.getItemInHand().shrink(1);

        return InteractionResult.SUCCESS;
    }
}
