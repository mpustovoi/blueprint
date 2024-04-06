package com.teamabnormals.blueprint.core.data.server.tags;

import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class BlueprintItemTagsProvider extends ItemTagsProvider {

	public BlueprintItemTagsProvider(String modid, PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> tagLookup, ExistingFileHelper fileHelper) {
		super(output, lookupProvider, tagLookup, modid, fileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider p_256380_) {
		this.tag(BlueprintItemTags.CHICKEN_FOOD);
		this.tag(BlueprintItemTags.PIG_FOOD);
		this.tag(BlueprintItemTags.STRIDER_FOOD);
		this.tag(BlueprintItemTags.STRIDER_TEMPT_ITEMS);
		this.tag(BlueprintItemTags.OCELOT_FOOD);
		this.tag(BlueprintItemTags.CAT_FOOD);

		this.tag(BlueprintItemTags.MILK).addTag(BlueprintItemTags.BUCKETS_MILK);
		this.tag(BlueprintItemTags.PUMPKINS).add(Items.PUMPKIN);

		this.tag(BlueprintItemTags.BUCKETS_EMPTY).add(Items.BUCKET);
		this.tag(BlueprintItemTags.BUCKETS_WATER).add(Items.WATER_BUCKET);
		this.tag(BlueprintItemTags.BUCKETS_LAVA).add(Items.LAVA_BUCKET);
		this.tag(BlueprintItemTags.BUCKETS_MILK).add(Items.MILK_BUCKET);
		this.tag(BlueprintItemTags.BUCKETS_POWDER_SNOW).add(Items.POWDER_SNOW_BUCKET);
		this.tag(BlueprintItemTags.BUCKETS).addTag(BlueprintItemTags.BUCKETS_EMPTY).addTag(BlueprintItemTags.BUCKETS_WATER).addTag(BlueprintItemTags.BUCKETS_LAVA).addTag(BlueprintItemTags.BUCKETS_MILK).addTag(BlueprintItemTags.BUCKETS_LAVA);

		this.tag(BlueprintItemTags.FURNACE_BOATS);
		this.tag(BlueprintItemTags.LARGE_BOATS);
	}
}
