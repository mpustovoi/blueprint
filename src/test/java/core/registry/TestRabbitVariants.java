package core.registry;

import com.teamabnormals.blueprint.core.api.BlueprintRabbitVariants;
import com.teamabnormals.blueprint.core.api.BlueprintRabbitVariants.BlueprintRabbitVariant;
import core.BlueprintTest;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = BlueprintTest.MOD_ID)
public class TestRabbitVariants {
	private static final int UNIQUE_OFFSET = 11555;

	public static final BlueprintRabbitVariant CRAIG = BlueprintRabbitVariants.register(UNIQUE_OFFSET, new ResourceLocation(BlueprintTest.MOD_ID, "craig"));
}