package com.teamabnormals.blueprint.core.util.registry;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.client.BlueprintChestMaterials;
import com.teamabnormals.blueprint.client.renderer.block.ChestBlockEntityWithoutLevelRenderer;
import com.teamabnormals.blueprint.common.block.chest.BlueprintChestBlock;
import com.teamabnormals.blueprint.common.block.chest.BlueprintTrappedChestBlock;
import com.teamabnormals.blueprint.common.block.entity.BlueprintChestBlockEntity;
import com.teamabnormals.blueprint.common.block.entity.BlueprintTrappedChestBlockEntity;
import com.teamabnormals.blueprint.common.block.sign.BlueprintCeilingHangingSignBlock;
import com.teamabnormals.blueprint.common.block.sign.BlueprintStandingSignBlock;
import com.teamabnormals.blueprint.common.block.sign.BlueprintWallHangingSignBlock;
import com.teamabnormals.blueprint.common.block.sign.BlueprintWallSignBlock;
import com.teamabnormals.blueprint.common.item.BEWLRBlockItem;
import com.teamabnormals.blueprint.common.item.BEWLRFuelBlockItem;
import com.teamabnormals.blueprint.common.item.FuelBlockItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * A basic {@link AbstractSubRegistryHelper} for blocks. This contains some useful registering methods for blocks.
 *
 * @author SmellyModder (Luke Tonon)
 * @see AbstractSubRegistryHelper
 */
public class BlockSubRegistryHelper extends AbstractSubRegistryHelper<Block> {
	protected final DeferredRegister<Item> itemRegister;

	public BlockSubRegistryHelper(RegistryHelper parent) {
		this(parent, parent.getSubHelper(Registries.ITEM).getDeferredRegister(), DeferredRegister.create(Registries.BLOCK, parent.getModId()));
	}

	public BlockSubRegistryHelper(RegistryHelper parent, ISubRegistryHelper<Item> itemHelper) {
		this(parent, itemHelper.getDeferredRegister(), DeferredRegister.create(Registries.BLOCK, parent.getModId()));
	}

	public BlockSubRegistryHelper(RegistryHelper parent, DeferredRegister<Item> itemRegister, DeferredRegister<Block> deferredRegister) {
		super(parent, deferredRegister);
		this.itemRegister = itemRegister;
	}

	@OnlyIn(Dist.CLIENT)
	private static BEWLRBlockItem.LazyBEWLR chestBEWLR(boolean trapped) {
		return trapped ? new BEWLRBlockItem.LazyBEWLR((dispatcher, entityModelSet) -> {
			return new ChestBlockEntityWithoutLevelRenderer<>(dispatcher, entityModelSet, new BlueprintTrappedChestBlockEntity(BlockPos.ZERO, Blocks.TRAPPED_CHEST.defaultBlockState()));
		}) : new BEWLRBlockItem.LazyBEWLR((dispatcher, entityModelSet) -> {
			return new ChestBlockEntityWithoutLevelRenderer<>(dispatcher, entityModelSet, new BlueprintChestBlockEntity(BlockPos.ZERO, Blocks.CHEST.defaultBlockState()));
		});
	}

	/**
	 * Creates and registers a {@link Block} with a {@link BlockItem}.
	 *
	 * @param name     The block's name.
	 * @param supplier The supplied {@link Block}.
	 * @return A {@link DeferredHolder} containing the created {@link Block}.
	 */
	public <B extends Block> DeferredHolder<Block, B> createBlock(String name, Supplier<? extends B> supplier) {
		DeferredHolder<Block, B> block = this.deferredRegister.register(name, supplier);
		this.itemRegister.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
		return block;
	}

