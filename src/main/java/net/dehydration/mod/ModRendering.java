package net.dehydration.mod;

import net.dehydration.block.CampfireCauldronBlock;
import net.dehydration.block.entity.CampfireCauldronEntity;
import net.dehydration.block.render.BambooPumpRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

@Environment(EnvType.CLIENT)
public class ModRendering {

	public static final Identifier THIRST_ICON = new Identifier("dehydration:textures/gui/thirst.png");

	public static void init() {
		ColorProviderRegistry.BLOCK.register(
				(state, world, pos, tintIndex) -> world != null && world.getBlockEntity(pos) != null
						? isPurifiedWater(world, pos, state) ? 3708358 : BiomeColors.getWaterColor(world, pos)
						: 3708358,
				ModBlocks.CAMPFIRE_CAULDRON_BLOCK);
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> BiomeColors.getWaterColor(world, pos),
				ModBlocks.COPPER_WATER_CAULDRON_BLOCK);
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> 3708358,
				ModBlocks.COPPER_PURIFIED_WATER_CAULDRON_BLOCK);

		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CAMPFIRE_CAULDRON_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COPPER_CAULDRON_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COPPER_WATER_CAULDRON_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COPPER_POWDERED_CAULDRON_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COPPER_PURIFIED_WATER_CAULDRON_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BAMBOO_PUMP_BLOCK, RenderLayer.getCutout());

		BlockEntityRendererFactories.register(ModBlocks.BAMBOO_PUMP_ENTITY, BambooPumpRenderer::new);

		FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.PURIFIED_WATER,
				SimpleFluidRenderHandler.coloredWater(3708358));
		FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.PURIFIED_FLOWING_WATER,
				SimpleFluidRenderHandler.coloredWater(3708358));
	}

	private static boolean isPurifiedWater(BlockRenderView blockRenderView, BlockPos blockPos, BlockState state) {
		if (blockRenderView.getBlockEntity(blockPos) != null
				&& blockRenderView.getBlockEntity(blockPos) instanceof CampfireCauldronEntity) {
			return ((CampfireCauldronBlock) state.getBlock())
					.isPurifiedWater(blockRenderView.getBlockEntity(blockPos).getWorld(), blockPos);
		}
		return false;

	}

}
