package net.dehydration.mod;

import net.dehydration.compat.autohud.AutoHudCompat;
import net.dehydration.compat.croptopia.CroptopiaCompat;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

public class ModCompat {

	public static void init() {
		var loader = FabricLoader.getInstance();

		if (loader.isModLoaded("croptopia")) {
			CroptopiaCompat.init();
		}

		if (loader.getEnvironmentType() == EnvType.CLIENT && loader.isModLoaded("autohud")) {
			ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
				AutoHudCompat.init();
			});
		}
	}

}
