package net.dehydration.mixin.compat.autohud;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import mod.crend.autohud.compat.DehydrationCompat;
import net.dehydration.mixinlogic.compat.autohud.AutoHudDehydrationCompatMixinLogic;
import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(DehydrationCompat.class)
public abstract class AutoHudDehydrationCompatMixin implements AutoHudDehydrationCompatMixinLogic {

	/**
	 * Initializes hydration bar and associated state.
	 * 
	 * @reason Overwrites original compatibility method to support changes in fork.
	 * @author Saint
	 * @param player
	 * @param callbackInfo
	 */
	@Overwrite
	public void init(ClientPlayerEntity player) {
		mixinInit(player);
	}

	// @Inject(method = "tickState", at = @At("HEAD"), cancellable = true)

	/**
	 * Tick logic for hydration bar.
	 * 
	 * @reason Overwrites original compatibility method to support changes in fork.
	 * @author Saint
	 * @param player
	 */
	@Overwrite
	public void tickState(ClientPlayerEntity player) {
		mixinTickState(player);
	}

}
