package suszombification.renderer;

import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;
import suszombification.SuspiciousZombification;
import suszombification.entity.ZombifiedCow;

public class ZombifiedCowRenderer extends CowRenderer {
	private static final ResourceLocation COW_LOCATION = new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_cow.png");

	public ZombifiedCowRenderer(Context ctx) {
		super(ctx);
	}

	@Override
	public ResourceLocation getTextureLocation(Cow entity) {
		return COW_LOCATION;
	}

	@Override
	protected boolean isShaking(Cow cow) {
		return super.isShaking(cow) || ((ZombifiedCow) cow).isConverting();
	}
}
