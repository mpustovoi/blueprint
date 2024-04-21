package com.teamabnormals.blueprint.core.data.server;

import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;

import javax.annotation.Nullable;
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

	public void leafPileRecipes(Consumer<FinishedRecipe> consumer, ItemLike leaves, ItemLike leafPile) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, leafPile, 4).requires(leaves).group("leaf_pile").unlockedBy(getHasName(leaves), has(leaves)).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, leaves).define('#', leafPile).pattern("##").pattern("##").group("leaves").unlockedBy(getHasName(leafPile), has(leafPile)).save(consumer, this.getModConversionRecipeName(leaves, leafPile));
	}

	public void stonecutterResultFromBaseMod(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike output, ItemLike input) {
		stonecutterResultFromBase(consumer, category, output, input, 1);
	}

	public void stonecutterResultFromBaseMod(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike output, ItemLike input, int count) {
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), category, output, count).unlockedBy(getHasName(input), has(input)).save(consumer, this.getModConversionRecipeName(output, input) + "_stonecutting");
	}

	public void oneToOneConversionRecipeMod(Consumer<FinishedRecipe> consumer, ItemLike output, ItemLike input, @Nullable String group) {
		this.oneToOneConversionRecipeMod(consumer, output, input, group, 1);
	}

	public void oneToOneConversionRecipeMod(Consumer<FinishedRecipe> consumer, ItemLike output, ItemLike input, @Nullable String group, int count) {
		oneToOneConversionRecipeBuilder(output, input, count).requires(input).group(group).unlockedBy(getHasName(input), has(input)).save(consumer, this.getModConversionRecipeName(output, input));
	}

	public static ShapelessRecipeBuilder oneToOneConversionRecipeBuilder(ItemLike output, ItemLike input, int count) {
		return oneToOneConversionRecipeBuilder(RecipeCategory.MISC, output, input, count);
	}

	public static ShapelessRecipeBuilder oneToOneConversionRecipeBuilder(RecipeCategory category, ItemLike output, ItemLike input, int count) {
		return ShapelessRecipeBuilder.shapeless(category, output, count).requires(input).unlockedBy(getHasName(input), has(input));
	}

	public void nineBlockStorageRecipesMod(Consumer<FinishedRecipe> consumer, RecipeCategory itemCategory, ItemLike item, RecipeCategory storageCategory, ItemLike storage) {
		nineBlockStorageRecipes(consumer, itemCategory, item, storageCategory, storage, this.modid + ":" + getSimpleRecipeName(storage), null, this.modid + ":" + getSimpleRecipeName(item), null);
	}

	public void conditionalNineBlockStorageRecipes(Consumer<FinishedRecipe> consumer, ICondition condition, RecipeCategory itemCategory, ItemLike item, RecipeCategory storageCategory, ItemLike storage) {
		conditionalNineBlockStorageRecipes(consumer, condition, itemCategory, item, storageCategory, storage, getSimpleRecipeName(storage), null, getSimpleRecipeName(item), null);
	}

	public void conditionalNineBlockStorageRecipes(Consumer<FinishedRecipe> consumer, ICondition condition, RecipeCategory itemCategory, ItemLike item, RecipeCategory storageCategory, ItemLike storage, String storageLocation, @Nullable String itemGroup, String itemLocation, @Nullable String storageGroup) {
		conditionalRecipe(consumer, condition, itemCategory, ShapelessRecipeBuilder.shapeless(itemCategory, item, 9).requires(storage).group(storageGroup).unlockedBy(getHasName(storage), has(storage)), new ResourceLocation(this.modid, itemLocation));
		conditionalRecipe(consumer, condition, storageCategory, ShapedRecipeBuilder.shaped(storageCategory, storage).define('#', item).pattern("###").pattern("###").pattern("###").group(itemGroup).unlockedBy(getHasName(item), has(item)), new ResourceLocation(this.modid, storageLocation));
	}

	public void conditionalNineBlockStorageRecipesWithCustomUnpacking(Consumer<FinishedRecipe> consumer, ICondition condition, RecipeCategory itemCategory, ItemLike item, RecipeCategory storageCategory, ItemLike storage, String shapelessName, String shapelessGroup) {
		conditionalNineBlockStorageRecipes(consumer, condition, itemCategory, item, storageCategory, storage, getSimpleRecipeName(storage), null, shapelessName, shapelessGroup);
	}

	public static void conditionalRecipe(Consumer<FinishedRecipe> consumer, ICondition condition, RecipeCategory category, RecipeBuilder recipe) {
		conditionalRecipe(consumer, condition, category, recipe, RecipeBuilder.getDefaultRecipeId(recipe.getResult()));
	}

	public static void conditionalRecipe(Consumer<FinishedRecipe> consumer, ICondition condition, RecipeCategory category, RecipeBuilder recipe, ResourceLocation id) {
		ConditionalRecipe.builder().addCondition(condition).addRecipe(consumer1 -> recipe.save(consumer1, id)).generateAdvancement(new ResourceLocation(id.getNamespace(), "recipes/" + category.getFolderName() + "/" + id.getPath())).build(consumer, id);
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
}