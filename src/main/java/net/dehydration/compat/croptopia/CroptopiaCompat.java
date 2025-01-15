package net.dehydration.compat.croptopia;

public class CroptopiaCompat {

	public static void init() {
		// Potentially no longer needed with refactor.

		// DrinkEvent.DRINK.register((stack, player) -> {
		// ThirstManager thirstManager = ((ThirstManagerAccess)
		// player).getThirstManager();
		// int thirst = 0;
		// if (stack.isIn(TagInit.HYDRATING_STEW)) {
		// thirst = ConfigInit.CONFIG.stew_thirst_quench;
		// }
		// if (stack.isIn(TagInit.HYDRATING_FOOD_REGULAR)) {
		// thirst = ConfigInit.CONFIG.food_thirst_quench;
		// }
		// if (stack.isIn(TagInit.HYDRATING_DRINKS)) {
		// thirst = ConfigInit.CONFIG.drinks_hydration;
		// }
		// if (stack.isIn(TagInit.STRONGER_HYDRATING_STEW)) {
		// thirst = ConfigInit.CONFIG.stronger_stew_thirst_quench;
		// }
		// if (stack.isIn(TagInit.STRONGER_HYDRATING_FOOD)) {
		// thirst = ConfigInit.CONFIG.stronger_food_thirst_quench;
		// }
		// if (stack.isIn(TagInit.STRONGER_HYDRATING_DRINKS)) {
		// thirst = ConfigInit.CONFIG.stronger_drinks_thirst_quench;
		// }
		// for (int i = 0; i < DehydrationMain.HYDRATION_TEMPLATES.size(); i++) {
		// if (DehydrationMain.HYDRATION_TEMPLATES.get(i).containsItem(stack.getItem()))
		// {
		// thirst = DehydrationMain.HYDRATION_TEMPLATES.get(i).getHydration();
		// break;
		// }
		// }
		// if (thirst > 0) {
		// thirstManager.add(thirst);
		// }
		// });
	}

}
