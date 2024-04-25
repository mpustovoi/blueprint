package com.teamabnormals.blueprint.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class BlueprintDamageTypeTags {
	public static final TagKey<DamageType> IS_MAGIC = TagUtil.damageTypeTag("forge", "is_magic");
}