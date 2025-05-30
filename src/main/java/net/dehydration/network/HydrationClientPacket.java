package net.dehydration.network;

import java.util.ArrayList;
import java.util.List;

import it.unimi.dsi.fastutil.ints.IntList;
import net.dehydration.Mod;
import net.dehydration.access.HydrationManagerAccess;
import net.dehydration.api.HydrationTemplate;
import net.dehydration.hydration.HydrationManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

@Environment(EnvType.CLIENT)
public class HydrationClientPacket {

	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(HydrationServerPacket.THIRST_UPDATE,
				(client, handler, buffer, responseSender) -> {
					int[] bufferArray = buffer.readIntArray();
					int entityId = bufferArray[0];
					int thirstLevel = bufferArray[1];
					client.execute(() -> {
						if (client.player.getWorld().getEntityById(entityId) != null) {
							PlayerEntity player = (PlayerEntity) client.player.getWorld().getEntityById(entityId);
							HydrationManager hydrationManager = ((HydrationManagerAccess) player).getHydrationManager();
							hydrationManager.setHydrationLevel(thirstLevel);
						}
					});
				});
		ClientPlayNetworking.registerGlobalReceiver(HydrationServerPacket.EXCLUDED_SYNC,
				(client, handler, buffer, responseSender) -> {
					boolean setThirst = buffer.readBoolean();
					client.execute(() -> {
						((HydrationManagerAccess) client.player).getHydrationManager().setThirst(setThirst);
					});
				});
		ClientPlayNetworking.registerGlobalReceiver(HydrationServerPacket.HYDRATION_TEMPLATE_SYNC,
				(client, handler, buffer, responseSender) -> {
					List<HydrationTemplate> hydrationTemplates = new ArrayList<HydrationTemplate>();
					IntList intList = buffer.readIntList();
					for (int i = 0; i < intList.size(); i += 2) {
						List<Item> items = new ArrayList<Item>();
						for (int u = 0; u < intList.getInt(i + 1); u++) {
							items.add(Registries.ITEM.get(buffer.readIdentifier()));
						}
						hydrationTemplates.add(new HydrationTemplate(intList.getInt(i), items));
					}
					client.execute(() -> {
						Mod.HYDRATION_TEMPLATES.clear();
						Mod.HYDRATION_TEMPLATES.addAll(hydrationTemplates);
					});
				});
	}
}
