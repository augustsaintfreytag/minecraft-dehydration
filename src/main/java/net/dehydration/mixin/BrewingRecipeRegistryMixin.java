package net.dehydration.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.dehydration.mod.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {

	@Inject(method = "registerDefaults", at = @At("TAIL"))
	private static void registerDefaultsMixin(CallbackInfo info) {
		registerPotionRecipe(Potions.WATER, Items.CHARCOAL, ModItems.PURIFIED_WATER);
		registerPotionRecipe(Potions.WATER, Items.KELP, ModItems.PURIFIED_WATER);
		registerPotionRecipe(ModItems.PURIFIED_WATER, Items.GHAST_TEAR, ModItems.HYDRATION);
	}

	@Shadow
	public static void registerPotionRecipe(Potion input, Item item, Potion output) {
	}
}
