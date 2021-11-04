package suszombification.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import suszombification.entity.ZombifiedPig;

public class PorkchopOnAStickItem extends Item {
	public PorkchopOnAStickItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if(!level.isClientSide) {
			Entity entity = player.getVehicle();

			if(player.isPassenger() && entity instanceof ZombifiedPig pig) {
				if(pig.boost()) {
					stack.hurtAndBreak(7, player, e -> e.broadcastBreakEvent(hand));

					if(stack.isEmpty()) {
						ItemStack fishingRodStack = new ItemStack(Items.FISHING_ROD);

						fishingRodStack.setTag(stack.getTag());
						return ActionResult.success(fishingRodStack);
					}

					return ActionResult.success(stack);
				}
			}

			player.awardStat(Stats.ITEM_USED.get(this));
		}

		return ActionResult.pass(stack);
	}
}
