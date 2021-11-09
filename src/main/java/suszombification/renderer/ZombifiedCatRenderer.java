package suszombification.renderer;

import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.ResourceLocation;
import suszombification.entity.ZombifiedCat;

public class ZombifiedCatRenderer extends CatRenderer {
	public ZombifiedCatRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public ResourceLocation getTextureLocation(CatEntity entity) {
		return entity.getResourceLocation();
	}

	@Override
	protected boolean isShaking(CatEntity cat) {
		return super.isShaking(cat) || ((ZombifiedCat)cat).isConverting();
	}
}
