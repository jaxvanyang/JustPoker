package jaxvanyang.poker;

import jaxvanyang.poker.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static jaxvanyang.poker.Constants.MOD_ID;

public class Common {
    public static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, name));
    }

    public static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, name));
    }

    public static ResourceKey<EntityType<?>> keyOfEntity(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, name));
    }

    public static void init() {
        if (Services.PLATFORM.isModLoaded("just_poker")) {
            Constants.LOG.info("Just Poker mod is loaded");
        }
    }
}
