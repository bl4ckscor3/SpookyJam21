package suszombification.renderer;

import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import suszombification.entity.ZombifiedCat;

public class ZombifiedCatRenderer extends CatRenderer {
	public ZombifiedCatRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Override
	public ResourceLocation getTextureLocation(Cat entity) {
		return entity.getResourceLocation();
	}

	@Override
	protected boolean isShaking(Cat cat) {
		return super.isShaking(cat) || ((ZombifiedCat)cat).isConverting();
	}
}
