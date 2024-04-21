package core.registry;

import com.teamabnormals.blueprint.core.api.BlueprintRabbitVariants;
import core.BlueprintTest;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = BlueprintTest.MOD_ID)
public class TestRabbitVariants extends BlueprintRabbitVariants {
	private static final int UNIQUE_OFFSET = 11555;

	public static final BlueprintRabbitVariant CRAIG = register(UNIQUE_OFFSET, new ResourceLocation(BlueprintTest.MOD_ID, "craig"), context -> getBiome(context).is(BiomeTags.IS_END));
}