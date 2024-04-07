package com.teamabnormals.blueprint.core.data.client;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

public abstract class BlueprintLanguageProvider extends LanguageProvider {
	public final String modid;

	public BlueprintLanguageProvider(PackOutput output, String modid) {
		super(output, modid, "en_us");
		this.modid = modid;
	}

	public void add(Block... blocks) {
		List.of(blocks).forEach((block -> this.add(block, format(ForgeRegistries.BLOCKS.getKey(block)))));
	}

	public void addStorageBlock(Block... blocks) {
		List.of(blocks).forEach((block -> this.add(block, "Block of " + format(ForgeRegistries.BLOCKS.getKey(block)).replace(" Block", ""))));
	}

	public void add(Item... items) {
		List.of(items).forEach((item -> this.add(item, format(ForgeRegistries.ITEMS.getKey(item)))));
	}


	public void addMusicDisc(Item item, String description) {
		ResourceLocation name = ForgeRegistries.ITEMS.getKey(item);
		if (name != null) {
			this.add(item, "Music Disc");
			this.add(item.getDescriptionId() + ".desc", description);
		}
	}

	public void addEnchantment(Enchantment enchantment, String description) {
		String name = ForgeRegistries.ENCHANTMENTS.getKey(enchantment).getPath();
		this.add(enchantment, format(name));
		this.add(enchantment.getDescriptionId() + ".desc", description);
	}

	public void addCurse(Enchantment enchantment, String description) {
		String name = ForgeRegistries.ENCHANTMENTS.getKey(enchantment).getPath();
		this.add(enchantment, "Curse of " + format(name.replace("_curse", "")));
		this.add(enchantment.getDescriptionId() + ".desc", description);
	}

	public void addDamageType(String suffix, String value) {
		this.add("death.attack." + this.modid + "." + suffix, value);
	}

	public String format(ResourceLocation name) {
		return format(name.getPath());
	}

	public String format(String path) {
		return WordUtils.capitalizeFully(path.replace("_", " ")).replace(" Of ", " of ");
	}
}