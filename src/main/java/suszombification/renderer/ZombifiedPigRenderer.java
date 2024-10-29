package suszombification.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.client.renderer.entity.state.PigRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;
import suszombification.SuspiciousZombification;
import suszombification.entity.ZombifiedPig;

public class ZombifiedPigRenderer extends PigRenderer {
	private static final ResourceLocation PIG_LOCATION = SuspiciousZombification.resLoc("textures/entity/zombified_pig.png");

	public ZombifiedPigRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Override
	public ResourceLocation getTextureLocation(PigRenderState renderState) {
		return PIG_LOCATION;
	}

	@Override
	public PigRenderState createRenderState() {
		return new ZombifiedRenderState.Pig();
	}

	@Override
	public void extractRenderState(Pig pig, PigRenderState renderState, float partialTicks) {
		super.extractRenderState(pig, renderState, partialTicks);
		((ZombifiedRenderState.Pig) renderState).isConverting = ((ZombifiedPig) pig).isConverting();
	}

	@Override
	protected boolean isShaking(PigRenderState renderState) {
		return super.isShaking(renderState) || ((ZombifiedRenderState.Pig) renderState).isConverting;
	}
}
