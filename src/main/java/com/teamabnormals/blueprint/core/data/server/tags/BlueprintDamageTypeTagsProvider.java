package com.teamabnormals.blueprint.core.data.server.tags;

import com.teamabnormals.blueprint.core.other.tags.BlueprintDamageTypeTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class BlueprintDamageTypeTagsProvider extends DamageTypeTagsProvider {

	public BlueprintDamageTypeTagsProvider(String modid, PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		super(output, provider, modid, helper);
	}

	@Override
	protected void addTags(Provider provider) {
		this.tag(BlueprintDamageTypeTags.IS_MAGIC).add(DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC, DamageTypes.SONIC_BOOM, DamageTypes.THORNS);
	}
}
