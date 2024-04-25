package com.teamabnormals.blueprint.core.other.tags;

import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.armortrim.TrimMaterial;

/**
 * Class for all of Blueprint's trim material tags
 */
public class BlueprintTrimMaterialTags {
	public static final TagKey<TrimMaterial> GENERATES_OVERRIDES = trimMaterialTag("generates_overrides");

	private static TagKey<TrimMaterial> trimMaterialTag(String name) {
		return TagUtil.trimMaterialTag(Blueprint.MOD_ID, name);
	}
}
