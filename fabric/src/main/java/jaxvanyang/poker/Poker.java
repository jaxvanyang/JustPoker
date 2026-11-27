package jaxvanyang.poker;

import jaxvanyang.poker.block.CasinoChair;
import jaxvanyang.poker.block.CasinoTable;
import jaxvanyang.poker.entity.CardDeckEntity;
import jaxvanyang.poker.entity.CardEntity;
import jaxvanyang.poker.entity.ChipEntity;
import jaxvanyang.poker.entity.SeatEntity;
import jaxvanyang.poker.item.Card;
import jaxvanyang.poker.item.CardBack;
import jaxvanyang.poker.item.CardDeck;
import jaxvanyang.poker.item.Chip;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

import static jaxvanyang.poker.Common.*;

public class Poker implements ModInitializer {
    public static Block CASINO_TABLE;
    public static Block CASINO_CHAIR;

    public static Item BLACK_CARD_DECK;
    public static Item BLUE_CARD_DECK;
    public static Item PINK_CARD_DECK;
    public static Item RED_CARD_DECK;
    public static Item CARD;
    public static Item CARD_BACK;
    public static Item BLACK_CASINO_CHIP;
    public static Item BLUE_CASINO_CHIP;
    public static Item GREEN_CASINO_CHIP;
    public static Item RED_CASINO_CHIP;
    public static Item WHITE_CASINO_CHIP;

    public static EntityType<SeatEntity> SEAT;
    public static EntityType<CardDeckEntity> CARD_DECK_TYPE;
    public static EntityType<CardEntity> CARD_TYPE;
    public static EntityType<ChipEntity> CHIP_TYPE;

    public static CreativeModeTab POKER_TAB;

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        Common.init();

        CASINO_TABLE = registerBlock("casino_table", CasinoTable::new, Block.Properties.ofFullCopy(Blocks.OAK_PLANKS));
        CASINO_CHAIR = registerBlock("casino_chair", CasinoChair::new, Block.Properties.ofFullCopy(Blocks.OAK_PLANKS));

        BLACK_CARD_DECK = registerItem("black_card_deck", CardDeck::new, new Item.Properties());
        BLUE_CARD_DECK = registerItem("blue_card_deck", CardDeck::new, new Item.Properties());
        PINK_CARD_DECK = registerItem("pink_card_deck", CardDeck::new, new Item.Properties());
        RED_CARD_DECK = registerItem("red_card_deck", CardDeck::new, new Item.Properties());
        CARD = registerItem("card", Card::new, new Item.Properties());
        CARD_BACK = registerItem("card_back", CardBack::new, new Item.Properties());
        BLACK_CASINO_CHIP = registerItem("black_casino_chip", Chip::new, new Item.Properties());
        BLUE_CASINO_CHIP = registerItem("blue_casino_chip", Chip::new, new Item.Properties());
        GREEN_CASINO_CHIP = registerItem("green_casino_chip", Chip::new, new Item.Properties());
        RED_CASINO_CHIP = registerItem("red_casino_chip", Chip::new, new Item.Properties());
        WHITE_CASINO_CHIP = registerItem("white_casino_chip", Chip::new, new Item.Properties());

        FabricTrackedDataRegistry.register(id("byte_array"), CardDeckEntity.BYTE_ARRAY);
        SEAT = registerEntity("seat", EntityType.Builder.of(SeatEntity::new, MobCategory.MISC).sized(0, 0));
        CARD_DECK_TYPE = registerEntity(
                "card_deck",
                EntityType.Builder.<CardDeckEntity>of(CardDeckEntity::new, MobCategory.MISC)
                        .sized(0.5F, 0.1F)
        );
        CARD_TYPE = registerEntity(
                "card",
                EntityType.Builder.<CardEntity>of(CardEntity::new, MobCategory.MISC)
                        .sized(0.5F, 0.01F)
        );
        CHIP_TYPE = registerEntity(
                "chip",
                EntityType.Builder.<ChipEntity>of(ChipEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.025F)
        );

        POKER_TAB = Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                id("poker_tab"),
                FabricItemGroup.builder()
                        .title(Component.translatable("itemGroup.%s".formatted(MOD_ID)))
                        .icon(() -> CARD.getDefaultInstance())
                        .displayItems((parameters, output) -> {
                            output.accept(BLACK_CARD_DECK);
                            output.accept(BLUE_CARD_DECK);
                            output.accept(PINK_CARD_DECK);
                            output.accept(RED_CARD_DECK);

                            output.accept(BLACK_CASINO_CHIP);
                            output.accept(BLUE_CASINO_CHIP);
                            output.accept(GREEN_CASINO_CHIP);
                            output.accept(RED_CASINO_CHIP);
                            output.accept(WHITE_CASINO_CHIP);

                            output.accept(CASINO_TABLE);
                            output.accept(CASINO_CHAIR);
                        })
                        .build()
        );
    }

    public static Item registerItem(
            String name,
            Function<Item.Properties, Item> itemFactory,
            Item.Properties settings
    ) {
        var itemKey = keyOfItem(name);
        var item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    private static Block registerBlock(
            String name,
            Function<BlockBehaviour.Properties, Block> blockFactory,
            BlockBehaviour.Properties settings
    ) {
        var blockKey = keyOfBlock(name);
        var block = blockFactory.apply(settings.setId(blockKey));
        // create associated item for the block
        var itemKey = keyOfItem(name);
        var blockItem = new BlockItem(block, new Item.Properties().setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static <T extends net.minecraft.world.entity.Entity> EntityType<T> registerEntity(
            String name,
            EntityType.Builder<T> entityBuilder
    ) {
        var entityKey = keyOfEntity(name);
        var entityType = entityBuilder.build(entityKey);
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, entityKey, entityType);
    }
}
