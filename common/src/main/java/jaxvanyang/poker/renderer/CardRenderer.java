package jaxvanyang.poker.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import jaxvanyang.poker.entity.CardEntity;
import jaxvanyang.poker.item.Card;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CardRenderer extends EntityRenderer<CardEntity> {
    public CardRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(
            CardEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight
    ) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        ItemStack stack = Card.newStack(entity.getColor(), entity.getCard());

        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getRotation() + 180.0F));
        poseStack.scale(1.5F, 1.5F, 1.5F);
        poseStack.translate(0.0D, 0.001D, 0.0D);
        Minecraft.getInstance().getItemRenderer().renderStatic(
                stack,
                ItemDisplayContext.GROUND,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                null,
                0
        );

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(CardEntity cardEntity) {
        return null;
    }
}
