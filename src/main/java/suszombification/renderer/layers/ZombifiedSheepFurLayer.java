package suszombification.renderer.layers;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
import net.minecraft.client.renderer.entity.model.SheepModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import suszombification.SuspiciousZombification;

public class ZombifiedSheepFurLayer extends SheepWoolLayer {
	private static final ResourceLocation SHEEP_FUR_LOCATION = new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_sheep/zombified_sheep_fur.png");
	private final SheepWoolModel<SheepEntity> model = new SheepWoolModel<>();

	public ZombifiedSheepFurLayer(IEntityRenderer<SheepEntity, SheepModel<SheepEntity>> renderer) {
		super(renderer);
	}

	@Override
	public void render(MatrixStack pose, IRenderTypeBuffer buffer, int packedLight, SheepEntity sheep, float limbSwing, float limbSwingAmount, float partialTicks, float age, float headYaw, float headPitch) {
		if(!sheep.isSheared() && !sheep.isInvisible()) {
			float red;
			float green;
			float blue;

			if(sheep.hasCustomName() && "jeb_".equals(sheep.getName().getContents())) {
				int i = sheep.tickCount / 25 + sheep.getId();
				int colorCount = DyeColor.values().length;
				int firstColorId = i % colorCount;
				int secondColorId = (i + 1) % colorCount;
				float interp = (sheep.tickCount % 25 + partialTicks) / 25.0F;
				float[] colorRgbOne = SheepEntity.getColorArray(DyeColor.byId(firstColorId));
				float[] colorRgbTwo = SheepEntity.getColorArray(DyeColor.byId(secondColorId));

				red = colorRgbOne[0] * (1.0F - interp) + colorRgbTwo[0] * interp;
				green = colorRgbOne[1] * (1.0F - interp) + colorRgbTwo[1] * interp;
				blue = colorRgbOne[2] * (1.0F - interp) + colorRgbTwo[2] * interp;
			}
			else {
				float[] colorRgb = SheepEntity.getColorArray(sheep.getColor());

				red = colorRgb[0];
				green = colorRgb[1];
				blue = colorRgb[2];
			}

			coloredCutoutModelCopyLayerRender(getParentModel(), model, SHEEP_FUR_LOCATION, pose, buffer, packedLight, sheep, limbSwing, limbSwingAmount, age, headYaw, headPitch, partialTicks, red, green, blue);
		}
	}
}
