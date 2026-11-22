package jaxvanyang.poker.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import jaxvanyang.poker.entity.CardEntity;
import jaxvanyang.poker.item.Card;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.item.ItemStack;

public class CardRenderer extends EntityRenderer<CardEntity, CardRenderer.CardRenderState> {
    public static class CardRenderState extends EntityRenderState {
        public byte color;
        public byte card;
        public float rotation;
    }

    public CardRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public CardRenderState createRenderState() {
        return new CardRenderState();
    }

    @Override
    public void extractRenderState(CardEntity entity, CardRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.color = entity.getColor();
        state.card = entity.getCard();
        state.rotation = entity.getRotation();
    }

    @Override
    public void render(
            CardRenderState state,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        super.render(state, poseStack, buffer, packedLight);

        ItemStack stack = Card.newStack(state.color, state.card);
        poseStack.pushPose();
        // fix z-fighting
        double dy = 0.001D + 0.00001D * state.card;
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.rotation + 180.0F));
        poseStack.scale(2.0F, 1.5F, 2.0F);
        Helper.renderItem(stack, 0, dy, 0, poseStack, buffer, packedLight);
        poseStack.popPose();
    }
}
