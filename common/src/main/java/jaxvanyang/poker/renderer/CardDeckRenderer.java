package jaxvanyang.poker.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import jaxvanyang.poker.entity.CardDeckEntity;
import jaxvanyang.poker.item.CardBack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CardDeckRenderer extends EntityRenderer<CardDeckEntity> {
    public CardDeckRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(
            CardDeckEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight
    ) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        byte color = entity.getEntityData().get(CardDeckEntity.COLOR);
        ItemStack cardStack = CardBack.newStack(color);

        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getRotation() + 180.0F));
        poseStack.scale(1.5F, 1.5F, 1.5F);
        // TODO: just render a scaled card
        for (int i = 0; i < entity.getEntityData().get(CardDeckEntity.DECK).length; ++i) {
            poseStack.pushPose();
            poseStack.translate(0.0D, i * 0.001D, 0.0D);
            Minecraft.getInstance().getItemRenderer().renderStatic(
                    cardStack,
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

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(CardDeckEntity cardDeckEntity) {
        return null;
    }
}
