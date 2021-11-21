package suszombification.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import net.minecraft.client.renderer.entity.model.HorseModel;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import suszombification.entity.ZombifiedAnimal;

@Mixin(UndeadHorseRenderer.class)
public abstract class UndeadHorseRendererMixin extends AbstractHorseRenderer<AbstractHorseEntity, HorseModel<AbstractHorseEntity>>{
	public UndeadHorseRendererMixin(EntityRendererManager manager, HorseModel<AbstractHorseEntity> model, float scale) {
		super(manager, model, scale);
	}

	@Override
	public boolean isShaking(AbstractHorseEntity horse) {
		return super.isShaking(horse) || (horse instanceof ZombifiedAnimal && ((ZombifiedAnimal)horse).isConverting());
	}
}
