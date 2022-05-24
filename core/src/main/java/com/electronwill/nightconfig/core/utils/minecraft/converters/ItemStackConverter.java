package com.electronwill.nightconfig.core.utils.minecraft.converters;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.conversion.Converter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemStackConverter implements Converter<ItemStack, Config> {

	public static final ItemStackConverter DEFAULT = new ItemStackConverter();

	@Override
	public ItemStack convertToField(Config value) {

		if(value.isNull("type")) {
			throw new IllegalArgumentException("The material type of an item can't be null");
		}

		ItemStack item = new ItemStack(Material.matchMaterial(value.get("type")), value.getOrElse("quantity", 1));

		Config meta = value.get("meta");
		if(meta != null) {
			ItemMeta itemMeta = item.getItemMeta();

			String name = meta.get("name");
			if(name != null) {
				itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			}

			List<String> lore = meta.get("description");
			if(lore != null) {
				itemMeta.setLore(
					lore.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList())
				);
			}

			Config enchants = meta.get("enchants");
			if(enchants != null) {
				for(Map.Entry<String, Object> entry : enchants.valueMap().entrySet()) {
					itemMeta.addEnchant(Enchantment.getByName(entry.getKey()), (Integer) entry.getValue(), true);
				}
			}

			List<String> flags = meta.get("flags");
			if(flags != null) {
				for(String flag : flags) {
					itemMeta.addItemFlags(ItemFlag.valueOf(flag.toUpperCase(Locale.ENGLISH)));
				}
			}

			item.setItemMeta(itemMeta);
		}

		return item;
	}

	@Override
	public Config convertFromField(ItemStack value) {

		Config config = Config.inMemory();

		config.add("type", value.getType());
		config.add("quantity", value.getAmount());

		if(value.hasItemMeta()) {
			ItemMeta itemMeta = value.getItemMeta();
			Config meta = Config.inMemory();

			if(itemMeta.hasDisplayName()) {
				meta.add("name", itemMeta.getDisplayName());
			}

			if(itemMeta.hasLore()) {
				meta.add("description", itemMeta.getLore());
			}

			if(itemMeta.hasEnchants()) {
				Config enchants = Config.inMemory();
				for(Map.Entry<Enchantment, Integer> enchantment : itemMeta.getEnchants().entrySet()) {
					enchants.add(enchantment.getKey().getName(), enchantment.getValue());
				}
				meta.add("enchants", enchants);
			}

			Set<ItemFlag> flags = itemMeta.getItemFlags();
			if(flags.size() > 0) {
				meta.add("flags",
					flags.stream().map(Enum::name).collect(Collectors.toList())
				);
			}

			config.add("meta", meta);
		}

		return config;
	}

}
