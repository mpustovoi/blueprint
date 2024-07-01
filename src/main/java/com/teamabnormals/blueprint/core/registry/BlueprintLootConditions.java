package com.teamabnormals.blueprint.core.registry;

import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.BlueprintConfig;
import com.teamabnormals.blueprint.core.api.conditions.loot.RaidCheckCondition.RaidCheckSerializer;
import com.teamabnormals.blueprint.core.api.conditions.loot.RandomDifficultyChanceCondition.RandomDifficultyChanceSerializer;
import com.teamabnormals.blueprint.core.util.DataUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry class for Blueprint's built-in loot conditions.
 *
 * <p>These conditions can be used by mods and datapacks.</p>
 *
 * @author abigailfails
 */
public final class BlueprintLootConditions {
	public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, Blueprint.MOD_ID);

	public static final DeferredHolder<LootItemConditionType, LootItemConditionType> RANDOM_DIFFICULTY_CHANCE = LOOT_CONDITION_TYPES.register("random_difficulty_chance", () -> new LootItemConditionType(new RandomDifficultyChanceSerializer()));
	public static final DeferredHolder<LootItemConditionType, LootItemConditionType> RAID_CHECK = LOOT_CONDITION_TYPES.register("raid_check", () -> new LootItemConditionType(new RaidCheckSerializer()));
	public static final DeferredHolder<LootItemConditionType, LootItemConditionType> CONFIG = LOOT_CONDITION_TYPES.register("config", () -> DataUtil.registerConfigCondition(Blueprint.MOD_ID, BlueprintConfig.CLIENT, BlueprintConfig.CLIENT.slabfishSettings));
}
