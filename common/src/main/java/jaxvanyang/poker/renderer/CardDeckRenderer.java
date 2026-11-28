package jaxvanyang.poker.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import jaxvanyang.poker.entity.CardDeckEntity;
import jaxvanyang.poker.item.CardBack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class CardDeckRenderer extends EntityRenderer<CardDeckEntity, CardDeckRenderer.CardDeckRenderState> {
    public static class CardDeckRenderState extends EntityRenderState {
        public byte color;
        public byte[] deck = new byte[0];
        public float rotation;
    }

    public CardDeckRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public CardDeckRenderState createRenderState() {
        return new CardDeckRenderState();
    }

    @Override
    public void extractRenderState(CardDeckEntity entity, CardDeckRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.color = entity.getEntityData().get(CardDeckEntity.COLOR);
        state.deck = entity.getEntityData().get(CardDeckEntity.DECK);
        state.rotation = entity.getRotation();
    }

    @Override
    public void render(
            CardDeckRenderState state,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        super.render(state, poseStack, buffer, packedLight);

        var stack = CardBack.newStack(state.color);
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.rotation + 180.0F));
        poseStack.scale(2.0F, 1.5F, 2.0F);
        // TODO: just render a scaled card
        for (int i = 0; i < state.deck.length; ++i) {
            poseStack.pushPose();
            Helper.renderItem(stack, 0, i * 0.001D, 0, poseStack, buffer, packedLight);
            poseStack.popPose();
        }
        poseStack.popPose();
    }
}