	/**
	 * Creates and registers a {@link Block} with a {@link BlockItem} with custom {@link Item.Properties}.
	 *
	 * @param name       The block's name.
	 * @param supplier   The supplied {@link Block}.
	 * @param properties The {@link Item.Properties} for the {@link BlockItem}.
	 * @return A {@link DeferredHolder} containing the created {@link Block}.
	 */
	public <B extends Block> DeferredHolder<Block, B> createBlock(String name, Supplier<? extends B> supplier, Item.Properties properties) {
		DeferredHolder<Block, B> block = this.deferredRegister.register(name, supplier);
		this.itemRegister.register(name, () -> new BlockItem(block.get(), properties));
		return block;
	}

	/**
	 * Creates and registers a {@link Block} with a specified {@link BlockItem}.
	 *
	 * @param name     The block's name.
	 * @param supplier The supplied {@link Block}.
	 * @param item     The {@link BlockItem} for this {@link Block}.
	 * @return A {@link DeferredHolder} containing the created {@link Block}.
	 */
	public <B extends Block> DeferredHolder<Block, B> createBlockWithItem(String name, Supplier<? extends B> supplier, Supplier<BlockItem> item) {
		this.itemRegister.register(name, item);
		return this.deferredRegister.register(name, supplier);
	}

	/**
	 * Creates and registers a {@link Block} with no {@link BlockItem}.
	 *
	 * @param name     The block's name.
	 * @param supplier The supplied Block.
	 * @return A {@link DeferredHolder} containing the created {@link Block}.
	 */
	public <B extends Block> DeferredHolder<Block, B> createBlockNoItem(String name, Supplier<? extends B> supplier) {
		return this.deferredRegister.register(name, supplier);
	}

	/**
	 * Creates and registers a {@link Block} with its {@link BlockItem} that can be used as fuel.
	 *
	 * @param name     The block's name.
	 * @param supplier The supplied {@link Block}.
	 * @param burnTime How long the item will burn (measured in ticks).
	 * @return A {@link DeferredHolder} containing the created {@link Block}.
	 */
	public <B extends Block> DeferredHolder<Block, B> createFuelBlock(String name, Supplier<? extends B> supplier, int burnTime) {
		DeferredHolder<Block, B> block = this.deferredRegister.register(name, supplier);
		this.itemRegister.register(name, () -> new FuelBlockItem(block.get(), burnTime, new Item.Properties()));
		return block;
	}

	/**
	 * Creates and registers a {@link Block} with a {@link BlockItem} with a {@link BlockEntityWithoutLevelRenderer}.
	 *
	 * @param name     The block's name.
	 * @param supplier The supplied {@link Block}.
	 * @param belwr    A supplier for getting the {@link BlockEntityWithoutLevelRenderer} for the {@link BlockItem}.
	 * @return A {@link DeferredHolder} containing the created {@link Block}.
	 */
	public <B extends Block> DeferredHolder<Block, B> createBlockWithBEWLR(String name, Supplier<? extends B> supplier, Supplier<Callable<BEWLRBlockItem.LazyBEWLR>> belwr) {
		DeferredHolder<Block, B> block = this.deferredRegister.register(name, supplier);
		this.itemRegister.register(name, () -> new BEWLRBlockItem(block.get(), new Item.Properties(), belwr));
		return block;
	}

	/**
	 * Creates and registers a {@link Block} with a {@link DoubleHighBlockItem}.
	 *
	 * @param name     The block's name.
	 * @param supplier The supplied {@link Block}.
	 * @return A {@link DeferredHolder} containing the created {@link Block}.
	 * @see DoubleHighBlockItem
	 */
	public <B extends Block> DeferredHolder<Block, B> createDoubleHighBlock(String name, Supplier<? extends B> supplier) {
		DeferredHolder<Block, B> block = this.deferredRegister.register(name, supplier);
		this.itemRegister.register(name, () -> new DoubleHighBlockItem(block.get(), new Item.Properties()));
		return block;
	}

