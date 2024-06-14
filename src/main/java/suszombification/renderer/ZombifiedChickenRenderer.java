package suszombification.renderer;

import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import suszombification.SuspiciousZombification;
import suszombification.entity.ZombifiedChicken;

public class ZombifiedChickenRenderer extends MobRenderer<ZombifiedChicken, ChickenModel<ZombifiedChicken>> {
	private static final ResourceLocation CHICKEN_LOCATION = SuspiciousZombification.resLoc("textures/entity/zombified_chicken.png");

	public ZombifiedChickenRenderer(Context ctx) {
		super(ctx, new ChickenModel<>(ctx.bakeLayer(ModelLayers.CHICKEN)), 0.3F);
	}

	@Override
	public ResourceLocation getTextureLocation(ZombifiedChicken entity) {
		return CHICKEN_LOCATION;
	}

	@Override
	protected float getBob(ZombifiedChicken entity, float partialTicks) {
		float flapLerp = Mth.lerp(partialTicks, entity.getPreviousFlap(), entity.getFlap());
		float flapSpeedLerp = Mth.lerp(partialTicks, entity.getPreviousFlapSpeed(), entity.getFlapSpeed());

		return (Mth.sin(flapLerp) + 1.0F) * flapSpeedLerp;
	}

	@Override
	protected boolean isShaking(ZombifiedChicken chicken) {
		return super.isShaking(chicken) || chicken.isConverting();
	}
}
