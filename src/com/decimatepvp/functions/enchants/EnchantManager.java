package com.decimatepvp.functions.enchants;

import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.enchants.CustomEnchant;
import com.decimatepvp.enchants.enchants.ExtinguishEnchant;
import com.decimatepvp.utils.DecimateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.md_5.bungee.api.ChatColor;

public class EnchantManager {
	
	private Map<String, CustomEnchant> customEnchants = Maps.newHashMap();
	
	public EnchantManager() {
		ExtinguishEnchant extinguish = new ExtinguishEnchant();
		customEnchants.put(extinguish.getEnchantName(), extinguish);
	}
	
	public List<CustomEnchant> getEnchantsOnItem(ItemStack item) {
		List<CustomEnchant> enchantments = Lists.newArrayList();
		if((item != null) && (item.hasItemMeta()) && (item.getItemMeta().hasLore())) {
			for(String lore : item.getItemMeta().getLore()) {
				lore = ChatColor.stripColor(lore);
				String enchant = lore.split(" ")[0];
				if(isEnchant(enchant)) {
					enchantments.add(getEnchant(enchant));
				}
			}
		}
		
		return enchantments;
	}
	
	public boolean addEnchantToItem(ItemStack item, String enchant, int level) {
		CustomEnchant enchantment = getEnchant(enchant);
		if((item != null) && (enchantment != null) && (enchantment.isItemApplicable(item))) {
			level = Math.min(enchantment.getEnchantMaxLevel(), Math.max(level, 0));
			
			if(!doesWeaponHaveEnchant(enchant, item)) {
				ItemMeta meta = item.getItemMeta();
				List<String> lore = Lists.newArrayList();
				lore.add(DecimateUtils.color("&2&l" + enchantment.getEnchantName() + " " + level));
				if(meta.hasLore()) {
					lore.addAll(meta.getLore());
				}
				meta.setLore(lore);
				item.setItemMeta(meta);
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isEnchant(String enchant) {
		return customEnchants.containsKey(enchant);
	}

	public boolean doesWeaponHaveEnchant(String enchant, ItemStack item) {
		if((item != null) && (item.getItemMeta().hasLore()) && (isEnchant(enchant))) {
			for(String lore : item.getItemMeta().getLore()) {
				if(lore.split(" ")[0].equals(enchant)) {
					return true;
				}
			}
		}
		return false;
	}

	public CustomEnchant getEnchant(String enchant) {
		return customEnchants.containsKey(enchant) ? customEnchants.get(enchant) : null;
	}
	
	public Map<String, CustomEnchant> getCustomEnchants() {
		return customEnchants;
	}

	public int getLevelFromItem(CustomEnchant enchantment, ItemStack item) {
		if((item != null) && (item.getItemMeta().hasLore()) && (enchantment != null)) {
			for(String lore : item.getItemMeta().getLore()) {
				if(lore.split(" ")[0].equals(enchantment.getEnchantName())) {
					return Integer.parseInt(lore.split(" ")[1]);
				}
			}
		}
		
		return -1;
	}

}
