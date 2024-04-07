package com.teamabnormals.blueprint.core.data.client;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;


public abstract class BlueprintItemModelProvider extends ItemModelProvider {

	public BlueprintItemModelProvider(PackOutput output, String modid, ExistingFileHelper helper) {
		super(output, modid, helper);
	}

	public ItemModelBuilder item(ItemLike item, String type) {
		return this.withExistingParent(name(item), "item/" + type).texture("layer0", itemTexture(item));
	}

	public ItemModelBuilder item(ItemLike item, String path, String type) {
		return this.withExistingParent(name(item), "item/" + type).texture("layer0", new ResourceLocation(this.modid, "item/" + path));
	}

	public ItemModelBuilder blockItem(Block block) {
		return this.getBuilder(BlueprintBlockStateProvider.name(block)).parent(new UncheckedModelFile(new ResourceLocation(this.modid, "block/" + BlueprintBlockStateProvider.name(block))));
	}

	public void generatedItem(ItemLike... items) {
		for (ItemLike item : items) {
			this.item(item, "generated");
		}
	}

	public void handheldItem(ItemLike... items) {
		for (ItemLike item : items) {
			this.item(item, "handheld");
		}
	}

	public void spawnEggItem(ItemLike... items) {
		for (ItemLike item : items) {
			this.withExistingParent(name(item), "item/template_spawn_egg");
		}
	}

	public void animatedItem(ItemLike item, int count) {
		for (int i = 0; i < count; i++) {
			String path = name(item) + "_" + String.format("%02d", i);
			this.withExistingParent(path, "item/generated").texture("layer0", new ResourceLocation(this.modid, "item/" + path));
		}
	}

	public void trimmableArmorItem(ItemLike... items) {
		for (ItemLike item : items) {
			if (item.asItem() instanceof ArmorItem armor) {
				ResourceLocation location = ForgeRegistries.ITEMS.getKey(armor);
				ItemModelBuilder itemModel = item(armor, "generated");
				int trimType = 1;
				for (String trim : new String[]{"quartz", "iron", "netherite", "redstone", "copper", "gold", "emerald", "diamond", "lapis", "amethyst"}) {
					ResourceLocation name = new ResourceLocation(location.getNamespace(), "item/" + location.getPath() + "_" + trim + "_trim");
					itemModel.override().model(new UncheckedModelFile(name)).predicate(new ResourceLocation("trim_type"), (float) (trimType / 10.0));
					ResourceLocation texture = new ResourceLocation("trims/items/" + armor.getType().getName() + "_trim_" + trim);
					this.existingFileHelper.trackGenerated(texture, PackType.CLIENT_RESOURCES, ".png", "textures");
					withExistingParent(name.getPath(), "item/generated").texture("layer0", new ResourceLocation(this.modid, "item/" + location.getPath())).texture("layer1", texture);
					trimType++;
				}
			}
		}
	}

	public static ResourceLocation key(ItemLike item) {
		return ForgeRegistries.ITEMS.getKey(item.asItem());
	}

	public static String name(ItemLike item) {
		return key(item).getPath();
	}

	public ResourceLocation itemTexture(ItemLike item) {
		ResourceLocation name = key(item);
		return new ResourceLocation(name.getNamespace(), ModelProvider.ITEM_FOLDER + "/" + name.getPath());
	}
}