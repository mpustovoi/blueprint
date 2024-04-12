package com.teamabnormals.blueprint.core.other.tags;

import com.teamabnormals.blueprint.core.Blueprint;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.armortrim.TrimMaterial;

/**
 * Class for all of Blueprint's trim material tags
 */
public class BlueprintTrimMaterialTags {
	public static final TagKey<TrimMaterial> GENERATES_OVERRIDES = tag("generates_overrides");

	private static TagKey<TrimMaterial> tag(String name) {
		return TagKey.create(Registries.TRIM_MATERIAL, new ResourceLocation(Blueprint.MOD_ID, name));
	}
}
