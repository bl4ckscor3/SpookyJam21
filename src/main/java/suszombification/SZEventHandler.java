package suszombification;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.horse.ZombieHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.TickRangeConverter;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.feature.structure.PillagerOutpostStructure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import suszombification.entity.ZombifiedAnimal;
import suszombification.entity.ZombifiedCat;
import suszombification.entity.ZombifiedChicken;
import suszombification.entity.ZombifiedCow;
import suszombification.entity.ZombifiedPig;
import suszombification.entity.ZombifiedSheep;
import suszombification.item.SuspiciousPumpkinPieItem;
import suszombification.misc.SuspiciousRitual;
import suszombification.registration.SZConfiguredStructures;
import suszombification.registration.SZEffects;
import suszombification.registration.SZItems;

@EventBusSubscriber(modid = SuspiciousZombification.MODID)
public class SZEventHandler {
	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();

		if(entity instanceof CreatureEntity) {
			CreatureEntity mob = (CreatureEntity)entity;
			EntityType<?> type = mob.getType();

			if(type == EntityType.CAT)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombifiedCat.class, 6.0F, 1.0F, 1.2F, animal -> !((ZombifiedCat)animal).isTame()));
			else if(type == EntityType.CHICKEN)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombifiedChicken.class, 4.0F, 1.0F, 1.2F));
			else if(type == EntityType.COW)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombifiedCow.class, 4.0F, 1.0F, 1.2F));
			else if(type == EntityType.PIG)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombifiedPig.class, 4.0F, 1.0F, 1.2F));
			else if(type == EntityType.SHEEP)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombifiedSheep.class, 4.0F, 1.0F, 1.2F));
			else if(type == EntityType.HORSE)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombieHorseEntity.class, 4.0F, 1.0F, 1.2F));
		}
	}

	@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
		Entity entity = event.getTarget();
		PlayerEntity player = event.getPlayer();

		if(entity instanceof AnimalEntity && ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.containsKey(((AnimalEntity)entity).getType())) {
			AnimalEntity animal = (AnimalEntity)entity;
			ItemStack stack = player.getItemInHand(event.getHand());

			if(stack.getItem() == SZItems.SUSPICIOUS_PUMPKIN_PIE.get() && SuspiciousPumpkinPieItem.hasIngredient(stack, Items.ROTTEN_FLESH) && !animal.hasEffect(SZEffects.DECOMPOSING.get())) {
				animal.addEffect(new EffectInstance(SZEffects.DECOMPOSING.get(), TickRangeConverter.rangeOfSeconds(50, 70).randomValue(animal.getRandom())));

				if(!player.abilities.instabuild)
					stack.shrink(1);

				event.setCanceled(true);
				event.setCancellationResult(ActionResultType.SUCCESS);
			}
		}
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		PlayerEntity player = event.getPlayer();

		if(player.getItemInHand(event.getHand()).getItem() == Items.LEAD)
			SuspiciousRitual.maybeSendInfoMessages(null, event.getWorld(), event.getPos(), player);
	}

	@SubscribeEvent
	public static void onEntityConvert(LivingConversionEvent.Pre event) {
		if(event.getEntityLiving() instanceof ZombifiedPig && event.getOutcome() == EntityType.ZOMBIFIED_PIGLIN)
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onLivingSetAttackTarget(LivingSetAttackTargetEvent event) {
		if(event.getTarget() != null && event.getTarget().hasEffect(SZEffects.ZOMBIES_GRACE.get()) && event.getEntityLiving().getType().is(SZTags.EntityTypes.AFFECTED_BY_ZOMBIES_GRACE))
			((MobEntity)event.getEntityLiving()).setTarget(null);
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		LivingEntity livingEntity = event.getEntityLiving();
		Entity killer = event.getSource().getEntity();
		World level = livingEntity.level;

		if(!level.isClientSide && (level.getDifficulty() == Difficulty.NORMAL || level.getDifficulty() == Difficulty.HARD) && killer instanceof ZombifiedAnimal) {
			EntityType<? extends MobEntity> conversionType = (EntityType<? extends MobEntity>)killer.getType();

			if(livingEntity instanceof AnimalEntity && ((AnimalEntity)livingEntity).getType() == ((ZombifiedAnimal)killer).getNormalVariant() && ForgeEventFactory.canLivingConvert(livingEntity, conversionType, timer -> {})) {
				if(level.getDifficulty() != Difficulty.HARD && level.random.nextBoolean())
					return;

				AnimalEntity killedEntity = (AnimalEntity)livingEntity;
				MobEntity convertedAnimal = killedEntity.convertTo(conversionType, false);

				convertedAnimal.finalizeSpawn((ServerWorld)level, level.getCurrentDifficultyAt(convertedAnimal.blockPosition()), SpawnReason.CONVERSION, null, null);
				((ZombifiedAnimal)convertedAnimal).readFromVanilla(killedEntity);
				ForgeEventFactory.onLivingConvert(livingEntity, convertedAnimal);

				if(!killer.isSilent())
					level.levelEvent(null, Constants.WorldEvents.ZOMBIE_INFECT_SOUND, killer.blockPosition(), 0);
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		if(event.getOriginal().hasEffect(SZEffects.ZOMBIES_CURSE.get()))
			event.getPlayer().addEffect(new EffectInstance(SZEffects.ZOMBIES_CURSE.get(), Integer.MAX_VALUE));
	}

	@SubscribeEvent
	public static void onLivingFall(LivingFallEvent event) {
		if(event.getEntityLiving().hasEffect(SZEffects.CUSHION.get()) && event.getDistance() > 3.0F) {
			EffectInstance cushionEffect = event.getEntityLiving().getEffect(SZEffects.CUSHION.get());

			event.setDamageMultiplier(Math.max(event.getDamageMultiplier() * (0.3F - cushionEffect.getAmplifier() * 0.2F), 0));
			event.getEntityLiving().playSound(SoundEvents.SLIME_SQUISH, 1.0F, 1.5F);
		}
	}

	@SubscribeEvent
	public static void onKnockback(LivingKnockBackEvent event) {
		if(event.getEntityLiving().hasEffect(SZEffects.CUSHION.get())) {
			EffectInstance cushionEffect = event.getEntityLiving().getEffect(SZEffects.CUSHION.get());

			event.setStrength(Math.max(event.getOriginalStrength() * (0.3F - cushionEffect.getAmplifier() * 0.2F), 0));
			event.getEntityLiving().playSound(SoundEvents.SLIME_SQUISH, 1.0F, 1.5F);
		}
	}

	@SubscribeEvent
	public static void onBiomeLoad(BiomeLoadingEvent event) {
		if(event.getGeneration().getStructures().stream().anyMatch(csf -> csf.get().feature instanceof PillagerOutpostStructure)) {
			if(event.getCategory() == Category.DESERT)
				event.getGeneration().addStructureStart(SZConfiguredStructures.DESERT_ZOMBIE_COVE);
			else
				event.getGeneration().addStructureStart(SZConfiguredStructures.ZOMBIE_COVE);
		}
	}
}
