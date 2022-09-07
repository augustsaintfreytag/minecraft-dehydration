package net.dehydration.init;

import net.dehydration.access.PlayerAccess;
import net.dehydration.access.ServerPlayerAccess;
import net.dehydration.access.ThirstManagerAccess;
import net.dehydration.thirst.ThirstManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class EventInit {

    public static void init() {
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
            ((ServerPlayerAccess) player).compatSync();
        });

        UseBlockCallback.EVENT.register((player, world, hand, result) -> {
            if (!player.isCreative() && !player.isSpectator() && player.isSneaking() && player.getMainHandStack().isEmpty()) {
                HitResult hitResult = player.raycast(1.5D, 0.0F, true);
                BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
                if (world.canPlayerModifyAt(player, blockPos) && world.getFluidState(blockPos).isIn(FluidTags.WATER)
                        && (world.getFluidState(blockPos).isStill() || ConfigInit.CONFIG.allow_non_flowing_water_sip)) {
                    ThirstManager thirstManager = ((ThirstManagerAccess) player).getThirstManager(player);
                    if (thirstManager.isNotFull()) {
                        int drinkTime = ((PlayerAccess) player).getDrinkTime();
                        if (world.isClient && drinkTime % 3 == 0)
                            player.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.5f, world.random.nextFloat() * 0.1f + 0.9f);

                        if (drinkTime > 20) {
                            if (!world.isClient) {
                                thirstManager.add(ConfigInit.CONFIG.water_souce_quench);
                                if (world.random.nextFloat() <= ConfigInit.CONFIG.water_sip_thirst_chance)
                                    player.addStatusEffect(new StatusEffectInstance(EffectInit.THIRST, ConfigInit.CONFIG.water_sip_thirst_duration, 1, false, false, true));
                            } else {
                                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundInit.WATER_SIP_EVENT, SoundCategory.PLAYERS, 1.0F, 0.9F + (world.random.nextFloat() / 5F));
                            }
                            ((PlayerAccess) player).setDrinkTime(0);
                            return ActionResult.SUCCESS;
                        }

                        ((PlayerAccess) player).setDrinkTime(drinkTime + 1);
                    }
                }
                return ActionResult.PASS;
            }
            return ActionResult.PASS;
        });
    }
}
