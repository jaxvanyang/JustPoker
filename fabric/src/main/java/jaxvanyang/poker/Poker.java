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
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static jaxvanyang.poker.Constants.MOD_ID;

public class Poker implements ModInitializer {
    public static Block CASINO_TABLE;
    public static Block CASINO_CHAIR;

    public static Item CASINO_TABLE_ITEM;
    public static Item CASINO_CHAIR_ITEM;

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

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        CommonClass.init();

        CASINO_TABLE = registerBlock("casino_table", new CasinoTable(Block.Properties.of()));
        CASINO_CHAIR = registerBlock("casino_chair", new CasinoChair(Block.Properties.of()));
        CASINO_TABLE_ITEM = registerItem("casino_table", new BlockItem(CASINO_TABLE, new Item.Properties()));
        CASINO_CHAIR_ITEM = registerItem("casino_chair", new BlockItem(CASINO_CHAIR, new Item.Properties()));

        BLACK_CARD_DECK = registerItem("black_card_deck", new CardDeck(new Item.Properties()));
        BLUE_CARD_DECK = registerItem("blue_card_deck", new CardDeck(new Item.Properties()));
        PINK_CARD_DECK = registerItem("pink_card_deck", new CardDeck(new Item.Properties()));
        RED_CARD_DECK = registerItem("red_card_deck", new CardDeck(new Item.Properties()));
        CARD = registerItem("card", new Card(new Item.Properties()));
        CARD_BACK = registerItem("card_back", new CardBack(new Item.Properties()));
        BLACK_CASINO_CHIP = registerItem("black_casino_chip", new Chip(new Item.Properties()));
        BLUE_CASINO_CHIP = registerItem("blue_casino_chip", new Chip(new Item.Properties()));
        GREEN_CASINO_CHIP = registerItem("green_casino_chip", new Chip(new Item.Properties()));
        RED_CASINO_CHIP = registerItem("red_casino_chip", new Chip(new Item.Properties()));
        WHITE_CASINO_CHIP = registerItem("white_casino_chip", new Chip(new Item.Properties()));

        SEAT = registerEntity(
                "seat",
                EntityType.Builder.of(SeatEntity::new, MobCategory.MISC).sized(0, 0).build("seat")
        );
        CARD_DECK_TYPE = registerEntity(
                "card_deck",
                EntityType.Builder.<CardDeckEntity>of(CardDeckEntity::new, MobCategory.MISC)
                        .sized(0.5F, 0.1F)
                        .build("card_deck")
        );
        CARD_TYPE = registerEntity(
                "card",
                EntityType.Builder.<CardEntity>of(CardEntity::new, MobCategory.MISC)
                        .sized(0.5F, 0.01F)
                        .build("card")
        );
        CHIP_TYPE = registerEntity(
                "chip",
                EntityType.Builder.<ChipEntity>of(ChipEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.025F)
                        .build("chip")
        );

        EntityDataSerializers.registerSerializer(CardDeckEntity.BYTE_ARRAY);

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

                            output.accept(CASINO_TABLE_ITEM);
                            output.accept(CASINO_CHAIR_ITEM);
                        })
                        .build()
        );
    }

    private static Block registerBlock(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, id(name), block);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, id(name), item);
    }

    private static <T extends net.minecraft.world.entity.Entity> EntityType<T> registerEntity(
            String name,
            EntityType<T> entityType
    ) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, id(name), entityType);
    }

}
