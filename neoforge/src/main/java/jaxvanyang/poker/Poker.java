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
import jaxvanyang.poker.renderer.CardDeckRenderer;
import jaxvanyang.poker.renderer.CardRenderer;
import jaxvanyang.poker.renderer.ChipRenderer;
import jaxvanyang.poker.renderer.SeatRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.*;

import java.util.function.Supplier;

import static jaxvanyang.poker.Constants.MOD_ID;

@Mod(MOD_ID)
public class Poker {
    // TODO: find better variable names
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB,
            MOD_ID
    );
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(
            NeoForgeRegistries.ENTITY_DATA_SERIALIZERS,
            MOD_ID
    );
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(
            BuiltInRegistries.ENTITY_TYPE,
            Constants.MOD_ID
    );

    public static final DeferredBlock<Block> CASINO_TABLE = BLOCKS.registerBlock("casino_table", CasinoTable::new);
    public static final DeferredItem<BlockItem> CASINO_TABLE_ITEM = ITEMS.registerSimpleBlockItem(CASINO_TABLE);

    public static final DeferredBlock<Block> CASINO_CHAIR = BLOCKS.registerBlock("casino_chair", CasinoChair::new);
    public static final DeferredItem<BlockItem> CASINO_CHAIR_ITEM = ITEMS.registerSimpleBlockItem(CASINO_CHAIR);

    public static final DeferredItem<Item> BLACK_CARD_DECK = ITEMS.registerItem("black_card_deck", CardDeck::new);
    public static final DeferredItem<Item> BLUE_CARD_DECK = ITEMS.registerItem("blue_card_deck", CardDeck::new);
    public static final DeferredItem<Item> PINK_CARD_DECK = ITEMS.registerItem("pink_card_deck", CardDeck::new);
    public static final DeferredItem<Item> RED_CARD_DECK = ITEMS.registerItem("red_card_deck", CardDeck::new);
    public static final DeferredItem<Item> CARD = ITEMS.registerItem("card", Card::new);
    public static final DeferredItem<Item> CARD_BACK = ITEMS.registerItem("card_back", CardBack::new);
    public static final DeferredItem<Item> BLACK_CASINO_CHIP = ITEMS.registerItem("black_casino_chip", Chip::new);
    public static final DeferredItem<Item> BLUE_CASINO_CHIP = ITEMS.registerItem("blue_casino_chip", Chip::new);
    public static final DeferredItem<Item> GREEN_CASINO_CHIP = ITEMS.registerItem("green_casino_chip", Chip::new);
    public static final DeferredItem<Item> RED_CASINO_CHIP = ITEMS.registerItem("red_casino_chip", Chip::new);
    public static final DeferredItem<Item> WHITE_CASINO_CHIP = ITEMS.registerItem("white_casino_chip", Chip::new);

    public static final Supplier<EntityType<SeatEntity>> SEAT = ENTITY_TYPES.register(
            "seat",
            () -> EntityType.Builder.of(
                    SeatEntity::new,
                    MobCategory.MISC
            ).sized(0, 0).build("seat")
    );
    public static final Supplier<EntityType<CardDeckEntity>> CARD_DECK_TYPE = ENTITY_TYPES.register(
            "card_deck",
            () -> EntityType.Builder.<CardDeckEntity>of(
                            CardDeckEntity::new,
                            MobCategory.MISC
                    )
                    .sized(
                            0.5F,
                            0.1F
                    )
                    .build("card_deck")
    );
    public static final Supplier<EntityType<CardEntity>> CARD_TYPE = ENTITY_TYPES.register(
            "card",
            () -> EntityType.Builder.<CardEntity>of(
                            CardEntity::new,
                            MobCategory.MISC
                    )
                    .sized(0.5F, 0.01F)
                    .build("card")
    );
    public static final Supplier<EntityType<ChipEntity>> CHIP_TYPE = ENTITY_TYPES.register(
            "chip",
            () -> EntityType.Builder.<ChipEntity>of(
                            ChipEntity::new,
                            MobCategory.MISC
                    )
                    .sized(0.25F, 0.025F)
                    .build("chip")
    );


    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> POKER_TAB = CREATIVE_MODE_TABS.register(
            "poker_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.%s".formatted(MOD_ID)))
                    .icon(() -> CARD.get().getDefaultInstance())
                    .displayItems((p, output) -> {
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

    public Poker(IEventBus bus) {
        CommonClass.init();

        BLOCKS.register(bus);
        ITEMS.register(bus);
        CREATIVE_MODE_TABS.register(bus);
        ENTITY_TYPES.register(bus);

        ENTITY_DATA_SERIALIZERS.register("byte_array", () -> CardDeckEntity.BYTE_ARRAY);
        ENTITY_DATA_SERIALIZERS.register(bus);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            bus.addListener(Poker::registerEntityRenders);
        }
    }

    private static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(SEAT.get(), SeatRenderer::new);
        event.registerEntityRenderer(CARD_DECK_TYPE.get(), CardDeckRenderer::new);
        event.registerEntityRenderer(CARD_TYPE.get(), CardRenderer::new);
        event.registerEntityRenderer(CHIP_TYPE.get(), ChipRenderer::new);
    }
}
