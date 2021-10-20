package suszombification.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import suszombification.entity.ZombifiedPig;

public class PorkchopOnAStickItem extends Item {
	public PorkchopOnAStickItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if(!level.isClientSide) {
			Entity entity = player.getVehicle();

			if(player.isPassenger() && entity instanceof ZombifiedPig pig) {
				if(pig.boost()) {
					stack.hurtAndBreak(7, player, e -> e.broadcastBreakEvent(hand));

					if(stack.isEmpty()) {
						ItemStack fishingRodStack = new ItemStack(Items.FISHING_ROD);

						fishingRodStack.setTag(stack.getTag());
						return InteractionResultHolder.success(fishingRodStack);
					}

					return InteractionResultHolder.success(stack);
				}
			}

			player.awardStat(Stats.ITEM_USED.get(this));
		}

		return InteractionResultHolder.pass(stack);
	}
}
