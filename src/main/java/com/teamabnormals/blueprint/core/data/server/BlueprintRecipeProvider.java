package com.teamabnormals.blueprint.core.data.server;

import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class BlueprintRecipeProvider extends RecipeProvider {
	private final String modid;

	public BlueprintRecipeProvider(String modid, PackOutput output) {
		super(output);
		this.modid = modid;
	}

	@Override
	public void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, Blocks.CAKE).define('A', BlueprintItemTags.BUCKETS_MILK).define('B', Items.SUGAR).define('C', Items.WHEAT).define('E', Tags.Items.EGGS).pattern("AAA").pattern("BEB").pattern("CCC").unlockedBy("has_egg", has(Tags.Items.EGGS)).save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.PUMPKIN_PIE).requires(BlueprintItemTags.PUMPKINS).requires(Items.SUGAR).requires(Tags.Items.EGGS).unlockedBy("has_carved_pumpkin", has(Blocks.CARVED_PUMPKIN)).unlockedBy("has_pumpkin", has(BlueprintItemTags.PUMPKINS)).save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.PUMPKIN_SEEDS, 4).requires(BlueprintItemTags.PUMPKINS).unlockedBy("has_pumpkin", has(BlueprintItemTags.PUMPKINS)).save(consumer);
	}

	public static void foodCookingRecipes(Consumer<FinishedRecipe> consumer, ItemLike input, ItemLike output) {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.FOOD, output, 0.35F, 200).unlockedBy(getHasName(input), has(input)).save(consumer);
		SimpleCookingRecipeBuilder.smoking(Ingredient.of(input), RecipeCategory.FOOD, output, 0.35F, 100).unlockedBy(getHasName(input), has(input)).save(consumer, RecipeBuilder.getDefaultRecipeId(output) + "_from_smoking");
		SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(input), RecipeCategory.FOOD, output, 0.35F, 600).unlockedBy(getHasName(input), has(input)).save(consumer, RecipeBuilder.getDefaultRecipeId(output) + "_from_campfire_cooking");
	}


	public void oreRecipes(Consumer<FinishedRecipe> consumer, List<ItemLike> inputs, RecipeCategory category, ItemLike output, float smeltingXp, int smeltingTime, String group) {
		oreRecipes(consumer, inputs, category, output, smeltingXp, smeltingTime, smeltingXp, smeltingTime / 2, group);
	}

	public void oreRecipes(Consumer<FinishedRecipe> consumer, List<ItemLike> inputs, RecipeCategory category, ItemLike output, float smeltingXp, int smeltingTime, float blastingXp, int blastingTime, String group) {
		smeltingRecipe(consumer, inputs, category, output, smeltingXp, smeltingTime, group);
		blastingRecipe(consumer, inputs, category, output, blastingXp, blastingTime, group);
	}

	public void smeltingRecipe(Consumer<FinishedRecipe> consumer, List<ItemLike> inputs, RecipeCategory category, ItemLike output, float xp, int cookTime, String group) {
		oreRecipe(consumer, RecipeSerializer.SMELTING_RECIPE, inputs, category, output, xp, cookTime, group, "_from_smelting");
	}

	public void blastingRecipe(Consumer<FinishedRecipe> consumer, List<ItemLike> p_176627_, RecipeCategory category, ItemLike output, float xp, int cookTime, String group) {
		oreRecipe(consumer, RecipeSerializer.BLASTING_RECIPE, p_176627_, category, output, xp, cookTime, group, "_from_blasting");
	}

	public void oreRecipe(Consumer<FinishedRecipe> consumer, RecipeSerializer<? extends AbstractCookingRecipe> recipeSerializer, List<ItemLike> inputs, RecipeCategory category, ItemLike output, float xp, int cookTime, String p_176540_, String suffix) {
		for (ItemLike itemlike : inputs) {
			SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), category, output, xp, cookTime, recipeSerializer).group(p_176540_).unlockedBy(getHasName(itemlike), has(itemlike)).save(consumer, new ResourceLocation(this.modid, getItemName(output) + suffix + "_" + getItemName(itemlike)));
		}
	}

	public void leafPileRecipes(Consumer<FinishedRecipe> consumer, ItemLike leaves, ItemLike leafPile) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, leafPile, 4).requires(leaves).group("leaf_pile").unlockedBy(getHasName(leaves), has(leaves)).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, leaves).define('#', leafPile).pattern("##").pattern("##").group("leaves").unlockedBy(getHasName(leafPile), has(leafPile)).save(consumer, this.getModConversionRecipeName(leaves, leafPile));
	}

	public void stonecutterRecipe(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike output, ItemLike input) {
		this.stonecutterRecipe(consumer, category, output, input, 1);
	}

	public void stonecutterRecipe(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike output, ItemLike input, int count) {
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), category, output, count).unlockedBy(getHasName(input), has(input)).save(consumer, this.getModConversionRecipeName(output, input) + "_stonecutting");
	}

	public void conversionRecipe(Consumer<FinishedRecipe> consumer, ItemLike output, ItemLike input, @Nullable String group) {
		this.conversionRecipe(consumer, output, input, group, 1);
	}

	public void conversionRecipe(Consumer<FinishedRecipe> consumer, ItemLike output, ItemLike input, @Nullable String group, int count) {
		conversionRecipeBuilder(output, input, count).group(group).save(consumer, this.getModConversionRecipeName(output, input));
	}

	public static ShapelessRecipeBuilder conversionRecipeBuilder(ItemLike output, ItemLike input, int count) {
		return conversionRecipeBuilder(RecipeCategory.MISC, output, input, count);
	}

	public static ShapelessRecipeBuilder conversionRecipeBuilder(RecipeCategory category, ItemLike output, ItemLike input, int count) {
		return ShapelessRecipeBuilder.shapeless(category, output, count).requires(input).unlockedBy(getHasName(input), has(input));
	}

	public void storageRecipes(Consumer<FinishedRecipe> consumer, RecipeCategory itemCategory, ItemLike item, RecipeCategory storageCategory, ItemLike storage, String storageName, String storageGroup, String itemName, String itemGroup) {
		nineBlockStorageRecipes(consumer, itemCategory, item, storageCategory, storage, this.modid + ":" + storageName, storageGroup, this.modid + ":" + itemName, itemGroup);
	}

	public void storageRecipes(Consumer<FinishedRecipe> consumer, RecipeCategory itemCategory, ItemLike item, RecipeCategory storageCategory, ItemLike storage) {
		storageRecipes(consumer, itemCategory, item, storageCategory, storage, getSimpleRecipeName(storage), null, getSimpleRecipeName(item), null);
	}

	public void storageRecipesWithCustomPacking(Consumer<FinishedRecipe> consumer, RecipeCategory itemCategory, ItemLike item, RecipeCategory storageCategory, ItemLike storage, String storageName, String storageGroup) {
		storageRecipes(consumer, itemCategory, item, storageCategory, storage, storageName, storageGroup, getSimpleRecipeName(item), null);
	}

	public void storageRecipesWithCustomUnpacking(Consumer<FinishedRecipe> consumer, RecipeCategory itemCategory, ItemLike item, RecipeCategory storageCategory, ItemLike storage, String itemName, String itemGroup) {
		storageRecipes(consumer, itemCategory, item, storageCategory, storage, getSimpleRecipeName(storage), null, itemName, itemGroup);
	}

	public void conditionalStorageRecipes(Consumer<FinishedRecipe> consumer, ICondition condition, RecipeCategory itemCategory, ItemLike item, RecipeCategory storageCategory, ItemLike storage) {
		conditionalStorageRecipes(consumer, condition, itemCategory, item, storageCategory, storage, getSimpleRecipeName(storage), null, getSimpleRecipeName(item), null);
	}

	public void conditionalStorageRecipes(Consumer<FinishedRecipe> consumer, ICondition condition, RecipeCategory itemCategory, ItemLike item, RecipeCategory storageCategory, ItemLike storage, String storageLocation, @Nullable String itemGroup, String itemLocation, @Nullable String storageGroup) {
		conditionalRecipe(consumer, condition, itemCategory, ShapelessRecipeBuilder.shapeless(itemCategory, item, 9).requires(storage).group(storageGroup).unlockedBy(getHasName(storage), has(storage)), new ResourceLocation(this.modid, itemLocation));
		conditionalRecipe(consumer, condition, storageCategory, ShapedRecipeBuilder.shaped(storageCategory, storage).define('#', item).pattern("###").pattern("###").pattern("###").group(itemGroup).unlockedBy(getHasName(item), has(item)), new ResourceLocation(this.modid, storageLocation));
	}

	public void conditionalStorageRecipesWithCustomUnpacking(Consumer<FinishedRecipe> consumer, ICondition condition, RecipeCategory itemCategory, ItemLike item, RecipeCategory storageCategory, ItemLike storage, String shapelessName, String shapelessGroup) {
		conditionalStorageRecipes(consumer, condition, itemCategory, item, storageCategory, storage, getSimpleRecipeName(storage), null, shapelessName, shapelessGroup);
	}

	public static void conditionalRecipe(Consumer<FinishedRecipe> consumer, ICondition condition, RecipeCategory category, RecipeBuilder recipe) {
		conditionalRecipe(consumer, condition, category, recipe, RecipeBuilder.getDefaultRecipeId(recipe.getResult()));
	}

	public static void conditionalRecipe(Consumer<FinishedRecipe> consumer, ICondition condition, RecipeCategory category, RecipeBuilder recipe, ResourceLocation id) {
		ConditionalRecipe.builder().addCondition(condition).addRecipe(consumer1 -> recipe.save(consumer1, id)).generateAdvancement(new ResourceLocation(id.getNamespace(), "recipes/" + category.getFolderName() + "/" + id.getPath())).build(consumer, id);
	}

	public void waxRecipe(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike input, ItemLike result) {
		ShapelessRecipeBuilder.shapeless(category, result).requires(input).requires(Items.HONEYCOMB).group(getItemName(result)).unlockedBy(getHasName(input), has(input)).save(consumer, this.getModConversionRecipeName(result, Items.HONEYCOMB));
	}

	public void netheriteSmithingRecipe(Consumer<FinishedRecipe> consumer, Item input, RecipeCategory category, Item output) {
		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(input), Ingredient.of(Items.NETHERITE_INGOT), category, output).unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(consumer, new ResourceLocation(this.modid, getItemName(output) + "_smithing"));
	}

	public static void trimSmithing(Consumer<FinishedRecipe> consumer, ItemLike item) {
		SmithingTrimRecipeBuilder.smithingTrim(Ingredient.of(item), Ingredient.of(ItemTags.TRIMMABLE_ARMOR), Ingredient.of(ItemTags.TRIM_MATERIALS), RecipeCategory.MISC).unlocks("has_smithing_trim_template", has(item)).save(consumer, suffix(RecipeBuilder.getDefaultRecipeId(item), "_smithing_trim"));
	}

	public static void trimRecipes(Consumer<FinishedRecipe> consumer, ItemLike item, TagKey<Item> copyItem) {
		trimSmithing(consumer, item);
		copySmithingTemplate(consumer, item, copyItem);
	}

	public static void trimRecipes(Consumer<FinishedRecipe> consumer, ItemLike item, ItemLike copyItem) {
		trimSmithing(consumer, item);
		copySmithingTemplate(consumer, item, copyItem);
	}

	public static ResourceLocation suffix(ResourceLocation rl, String suffix) {
		return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
	}

	public ResourceLocation getModConversionRecipeName(ItemLike output, ItemLike input) {
		return new ResourceLocation(this.modid, getConversionRecipeName(output, input));
	}

	public String getModID() {
		return this.modid;
	}
}