package net.dehydration.item;

import net.dehydration.hydration.HydrationUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class WaterBowlItem extends Item {

	public final boolean isContaminated;

	public WaterBowlItem(Settings settings, boolean isContaminated) {
		super(settings);
		this.isContaminated = isContaminated;
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		ItemStack itemStack = super.finishUsing(stack, world, user);

		if (!(user instanceof PlayerEntity)) {
			return itemStack;
		}

		PlayerEntity player = (PlayerEntity) user;

		if (player.isCreative() || player.isSpectator()) {
			return itemStack;
		}

		if (!world.isClient() && this.isContaminated) {
			HydrationUtil.addDefaultThirstEffectToPlayer(player);
		}

		return new ItemStack(Items.BOWL);
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 32;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}

}
