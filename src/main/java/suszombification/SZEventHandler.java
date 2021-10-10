package suszombification;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import suszombification.entity.ZombifiedChicken;
import suszombification.entity.ZombifiedCow;
import suszombification.entity.ZombifiedPig;
import suszombification.entity.ZombifiedSheep;

@EventBusSubscriber(modid = SuspiciousZombification.MODID)
public class SZEventHandler {
	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();

		if (entity instanceof PathfinderMob mob) {
			if (mob.getType() == EntityType.CHICKEN)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombifiedChicken.class, 4.0F, 1.0F, 1.2F));
			else if (mob.getType() == EntityType.COW)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombifiedCow.class, 4.0F, 1.0F, 1.2F));
			else if (mob.getType() == EntityType.PIG)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombifiedPig.class, 4.0F, 1.0F, 1.2F));
			else if (mob.getType() == EntityType.SHEEP)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombifiedSheep.class, 4.0F, 1.0F, 1.2F));
		}
	}

	@SubscribeEvent
	public static void onEntityConvert(LivingConversionEvent.Pre event) {
		if (event.getEntityLiving() instanceof ZombifiedPig && event.getOutcome() == EntityType.ZOMBIFIED_PIGLIN) {
			event.setCanceled(true);
		}
	}
}
