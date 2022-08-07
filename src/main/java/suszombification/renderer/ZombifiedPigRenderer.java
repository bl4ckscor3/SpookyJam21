package suszombification.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;
import suszombification.SuspiciousZombification;
import suszombification.entity.ZombifiedPig;

public class ZombifiedPigRenderer extends PigRenderer {
	private static final ResourceLocation PIG_LOCATION = new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_pig.png");

	public ZombifiedPigRenderer(Context ctx) {
		super(ctx);
	}

	@Override
	public ResourceLocation getTextureLocation(Pig entity) {
		return PIG_LOCATION;
	}

	@Override
	protected boolean isShaking(Pig pig) {
		return super.isShaking(pig) || ((ZombifiedPig) pig).isConverting();
	}
}
