package com.teamabnormals.blueprint.core.api;

import com.mojang.datafixers.util.Either;
import com.teamabnormals.blueprint.client.renderer.texture.atlas.BlueprintPalettedPermutations;
import net.minecraft.Util;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlueprintTrims {
	@OnlyIn(Dist.CLIENT)
	public static final ResourceLocation ARMOR_TRIMS_ATLAS = new ResourceLocation("armor_trims");
	@OnlyIn(Dist.CLIENT)
	private static final ResourceLocation PALETTE_KEY = new ResourceLocation("trims/color_palettes/trim_palette");
	@OnlyIn(Dist.CLIENT)
	private static final HashMap<String, ResourceLocation> PERMUTATIONS = Util.make(new HashMap<>(), map -> {
		map.put("quartz", new ResourceLocation("trims/color_palettes/quartz"));
		map.put("iron", new ResourceLocation("trims/color_palettes/iron"));
		map.put("gold", new ResourceLocation("trims/color_palettes/gold"));
		map.put("diamond", new ResourceLocation("trims/color_palettes/diamond"));
		map.put("netherite", new ResourceLocation("trims/color_palettes/netherite"));
		map.put("redstone", new ResourceLocation("trims/color_palettes/redstone"));
		map.put("copper", new ResourceLocation("trims/color_palettes/copper"));
		map.put("emerald", new ResourceLocation("trims/color_palettes/emerald"));
		map.put("lapis", new ResourceLocation("trims/color_palettes/lapis"));
		map.put("amethyst", new ResourceLocation("trims/color_palettes/amethyst"));
		map.put("iron_darker", new ResourceLocation("trims/color_palettes/iron_darker"));
		map.put("gold_darker", new ResourceLocation("trims/color_palettes/gold_darker"));
		map.put("diamond_darker", new ResourceLocation("trims/color_palettes/diamond_darker"));
		map.put("netherite_darker", new ResourceLocation("trims/color_palettes/netherite_darker"));
	});
	@OnlyIn(Dist.CLIENT)
	private static final List<ResourceLocation> ITEM_TRIMS = List.of(
			new ResourceLocation("trims/items/leggings_trim"),
			new ResourceLocation("trims/items/chestplate_trim"),
			new ResourceLocation("trims/items/helmet_trim"),
			new ResourceLocation("trims/items/boots_trim")
	);

	@OnlyIn(Dist.CLIENT)
	@SafeVarargs
	public static BlueprintPalettedPermutations patternPermutationsOfVanillaMaterials(ResourceKey<TrimPattern>... keys) {
		List<ResourceLocation> textures = new ArrayList<>(keys.length << 1);
		for (var key : keys) {
			ResourceLocation location = key.location();
			textures.add(location.withPath(string -> "trims/models/armor/" + string));
			textures.add(location.withPath(string -> "trims/models/armor/" + string + "_leggings"));
		}
		return new BlueprintPalettedPermutations(Either.right(textures), PALETTE_KEY, PERMUTATIONS);
	}

	@OnlyIn(Dist.CLIENT)
	@SafeVarargs
	private static HashMap<String, ResourceLocation> getPermutations(ResourceKey<TrimMaterial>... keys) {
		HashMap<String, ResourceLocation> permutations = new HashMap<>();
		for (var key : keys) {
			ResourceLocation location = key.location();
			permutations.put(location.getNamespace() + "_" + location.getPath(), location.withPath(string -> "trims/color_palettes/" + string));
		}
		return permutations;
	}

	@OnlyIn(Dist.CLIENT)
	@SafeVarargs
	public static BlueprintPalettedPermutations materialPatternPermutations(ResourceKey<TrimMaterial>... keys) {
		return new BlueprintPalettedPermutations(Either.left(List.of(new DirectoryLister("trims/models/armor", "trims/models/armor/"))), PALETTE_KEY, getPermutations(keys));
	}

	@OnlyIn(Dist.CLIENT)
	@SafeVarargs
	public static BlueprintPalettedPermutations materialPermutationsForItemLayers(ResourceKey<TrimMaterial>... keys) {
		return new BlueprintPalettedPermutations(Either.right(ITEM_TRIMS), PALETTE_KEY, getPermutations(keys));
	}
}
