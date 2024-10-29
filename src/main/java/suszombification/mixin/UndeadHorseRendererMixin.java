package suszombification.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import suszombification.entity.ZombifiedAnimal;
import suszombification.renderer.ZombifiedRenderState;

@Mixin(UndeadHorseRenderer.class)
public abstract class UndeadHorseRendererMixin extends AbstractHorseRenderer<AbstractHorse, EquineRenderState, HorseModel> {
	public UndeadHorseRendererMixin(EntityRendererProvider.Context ctx, ModelLayerLocation layer, ModelLayerLocation babyLayer, boolean isSkeletonHorse) {
		super(ctx, null, null, 1.0F);
	}

	@Override
	public EquineRenderState createRenderState() {
		return new ZombifiedRenderState.Horse();
	}

	@Override
	public void extractRenderState(AbstractHorse horse, EquineRenderState renderState, float partialTicks) {
		super.extractRenderState(horse, renderState, partialTicks);
		((ZombifiedRenderState.Horse) renderState).isConverting = horse instanceof ZombifiedAnimal zombifiedAnimal && zombifiedAnimal.isConverting();
	}

	@Override
	protected boolean isShaking(EquineRenderState renderState) {
		return super.isShaking(renderState) || ((ZombifiedRenderState.Horse) renderState).isConverting;
	}
}
