package com.decimatepvp.utils;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {
	
	public static ItemStack createItem(Material material, int amount, byte data, String displayName, String... lore) {
		ItemStack item = new ItemStack(material, amount, data);
		setDisplayName(item, displayName);
		setLore(item, lore);
		
		return item;
	}

	public static void setLore(ItemStack item, String[] lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
	}
	
	public static boolean isItemCloned(ItemStack i1, ItemStack i2) {
        return (i1 != null) && (i2 != null) &&
                (i1.getItemMeta().getDisplayName().equals(i2.getItemMeta().getDisplayName())) &&
                (i1.getType() == i2.getType());
    }

	public static void setDisplayName(ItemStack item, String displayName) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		item.setItemMeta(meta);
	}

}
