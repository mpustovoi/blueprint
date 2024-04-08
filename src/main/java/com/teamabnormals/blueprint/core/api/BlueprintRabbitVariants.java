package com.teamabnormals.blueprint.core.api;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Rabbit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BlueprintRabbitVariants {
	private static final Set<BlueprintRabbitVariant> RABBIT_VARIANTS = new ObjectArraySet<>();
	public static final Map<BlueprintRabbitVariant, Rabbit.Variant> RABBIT_VARIANT_MAP = new HashMap<>();

	public static synchronized BlueprintRabbitVariant register(int id, ResourceLocation name, ResourceLocation texturePath) {
		BlueprintRabbitVariant type = new BlueprintRabbitVariant(id, name, texturePath, null);
		RABBIT_VARIANTS.add(type);
		return type;
	}

	public static synchronized BlueprintRabbitVariant register(int id, ResourceLocation name) {
		return register(id, name, new ResourceLocation(name.getNamespace(), "textures/entity/rabbit/" + name.getPath() + ".png"));
	}

	public record BlueprintRabbitVariant(int id, ResourceLocation name, ResourceLocation textureLocation, Rabbit.Variant variant) {
	}

	public static ImmutableList<BlueprintRabbitVariant> values() {
		return ImmutableList.copyOf(RABBIT_VARIANTS);
	}
}