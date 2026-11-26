package jaxvanyang.poker;

import jaxvanyang.poker.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Common {
    public static final String MOD_ID = "just_poker";
    public static final String MOD_NAME = "Just Poker";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, name));
    }

    public static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name));
    }

    public static ResourceKey<EntityType<?>> keyOfEntity(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, name));
    }

    public static void init() {
        if (Services.PLATFORM.isModLoaded("just_poker")) {
            LOG.info("Just Poker mod is loaded");
        }
    }
}
