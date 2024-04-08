package com.teamabnormals.blueprint.core.mixin;

import com.mojang.serialization.Codec;
import com.teamabnormals.blueprint.core.api.BlueprintRabbitVariants;
import com.teamabnormals.blueprint.core.api.BlueprintRabbitVariants.BlueprintRabbitVariant;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Rabbit.Variant;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.IntFunction;

@Mixin(Variant.class)
public class RabbitVariantMixin {
	@Shadow
	@Mutable
	@Final
	private static Variant[] $VALUES;

	@Mutable
	@Shadow
	@Final
	private static IntFunction<Variant> BY_ID;

	@Mutable
	@Shadow
	@Final
	public static Codec<Variant> CODEC;

	@Invoker("<init>")
	private static Rabbit.Variant create(String name, int ordinal, int id, String location) {
		throw new IllegalStateException("Unreachable");
	}

	static {
		for (BlueprintRabbitVariant variant : BlueprintRabbitVariants.values()) {
			String name = variant.name().toString();
			var entry = create(WordUtils.capitalizeFully(name.replace(":", "_")), $VALUES.length, variant.id(), name);
			BlueprintRabbitVariants.RABBIT_VARIANT_MAP.put(variant, entry);
			$VALUES = ArrayUtils.add($VALUES, entry);
		}

		BY_ID = ByIdMap.sparse(Rabbit.Variant::id, $VALUES, Rabbit.Variant.BROWN);
		CODEC = StringRepresentable.fromEnum(() -> $VALUES);
	}
}
