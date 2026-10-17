package jaxvanyang.poker.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import jaxvanyang.poker.entity.ChipEntity;
import jaxvanyang.poker.item.Chip;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class ChipRenderer extends EntityRenderer<ChipEntity> {
    public ChipRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(
            ChipEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);

        ItemStack stack = Chip.newStack(entity.getColor());

        poseStack.pushPose();

        poseStack.translate(0.0D, 0.01D, 0.07D);
        poseStack.scale(0.5F, 0.5F, 0.5F);

        for (byte i = 0; i < entity.getSize(); ++i) {
            poseStack.pushPose();

            Random r = new Random(i);
            poseStack.translate(r.nextDouble() * 0.05D - 0.025D, 0, r.nextDouble() * 0.05D - 0.025D);
            poseStack.mulPose(Axis.XN.rotationDegrees(90));
            Helper.renderItem(stack, 0, 0, i * 0.032D, poseStack, buffer, packedLight);

            poseStack.popPose();
        }

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(ChipEntity cardEntity) {
        return null;
    }
}
