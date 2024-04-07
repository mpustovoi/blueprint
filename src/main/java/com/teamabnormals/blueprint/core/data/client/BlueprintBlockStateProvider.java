package com.teamabnormals.blueprint.core.data.client;

import com.teamabnormals.blueprint.core.Blueprint;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.BlockFamily.Variant;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public abstract class BlueprintBlockStateProvider extends BlockStateProvider {

	public BlueprintBlockStateProvider(PackOutput output, String modid, ExistingFileHelper helper) {
		super(output, modid, helper);
	}

	public void block(Block block) {
		this.simpleBlock(block);
		this.blockItem(block);
	}

	public void block(RegistryObject<Block> block) {
		this.block(block.get());
	}

	public void blockItem(Block block) {
		this.simpleBlockItem(block, new ExistingModelFile(blockTexture(block), this.models().existingFileHelper));
	}

	public void blockItem(RegistryObject<Block> block) {
		this.blockItem(block.get());
	}

	public void generatedItem(ItemLike item, String type) {
		this.generatedItem(item, item, type);
	}

	public void generatedItem(ItemLike item, ItemLike texture, String type) {
		this.itemModels().withExistingParent(ForgeRegistries.ITEMS.getKey(item.asItem()).getPath(), "item/generated").texture("layer0", new ResourceLocation(ForgeRegistries.ITEMS.getKey(texture.asItem()).getNamespace(), type + "/" + ForgeRegistries.ITEMS.getKey(texture.asItem()).getPath()));
	}

	public void cubeBottomTop(RegistryObject<Block> block) {
		ResourceLocation name = ForgeRegistries.BLOCKS.getKey(block.get());
		this.cubeBottomTop(block, prefix("block/", suffix(name, "_side")), prefix("block/", suffix(name, "_bottom")), prefix("block/", suffix(name, "_top")));
	}

	public void cubeBottomTop(RegistryObject<Block> block, ResourceLocation sideTexture, ResourceLocation bottomTexture, ResourceLocation topTexture) {
		this.simpleBlock(block.get(), this.models().cubeBottomTop(name(block.get()), sideTexture, bottomTexture, topTexture));
		this.blockItem(block);
	}

	public void crossBlock(Block cross) {
		this.simpleBlock(cross, models().cross(name(cross), blockTexture(cross)));
		this.generatedItem(cross, "block");
	}

	public void crossBlockWithPot(RegistryObject<Block> cross, RegistryObject<Block> flowerPot, ResourceLocation potTexture) {
		this.crossBlock(cross.get());
		this.simpleBlock(flowerPot.get(), models().singleTexture(name(flowerPot.get()), new ResourceLocation("block/flower_pot_cross"), "plant", potTexture));
	}

	public void crossBlockWithPot(RegistryObject<Block> cross, RegistryObject<Block> flowerPot) {
		this.crossBlockWithPot(cross, flowerPot, blockTexture(cross.get()));
	}

	public void crossBlockWithCustomPot(RegistryObject<Block> cross, RegistryObject<Block> flowerPot) {
		this.crossBlockWithPot(cross, flowerPot, blockTexture(flowerPot.get()));
	}

	public void slabBlock(Block block, Block slab) {
		if (slab instanceof SlabBlock slabBlock) {
			this.slabBlock(slabBlock, blockTexture(block), blockTexture(block));
			this.blockItem(slab);
		}
	}

	public void stairsBlock(Block block, Block stairs) {
		if (stairs instanceof StairBlock stairBlock) {
			this.stairsBlock(stairBlock, blockTexture(block));
			this.blockItem(stairs);
		}
	}

	public void wallBlock(Block block, Block wall) {
		if (wall instanceof WallBlock wallBlock) {
			this.wallBlock(wallBlock, blockTexture(block));
			this.itemModels().getBuilder(name(wall)).parent(this.models().wallInventory(name(wall) + "_inventory", blockTexture(block)));
		}
	}

	public void baseBlocks(Block block, Block stairs, Block slab) {
		this.baseBlocks(block, stairs, slab, null);
	}

	public void baseBlocks(Block block, Block stairs, Block slab, Block wall) {
		this.block(block);
		this.stairsBlock(block, stairs);
		this.slabBlock(block, slab);
		this.wallBlock(block, wall);
	}

	public void fenceBlock(Block block, Block fence) {
		if (fence instanceof FenceBlock fenceBlock) {
			this.fenceBlock(fenceBlock, blockTexture(block));
			this.itemModels().getBuilder(name(fence)).parent(this.models().fenceInventory(name(fence) + "_inventory", blockTexture(block)));
		}
	}

	public void fenceGateBlock(Block block, Block fenceGate) {
		if (fenceGate instanceof FenceGateBlock fenceGateBlock) {
			this.fenceGateBlock(fenceGateBlock, blockTexture(block));
			this.blockItem(fenceGate);
		}
	}

	public void fenceBlocks(Block block, Block fence, Block fenceGate) {
		this.fenceBlock(block, fence);
		this.fenceGateBlock(block, fenceGate);
	}

	public void doorBlock(Block door) {
		if (door instanceof DoorBlock doorBlock) {
			this.doorBlock(doorBlock, suffix(blockTexture(door), "_bottom"), suffix(blockTexture(door), "_top"));
			this.generatedItem(door, "item");
		}
	}

	public void trapDoorBlock(Block trapDoor) {
		if (trapDoor instanceof TrapDoorBlock trapDoorBlock) {
			this.trapdoorBlock(trapDoorBlock, blockTexture(trapDoor), true);
			this.itemModels().getBuilder(name(trapDoor)).parent(this.models().trapdoorOrientableBottom(name(trapDoor) + "_bottom", blockTexture(trapDoor)));
		}
	}

	public void doorBlocks(Block door, Block trapdoor) {
		this.doorBlock(door);
		this.trapDoorBlock(trapdoor);
	}

	public void signBlocks(Block block, Block sign, Block wallSign) {
		if (sign != null && wallSign != null) {
			ModelFile model = particle(sign, blockTexture(block));
			this.simpleBlock(sign, model);
			this.generatedItem(sign, "item");
			this.simpleBlock(wallSign, model);
		}
	}

	public void hangingSignBlocks(RegistryObject<Block> strippedLog, RegistryObject<Block> sign, RegistryObject<Block> wallSign) {
		ModelFile model = particle(sign, blockTexture(strippedLog.get()));
		this.simpleBlock(sign.get(), model);
		this.generatedItem(sign.get(), "item");
		this.simpleBlock(wallSign.get(), model);
	}

	public void buttonBlock(Block block, Block button) {
		this.buttonBlock(button, blockTexture(block));
	}

	public void buttonBlock(Block block, ResourceLocation texture) {
		ModelFile button = models().button(name(block), texture);
		ModelFile buttonPressed = models().buttonPressed(name(block) + "_pressed", texture);
		ModelFile buttonInventoryModel = models().buttonInventory(name(block) + "_inventory", blockTexture(block));
		if (block instanceof ButtonBlock buttonBlock) {
			this.buttonBlock(buttonBlock, button, buttonPressed);
		}

		this.itemModels().getBuilder(name(block)).parent(buttonInventoryModel);
	}

	public void pressurePlateBlock(Block block, Block pressurePlate) {
		this.pressurePlateBlock(pressurePlate, blockTexture(block));
	}

	public void pressurePlateBlock(Block block, ResourceLocation texture) {
		ModelFile pressurePlate = models().pressurePlate(name(block), texture);
		ModelFile pressurePlateDown = models().pressurePlateDown(name(block) + "_down", texture);
		this.getVariantBuilder(block)
				.partialState().with(PressurePlateBlock.POWERED, true).addModels(new ConfiguredModel(pressurePlateDown))
				.partialState().with(PressurePlateBlock.POWERED, false).addModels(new ConfiguredModel(pressurePlate));
		this.blockItem(block);
	}

	public void boardsBlock(RegistryObject<Block> boards) {
		ModelFile boardsModel = models().getBuilder(name(boards.get())).parent(new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "block/template_boards"))).texture("all", blockTexture(boards.get()));
		ModelFile boardsHorizontalModel = models().getBuilder(name(boards.get()) + "_horizontal").parent(new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "block/template_boards_horizontal"))).texture("all", blockTexture(boards.get()));
		this.getVariantBuilder(boards.get()).partialState().with(RotatedPillarBlock.AXIS, Axis.Y).modelForState().modelFile(boardsModel).addModel().partialState().with(RotatedPillarBlock.AXIS, Axis.Z).modelForState().modelFile(boardsHorizontalModel).addModel().partialState().with(RotatedPillarBlock.AXIS, Axis.X).modelForState().modelFile(boardsHorizontalModel).rotationY(270).addModel();
		this.blockItem(boards);
	}

	public void bookshelfBlock(RegistryObject<Block> planks, RegistryObject<Block> bookshelf) {
		this.simpleBlock(bookshelf.get(), this.models().cubeColumn(name(bookshelf.get()), blockTexture(bookshelf.get()), blockTexture(planks.get())));
		this.blockItem(bookshelf);
	}

	public static final String[] DEFAULT_BOOKSHELF_POSITIONS = new String[]{"top_left", "top_mid", "top_right", "bottom_left", "bottom_mid", "bottom_right"};
	public static final String[] ALTERNATE_BOOKSHELF_POSITIONS = new String[]{"top_left", "top_right", "mid_left", "mid_right", "bottom_left", "bottom_right"};
	public static final String[] BOTTOM_BOOKSHELF_POSITIONS = new String[]{"top_left", "top_right", "bottom_left", "bottom_mid_left", "bottom_mid_right", "bottom_right"};
	public static final String[] CHERRY_BOOKSHELF_POSITIONS = new String[]{"far_left", "mid_left", "top_mid", "bottom_mid", "mid_right", "far_right"};

	public void chiseledBookshelfBlock(RegistryObject<Block> registryObject) {
		chiseledBookshelfBlock(registryObject, DEFAULT_BOOKSHELF_POSITIONS, new ResourceLocation("template_chiseled_bookshelf"));
	}

	public void chiseledBookshelfBlock(RegistryObject<Block> registryObject, String[] parts) {
		chiseledBookshelfBlock(registryObject, parts, blockTexture(registryObject.get()));
	}

	public void chiseledBookshelfBlock(RegistryObject<Block> registryObject, String[] parts, ResourceLocation parent) {
		Block chiseledBookshelf = registryObject.get();
		String name = name(chiseledBookshelf);
		ResourceLocation texture = blockTexture(chiseledBookshelf);

		BlockModelBuilder model = this.models().withExistingParent(name, "block/chiseled_bookshelf").texture("top", texture + "_top").texture("side", texture + "_side");
		MultiPartBlockStateBuilder builder = getMultipartBuilder(chiseledBookshelf);
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			int rotation = (int) (direction.toYRot() + 180) % 360;
			builder.part().modelFile(model).rotationY(rotation).uvLock(true).addModel().condition(HorizontalDirectionalBlock.FACING, direction);
			for (int i = 0; i < 6; i++) {
				String part = parts[i];
				BooleanProperty property = ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(i);
				ResourceLocation slot = prefix("block/", suffix(parent, "_slot_" + part));

				builder.part().modelFile(this.models().withExistingParent(name + "_occupied_slot_" + part, slot).texture("texture", texture + "_occupied"))
						.rotationY(rotation).uvLock(true).addModel()
						.condition(HorizontalDirectionalBlock.FACING, direction).condition(property, true);
				builder.part().modelFile(this.models().withExistingParent(name + "_empty_slot_" + part, slot).texture("texture", texture + "_empty"))
						.rotationY(rotation).uvLock(true).addModel()
						.condition(HorizontalDirectionalBlock.FACING, direction).condition(property, false);
			}
		}

		this.simpleBlockItem(chiseledBookshelf, this.models().withExistingParent(name + "_inventory", "block/chiseled_bookshelf_inventory").texture("top", texture + "_top").texture("side", texture + "_side").texture("front", texture + "_empty"));
	}

	public void ladderBlock(RegistryObject<Block> ladder) {
		this.horizontalBlock(ladder.get(), models().withExistingParent(name(ladder.get()), "block/ladder").texture("particle", blockTexture(ladder.get())).texture("texture", blockTexture(ladder.get())));
		this.generatedItem(ladder.get(), "block");
	}

	public void chestBlocks(RegistryObject<Block> planks, RegistryObject<Block> chest, RegistryObject<Block> trappedChest) {
		ModelFile model = particle(chest, blockTexture(planks.get()));
		this.simpleBlock(chest.get(), model);
		this.simpleBlock(trappedChest.get(), model);
		this.simpleBlockItem(chest.get(), new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "item/template_chest")));
		this.simpleBlockItem(trappedChest.get(), new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "item/template_chest")));
	}

	public void beehiveBlock(RegistryObject<Block> registryObject) {
		Block block = registryObject.get();
		ModelFile beehive = models().orientableWithBottom(name(block), suffix(blockTexture(block), "_side"), suffix(blockTexture(block), "_front"), suffix(blockTexture(block), "_end"), suffix(blockTexture(block), "_end")).texture("particle", suffix(blockTexture(block), "_side"));
		ModelFile beehiveHoney = models().orientableWithBottom(name(block) + "_honey", suffix(blockTexture(block), "_side"), suffix(blockTexture(block), "_front_honey"), suffix(blockTexture(block), "_end"), suffix(blockTexture(block), "_end")).texture("particle", suffix(blockTexture(block), "_side"));
		this.horizontalBlock(block, (state -> state.getValue(BlockStateProperties.LEVEL_HONEY) == 5 ? beehiveHoney : beehive));
		this.blockItem(block);
	}

	public void logBlocks(RegistryObject<Block> log, RegistryObject<Block> wood) {
		this.logBlock(log);
		this.logBlock(wood, suffix(blockTexture(log.get()), "_top"));
	}

	public void logBlock(RegistryObject<Block> block) {
		this.logBlock(block, suffix(blockTexture(block.get()), "_top"));
	}

	public void logBlock(RegistryObject<Block> block, ResourceLocation topTexture) {
		if (block.get() instanceof RotatedPillarBlock log) {
			this.axisBlock(log, blockTexture(log), topTexture);
			this.blockItem(block);
		}
	}

	public void leavesBlock(RegistryObject<Block> leaves) {
		this.simpleBlock(leaves.get(), models().getBuilder(name(leaves.get())).parent(new UncheckedModelFile(new ResourceLocation("block/leaves"))).texture("all", blockTexture(leaves.get())));
		this.blockItem(leaves);
	}

	public void leafPileBlock(RegistryObject<Block> leaves, RegistryObject<Block> leafPile) {
		this.leafPileBlock(leaves, leafPile, true);
	}

	public void leafPileBlock(RegistryObject<Block> leaves, RegistryObject<Block> leafPile, boolean tint) {
		ModelFile leafPileModel = models().getBuilder(name(leafPile.get())).parent(new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "block/" + (tint ? "tinted_" : "") + "leaf_pile"))).texture("all", blockTexture(leaves.get()));
		MultiPartBlockStateBuilder builder = getMultipartBuilder(leafPile.get());
		builder.part().modelFile(leafPileModel).rotationX(270).uvLock(true).addModel().condition(BlockStateProperties.UP, true);
		builder.part().modelFile(leafPileModel).rotationX(270).uvLock(true).addModel().condition(BlockStateProperties.UP, false).condition(BlockStateProperties.NORTH, false).condition(BlockStateProperties.WEST, false).condition(BlockStateProperties.SOUTH, false).condition(BlockStateProperties.EAST, false).condition(BlockStateProperties.DOWN, false);
		builder.part().modelFile(leafPileModel).addModel().condition(BlockStateProperties.NORTH, true);
		builder.part().modelFile(leafPileModel).addModel().condition(BlockStateProperties.UP, false).condition(BlockStateProperties.NORTH, false).condition(BlockStateProperties.WEST, false).condition(BlockStateProperties.SOUTH, false).condition(BlockStateProperties.EAST, false).condition(BlockStateProperties.DOWN, false);
		builder.part().modelFile(leafPileModel).rotationY(270).uvLock(true).addModel().condition(BlockStateProperties.WEST, true);
		builder.part().modelFile(leafPileModel).rotationY(270).uvLock(true).addModel().condition(BlockStateProperties.UP, false).condition(BlockStateProperties.NORTH, false).condition(BlockStateProperties.WEST, false).condition(BlockStateProperties.SOUTH, false).condition(BlockStateProperties.EAST, false).condition(BlockStateProperties.DOWN, false);
		builder.part().modelFile(leafPileModel).rotationY(180).uvLock(true).addModel().condition(BlockStateProperties.SOUTH, true);
		builder.part().modelFile(leafPileModel).rotationY(180).uvLock(true).addModel().condition(BlockStateProperties.UP, false).condition(BlockStateProperties.NORTH, false).condition(BlockStateProperties.WEST, false).condition(BlockStateProperties.SOUTH, false).condition(BlockStateProperties.EAST, false).condition(BlockStateProperties.DOWN, false);
		builder.part().modelFile(leafPileModel).rotationY(90).uvLock(true).addModel().condition(BlockStateProperties.EAST, true);
		builder.part().modelFile(leafPileModel).rotationY(90).uvLock(true).addModel().condition(BlockStateProperties.UP, false).condition(BlockStateProperties.NORTH, false).condition(BlockStateProperties.WEST, false).condition(BlockStateProperties.SOUTH, false).condition(BlockStateProperties.EAST, false).condition(BlockStateProperties.DOWN, false);
		builder.part().modelFile(leafPileModel).rotationX(90).uvLock(true).addModel().condition(BlockStateProperties.DOWN, true);
		builder.part().modelFile(leafPileModel).rotationX(90).uvLock(true).addModel().condition(BlockStateProperties.UP, false).condition(BlockStateProperties.NORTH, false).condition(BlockStateProperties.WEST, false).condition(BlockStateProperties.SOUTH, false).condition(BlockStateProperties.EAST, false).condition(BlockStateProperties.DOWN, false);
		this.generatedItem(leafPile.get(), leaves.get(), "block");
	}

	public void leavesBlocks(RegistryObject<Block> leaves, RegistryObject<Block> leafPile) {
		this.leavesBlocks(leaves, leafPile, true);
	}

	public void leavesBlocks(RegistryObject<Block> leaves, RegistryObject<Block> leafPile, boolean tinted) {
		this.leavesBlock(leaves);
		this.leafPileBlock(leaves, leafPile, tinted);
	}

	public void ironBarsBlock(Block bars) {
		this.ironBarsBlock(bars, blockTexture(bars));
		this.generatedItem(bars, "block");
	}

	public void ironBarsBlock(Block block, ResourceLocation texture) {
		String name = name(block);
		ResourceLocation edgeTexture = suffix(texture, "_edge");

		ModelFile post = ironBarsBlock(name, "post", texture).texture("bars", edgeTexture);
		ModelFile postEnds = ironBarsBlock(name, "post_ends", texture).texture("edge", edgeTexture);
		ModelFile side = ironBarsBlock(name, "side", texture).texture("bars", texture).texture("edge", edgeTexture);
		ModelFile sideAlt = ironBarsBlock(name, "side_alt", texture).texture("bars", texture).texture("edge", edgeTexture);
		ModelFile cap = ironBarsBlock(name, "cap", texture).texture("bars", texture).texture("edge", edgeTexture);
		ModelFile capAlt = ironBarsBlock(name, "cap_alt", texture).texture("bars", texture).texture("edge", edgeTexture);

		this.paneBlock(block, post, postEnds, side, sideAlt, cap, capAlt);
	}

	public BlockModelBuilder ironBarsBlock(String name, String suffix, ResourceLocation barsTexture) {
		return models().getBuilder(name + "_" + suffix).parent(new UncheckedModelFile(new ResourceLocation("block/iron_bars_" + suffix))).texture("particle", barsTexture);
	}

	public void paneBlock(Block block, ModelFile post, ModelFile postEnds, ModelFile side, ModelFile sideAlt, ModelFile cap, ModelFile capAlt) {
		MultiPartBlockStateBuilder builder = getMultipartBuilder(block).part().modelFile(postEnds).addModel().end();
		builder.part().modelFile(post).addModel().condition(BlockStateProperties.NORTH, false).condition(BlockStateProperties.WEST, false).condition(BlockStateProperties.SOUTH, false).condition(BlockStateProperties.EAST, false).end();

		for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
			builder.part().modelFile(direction == Direction.SOUTH || direction == Direction.WEST ? capAlt : cap).rotationY(direction.getAxis() == Axis.X ? 90 : 0).addModel()
					.condition(BlockStateProperties.NORTH, PipeBlock.PROPERTY_BY_DIRECTION.get(direction) == BlockStateProperties.NORTH)
					.condition(BlockStateProperties.WEST, PipeBlock.PROPERTY_BY_DIRECTION.get(direction) == BlockStateProperties.WEST)
					.condition(BlockStateProperties.SOUTH, PipeBlock.PROPERTY_BY_DIRECTION.get(direction) == BlockStateProperties.SOUTH)
					.condition(BlockStateProperties.EAST, PipeBlock.PROPERTY_BY_DIRECTION.get(direction) == BlockStateProperties.EAST)
					.end();

		}

		PipeBlock.PROPERTY_BY_DIRECTION.forEach((dir, value) -> {
			if (dir.getAxis().isHorizontal()) {
				builder.part().modelFile(dir == Direction.SOUTH || dir == Direction.WEST ? sideAlt : side).rotationY(dir.getAxis() == Axis.X ? 90 : 0).addModel().condition(value, true).end();
			}
		});
	}

	public ModelFile particle(Block block, ResourceLocation texture) {
		return this.models().getBuilder(name(block)).texture("particle", texture);
	}

	public ModelFile particle(RegistryObject<Block> block, ResourceLocation texture) {
		return this.particle(block.get(), texture);
	}

	public static String name(Block block) {
		return ForgeRegistries.BLOCKS.getKey(block).getPath();
	}

	public static ResourceLocation prefix(String prefix, ResourceLocation rl) {
		return new ResourceLocation(rl.getNamespace(), prefix + rl.getPath());
	}

	public static ResourceLocation suffix(ResourceLocation rl, String suffix) {
		return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
	}

	public static ResourceLocation remove(ResourceLocation rl, String remove) {
		return new ResourceLocation(rl.getNamespace(), rl.getPath().replace(remove, ""));
	}

	public void woodworksBlocks(RegistryObject<Block> planks, RegistryObject<Block> boards, RegistryObject<Block> ladder, RegistryObject<Block> beehive, RegistryObject<Block> chest, RegistryObject<Block> trappedChest, RegistryObject<Block> bookshelf) {
		this.boardsBlock(boards);
		this.ladderBlock(ladder);
		this.beehiveBlock(beehive);
		this.chestBlocks(planks, chest, trappedChest);
		this.bookshelfBlock(planks, bookshelf);
	}

	public void blockFamily(BlockFamily family) {
		Map<Variant, Block> variants = family.getVariants();
		Block block = family.getBaseBlock();

		this.baseBlocks(block, family.get(Variant.STAIRS), family.get(Variant.SLAB), family.get(Variant.WALL));
		this.fenceBlocks(block, family.get(Variant.FENCE), family.get(Variant.FENCE_GATE));
		this.doorBlocks(family.get(Variant.DOOR), family.get(Variant.TRAPDOOR));
		this.signBlocks(block, family.get(Variant.SIGN), family.get(Variant.WALL_SIGN));

		if (variants.containsKey(Variant.CHISELED)) {
			this.block(family.get(Variant.CHISELED));
		}

		if (variants.containsKey(Variant.CRACKED)) {
			this.block(family.get(Variant.POLISHED));
		}

		if (variants.containsKey(Variant.CUT)) {
			this.block(family.get(Variant.POLISHED));
		}

		if (variants.containsKey(Variant.POLISHED)) {
			this.block(family.get(Variant.POLISHED));
		}

		if (family.get(Variant.BUTTON) instanceof ButtonBlock button) {
			this.buttonBlock(block, button);
		}

		if (family.get(Variant.PRESSURE_PLATE) instanceof BasePressurePlateBlock pressurePlate) {
			this.pressurePlateBlock(block, pressurePlate);
		}
	}
}