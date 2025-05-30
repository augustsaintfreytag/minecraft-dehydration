package net.dehydration.mod;

import net.dehydration.access.ServerPlayerAccess;
import net.dehydration.event.BlockEventUtil;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.util.Identifier;

public class ModEvents {

	public static void init() {
		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
			((ServerPlayerAccess) player).compatSync();
		});

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, supplier, setter) -> {
			if (id.equals(new Identifier(LootTables.SPAWN_BONUS_CHEST.toString()))) {
				LootPool pool = LootPool.builder().with(ItemEntry.builder(Items.GLASS_BOTTLE).build())
						.rolls(BinomialLootNumberProvider.create(5, 0.9F)).build();
				supplier.pool(pool);
			}
		});

		UseBlockCallback.EVENT.register((player, world, hand, result) -> {
			return BlockEventUtil.handleUseBlockEvent(player, world, hand, result);
		});
	}
}
