package suszombification.renderer;

import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import suszombification.SuspiciousZombification;
import suszombification.entity.ZombifiedSheep;
import suszombification.renderer.layers.ZombifiedSheepFurLayer;

public class ZombifiedSheepRenderer extends MobRenderer<Sheep, SheepModel<Sheep>> {
	private static final ResourceLocation SHEEP_LOCATION = new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_sheep/zombified_sheep.png");

	public ZombifiedSheepRenderer(Context ctx) {
		super(ctx, new SheepModel<>(ctx.bakeLayer(ModelLayers.SHEEP)), 0.7F);
		addLayer(new ZombifiedSheepFurLayer(this, ctx.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(Sheep entity) {
		return SHEEP_LOCATION;
	}

	@Override
	protected boolean isShaking(Sheep sheep) {
		return super.isShaking(sheep) || ((ZombifiedSheep) sheep).isConverting();
	}
}
