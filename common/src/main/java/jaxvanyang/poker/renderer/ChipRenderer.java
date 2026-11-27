package jaxvanyang.poker.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import jaxvanyang.poker.entity.ChipEntity;
import jaxvanyang.poker.item.Chip;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class ChipRenderer extends EntityRenderer<ChipEntity, ChipRenderer.ChipRenderState> {
    public static class ChipRenderState extends EntityRenderState {
        public byte color;
        public byte size;
    }

    public ChipRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ChipRenderState createRenderState() {
        return new ChipRenderState();
    }

    @Override
    public void extractRenderState(ChipEntity entity, ChipRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.color = entity.getColor();
        state.size = entity.getSize();
    }

    @Override
    public void submit(
            ChipRenderState state,
            PoseStack poseStack,
            SubmitNodeCollector nodeCollector,
            CameraRenderState cameraRenderState
    ) {
        super.submit(state, poseStack, nodeCollector, cameraRenderState);

        ItemStack stack = Chip.newStack(state.color);
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.01D, 0.07D);
        poseStack.scale(0.5F, 0.5F, 0.5F);
        for (byte i = 0; i < state.size; ++i) {
            poseStack.pushPose();
            Random r = new Random(i);
            poseStack.translate(r.nextDouble() * 0.05D - 0.025D, 0, r.nextDouble() * 0.05D - 0.025D);
            poseStack.rotate(Axis.XN.rotationDegrees(90));
            Helper.renderItem(stack, 0, 0, i * 0.032D, poseStack, nodeCollector, state.lightCoords);
            poseStack.popPose();
        }
        poseStack.popPose();
    }
}