	/**
	 * Creates and registers {@link Block} with a {@link StandingAndWallBlockItem}.
	 *
	 * @param name         The block's name.
	 * @param supplier     The supplied floor {@link Block}.
	 * @param wallSupplier The supplied wall {@link Block}.
	 * @param direction    The attachment {@link Direction}.
	 * @return A {@link DeferredHolder} containing the created {@link Block}.
	 * @see StandingAndWallBlockItem
	 */
	public <B extends Block> DeferredHolder<Block, B> createStandingAndWallBlock(String name, Supplier<? extends B> supplier, Supplier<? extends B> wallSupplier, Direction direction) {
		DeferredHolder<Block, B> block = this.deferredRegister.register(name, supplier);
		this.itemRegister.register(name, () -> new StandingAndWallBlockItem(block.get(), wallSupplier.get(), new Item.Properties(), direction));
		return block;
	}

	/**
	 * Creates and registers {@link Block} with a {@link BlockItem} that has {@link Rarity}.
	 *
	 * @param name   The block's name.
	 * @param rarity The {@link Rarity} of the {@link BlockItem}.
	 * @return A {@link DeferredHolder} containing the created {@link Block}.
	 */
	public <B extends Block> DeferredHolder<Block, B> createRareBlock(String name, Supplier<? extends B> supplier, Rarity rarity) {
		DeferredHolder<Block, B> block = this.deferredRegister.register(name, supplier);
		this.itemRegister.register(name, () -> new BlockItem(block.get(), new Item.Properties().rarity(rarity)));
		return block;
	}

	/**
	 * Creates and registers {@link BlueprintChestBlock} with a {@link BEWLRFuelBlockItem}.
	 *
	 * @param name         The name for this {@link BlueprintChestBlock}.
	 * @param materialName The name of the material used for this {@link BlueprintChestBlock}
	 * @param properties   The properties for this {@link BlueprintChestBlock}.
	 * @return A {@link DeferredHolder} containing the created {@link BlueprintChestBlock}.
	 */
	public DeferredHolder<Block, BlueprintChestBlock> createChestBlock(String name, String materialName, Block.Properties properties) {
		String modId = this.parent.getModId();
		String chestMaterialsName = BlueprintChestMaterials.registerMaterials(modId, materialName, false);
		DeferredHolder<Block, BlueprintChestBlock> block = this.deferredRegister.register(name, () -> new BlueprintChestBlock(chestMaterialsName, properties));
		this.itemRegister.register(name, () -> new BEWLRFuelBlockItem(block.get(), new Item.Properties(), () -> () -> chestBEWLR(false), 300));
		return block;
	}

	/**
	 * Creates and registers {@link BlueprintChestBlock} with a {@link BEWLRFuelBlockItem}.
	 *
	 * @param materialName The name of the material used for this {@link BlueprintChestBlock}
	 * @param properties   The properties for this {@link BlueprintChestBlock}.
	 * @return A {@link DeferredHolder} containing the created {@link BlueprintChestBlock}.
	 */
	public DeferredHolder<Block, BlueprintChestBlock> createChestBlock(String materialName, Block.Properties properties) {
		return createChestBlock(materialName + "_chest", materialName, properties);
	}

	/**
	 * Creates and registers {@link BlueprintTrappedChestBlock} with a {@link BEWLRFuelBlockItem}.
	 *
	 * @param name         The name for this {@link BlueprintTrappedChestBlock}.
	 * @param materialName The name of the material used for this {@link BlueprintTrappedChestBlock}
	 * @param properties   The properties for this {@link BlueprintTrappedChestBlock}.
	 * @return A {@link DeferredHolder} containing the created {@link BlueprintTrappedChestBlock}.
	 */
	public DeferredHolder<Block, BlueprintTrappedChestBlock> createTrappedChestBlock(String name, String materialName, Block.Properties properties) {
		String modId = this.parent.getModId();
		DeferredHolder<Block, BlueprintTrappedChestBlock> block = this.deferredRegister.register(name, () -> new BlueprintTrappedChestBlock(modId + ":" + materialName + "_trapped", properties));
		String chestMaterialsName = BlueprintChestMaterials.registerMaterials(modId, materialName, true);
		this.itemRegister.register(name, () -> new BEWLRFuelBlockItem(block.get(), new Item.Properties(), () -> () -> chestBEWLR(true), 300));
		return block;
	}

