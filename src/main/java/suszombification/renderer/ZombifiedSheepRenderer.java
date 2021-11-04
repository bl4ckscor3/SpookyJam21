package suszombification.renderer;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SheepModel;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.ResourceLocation;
import suszombification.SuspiciousZombification;
import suszombification.entity.ZombifiedSheep;
import suszombification.renderer.layers.ZombifiedSheepFurLayer;

public class ZombifiedSheepRenderer extends MobRenderer<SheepEntity, SheepModel<SheepEntity>> {
	private static final ResourceLocation SHEEP_LOCATION = new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_sheep/zombified_sheep.png");

	public ZombifiedSheepRenderer(EntityRendererManager renderManager) {
		super(renderManager, new SheepModel<>(), 0.7F);
		addLayer(new ZombifiedSheepFurLayer(this));
	}

	@Override
	public ResourceLocation getTextureLocation(SheepEntity entity) {
		return SHEEP_LOCATION;
	}

	@Override
	protected boolean isShaking(SheepEntity sheep) {
		return super.isShaking(sheep) || ((ZombifiedSheep)sheep).isConverting();
	}
}
