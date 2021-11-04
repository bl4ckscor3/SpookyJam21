package suszombification.renderer;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import suszombification.entity.ZombifiedAnimal;

public class ZombieHorseRenderer extends UndeadHorseRenderer {
	public ZombieHorseRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	protected boolean isShaking(AbstractHorseEntity horse) {
		return super.isShaking(horse) || ((ZombifiedAnimal)horse).isConverting();
	}
}
