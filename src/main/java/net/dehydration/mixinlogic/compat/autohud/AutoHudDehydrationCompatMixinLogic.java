package net.dehydration.mixinlogic.compat.autohud;

import java.util.Objects;

import mod.crend.autohud.AutoHud;
import mod.crend.autohud.compat.DehydrationCompat;
import mod.crend.autohud.component.Component;
import mod.crend.autohud.component.Components;
import mod.crend.autohud.component.state.PolicyComponentState;
import mod.crend.autohud.render.ComponentRenderer;
import net.dehydration.Mod;
import net.dehydration.access.HydrationManagerAccess;
import net.dehydration.misc.ThirstTooltipData;
import net.dehydration.mod.ModEffects;
import net.minecraft.client.network.ClientPlayerEntity;

public interface AutoHudDehydrationCompatMixinLogic {

	default void mixinInit() {
		if (Component.getComponents().contains(DehydrationCompat.Thirst)) {
			Mod.LOGGER.info("Thirst component already registered with AutoHUD, skipping.");
			return;
		}

		Mod.LOGGER.info("Registering thirst component with AutoHUD.");

		var thirstComponent = DehydrationCompat.Thirst;
		Components.Hunger.addStackComponent(thirstComponent);
	}

	default void mixinTickState(ClientPlayerEntity player) {
		var thirstComponent = DehydrationCompat.Thirst;

		if (player.hasStatusEffect(ModEffects.THIRST)) {
			thirstComponent.reveal();
		} else if (((HydrationManagerAccess) player).getHydrationManager().isNotFull()) {
			if (!player.getMainHandStack().isEmpty() && player.getMainHandStack().getTooltipData().isPresent()
					&& player.getMainHandStack().getTooltipData().get() instanceof ThirstTooltipData) {
				thirstComponent.reveal();
			} else if (!player.getOffHandStack().isEmpty() && player.getOffHandStack().getTooltipData().isPresent()
					&& player.getOffHandStack().getTooltipData().get() instanceof ThirstTooltipData) {
				thirstComponent.reveal();
			}
		}
	}

	static void mixinStatic() {
		// Overwrite the static block's behavior
		DehydrationCompat.Thirst = Component.builder("dehydration", "thirst")
				.config(AutoHud.config.hunger())
				.stackComponents(new Component[] { Components.Air })
				.inMainHud()
				.state((player) -> {
					var hydrationManager = ((HydrationManagerAccess) player).getHydrationManager();

					if (hydrationManager.hasThirst()) {
						Component thirstComponent = DehydrationCompat.Thirst;
						Objects.requireNonNull(hydrationManager);

						return new PolicyComponentState(thirstComponent, hydrationManager::getHydrationLevel, 20);
					} else {
						return null;
					}
				}).build();
		DehydrationCompat.THIRST_WRAPPER = ComponentRenderer.of(DehydrationCompat.Thirst);
		AutoHud.addApi(new DehydrationCompat());
	}
}