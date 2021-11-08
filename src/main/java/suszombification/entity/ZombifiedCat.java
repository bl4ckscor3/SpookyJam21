package suszombification.entity;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.google.common.collect.Maps;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.CatLieOnBedGoal;
import net.minecraft.world.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import suszombification.SZEntityTypes;
import suszombification.SZLootTables;
import suszombification.SuspiciousZombification;
import suszombification.entity.ai.NearestNormalVariantTargetGoal;
import suszombification.entity.ai.SPPTemptGoal;
import suszombification.misc.AnimalUtil;

public class ZombifiedCat extends Cat implements NeutralMob, ZombifiedAnimal {
	public static final Map<Integer, ResourceLocation> TEXTURE_BY_TYPE = Util.make(Maps.newHashMap(), map -> {
		map.put(0, new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_cat/tabby.png"));
		map.put(1, new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_cat/black.png"));
		map.put(2, new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_cat/red.png"));
		map.put(3, new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_cat/siamese.png"));
		map.put(4, new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_cat/british_shorthair.png"));
		map.put(5, new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_cat/calico.png"));
		map.put(6, new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_cat/persian.png"));
		map.put(7, new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_cat/ragdoll.png"));
		map.put(8, new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_cat/white.png"));
		map.put(9, new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_cat/jellie.png"));
		map.put(10, new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_cat/all_black.png"));
	});
	private static final Ingredient TEMPT_INGREDIENT = Ingredient.of(Items.STRING);
	private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(ZombifiedCat.class, EntityDataSerializers.BOOLEAN);
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;
	private SPPTemptGoal temptGoal;
	private int conversionTime;

	public ZombifiedCat(EntityType<? extends Cat> type, Level level) {
		super(type, level);
	}

	@Override
	public ResourceLocation getResourceLocation() {
		return TEXTURE_BY_TYPE.getOrDefault(getCatType(), TEXTURE_BY_TYPE.get(0));
	}

	@Override
	protected void registerGoals() {
		temptGoal = new ZombifiedCatTemptGoal(this, 0.6D, TEMPT_INGREDIENT, true);
		goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
		goalSelector.addGoal(2, new ZombifiedCatRelaxOnOwnerGoal(this));
		goalSelector.addGoal(3, temptGoal);
		goalSelector.addGoal(5, new CatLieOnBedGoal(this, 1.1D, 8));
		goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 5.0F, false));
		goalSelector.addGoal(7, new CatSitOnBlockGoal(this, 0.8D));
		goalSelector.addGoal(9, new OcelotAttackGoal(this));
		goalSelector.addGoal(10, new BreedGoal(this, 0.8D));
		goalSelector.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 0.8D, 1.0000001E-5F));
		goalSelector.addGoal(12, new LookAtPlayerGoal(this, Player.class, 10.0F));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(this, Rabbit.class, false, null));
		targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
		targetSelector.addGoal(2, new NearestNormalVariantTargetGoal(this, true, false, animal -> !((Cat)animal).isTame()));
		targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_CONVERTING_ID, false);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.25F).add(Attributes.ATTACK_DAMAGE, 4.0D);
	}

	@Override
	public void tick() {
		AnimalUtil.tick(this);
		super.tick();
	}

	@Override
	public float getVoicePitch() {
		return isBaby() ? (random.nextFloat() - random.nextFloat()) * 0.2F + 0.5F : (random.nextFloat() - random.nextFloat()) * 0.2F;
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		InteractionResult returnValue = AnimalUtil.mobInteract(this, player, hand);

		if(returnValue != InteractionResult.PASS)
			return returnValue;

		return super.mobInteract(player, hand);
	}

	@Override
	public void handleEntityEvent(byte id) {
		if(!AnimalUtil.handleEntityEvent(this, id))
			super.handleEntityEvent(id);
	}

	@Override
	public Cat getBreedOffspring(ServerLevel level, AgeableMob mob) {
		return SZEntityTypes.ZOMBIFIED_CAT.get().create(level);
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return AnimalUtil.isFood(stack, TEMPT_INGREDIENT);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);

		if(tag.contains("ConversionTime", Tag.TAG_ANY_NUMERIC) && tag.getInt("ConversionTime") > -1)
			startConverting(tag.getInt("ConversionTime"));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("ConversionTime", isConverting() ? conversionTime : -1);
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	@Override
	protected void reassessTameGoals() {
	}

	@Override
	public int getRemainingPersistentAngerTime() {
		return remainingPersistentAngerTime;
	}

	@Override
	public void setRemainingPersistentAngerTime(int time) {
		remainingPersistentAngerTime = time;
	}

	@Override
	public UUID getPersistentAngerTarget() {
		return persistentAngerTarget;
	}

	@Override
	public void setPersistentAngerTarget(UUID target) {
		persistentAngerTarget = target;
	}

	@Override
	public void startPersistentAngerTimer() {
		setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(random));
	}

	@Override
	public EntityType<? extends Animal> getNormalVariant() {
		return EntityType.CAT;
	}

	@Override
	public void readFromVanilla(Animal animal) {
		if(animal instanceof Cat cat) {
			setCatType(cat.getCatType());
			setTame(cat.isTame());
			setCollarColor(cat.getCollarColor());
			setOwnerUUID(cat.getOwnerUUID());
		}
	}

	@Override
	public void writeToVanilla(Animal animal) {
		if(animal instanceof Cat cat) {
			cat.setCatType(getCatType());
			cat.setTame(isTame());
			cat.setCollarColor(getCollarColor());
			cat.setOwnerUUID(getOwnerUUID());
		}
	}

	@Override
	public boolean isConverting() {
		return getEntityData().get(DATA_CONVERTING_ID);
	}

	@Override
	public void setConverting() {
		getEntityData().set(DATA_CONVERTING_ID, true);
	}

	@Override
	public void setConversionTime(int conversionTime) {
		this.conversionTime = conversionTime;
	}

	@Override
	public int getConversionTime() {
		return conversionTime;
	}

	static class ZombifiedCatRelaxOnOwnerGoal extends CatRelaxOnOwnerGoal {
		public ZombifiedCatRelaxOnOwnerGoal(Cat cat) {
			super(cat);
		}

		@Override
		public void giveMorningGift() {
			Random random = cat.getRandom();
			BlockPos.MutableBlockPos catPos = new BlockPos.MutableBlockPos();
			LootTable lootTable = cat.level.getServer().getLootTables().get(SZLootTables.ZOMBIFIED_CAT_MORNING_GIFT);
			LootContext.Builder lootContextBuilder = new LootContext.Builder((ServerLevel)this.cat.level)
					.withParameter(LootContextParams.ORIGIN, cat.position())
					.withParameter(LootContextParams.THIS_ENTITY, cat)
					.withRandom(random);

			catPos.set(cat.blockPosition());
			cat.randomTeleport(catPos.getX() + random.nextInt(11) - 5, catPos.getY() + random.nextInt(5) - 2, catPos.getZ() + random.nextInt(11) - 5, false);
			catPos.set(cat.blockPosition());

			for(ItemStack stack : lootTable.getRandomItems(lootContextBuilder.create(LootContextParamSets.GIFT))) {
				cat.level.addFreshEntity(new ItemEntity(cat.level, (double)catPos.getX() - (double)Mth.sin(cat.yBodyRot * ((float)Math.PI / 180F)), catPos.getY(), (double)catPos.getZ() + (double)Mth.cos(cat.yBodyRot * ((float)Math.PI / 180F)), stack));
			}
		}
	}

	static class ZombifiedCatTemptGoal extends SPPTemptGoal {
		private Player selectedPlayer;
		private final ZombifiedCat cat;

		public ZombifiedCatTemptGoal(ZombifiedCat mob, double speedModifier, Ingredient items, boolean canScare) {
			super(mob, speedModifier, items, canScare);
			this.cat = mob;
		}

		@Override
		public void tick() {
			super.tick();

			if(selectedPlayer == null && mob.getRandom().nextInt(600) == 0)
				selectedPlayer = player;
			else if(mob.getRandom().nextInt(500) == 0)
				selectedPlayer = null;
		}

		@Override
		protected boolean canScare() {
			return (selectedPlayer == null || !selectedPlayer.equals(player)) && super.canScare();
		}

		@Override
		public boolean canUse() {
			return super.canUse() && !cat.isTame();
		}
	}
}
