package suszombification.renderer;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.ResourceLocation;
import suszombification.SuspiciousZombification;
import suszombification.entity.ZombifiedPig;

public class ZombifiedPigRenderer extends PigRenderer {
	private static final ResourceLocation PIG_LOCATION = new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_pig.png");

	public ZombifiedPigRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public ResourceLocation getTextureLocation(PigEntity entity) {
		return PIG_LOCATION;
	}

	@Override
	protected boolean isShaking(PigEntity pig) {
		return super.isShaking(pig) || ((ZombifiedPig)pig).isConverting();
	}
}
