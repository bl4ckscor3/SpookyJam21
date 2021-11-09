package suszombification.entity;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.google.common.collect.Maps;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.CatLieOnBedGoal;
import net.minecraft.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NonTamedTargetGoal;
import net.minecraft.entity.ai.goal.OcelotAttackGoal;
import net.minecraft.entity.ai.goal.ResetAngerGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.RangedInteger;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.TickRangeConverter;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;
import suszombification.SZEntityTypes;
import suszombification.SZLootTables;
import suszombification.SuspiciousZombification;
import suszombification.entity.ai.NearestNormalVariantTargetGoal;
import suszombification.entity.ai.SPPTemptGoal;
import suszombification.misc.AnimalUtil;

public class ZombifiedCat extends CatEntity implements IAngerable, ZombifiedAnimal {
	private static final Ingredient TEMPT_INGREDIENT = Ingredient.of(Items.STRING);
	private static final DataParameter<Boolean> DATA_CONVERTING_ID = EntityDataManager.defineId(ZombifiedCat.class, DataSerializers.BOOLEAN);
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
	private static final RangedInteger PERSISTENT_ANGER_TIME = TickRangeConverter.rangeOfSeconds(20, 39);
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;
	private SPPTemptGoal temptGoal;
	private int conversionTime;

	public ZombifiedCat(EntityType<? extends CatEntity> type, World level) {
		super(type, level);
	}

	@Override
	public ResourceLocation getResourceLocation() {
		return TEXTURE_BY_TYPE.getOrDefault(getCatType(), TEXTURE_BY_TYPE.get(0));
	}

	@Override
	protected void registerGoals() {
		temptGoal = new ZombifiedCatTemptGoal(this, 0.6D, TEMPT_INGREDIENT, true);
		goalSelector.addGoal(1, new SitGoal(this));
		goalSelector.addGoal(2, new ZombifiedCatMorningGiftGoal(this));
		goalSelector.addGoal(3, temptGoal);
		goalSelector.addGoal(5, new CatLieOnBedGoal(this, 1.1D, 8));
		goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 5.0F, false));
		goalSelector.addGoal(7, new CatSitOnBlockGoal(this, 0.8D));
		goalSelector.addGoal(9, new OcelotAttackGoal(this));
		goalSelector.addGoal(10, new BreedGoal(this, 0.8D));
		goalSelector.addGoal(11, new WaterAvoidingRandomWalkingGoal(this, 0.8D, 1.0000001E-5F));
		goalSelector.addGoal(12, new LookAtGoal(this, PlayerEntity.class, 10.0F));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(1, new NonTamedTargetGoal<>(this, RabbitEntity.class, false, null));
		targetSelector.addGoal(1, new NonTamedTargetGoal<>(this, TurtleEntity.class, false, TurtleEntity.BABY_ON_LAND_SELECTOR));
		targetSelector.addGoal(2, new NearestNormalVariantTargetGoal(this, true, false, animal -> !((CatEntity)animal).isTame()));
		targetSelector.addGoal(3, new ResetAngerGoal<>(this, false));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_CONVERTING_ID, false);
	}

	public static AttributeModifierMap.MutableAttribute createAttributes() {
		return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.25F).add(Attributes.ATTACK_DAMAGE, 4.0D);
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
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		ActionResultType returnValue = AnimalUtil.mobInteract(this, player, hand);

		if(returnValue != ActionResultType.PASS)
			return returnValue;

		return super.mobInteract(player, hand);
	}

	@Override
	public void handleEntityEvent(byte id) {
		if(!AnimalUtil.handleEntityEvent(this, id))
			super.handleEntityEvent(id);
	}

	@Override
	public CatEntity getBreedOffspring(ServerWorld level, AgeableEntity mob) {
		return SZEntityTypes.ZOMBIFIED_CAT.get().create(level);
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return AnimalUtil.isFood(stack, TEMPT_INGREDIENT);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT tag) {
		super.readAdditionalSaveData(tag);

		if(tag.contains("ConversionTime", Constants.NBT.TAG_ANY_NUMERIC) && tag.getInt("ConversionTime") > -1)
			startConverting(tag.getInt("ConversionTime"));
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("ConversionTime", isConverting() ? conversionTime : -1);
	}

	@Override
	public CreatureAttribute getMobType() {
		return CreatureAttribute.UNDEAD;
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
		setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.randomValue(random));
	}

	@Override
	public EntityType<? extends AnimalEntity> getNormalVariant() {
		return EntityType.CAT;
	}

	@Override
	public void readFromVanilla(AnimalEntity animal) {
		if(animal instanceof CatEntity) {
			CatEntity cat = (CatEntity)animal;

			setCatType(cat.getCatType());
			setTame(cat.isTame());
			setCollarColor(cat.getCollarColor());
			setOwnerUUID(cat.getOwnerUUID());
		}
	}

	@Override
	public void writeToVanilla(AnimalEntity animal) {
		if(animal instanceof CatEntity) {
			CatEntity cat = (CatEntity)animal;

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

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	static class ZombifiedCatMorningGiftGoal extends MorningGiftGoal {
		public ZombifiedCatMorningGiftGoal(CatEntity cat) {
			super(cat);
		}

		@Override
		public void giveMorningGift() {
			Random random = cat.getRandom();
			BlockPos.Mutable catPos = new BlockPos.Mutable();
			LootTable lootTable = cat.level.getServer().getLootTables().get(SZLootTables.ZOMBIFIED_CAT_MORNING_GIFT);
			LootContext.Builder lootContextBuilder = new LootContext.Builder((ServerWorld)cat.level)
					.withParameter(LootParameters.ORIGIN, cat.position())
					.withParameter(LootParameters.THIS_ENTITY, cat)
					.withRandom(random);

			catPos.set(cat.blockPosition());
			cat.randomTeleport(catPos.getX() + random.nextInt(11) - 5, catPos.getY() + random.nextInt(5) - 2, catPos.getZ() + random.nextInt(11) - 5, false);
			catPos.set(cat.blockPosition());

			for(ItemStack stack : lootTable.getRandomItems(lootContextBuilder.create(LootParameterSets.GIFT))) {
				cat.level.addFreshEntity(new ItemEntity(cat.level, (double)catPos.getX() - (double)MathHelper.sin(cat.yBodyRot * ((float)Math.PI / 180F)), catPos.getY(), (double)catPos.getZ() + (double)MathHelper.cos(cat.yBodyRot * ((float)Math.PI / 180F)), stack));
			}
		}
	}

	static class ZombifiedCatTemptGoal extends SPPTemptGoal {
		private PlayerEntity selectedPlayer;
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
