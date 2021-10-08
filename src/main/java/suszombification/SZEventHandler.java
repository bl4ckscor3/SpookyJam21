package suszombification;

import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import suszombification.entity.ZombifiedChicken;

@EventBusSubscriber(modid = SuspiciousZombification.MODID)
public class SZEventHandler {
	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if(event.getEntity() instanceof Chicken chicken)
			chicken.goalSelector.addGoal(0, new AvoidEntityGoal<>(chicken, ZombifiedChicken.class, 4.0F, 1.0F, 1.2F));
	}
}
