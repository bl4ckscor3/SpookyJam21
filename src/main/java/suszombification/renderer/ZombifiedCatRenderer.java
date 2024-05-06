package suszombification.renderer;

import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import suszombification.entity.ZombifiedCat;
import suszombification.renderer.layers.ZombifiedCatZombieLayer;

public class ZombifiedCatRenderer extends CatRenderer {
	public ZombifiedCatRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
		addLayer(new ZombifiedCatZombieLayer<>(this, ctx.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(Cat entity) {
		return entity.getTextureId();
	}

	@Override
	protected boolean isShaking(Cat cat) {
		return super.isShaking(cat) || ((ZombifiedCat) cat).isConverting();
	}
}
