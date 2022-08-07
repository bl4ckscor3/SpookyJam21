package suszombification.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import suszombification.SuspiciousZombification;

public class ZombifiedSheepFurLayer extends SheepFurLayer {
	private static final ResourceLocation SHEEP_FUR_LOCATION = new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_sheep/zombified_sheep_fur.png");
	private final SheepFurModel<Sheep> model;

	public ZombifiedSheepFurLayer(RenderLayerParent<Sheep, SheepModel<Sheep>> renderer, EntityModelSet modelSet) {
		super(renderer, modelSet);
		this.model = new SheepFurModel<>(modelSet.bakeLayer(ModelLayers.SHEEP_FUR));
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, Sheep sheep, float limbSwing, float limbSwingAmount, float partialTicks, float age, float headYaw, float headPitch) {
		if (!sheep.isSheared()) {
			if (sheep.isInvisible()) {
				Minecraft minecraft = Minecraft.getInstance();
				boolean glowing = minecraft.shouldEntityAppearGlowing(sheep);

				if (glowing) {
					VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.outline(SHEEP_FUR_LOCATION));

					getParentModel().copyPropertiesTo(model);
					model.prepareMobModel(sheep, limbSwing, limbSwingAmount, partialTicks);
					model.setupAnim(sheep, limbSwing, limbSwingAmount, age, headYaw, headPitch);
					model.renderToBuffer(pose, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(sheep, 0.0F), 0.0F, 0.0F, 0.0F, 1.0F);
				}
			}
			else {
				float red;
				float green;
				float blue;

				if (sheep.hasCustomName() && "jeb_".equals(sheep.getName().getContents())) {
					int i = sheep.tickCount / 25 + sheep.getId();
					int colorCount = DyeColor.values().length;
					int firstColorId = i % colorCount;
					int secondColorId = (i + 1) % colorCount;
					float interp = (sheep.tickCount % 25 + partialTicks) / 25.0F;
					float[] colorRgbOne = Sheep.getColorArray(DyeColor.byId(firstColorId));
					float[] colorRgbTwo = Sheep.getColorArray(DyeColor.byId(secondColorId));

					red = colorRgbOne[0] * (1.0F - interp) + colorRgbTwo[0] * interp;
					green = colorRgbOne[1] * (1.0F - interp) + colorRgbTwo[1] * interp;
					blue = colorRgbOne[2] * (1.0F - interp) + colorRgbTwo[2] * interp;
				}
				else {
					float[] colorRgb = Sheep.getColorArray(sheep.getColor());

					red = colorRgb[0];
					green = colorRgb[1];
					blue = colorRgb[2];
				}

				coloredCutoutModelCopyLayerRender(getParentModel(), model, SHEEP_FUR_LOCATION, pose, buffer, packedLight, sheep, limbSwing, limbSwingAmount, age, headYaw, headPitch, partialTicks, red, green, blue);
			}
		}
	}
}
