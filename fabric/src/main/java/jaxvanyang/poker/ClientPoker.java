package jaxvanyang.poker;

import jaxvanyang.poker.renderer.CardDeckRenderer;
import jaxvanyang.poker.renderer.CardRenderer;
import jaxvanyang.poker.renderer.ChipRenderer;
import jaxvanyang.poker.renderer.SeatRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ClientPoker implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Poker.SEAT, SeatRenderer::new);
        EntityRendererRegistry.register(Poker.CARD_DECK_TYPE, CardDeckRenderer::new);
        EntityRendererRegistry.register(Poker.CARD_TYPE, CardRenderer::new);
        EntityRendererRegistry.register(Poker.CHIP_TYPE, ChipRenderer::new);
    }
}
