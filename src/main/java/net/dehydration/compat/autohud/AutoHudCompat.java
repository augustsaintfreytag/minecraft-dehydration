package net.dehydration.compat.autohud;

import mod.crend.autohud.AutoHud;
import mod.crend.autohud.compat.DehydrationCompat;
import net.dehydration.Mod;

public class AutoHudCompat {

	public static void init() {
		var compatLoaded = false;

		for (var api : AutoHud.apis) {
			if (api instanceof DehydrationCompat) {
				compatLoaded = true;
				break;
			}
		}

		if (compatLoaded) {
			Mod.LOGGER.info("Dehydration Compat already registered with AutoHud, skipping extra init.");
			return;
		}

		Mod.LOGGER.info("Registering Dehydration Compat with AutoHud in extra init.");
		AutoHud.addApi(new DehydrationCompat());
	}

}
