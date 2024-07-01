package com.teamabnormals.blueprint.common.world.modification.structure;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * An implementation of {@link StructureRepaletter} that replaces tagged {@link BlockState} instances with weighted random blocks.
 * <p>Use {@link #CODEC} for serializing and deserializing instances of this class.</p>
 *
 * @author SmellyModder (Luke Tonon)
 * @see StructureRepaletter
 */
public record WeightedStructureRepaletter(TagKey<Block> replacesBlocks, WeightedRandomList<WeightedEntry.Wrapper<Block>> replacesWith) implements StructureRepaletter {
	public static final MapCodec<WeightedStructureRepaletter> CODEC = RecordCodecBuilder.mapCodec(instance -> {
		return instance.group(
				TagKey.codec(Registries.BLOCK).fieldOf("replaces_blocks").forGetter(repaletter -> repaletter.replacesBlocks),
				WeightedRandomList.codec(WeightedEntry.Wrapper.codec(BuiltInRegistries.BLOCK.byNameCodec())).fieldOf("replaces_with").forGetter(repaletter -> repaletter.replacesWith)
		).apply(instance, WeightedStructureRepaletter::new);
	});

	@Nullable
	@Override
	public BlockState getReplacement(ServerLevelAccessor level, BlockState state, RandomSource random) {
		return state.is(this.replacesBlocks) ? this.replacesWith.getRandom(random).orElseThrow().data().withPropertiesOf(state) : null;
	}

	@Override
	public MapCodec<? extends StructureRepaletter> codec() {
		return CODEC;
	}
}
