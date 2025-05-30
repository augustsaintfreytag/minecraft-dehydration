package net.dehydration.misc;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;

public class PotionItemUtil {

	public static boolean isContaminatedPotionItemStack(ItemStack stack) {
		Potion potion = PotionUtil.getPotion(stack);
		return isContaminatedPotionItem(potion);
	}

	public static boolean isContaminatedPotionItem(Potion potion) {
		if (potion == Potions.WATER || potion == Potions.AWKWARD || potion == Potions.THICK || potion == Potions.HARMING
				|| potion == Potions.LONG_POISON || potion == Potions.LONG_SLOWNESS
				|| potion == Potions.LONG_WEAKNESS || potion == Potions.MUNDANE || potion == Potions.POISON
				|| potion == Potions.SLOWNESS || potion == Potions.STRONG_HARMING
				|| potion == Potions.STRONG_POISON || potion == Potions.STRONG_SLOWNESS || potion == Potions.WEAKNESS) {
			return true;
		} else {
			return false;
		}
	}

}
