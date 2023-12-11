package suszombification.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import suszombification.entity.ZombifiedAnimal;

@Mixin(UndeadHorseRenderer.class)
public abstract class UndeadHorseRendererMixin extends AbstractHorseRenderer<AbstractHorse, HorseModel<AbstractHorse>> {
	public UndeadHorseRendererMixin(Context ctx, HorseModel<AbstractHorse> model, float scale) {
		super(ctx, model, scale);
	}

	@Override
	public boolean isShaking(AbstractHorse horse) {
		return super.isShaking(horse) || (horse instanceof ZombifiedAnimal zombifiedAnimal && zombifiedAnimal.isConverting());
	}
}
