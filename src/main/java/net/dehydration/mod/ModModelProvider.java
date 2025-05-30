package net.dehydration.mod;

import net.dehydration.item.LeatherFlask;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ModModelProvider {

	public static void init() {
		for (int i = 0; i < ModItems.FLASK_ITEM_LIST.size(); i++) {
			ModelPredicateProviderRegistry.register(ModItems.FLASK_ITEM_LIST.get(i), new Identifier("empty"),
					(stack, world, entity, seed) -> {
						NbtCompound tags = stack.getNbt();
						if (!stack.hasNbt() || (stack.hasNbt() && tags.getInt(LeatherFlask.TAG_WATER) != 0))
							return 0.0F;
						else
							return 1.0F;
					});
		}
	}
}
