package com.teamabnormals.blueprint.common.entity;

import com.teamabnormals.blueprint.common.block.BlueprintFallingBlock;
import com.teamabnormals.blueprint.core.events.FallingBlockEvent;
import com.teamabnormals.blueprint.core.registry.BlueprintEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A {@link FallingBlockEntity} extension with some additional attributes.
 * The entity is spawned when a {@link BlueprintFallingBlock} falls.
 */
public class BlueprintFallingBlockEntity extends FallingBlockEntity implements IEntityWithComplexSpawn {
	private static final Logger LOGGER = LogManager.getLogger();
	private boolean dropsBlockLoot = true;
	private boolean allowsPlacing = true;

	public BlueprintFallingBlockEntity(EntityType<? extends FallingBlockEntity> type, Level level) {
		super(type, level);
	}

	public BlueprintFallingBlockEntity(Level level, double x, double y, double z, BlockState state) {
		this(BlueprintEntityTypes.FALLING_BLOCK.get(), level);
		this.blockState = state;
		this.blocksBuilding = true;
		this.setPos(x, y, z);
		this.setDeltaMovement(Vec3.ZERO);
		this.xo = x;
		this.yo = y;
		this.zo = z;
		this.setStartPos(this.blockPosition());
	}

	public static BlueprintFallingBlockEntity fall(Level level, BlockPos pos, BlockState state) {
		BlueprintFallingBlockEntity fallingblockentity = new BlueprintFallingBlockEntity(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, state.hasProperty(BlockStateProperties.WATERLOGGED) ? state.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)) : state);
		FallingBlockEntity fallingblockentity1 = FallingBlockEvent.onBlockFall(level, pos, state, fallingblockentity);
		if (fallingblockentity1 instanceof BlueprintFallingBlockEntity)
			fallingblockentity = (BlueprintFallingBlockEntity) fallingblockentity1;

		level.setBlock(pos, state.getFluidState().createLegacyBlock(), 3);
		level.addFreshEntity(fallingblockentity);
		return fallingblockentity;
	}

	@Override
	public void tick() {
		if (this.blockState.isAir()) {
			this.discard();
		} else {
			Block block = this.blockState.getBlock();
			++this.time;

			if (!this.isNoGravity()) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
			}

			this.move(MoverType.SELF, this.getDeltaMovement());

			if (FallingBlockEvent.onFallingBlockTick(this)) {
				return;
			}

			if (!this.level().isClientSide) {
				BlockPos blockpos = this.blockPosition();
				boolean flag = this.blockState.getBlock() instanceof ConcretePowderBlock;
				boolean flag1 = flag && this.blockState.canBeHydrated(this.level(), blockpos, this.level().getFluidState(blockpos), blockpos);
				double d0 = this.getDeltaMovement().lengthSqr();
				if (flag && d0 > 1.0D) {
					BlockHitResult blockhitresult = this.level().clip(new ClipContext(new Vec3(this.xo, this.yo, this.zo), this.position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, this));
					if (blockhitresult.getType() != HitResult.Type.MISS && this.blockState.canBeHydrated(this.level(), blockpos, this.level().getFluidState(blockhitresult.getBlockPos()), blockhitresult.getBlockPos())) {
						blockpos = blockhitresult.getBlockPos();
						flag1 = true;
					}
				}

				if (!this.onGround() && !flag1) {
					if (!this.level().isClientSide && (this.time > 100 && (blockpos.getY() <= this.level().getMinBuildHeight() || blockpos.getY() > this.level().getMaxBuildHeight()) || this.time > 600)) {
						if (this.dropItem && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
							this.spawnDrops();
						}

						this.discard();
					}
				} else {
					BlockState blockstate = this.level().getBlockState(blockpos);
					this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
					if (!blockstate.is(Blocks.MOVING_PISTON)) {
						if (!this.cancelDrop) {
							boolean flag2 = blockstate.canBeReplaced(new DirectionalPlaceContext(this.level(), blockpos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
							boolean flag3 = FallingBlock.isFree(this.level().getBlockState(blockpos.below())) && (!flag || !flag1);
							boolean flag4 = this.blockState.canSurvive(this.level(), blockpos) && !flag3;
							if (flag2 && flag4) {
								if (this.blockState.hasProperty(BlockStateProperties.WATERLOGGED) && this.level().getFluidState(blockpos).getType() == Fluids.WATER) {
									this.blockState = this.blockState.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
								}

								if (this.allowsPlacing && this.level().setBlock(blockpos, this.blockState, 3)) {
									((ServerLevel) this.level()).getChunkSource().chunkMap.broadcast(this, new ClientboundBlockUpdatePacket(blockpos, this.level().getBlockState(blockpos)));
									this.discard();
									if (block instanceof Fallable) {
										((Fallable) block).onLand(this.level(), blockpos, this.blockState, blockstate, this);
									}

									if (this.blockData != null && this.blockState.hasBlockEntity()) {
										BlockEntity blockentity = this.level().getBlockEntity(blockpos);
										if (blockentity != null) {
											CompoundTag compoundtag = blockentity.saveWithoutMetadata();

											for (String s : this.blockData.getAllKeys()) {
												compoundtag.put(s, this.blockData.get(s).copy());
											}

											try {
												blockentity.load(compoundtag);
											} catch (Exception exception) {
												LOGGER.error("Failed to load block entity from falling block", exception);
											}

											blockentity.setChanged();
										}
									}
								} else if (this.dropItem && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
									this.discard();
									this.callOnBrokenAfterFall(block, blockpos);
									this.spawnDrops();
								}
							} else {
								this.discard();
								if (this.dropItem && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
									this.callOnBrokenAfterFall(block, blockpos);
									this.spawnDrops();
								}
							}
						} else {
							this.discard();
							this.callOnBrokenAfterFall(block, blockpos);
						}
					}
				}
			}

			this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
		}
	}

	/**
	 * Sets the falling block to use the loot table of its block state to determine its drops when broken by a fall.
	 */
	public void setDropsBlockLoot(boolean dropsLoot) {
		this.dropsBlockLoot = dropsLoot;
	}

	/**
	 * Sets the falling block to not place itself when it hits the ground. Instead, it will break into its drops.
	 */
	public void setAllowsPlacing(boolean allowsPlacing) {
		this.allowsPlacing = allowsPlacing;
	}

	/**
	 * Sets the {@link #blockState} stored by the falling block.
	 */
	public void setBlockState(BlockState state) {
		this.blockState = state;
	}

	protected void spawnDrops() {
		if (this.dropsBlockLoot) {
			LootParams.Builder builder = (new LootParams.Builder((ServerLevel) this.level())).withParameter(LootContextParams.ORIGIN, this.position()).withParameter(LootContextParams.TOOL, ItemStack.EMPTY);
			this.blockState.getDrops(builder).forEach(this::spawnAtLocation);
		} else {
			this.spawnAtLocation(this.blockState.getBlock());
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("DropsBlockLoot", this.dropsBlockLoot);
		compound.putBoolean("AllowsPlacing", this.allowsPlacing);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("DropsBlockLoot", 99))
			this.dropsBlockLoot = compound.getBoolean("DropsBlockLoot");
		if (compound.contains("AllowsPlacing", 99))
			this.allowsPlacing = compound.getBoolean("AllowsPlacing");
	}

	@Override
	public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
		buffer.writeInt(Block.getId(this.blockState));
	}

	@Override
	public void readSpawnData(RegistryFriendlyByteBuf buffer) {
		this.blockState = Block.stateById(buffer.readInt());
	}
}