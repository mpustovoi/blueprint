package core.registry;

import com.teamabnormals.blueprint.core.util.registry.SoundSubRegistryHelper;
import core.BlueprintTest;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;

@EventBusSubscriber(modid = BlueprintTest.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class TestSounds {
	public static final SoundSubRegistryHelper HELPER = BlueprintTest.REGISTRY_HELPER.getSoundSubHelper();

	public static final DeferredHolder<SoundEvent, SoundEvent> AMBIENCE_TEST = HELPER.createSoundEvent("ambient.end_city.test");
}
