package suszombification.renderer;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import suszombification.entity.ZombifiedAnimal;

public class ZombieHorseRenderer extends UndeadHorseRenderer {
	public ZombieHorseRenderer(Context ctx) {
		super(ctx, ModelLayers.ZOMBIE_HORSE);
	}

	@Override
	protected boolean isShaking(AbstractHorse horse) {
		return super.isShaking(horse) || ((ZombifiedAnimal)horse).isConverting();
	}
}
