package net.dehydration.thirst;

import com.mojang.blaze3d.systems.RenderSystem;

import net.dehydration.access.HudAccess;
import net.dehydration.access.ThirstManagerAccess;
import net.dehydration.init.ConfigInit;
import net.dehydration.init.EffectInit;
import net.dehydration.init.RenderInit;
import net.dehydration.misc.ThirstTooltipData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class ThirstHudRender {

	// Could implement HudRenderCallback
	public static void renderThirstHud(DrawContext context, MinecraftClient client, PlayerEntity playerEntity,
			int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount, float flashAlpha,
			float otherFlashAlpha) {
		if (playerEntity != null && !playerEntity.isInvulnerable()) {
			ThirstManager thirstManager = ((ThirstManagerAccess) playerEntity).getThirstManager();
			if (thirstManager.hasThirst()) {
				int thirst = thirstManager.getThirstLevel();
				int dropletIndex;
				int horizontalOffset;
				int verticalOffset;
				int height = scaledHeight - 49;
				int width = scaledWidth / 2 + 91;
				if (vehicleHeartCount == 0) {

					ItemStack itemStack = null;
					if (ConfigInit.CONFIG.thirst_preview && thirst < 20) {
						if (!playerEntity.getMainHandStack().isEmpty()
								&& playerEntity.getMainHandStack().getTooltipData().isPresent()
								&& playerEntity.getMainHandStack().getTooltipData()
										.get() instanceof ThirstTooltipData) {
							itemStack = playerEntity.getMainHandStack();
						} else if (!playerEntity.getOffHandStack().isEmpty()
								&& playerEntity.getOffHandStack().getTooltipData().isPresent()
								&& playerEntity.getOffHandStack().getTooltipData().get() instanceof ThirstTooltipData) {
							itemStack = playerEntity.getOffHandStack();
						}
					}
					if (itemStack != null) {
						((HudAccess) client.inGameHud).setOtherFlashAlpha(otherFlashAlpha += MathHelper.PI / (48F));
						((HudAccess) client.inGameHud)
								.setFlashAlpha((MathHelper.cos(otherFlashAlpha + MathHelper.PI) + 1f) / 2f);
					} else if (otherFlashAlpha > 0.01F) {
						((HudAccess) client.inGameHud).setOtherFlashAlpha(0.0F);
						((HudAccess) client.inGameHud).setFlashAlpha(0.0F);
					}

					// Render ui droplets
					for (dropletIndex = 0; dropletIndex < 10; ++dropletIndex) {
						verticalOffset = height;

						if (thirstManager.dehydration >= 4.0F && ticks % (thirst * 3 + 1) == 0) {
							verticalOffset = height + (playerEntity.getWorld().getRandom().nextInt(3) - 1); // bouncy
							thirstManager.dehydration -= 4.0F;
						} else if (ticks % (thirst * 8 + 3) == 0) {
							verticalOffset = height + (playerEntity.getWorld().getRandom().nextInt(3) - 1); // bouncy
						}

						int uppderCoord = 9;

						if (ConfigInit.CONFIG.other_droplet_texture) {
							uppderCoord = uppderCoord + 9;
						}

						int beneathCoord = 0;

						if (playerEntity.hasStatusEffect(EffectInit.THIRST)) {
							beneathCoord = 36;
						}

						horizontalOffset = width - dropletIndex * 8 - 9;
						horizontalOffset = horizontalOffset + ConfigInit.CONFIG.hud_x;
						verticalOffset = verticalOffset + ConfigInit.CONFIG.hud_y;

						context.drawTexture(RenderInit.THIRST_ICON, horizontalOffset, verticalOffset, 0, 0, 9, 9, 256,
								256); // Background

						if (dropletIndex * 2 + 1 < thirst) {
							context.drawTexture(RenderInit.THIRST_ICON, horizontalOffset, verticalOffset, beneathCoord,
									uppderCoord, 9, 9, 256, 256); // Big icon
						}

						if (dropletIndex * 2 + 1 == thirst) {
							context.drawTexture(RenderInit.THIRST_ICON, horizontalOffset, verticalOffset,
									beneathCoord + 9, uppderCoord, 9, 9, 256, 256); // Small icon
						}

						// Show item thirst quench
						if (dropletIndex >= thirst / 2 && itemStack != null) {
							RenderSystem.enableBlend();
							RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, flashAlpha);
							int thirstQuench = ((ThirstTooltipData) itemStack.getTooltipData().get()).getThirstQuench();
							int quality = ((ThirstTooltipData) itemStack.getTooltipData().get()).getDrinkQuality();

							if (dropletIndex < (thirst + thirstQuench) / 2) {
								context.drawTexture(RenderInit.THIRST_ICON, horizontalOffset, verticalOffset,
										quality * 18, 9, 9, 9, 256, 256);
							} else if ((thirst + thirstQuench) % 2 != 0
									&& dropletIndex < (thirst + thirstQuench) / 2 + 1) {
								context.drawTexture(RenderInit.THIRST_ICON, horizontalOffset, verticalOffset,
										quality * 18 + 9, 9, 9, 9, 256, 256);
							}

							RenderSystem.disableBlend();
							RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
						}

						// Freezing
						if (playerEntity.getFrozenTicks() > 0) {
							RenderSystem.enableBlend();
							RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, playerEntity.getFreezingScale());

							if (dropletIndex * 2 + 1 < thirst) {
								context.drawTexture(RenderInit.THIRST_ICON, horizontalOffset, verticalOffset, 54,
										uppderCoord, 9, 9, 256, 256);
							}

							if (dropletIndex * 2 + 1 == thirst) {
								context.drawTexture(RenderInit.THIRST_ICON, horizontalOffset, verticalOffset, 54 + 9,
										uppderCoord, 9, 9, 256, 256);
							}

							RenderSystem.disableBlend();
							RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
						}
					}
				}
			}
		}
	}

}
