package com.teamabnormals.blueprint.core.other.tags;

import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BlueprintItemTags {
	public static final TagKey<Item> MILK = TagUtil.itemTag("forge", "milk");
	public static final TagKey<Item> PUMPKINS = TagUtil.itemTag("forge", "pumpkins");

	public static final TagKey<Item> BUCKETS = TagUtil.itemTag("forge", "buckets");
	public static final TagKey<Item> BUCKETS_EMPTY = TagUtil.itemTag("forge", "buckets/empty");
	public static final TagKey<Item> BUCKETS_WATER = TagUtil.itemTag("forge", "buckets/water");
	public static final TagKey<Item> BUCKETS_LAVA = TagUtil.itemTag("forge", "buckets/lava");
	public static final TagKey<Item> BUCKETS_MILK = TagUtil.itemTag("forge", "buckets/milk");
	public static final TagKey<Item> BUCKETS_POWDER_SNOW = TagUtil.itemTag("forge", "buckets/powder_snow");

	public static final TagKey<Item> LADDERS = TagUtil.itemTag("forge", "ladders");

	public static final TagKey<Item> WOODEN_CHESTS = itemTag("wooden_chests");
	public static final TagKey<Item> WOODEN_TRAPPED_CHESTS = itemTag("wooden_trapped_chests");
	public static final TagKey<Item> WOODEN_LADDERS = itemTag("wooden_ladders");
	public static final TagKey<Item> WOODEN_BEEHIVES = itemTag("wooden_beehives");
	public static final TagKey<Item> WOODEN_BOOKSHELVES = itemTag("wooden_bookshelves");
	public static final TagKey<Item> WOODEN_CHISELED_BOOKSHELVES = itemTag("wooden_chiseled_bookshelves");

	public static final TagKey<Item> WOODEN_BOARDS = TagUtil.itemTag("woodworks","wooden_boards");
	public static final TagKey<Item> LEAF_PILES = TagUtil.itemTag("woodworks","leaf_piles");

	public static final TagKey<Item> FURNACE_BOATS = TagUtil.itemTag("boatload", "furnace_boats");
	public static final TagKey<Item> LARGE_BOATS = TagUtil.itemTag("boatload", "large_boats");

	private static TagKey<Item> itemTag(String name) {
		return TagUtil.itemTag(Blueprint.MOD_ID, name);
	}
}