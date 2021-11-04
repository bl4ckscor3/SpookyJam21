package suszombification.renderer;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.ChickenModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import suszombification.SuspiciousZombification;
import suszombification.entity.ZombifiedChicken;

public class ZombifiedChickenRenderer extends MobRenderer<ZombifiedChicken, ChickenModel<ZombifiedChicken>> {
	private static final ResourceLocation CHICKEN_LOCATION = new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_chicken.png");

	public ZombifiedChickenRenderer(EntityRendererManager renderManager) {
		super(renderManager, new ChickenModel<>(), 0.3F);
	}

	@Override
	public ResourceLocation getTextureLocation(ZombifiedChicken entity) {
		return CHICKEN_LOCATION;
	}

	@Override
	protected float getBob(ZombifiedChicken entity, float partialTicks) {
		float flapLerp = MathHelper.lerp(partialTicks, entity.previousFlap, entity.flap);
		float flapSpeedLerp = MathHelper.lerp(partialTicks, entity.previousFlapSpeed, entity.flapSpeed);

		return (MathHelper.sin(flapLerp) + 1.0F) * flapSpeedLerp;
	}

	@Override
	protected boolean isShaking(ZombifiedChicken chicken) {
		return super.isShaking(chicken) || chicken.isConverting();
	}
}
