package net.dehydration.mixinlogic.compat.autohud;

import mod.crend.autohud.compat.DehydrationCompat;
import mod.crend.autohud.component.Component;
import mod.crend.autohud.component.state.PolicyComponentState;
import net.dehydration.Mod;
import net.dehydration.access.HydrationManagerAccess;
import net.dehydration.hydration.HydrationManager;
import net.dehydration.mod.ModEffects;
import net.minecraft.client.network.ClientPlayerEntity;

public interface AutoHudDehydrationCompatMixinLogic {

	public default void mixinInit(ClientPlayerEntity player) {
		if (Component.getComponents().contains(DehydrationCompat.Thirst)) {
			Mod.LOGGER.info("Thirst component already registered with AutoHUD, skipping.");
			return;
		}

		Mod.LOGGER.info("Registering thirst component with AutoHUD.");

		Component.registerComponent(DehydrationCompat.Thirst);
		Component.Hunger.addStackComponent(DehydrationCompat.Thirst);

		HydrationManager hydrationManager = ((HydrationManagerAccess) player).getHydrationManager();

		if (hydrationManager.hasThirst()) {
			DehydrationCompat.Thirst.state = new PolicyComponentState(DehydrationCompat.Thirst,
					hydrationManager::getHydrationLevel, 20);
		}

		DehydrationCompat.Thirst.hideNow();
	}

	public default void mixinTickState(ClientPlayerEntity player) {
		if (DehydrationCompat.Thirst == null) {
			return;
		}

		if (player.hasStatusEffect(ModEffects.THIRST)) {
			DehydrationCompat.Thirst.reveal();
			return;
		}

		var hydrationManager = ((HydrationManagerAccess) player).getHydrationManager();

		var mainHandStack = player.getMainHandStack();
		var offHandStack = player.getOffHandStack();

		var playerHasEmptyHands = mainHandStack.isEmpty() && offHandStack.isEmpty();
		var playerHeldItemHasTooltip = !playerHasEmptyHands && (mainHandStack.getTooltipData().isPresent()
				|| offHandStack.getTooltipData().isPresent());

		if (hydrationManager.isNotFull() && playerHeldItemHasTooltip) {
			DehydrationCompat.Thirst.reveal();
		}
	}
}
