package com.minecraftabnormals.abnormals_core.common.blockentity;

import com.minecraftabnormals.abnormals_core.core.registry.ACBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class AbnormalsChestBlockEntity extends ChestBlockEntity {

	protected AbnormalsChestBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}

	public AbnormalsChestBlockEntity(BlockPos pos, BlockState state) {
		super(ACBlockEntities.CHEST.get(), pos, state);
	}

	@Override
	public AABB getRenderBoundingBox() {
		BlockPos worldPos = this.worldPosition;
		int x = worldPos.getX();
		int y = worldPos.getY();
		int z = worldPos.getZ();
		return new AABB(x - 1, y, z - 1, x + 2, y + 2, z + 2);
	}

}