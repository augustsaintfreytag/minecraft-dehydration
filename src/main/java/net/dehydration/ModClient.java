package net.dehydration;

import net.dehydration.mod.ModModelProvider;
import net.dehydration.mod.ModRendering;
import net.dehydration.network.HydrationClientPacket;
import net.fabricmc.api.ClientModInitializer;

public class ModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModModelProvider.init();
		ModRendering.init();
		HydrationClientPacket.init();
	}

}
