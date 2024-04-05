package core.data.client;

import com.teamabnormals.blueprint.core.api.BlueprintTrims;
import core.BlueprintTest;
import core.registry.TestTrimPatterns;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;

public final class TestSpriteSourceProvider extends SpriteSourceProvider {

	public TestSpriteSourceProvider(PackOutput output, ExistingFileHelper fileHelper) {
		super(output, fileHelper, BlueprintTest.MOD_ID);
	}

	@Override
	protected void addSources() {
		// TODO: Materials
		this.atlas(BlueprintTrims.ARMOR_TRIMS_ATLAS)
				.addSource(BlueprintTrims.patternPermutationsOfVanillaMaterials(TestTrimPatterns.PRIMAL))
				.addSource(BlueprintTrims.materialPatternPermutations());
		this.atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(BlueprintTrims.materialPermutationsForItemLayers());
	}

}
