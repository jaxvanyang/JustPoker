package jaxvanyang.poker.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class Helper {
    public static void renderItem(
            ItemStack stack,
            double dx,
            double dy,
            double dz,
            PoseStack poseStack,
            SubmitNodeCollector nodeCollector,
            int packedLight
    ) {
        poseStack.pushPose();
        poseStack.translate(dx, dy, dz);
        var itemState = new ItemStackRenderState();
        var minecraft = Minecraft.getInstance();
        minecraft.getItemModelResolver()
                .appendItemLayers(
                        itemState,
                        stack,
                        ItemDisplayContext.GROUND,
                        minecraft.level,
                        null,
                        0
                );
        itemState.submit(poseStack, nodeCollector, packedLight, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
