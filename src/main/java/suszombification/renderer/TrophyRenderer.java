package suszombification.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.vector.Vector3f;
import suszombification.block.TrophyBlock;
import suszombification.block.entity.TrophyBlockEntity;

public class TrophyRenderer extends TileEntityRenderer<TrophyBlockEntity> {
	public TrophyRenderer(TileEntityRendererDispatcher terd) {
		super(terd);
	}

	@Override
	public void render(TrophyBlockEntity be, float partialTick, MatrixStack pose, IRenderTypeBuffer bufferSource, int packedLight, int packedOverlay) {
		ItemStack stackToRender = be.getTrophyType().displayItem;
		IBakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stackToRender, be.getLevel(), null);
		Direction direction = be.getBlockState().getValue(TrophyBlock.FACING);
		int additionalRotation = direction.getAxis() == Axis.X ? 180 : 0; //fixes item being mirrored when the trophy is placed facing on the X axis

		pose.pushPose();
		pose.translate(0.5D, 0.6D, 0.5D);
		pose.scale(0.7F, 0.7F, 0.7F);
		pose.mulPose(Vector3f.YP.rotationDegrees(direction.toYRot() + additionalRotation));
		Minecraft.getInstance().getItemRenderer().render(stackToRender, TransformType.GROUND, false, pose, bufferSource, packedLight, packedOverlay, model);
		pose.popPose();
	}
}
