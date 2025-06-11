package net.dehydration.event;

import net.dehydration.access.HydrationManagerAccess;
import net.dehydration.access.PlayerAccess;
import net.dehydration.hydration.HydrationManager;
import net.dehydration.hydration.HydrationUtil;
import net.dehydration.mod.ModConfig;
import net.dehydration.mod.ModSounds;
import net.dehydration.mod.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEventUtil {

	public static ActionResult handleUseBlockEvent(PlayerEntity player, World world, Hand hand, BlockHitResult result) {
		if (!player.isCreative() && !player.isSpectator() && player.isSneaking()
				&& (player.getMainHandStack().isEmpty() || player.getMainHandStack().isOf(Items.BOWL))) {
			HitResult hitResult = player.raycast(1.5D, 0.0F, true);
			BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
			if (world.canPlayerModifyAt(player, blockPos) && world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
				if (world.getFluidState(blockPos).isStill() || ModConfig.CONFIG.allowDrinkingFlowingWaterBlocks) {
					HydrationManager hydrationManager = ((HydrationManagerAccess) player).getHydrationManager();
					if (hydrationManager.isNotFull()) {
						int drinkTime = ((PlayerAccess) player).getDrinkTime();
						if (world.isClient() && drinkTime % 3 == 0)
							player.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.5f,
									world.getRandom().nextFloat() * 0.1f + 0.9f);

						if (drinkTime > 20) {
							if (!world.isClient()) {
								if (!ModConfig.CONFIG.allowDrinkingFlowingWaterBlocks
										&& world.getFluidState(blockPos).isStill())
									if (world.getBlockState(blockPos).contains(Properties.WATERLOGGED)) {
										world.setBlockState(blockPos,
												world.getBlockState(blockPos).with(Properties.WATERLOGGED, false));
									} else {
										world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
									}
								hydrationManager.add(ModConfig.CONFIG.waterBlockHydrationValue);
								if (!world.getFluidState(blockPos).isIn(ModTags.PURIFIED_WATER)) {
									HydrationUtil.addDefaultThirstEffectToPlayer(player);
								}
							} else {
								world.playSound(player, player.getX(), player.getY(), player.getZ(),
										ModSounds.WATER_SIP_EVENT, SoundCategory.PLAYERS, 1.0F,
										0.9F + (world.getRandom().nextFloat() / 5F));
							}
							((PlayerAccess) player).setDrinkTime(0);
							return ActionResult.SUCCESS;
						}

						((PlayerAccess) player).setDrinkTime(drinkTime + 1);
					}
				}
			}
			return ActionResult.PASS;
		}
		return ActionResult.PASS;
	}

}
