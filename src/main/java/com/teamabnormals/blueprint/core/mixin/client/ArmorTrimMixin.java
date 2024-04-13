package com.teamabnormals.blueprint.core.mixin.client;

import com.teamabnormals.blueprint.core.api.BlueprintTrims;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorTrim.class)
public final class ArmorTrimMixin {
	@Shadow
	@Final
	private Holder<TrimMaterial> material;

	@Inject(method = "getColorPaletteSuffix", at = @At("HEAD"), cancellable = true)
	private void getBlueprintColorPaletteSuffix(ArmorMaterial armorMaterial, CallbackInfoReturnable<String> info) {
		// TODO: Remove in later Minecraft versions because Mojang fixed this
		var trimMaterialKey = this.material.unwrapKey();
		if (trimMaterialKey.isEmpty()) return;
		var overrides = BlueprintTrims.getOverrideArmorMaterials(trimMaterialKey.get());
		if (overrides == null) return;
		String assetName = overrides.get(armorMaterial);
		if (assetName != null) info.setReturnValue(assetName);
	}
}
