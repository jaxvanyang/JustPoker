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
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

import static jaxvanyang.poker.Common.*;

@Mod(MOD_ID)
public class Poker {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB,
            MOD_ID
    );
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.ENTITY_DATA_SERIALIZERS,
                                                                                                                    MOD_ID
    );
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(
            Registries.ENTITY_TYPE,
            MOD_ID
    );

    public static final RegistryObject<Block> CASINO_TABLE = registerBlock(
            "casino_table",
            CasinoTable::new,
            Block.Properties.ofFullCopy(Blocks.OAK_PLANKS)
    );
    public static final RegistryObject<Block> CASINO_CHAIR = registerBlock(
            "casino_chair",
            CasinoChair::new,
            Block.Properties.ofFullCopy(Blocks.OAK_PLANKS)
    );

    public static final RegistryObject<Item> BLACK_CARD_DECK = registerItem("black_card_deck", CardDeck::new);
    public static final RegistryObject<Item> BLUE_CARD_DECK = registerItem("blue_card_deck", CardDeck::new);
    public static final RegistryObject<Item> PINK_CARD_DECK = registerItem("pink_card_deck", CardDeck::new);
    public static final RegistryObject<Item> RED_CARD_DECK = registerItem("red_card_deck", CardDeck::new);
    public static final RegistryObject<Item> CARD = registerItem("card", Card::new);
    public static final RegistryObject<Item> CARD_BACK = registerItem("card_back", CardBack::new);
    public static final RegistryObject<Item> BLACK_CASINO_CHIP = registerItem("black_casino_chip", Chip::new);
    public static final RegistryObject<Item> BLUE_CASINO_CHIP = registerItem("blue_casino_chip", Chip::new);
    public static final RegistryObject<Item> GREEN_CASINO_CHIP = registerItem("green_casino_chip", Chip::new);
    public static final RegistryObject<Item> RED_CASINO_CHIP = registerItem("red_casino_chip", Chip::new);
    public static final RegistryObject<Item> WHITE_CASINO_CHIP = registerItem("white_casino_chip", Chip::new);

    public static final Supplier<EntityType<SeatEntity>> SEAT = ENTITY_TYPES.register(
            "seat",
            () -> EntityType.Builder.of(SeatEntity::new, MobCategory.MISC).sized(0, 0).build(keyOfEntity("seat"))
    );
    public static final Supplier<EntityType<CardDeckEntity>> CARD_DECK_TYPE = ENTITY_TYPES.register(
            "card_deck",
            () -> EntityType.Builder.<CardDeckEntity>of(CardDeckEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.1F)
                    .build(keyOfEntity("card_deck"))
    );
    public static final Supplier<EntityType<CardEntity>> CARD_TYPE = ENTITY_TYPES.register(
            "card",
            () -> EntityType.Builder.<CardEntity>of(CardEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.01F)
                    .build(keyOfEntity("card"))
    );
    public static final Supplier<EntityType<ChipEntity>> CHIP_TYPE = ENTITY_TYPES.register(
            "chip",
            () -> EntityType.Builder.<ChipEntity>of(ChipEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.025F)
                    .build(keyOfEntity("chip"))
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
                        output.accept(CASINO_TABLE.get());
                        output.accept(CASINO_CHAIR.get());
                    })
                    .build()
    );

    public Poker(FMLJavaModLoadingContext context) {
        var modBusGroup = context.getModBusGroup();
        Common.init();

        BLOCKS.register(modBusGroup);
        ITEMS.register(modBusGroup);
        CREATIVE_MODE_TABS.register(modBusGroup);
        ENTITY_TYPES.register(modBusGroup);
        ENTITY_DATA_SERIALIZERS.register("byte_array", () -> CardDeckEntity.BYTE_ARRAY);
        ENTITY_DATA_SERIALIZERS.register(modBusGroup);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            EntityRenderersEvent.RegisterRenderers.getBus(modBusGroup).addListener(Poker::registerEntityRenders);
        }
    }

    private static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(SEAT.get(), SeatRenderer::new);
        event.registerEntityRenderer(CARD_DECK_TYPE.get(), CardDeckRenderer::new);
        event.registerEntityRenderer(CARD_TYPE.get(), CardRenderer::new);
        event.registerEntityRenderer(CHIP_TYPE.get(), ChipRenderer::new);
    }

    private static RegistryObject<Block> registerBlock(
            String name,
            Function<BlockBehaviour.Properties, Block> blockFactory,
            BlockBehaviour.Properties settings
    ) {
        ResourceKey<Block> blockKey = keyOfBlock(name);
        var block = BLOCKS.register(name, () -> blockFactory.apply(settings.setId(blockKey)));
        // create associated item for the block
        ResourceKey<Item> itemKey = keyOfItem(name);
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().setId(itemKey)));

        return block;
    }

    public static RegistryObject<Item> registerItem(String name, Function<Item.Properties, Item> itemFactory) {
        ResourceKey<Item> itemKey = keyOfItem(name);
        return ITEMS.register(name, () -> itemFactory.apply(new Item.Properties().setId(itemKey)));
    }
}
