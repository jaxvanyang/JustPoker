package jaxvanyang.poker;

import jaxvanyang.poker.block.CasinoChair;
import jaxvanyang.poker.block.CasinoTable;
import jaxvanyang.poker.item.CardDeck;
import jaxvanyang.poker.platform.NeoForgePlatformHelper;
import jaxvanyang.poker.platform.Services;
import jaxvanyang.poker.renderer.SeatRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static jaxvanyang.poker.Constants.MOD_ID;

@Mod(MOD_ID)
public class Poker {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB,
            MOD_ID
    );

    public static final DeferredBlock<Block> POKER_TABLE = BLOCKS.registerBlock("casino_table", CasinoTable::new);
    public static final DeferredItem<BlockItem> POKER_TABLE_ITEM = ITEMS.registerSimpleBlockItem(POKER_TABLE);

    public static final DeferredBlock<Block> CASINO_CHAIR = BLOCKS.registerBlock("casino_chair", CasinoChair::new);
    public static final DeferredItem<BlockItem> CASINO_CHAIR_ITEM = ITEMS.registerSimpleBlockItem(CASINO_CHAIR);

    public static final DeferredItem<Item> BLACK_CARD_DECK = ITEMS.registerItem("black_card_deck", CardDeck::new);
    public static final DeferredItem<Item> BLUE_CARD_DECK = ITEMS.registerItem("blue_card_deck", CardDeck::new);
    public static final DeferredItem<Item> PINK_CARD_DECK = ITEMS.registerItem("pink_card_deck", CardDeck::new);
    public static final DeferredItem<Item> RED_CARD_DECK = ITEMS.registerItem("red_card_deck", CardDeck::new);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> POKER_TAB = CREATIVE_MODE_TABS.register(
            "poker_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.jaxvanyang_poker"))
                    .icon(() -> POKER_TABLE_ITEM.get().getDefaultInstance())
                    .displayItems((p, output) -> {
                        output.accept(POKER_TABLE_ITEM);
                        output.accept(CASINO_CHAIR_ITEM);
                        output.accept(BLACK_CARD_DECK);
                        output.accept(BLUE_CARD_DECK);
                        output.accept(PINK_CARD_DECK);
                        output.accept(RED_CARD_DECK);
                    })
                    .build()
    );


    public Poker(IEventBus bus) {
        CommonClass.init();

        BLOCKS.register(bus);
        ITEMS.register(bus);
        CREATIVE_MODE_TABS.register(bus);

        NeoForgePlatformHelper.ENTITY_TYPES.register(bus);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            bus.addListener(Poker::registerEntityRenders);
        }
    }

    private static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Services.PLATFORM.getSeatEntityType(), SeatRenderer::new);
    }
}
