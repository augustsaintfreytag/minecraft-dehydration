package net.dehydration.mixin.compat.autohud;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mod.crend.autohud.compat.DehydrationCompat;
import net.dehydration.mixinlogic.compat.autohud.AutoHudDehydrationCompatMixinLogic;
import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(DehydrationCompat.class)
public abstract class AutoHudDehydrationCompatMixin implements AutoHudDehydrationCompatMixinLogic {

	@Overwrite
	public void init() {
		mixinInit();
	}

	@Overwrite
	public void tickState(ClientPlayerEntity player) {
		mixinTickState(player);
	}

	@Inject(method = "<clinit>", at = @At("HEAD"), cancellable = true)
	private static void overwriteStaticBlock(CallbackInfo ci) {
		AutoHudDehydrationCompatMixinLogic.mixinStatic();
		ci.cancel();
	}
}