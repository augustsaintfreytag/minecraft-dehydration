package net.dehydration.mod;

import net.dehydration.compat.autohud.AutoHudCompat;
import net.dehydration.compat.croptopia.CroptopiaCompat;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

public class ModCompat {

	public static void init() {
		ifLoaded("croptopia", CroptopiaCompat::init);

		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
				AutoHudCompat.init();
			});
		}

		// ClientTickEvents.START_WORLD_TICK.register(world -> {
		// AutoHudCompat.init();
		// });
	}

	private static void ifLoaded(String mod, Action action) {
		if (FabricLoader.getInstance().isModLoaded(mod))
			action.act();
	}

	@FunctionalInterface
	private interface Action {
		void act();
	}
}
