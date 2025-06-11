package net.dehydration.fluid;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.dehydration.mod.ModBlocks;
import net.dehydration.mod.ModFluids;
import net.dehydration.mod.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

@SuppressWarnings("deprecation")
public abstract class PurifiedWaterFluid extends FlowableFluid {
	@Override
	public Fluid getFlowing() {
		return ModFluids.PURIFIED_FLOWING_WATER;
	}

	@Override
	public Fluid getStill() {
		return ModFluids.PURIFIED_WATER;
	}

	@Override
	public Item getBucketItem() {
		return ModItems.PURIFIED_WATER_BUCKET;
	}

	@Override
	public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
		if (!state.isStill() && !state.get(FALLING).booleanValue()) {
			if (random.nextInt(64) == 0) {
				world.playSound((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5,
						SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS,
						random.nextFloat() * 0.25f + 0.75f, random.nextFloat() + 0.5f, false);
			}
		} else if (random.nextInt(10) == 0) {
			world.addParticle(ParticleTypes.UNDERWATER, (double) pos.getX() + random.nextDouble(),
					(double) pos.getY() + random.nextDouble(), (double) pos.getZ() + random.nextDouble(), 0.0, 0.0,
					0.0);
		}
	}

	@Override
	@Nullable
	public ParticleEffect getParticle() {
		return ParticleTypes.DRIPPING_WATER;
	}

	@Override
	protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
		BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
		Block.dropStacks(state, world, pos, blockEntity);
	}

	@Override
	public int getFlowSpeed(WorldView world) {
		return 4;
	}

	@Override
	public BlockState toBlockState(FluidState state) {
		return (BlockState) ModBlocks.PURIFIED_WATER.getDefaultState().with(FluidBlock.LEVEL,
				PurifiedWaterFluid.getBlockStateLevel(state));
	}

	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == ModFluids.PURIFIED_WATER || fluid == ModFluids.PURIFIED_FLOWING_WATER;
	}

	@Override
	public int getLevelDecreasePerBlock(WorldView world) {
		return 1;
	}

	@Override
	public int getTickRate(WorldView world) {
		return 5;
	}

	@Override
	public boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid,
			Direction direction) {
		return fluid.isIn(FluidTags.WATER);
	}

	@Override
	protected float getBlastResistance() {
		return 100.0f;
	}

	@Override
	public Optional<SoundEvent> getBucketFillSound() {
		return Optional.of(SoundEvents.ITEM_BUCKET_FILL);
	}

	@Override
	protected void flow(WorldAccess world, BlockPos pos, BlockState state, Direction direction, FluidState fluidState) {
		if (!state.getFluidState().isIn(FluidTags.WATER))
			super.flow(world, pos, state, direction, fluidState);
	}

	public static class Flowing extends PurifiedWaterFluid {
		@Override
		protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
			super.appendProperties(builder);
			builder.add(LEVEL);
		}

		@Override
		public int getLevel(FluidState state) {
			return state.get(LEVEL);
		}

		@Override
		public boolean isStill(FluidState state) {
			return false;
		}

		@Override
		protected boolean isInfinite(World var1) {
			return false;
		}
	}

	public static class Still extends PurifiedWaterFluid {
		@Override
		public int getLevel(FluidState state) {
			return 8;
		}

		@Override
		public boolean isStill(FluidState state) {
			return true;
		}

		@Override
		protected boolean isInfinite(World var1) {
			return false;
		}
	}
}
