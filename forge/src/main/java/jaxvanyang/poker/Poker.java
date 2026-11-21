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
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static jaxvanyang.poker.Constants.MOD_ID;

@Mod(Constants.MOD_ID)
public class Poker {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB,
            MOD_ID
    );
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(
            ForgeRegistries.ENTITY_DATA_SERIALIZERS,
            MOD_ID
    );
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(
            Registries.ENTITY_TYPE,
            MOD_ID
    );

    public static final RegistryObject<Block> CASINO_TABLE = BLOCKS.register(
            "casino_table",
            () -> new CasinoTable(Block.Properties.of())
    );
    public static final RegistryObject<Item> CASINO_TABLE_ITEM = ITEMS.register(
            "casino_table",
            () -> new BlockItem(
                    CASINO_TABLE.get(),
                    new Item.Properties()
            )
    );
    public static final RegistryObject<Block> CASINO_CHAIR = BLOCKS.register(
            "casino_chair",
            () -> new CasinoChair(Block.Properties.of())
    );
    public static final RegistryObject<Item> CASINO_CHAIR_ITEM = ITEMS.register(
            "casino_chair",
            () -> new BlockItem(
                    CASINO_CHAIR.get(),
                    new Item.Properties()
            )
    );

    public static final RegistryObject<Item> BLACK_CARD_DECK = ITEMS.register(
            "black_card_deck",
            () -> new CardDeck(new Item.Properties())
    );
    public static final RegistryObject<Item> BLUE_CARD_DECK = ITEMS.register(
            "blue_card_deck",
            () -> new CardDeck(new Item.Properties())
    );
    public static final RegistryObject<Item> PINK_CARD_DECK = ITEMS.register(
            "pink_card_deck",
            () -> new CardDeck(new Item.Properties())
    );
    public static final RegistryObject<Item> RED_CARD_DECK = ITEMS.register(
            "red_card_deck",
            () -> new CardDeck(new Item.Properties())
    );
    public static final RegistryObject<Item> CARD = ITEMS.register("card", () -> new Card(new Item.Properties()));
    public static final RegistryObject<Item> CARD_BACK = ITEMS.register(
            "card_back",
            () -> new CardBack(new Item.Properties())
    );
    public static final RegistryObject<Item> BLACK_CASINO_CHIP = ITEMS.register(
            "black_casino_chip",
            () -> new Chip(new Item.Properties())
    );
    public static final RegistryObject<Item> BLUE_CASINO_CHIP = ITEMS.register(
            "blue_casino_chip",
            () -> new Chip(new Item.Properties())
    );
    public static final RegistryObject<Item> GREEN_CASINO_CHIP = ITEMS.register(
            "green_casino_chip",
            () -> new Chip(new Item.Properties())
    );
    public static final RegistryObject<Item> RED_CASINO_CHIP = ITEMS.register(
            "red_casino_chip",
            () -> new Chip(new Item.Properties())
    );
    public static final RegistryObject<Item> WHITE_CASINO_CHIP = ITEMS.register(
            "white_casino_chip",
            () -> new Chip(new Item.Properties())
    );

    public static final Supplier<EntityType<SeatEntity>> SEAT = ENTITY_TYPES.register(
            "seat",
            () -> EntityType.Builder.of(
                    SeatEntity::new,
                    MobCategory.MISC
            ).sized(0, 0).build("seat")
    );
    public static final Supplier<EntityType<CardDeckEntity>> CARD_DECK_TYPE = ENTITY_TYPES.register(
            "card_deck",
            () -> EntityType.Builder.<CardDeckEntity>of(CardDeckEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.1F)
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

    public static final RegistryObject<CreativeModeTab> POKER_TAB = CREATIVE_MODE_TABS.register(
            "poker_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.%s".formatted(MOD_ID)))
                    .icon(() -> CARD.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(BLACK_CARD_DECK.get());
                        output.accept(BLUE_CARD_DECK.get());
                        output.accept(PINK_CARD_DECK.get());
                        output.accept(RED_CARD_DECK.get());
                        output.accept(BLACK_CASINO_CHIP.get());
                        output.accept(BLUE_CASINO_CHIP.get());
                        output.accept(GREEN_CASINO_CHIP.get());
                        output.accept(RED_CASINO_CHIP.get());
                        output.accept(WHITE_CASINO_CHIP.get());
                        output.accept(CASINO_TABLE_ITEM.get());
                        output.accept(CASINO_CHAIR_ITEM.get());
                    })
                    .build()
    );

    public Poker(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
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
