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
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import suszombification.SuspiciousZombification;

public class ZombifiedSheepFurLayer extends SheepFurLayer {
	private static final ResourceLocation SHEEP_FUR_LOCATION = SuspiciousZombification.resLoc("textures/entity/zombified_sheep/zombified_sheep_fur.png");
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
					model.renderToBuffer(pose, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(sheep, 0.0F), 0xFF000000);
				}
			}
			else {
				int colorToUse;

				if (sheep.hasCustomName() && "jeb_".equals(sheep.getName().getString())) {
					int speed = 25;
					int colorTick = sheep.tickCount / speed + sheep.getId();
					int colorCount = DyeColor.values().length;
					int currentColorIndex = colorTick % colorCount;
					int nextColorIndex = (colorTick + 1) % colorCount;
					float interp = (sheep.tickCount % speed + partialTicks) / speed;
					int currentColor = Sheep.getColor(DyeColor.byId(currentColorIndex));
					int nextColor = Sheep.getColor(DyeColor.byId(nextColorIndex));

					colorToUse = ARGB.lerp(interp, currentColor, nextColor);
				}
				else
					colorToUse = Sheep.getColor(sheep.getColor());

				coloredCutoutModelCopyLayerRender(getParentModel(), model, SHEEP_FUR_LOCATION, pose, buffer, packedLight, sheep, limbSwing, limbSwingAmount, age, headYaw, headPitch, partialTicks, colorToUse);
			}
		}
	}
}
