package com.teamabnormals.blueprint.core.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.common.NeoForge;


/**
 * This event is fired when an {@link Entity} steps on a block.
 * <p>Cancelling this event will prevent {@link net.minecraft.world.level.block.Block#stepOn} in the block's class from being called.</p>
 *
 * @author abigailfails
 */
public final class EntityStepEvent extends Event implements ICancellableEvent {
	private final Level level;
	private final BlockPos pos;
	private final BlockState state;
	private final Entity entity;

	public EntityStepEvent(Level level, BlockPos pos, BlockState state, Entity entity) {
		this.level = level;
		this.pos = pos;
		this.state = state;
		this.entity = entity;
	}

	/**
	 * Fires the {@link EntityStepEvent} for a given {@link Level}, {@link BlockPos}, and {@link Entity}.
	 *
	 * @param level  The {@link Level} that the {@code pos} is in.
	 * @param pos    The {@link BlockPos} that the stepped-on block is at.
	 * @param state  The {@link BlockState} getting stepped on.
	 * @param entity The {@link Entity} that stepped on the block at {@code pos}.
	 */
	public static boolean onEntityStep(Level level, BlockPos pos, BlockState state, Entity entity) {
		return NeoForge.EVENT_BUS.post(new EntityStepEvent(level, pos, state, entity)).isCanceled();
	}

	public Level getLevel() {
		return this.level;
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public BlockState getState() {
		return this.state;
	}

	public Entity getEntity() {
		return this.entity;
	}
}
