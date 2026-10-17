package jaxvanyang.poker.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import jaxvanyang.poker.entity.CardEntity;
import jaxvanyang.poker.item.Card;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
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
            MultiBufferSource buffer,
            int packedLight
    ) {
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);

        ItemStack stack = Card.newStack(entity.getColor(), entity.getCard());

        poseStack.pushPose();

        // TODO: fix z-fighting
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getRotation() + 180.0F));
        poseStack.scale(1.5F, 1.5F, 1.5F);
        Helper.renderItem(stack, 0, 0, 0.001D, poseStack, buffer, packedLight);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(CardEntity cardEntity) {
        return null;
    }
}
