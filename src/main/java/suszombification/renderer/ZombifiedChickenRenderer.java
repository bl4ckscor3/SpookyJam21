package suszombification.renderer;

import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import suszombification.SuspiciousZombification;
import suszombification.entity.ZombifiedChicken;

public class ZombifiedChickenRenderer extends AgeableMobRenderer<ZombifiedChicken, ZombifiedRenderState.Chicken, ChickenModel> {
	private static final ResourceLocation CHICKEN_LOCATION = SuspiciousZombification.resLoc("textures/entity/zombified_chicken.png");

	public ZombifiedChickenRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new ChickenModel(ctx.bakeLayer(ModelLayers.CHICKEN)), new ChickenModel(ctx.bakeLayer(ModelLayers.CHICKEN_BABY)), 0.3F);
	}

	@Override
	public ResourceLocation getTextureLocation(ZombifiedRenderState.Chicken renderState) {
		return CHICKEN_LOCATION;
	}

	@Override
	public ZombifiedRenderState.Chicken createRenderState() {
		return new ZombifiedRenderState.Chicken();
	}

	@Override
	public void extractRenderState(ZombifiedChicken chicken, ZombifiedRenderState.Chicken renderState, float partialTicks) {
		super.extractRenderState(chicken, renderState, partialTicks);
		renderState.isConverting = chicken.isConverting();
		renderState.flap = Mth.lerp(partialTicks, chicken.getPreviousFlap(), chicken.getFlap());
		renderState.flapSpeed = Mth.lerp(partialTicks, chicken.getPreviousFlapSpeed(), chicken.getFlapSpeed());
	}

	@Override
	protected boolean isShaking(ZombifiedRenderState.Chicken renderState) {
		return super.isShaking(renderState) || renderState.isConverting;
	}
}
