package suszombification.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import suszombification.block.TrophyBlock;
import suszombification.block.entity.TrophyBlockEntity;

public class TrophyRenderer implements BlockEntityRenderer<TrophyBlockEntity> {
	public TrophyRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(TrophyBlockEntity be, float partialTick, PoseStack pose, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		ItemStack stackToRender = be.getTrophyType().displayItem;
		BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stackToRender, be.getLevel(), null, 0);
		Direction direction = be.getBlockState().getValue(TrophyBlock.FACING);
		int additionalRotation = direction.getAxis() == Axis.X ? 180 : 0; //fixes item being mirrored when the trophy is placed facing on the X axis

		pose.pushPose();
		pose.translate(0.5D, 0.6D, 0.5D);
		pose.scale(0.7F, 0.7F, 0.7F);
		pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(direction.toYRot() + additionalRotation));
		Minecraft.getInstance().getItemRenderer().render(stackToRender, ItemDisplayContext.GROUND, false, pose, bufferSource, packedLight, packedOverlay, model);
		pose.popPose();
	}
}