	/**
	 * Creates and registers {@link BlueprintTrappedChestBlock} with a {@link BEWLRFuelBlockItem}.
	 *
	 * @param materialName The name of the material used for this {@link BlueprintTrappedChestBlock}
	 * @param properties   The properties for this {@link BlueprintTrappedChestBlock}.
	 * @return A {@link DeferredHolder} containing the created {@link BlueprintTrappedChestBlock}.
	 */
	public DeferredHolder<Block, BlueprintTrappedChestBlock>createTrappedChestBlock(String materialName, Block.Properties properties) {
		return createTrappedChestBlock("trapped_" + materialName + "_chest", materialName, properties);
	}

	/**
	 * Creates and registers a {@link BlueprintStandingSignBlock} and a {@link BlueprintWallSignBlock} with a {@link SignItem}.
	 *
	 * @param name     The name for the sign blocks.
	 * @param woodType The {@link WoodType} for the sign blocks. <b>Also call {@link com.teamabnormals.blueprint.core.api.WoodTypeRegistryHelper#registerWoodType(WoodType)} on this</b>!
	 * @return A {@link Pair} containing {@link DeferredHolder}s of the {@link BlueprintStandingSignBlock} and the {@link BlueprintWallSignBlock}.
	 */
	public Pair<DeferredHolder<Block, BlueprintStandingSignBlock>, DeferredHolder<Block, BlueprintWallSignBlock>> createSignBlock(String name, WoodType woodType, Block.Properties properties) {
		DeferredHolder<Block, BlueprintStandingSignBlock> standing = this.deferredRegister.register(name + "_sign", () -> new BlueprintStandingSignBlock(properties, woodType));
		DeferredHolder<Block, BlueprintWallSignBlock> wall = this.deferredRegister.register(name + "_wall_sign", () -> new BlueprintWallSignBlock(properties.lootFrom(standing), woodType));
		this.itemRegister.register(name + "_sign", () -> new SignItem(new Item.Properties().stacksTo(16), standing.get(), wall.get()));
		return Pair.of(standing, wall);
	}

	/**
	 * Creates and registers a {@link BlueprintCeilingHangingSignBlock} and a {@link BlueprintWallHangingSignBlock} with a {@link HangingSignItem}.
	 *
	 * @param name     The name for the sign blocks.
	 * @param woodType The {@link WoodType} for the sign blocks. <b>Also call {@link com.teamabnormals.blueprint.core.api.WoodTypeRegistryHelper#registerWoodType(WoodType)} on this</b>!
	 * @return A {@link Pair} containing {@link DeferredHolder}s of the {@link BlueprintCeilingHangingSignBlock} and the {@link BlueprintWallHangingSignBlock}.
	 */
	public Pair<DeferredHolder<Block, BlueprintCeilingHangingSignBlock>, DeferredHolder<Block, BlueprintWallHangingSignBlock>> createHangingSignBlock(String name, WoodType woodType, Block.Properties properties) {
		DeferredHolder<Block, BlueprintCeilingHangingSignBlock> ceiling = this.deferredRegister.register(name + "_hanging_sign", () -> new BlueprintCeilingHangingSignBlock(properties, woodType));
		DeferredHolder<Block, BlueprintWallHangingSignBlock> wall = this.deferredRegister.register(name + "_wall_hanging_sign", () -> new BlueprintWallHangingSignBlock(properties.lootFrom(ceiling), woodType));
		this.itemRegister.register(name + "_hanging_sign", () -> new HangingSignItem(ceiling.get(), wall.get(), new Item.Properties().stacksTo(16)));
		return Pair.of(ceiling, wall);
	}
}
