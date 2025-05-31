package net.dehydration.mixin.compat.autohud;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mod.crend.autohud.compat.DehydrationCompat;
import net.dehydration.mixinlogic.compat.autohud.AutoHudDehydrationCompatMixinLogic;

@Mixin(DehydrationCompat.class)
public abstract class AutoHudDehydrationCompatMixin implements AutoHudDehydrationCompatMixinLogic {

	@Inject(method = "<clinit>", at = @At("HEAD"), cancellable = true)
	private static void overwriteStaticBlock(CallbackInfo ci) {
		AutoHudDehydrationCompatMixinLogic.mixinStatic();
		ci.cancel();
	}
}