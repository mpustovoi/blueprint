package com.teamabnormals.blueprint.core.data.client;

import com.teamabnormals.blueprint.core.Blueprint;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.BlockFamily.Variant;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
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

	private void generatedItem(ItemLike item, String type) {
		this.generatedItem(item, item, type);
	}

	private void generatedItem(ItemLike item, ItemLike texture, String type) {
		this.itemModels().withExistingParent(ForgeRegistries.ITEMS.getKey(item.asItem()).getPath(), "item/generated").texture("layer0", new ResourceLocation(ForgeRegistries.ITEMS.getKey(texture.asItem()).getNamespace(), type + "/" + ForgeRegistries.ITEMS.getKey(texture.asItem()).getPath()));
	}

	public void crossBlock(Block cross) {
		this.simpleBlock(cross, models().cross(name(cross), blockTexture(cross)));
		this.generatedItem(cross, "block");
	}

	public void crossBlockWithPot(RegistryObject<Block> cross, RegistryObject<Block> flowerPot) {
		this.crossBlock(cross.get());
		this.simpleBlock(flowerPot.get(), models().singleTexture(name(flowerPot.get()), new ResourceLocation("block/flower_pot_cross"), "plant", blockTexture(cross.get())));
	}

	public void leavesBlock(RegistryObject<Block> leaves) {
		this.simpleBlock(leaves.get(), models().getBuilder(name(leaves.get())).parent(new UncheckedModelFile(new ResourceLocation("block/leaves"))).texture("all", blockTexture(leaves.get())));
		this.blockItem(leaves);
	}

	public void hangingSigns(RegistryObject<Block> strippedLog, RegistryObject<Block> sign, RegistryObject<Block> wallSign) {
		ModelFile model = particle(sign, blockTexture(strippedLog.get()));
		this.simpleBlock(sign.get(), particle(sign, blockTexture(strippedLog.get())));
		this.generatedItem(sign.get(), "item");
		this.simpleBlock(wallSign.get(), model);
	}

	public void chestBlocks(RegistryObject<Block> planks, RegistryObject<Block> chest, RegistryObject<Block> trappedChest) {
		ModelFile model = particle(chest, blockTexture(planks.get()));
		this.simpleBlock(chest.get(), model);
		this.simpleBlock(trappedChest.get(), model);
		this.simpleBlockItem(chest.get(), new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "item/template_chest")));
		this.simpleBlockItem(trappedChest.get(), new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "item/template_chest")));
	}

	public ModelFile particle(Block block, ResourceLocation texture) {
		return this.models().getBuilder(name(block)).texture("particle", texture);
	}

	public ModelFile particle(RegistryObject<Block> block, ResourceLocation texture) {
		return particle(block.get(), texture);
	}

	public String name(Block block) {
		return ForgeRegistries.BLOCKS.getKey(block).getPath();
	}

	public ResourceLocation prefix(String prefix, ResourceLocation rl) {
		return new ResourceLocation(rl.getNamespace(), prefix + rl.getPath());
	}

	public ResourceLocation suffix(ResourceLocation rl, String suffix) {
		return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
	}

	public void blockFamily(BlockFamily family) {
		Map<Variant, Block> variants = family.getVariants();

		Block block = family.getBaseBlock();
		this.block(block);

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

		if (family.get(Variant.SLAB) instanceof SlabBlock slab) {
			this.slabBlock(slab, blockTexture(block), blockTexture(block));
			this.blockItem(slab);
		}

		if (family.get(Variant.STAIRS) instanceof StairBlock stairs) {
			this.stairsBlock(stairs, blockTexture(block));
			this.blockItem(stairs);
		}

		if (family.get(Variant.WALL) instanceof WallBlock wall) {
			this.wallBlock(wall, blockTexture(block));
			this.itemModels().getBuilder(name(wall)).parent(this.models().wallInventory(name(wall) + "_inventory", blockTexture(block)));
		}

		if (family.get(Variant.FENCE) instanceof FenceBlock fence) {
			this.fenceBlock(fence, blockTexture(block));
			this.itemModels().getBuilder(name(fence)).parent(this.models().fenceInventory(name(fence) + "_inventory", blockTexture(block)));
		}

		if (family.get(Variant.FENCE_GATE) instanceof FenceGateBlock fenceGate) {
			this.fenceGateBlock(fenceGate, blockTexture(block));
			this.blockItem(fenceGate);
		}

		if (family.get(Variant.BUTTON) instanceof ButtonBlock button) {
			ModelFile buttonInventoryModel = models().withExistingParent(name(button) + "_inventory", "block/button_inventory").texture("texture", blockTexture(block));
			this.buttonBlock(button, blockTexture(block));
			this.itemModels().getBuilder(name(button)).parent(buttonInventoryModel);
		}

		if (family.get(Variant.PRESSURE_PLATE) instanceof PressurePlateBlock pressurePlate) {
			this.pressurePlateBlock(pressurePlate, blockTexture(block));
			this.blockItem(pressurePlate);
		}

		if (family.get(Variant.DOOR) instanceof DoorBlock door) {
			this.doorBlock(door, suffix(blockTexture(door), "_bottom"), suffix(blockTexture(door), "_top"));
			this.generatedItem(door, "item");
		}

		if (family.get(Variant.TRAPDOOR) instanceof TrapDoorBlock trapDoor) {
			this.trapdoorBlock(trapDoor, blockTexture(trapDoor), true);
			this.itemModels().getBuilder(name(trapDoor)).parent(this.models().trapdoorOrientableBottom(name(trapDoor) + "_bottom", blockTexture(trapDoor)));
		}

		if (family.get(Variant.SIGN) instanceof SignBlock sign) {
			ModelFile model = particle(sign, blockTexture(block));
			this.simpleBlock(sign, model);
			this.generatedItem(sign, "item");
			if (family.get(Variant.WALL_SIGN) instanceof WallSignBlock wallSign) {
				this.simpleBlock(wallSign, model);
			}
		}
	}
}