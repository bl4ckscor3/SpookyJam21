package suszombification.renderer;

import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.ResourceLocation;
import suszombification.SuspiciousZombification;
import suszombification.entity.ZombifiedCow;

public class ZombifiedCowRenderer extends CowRenderer {
	private static final ResourceLocation COW_LOCATION = new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_cow.png");

	public ZombifiedCowRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public ResourceLocation getTextureLocation(CowEntity entity) {
		return COW_LOCATION;
	}

	@Override
	protected boolean isShaking(CowEntity cow) {
		return super.isShaking(cow) || ((ZombifiedCow)cow).isConverting();
	}
}
