package com.decimatepvp.enchants;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.enchants.enchants.ExpEnchant;
import com.decimatepvp.enchants.enchants.FishEnchant;
import com.decimatepvp.enchants.enchants.SwiftnessEnchant;
import com.decimatepvp.enchants.enchants.WarriorEnchant;
import com.decimatepvp.utils.DecimateUtils;
import com.decimatepvp.utils.ItemUtils;
import com.decimatepvp.utils.RomanNumeralUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.md_5.bungee.api.ChatColor;

public class EnchantManager {

	private Map<String, CustomEnchant> customEnchants = Maps.newHashMap();
	private Map<CustomEnchant, Integer> customEnchantIds = Maps.newHashMap();
	private Map<Integer, CustomEnchant> customEnchantIdsR = Maps.newHashMap();
	
	public EnchantManager() {
		registerEnchant(new ExpEnchant(), 0);
		registerEnchant(new FishEnchant(), 1);
		registerEnchant(new SwiftnessEnchant(), 2);
		registerEnchant(new WarriorEnchant(), 3);
	}
	
	private void registerEnchant(CustomEnchant enchantment, int id) {
		customEnchants.put(enchantment.getEnchantName(), enchantment);
		customEnchantIds.put(enchantment, id);
		customEnchantIdsR.put(id, enchantment);
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
				lore.add(DecimateUtils.color("&7" + enchantment.getEnchantName() + " " + toRoman(level)));
				if(meta.hasLore()) {
					lore.addAll(meta.getLore());
				}
				for(String str : enchantment.getLore()) {
					lore.add(str);
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
					return fromRoman(lore.split(" ")[1]);
				}
			}
		}
		
		return -1;
	}
	
	private int fromRoman(String roman) {
		return RomanNumeralUtils.valueOf(roman);
	}
	
	private String toRoman(int number) {
		return RomanNumeralUtils.convertToRoman(number);
	}

	public ItemStack getEnchantedBook(String enchant, int level) {
		ItemStack book = new ItemStack(Material.BOOK);
		CustomEnchant enchantment = getEnchant(enchant);
		if(enchantment != null) {
			ItemUtils.setDisplayName(book, enchantment.getEnchantName() + " " + toRoman(level));
			ItemUtils.setLore(book, enchantment.getLore());
			book.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 69);
			book.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, level);
			book.addUnsafeEnchantment(Enchantment.ARROW_FIRE, customEnchantIds.get(enchantment));
			ItemMeta meta = book.getItemMeta();
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			book.setItemMeta(meta);
			return book;
		}
		return null;
	}

	public boolean isEnchantedBook(ItemStack item) {
		return (item.getEnchantmentLevel(Enchantment.ARROW_INFINITE) == 69) && 
				(item.getType() == Material.BOOK) &&
				(customEnchantIds.containsValue(item.getEnchantmentLevel(Enchantment.ARROW_FIRE)));
	}

	public CustomEnchant getEnchantById(int enchantmentLevel) {
		return customEnchantIdsR.get(enchantmentLevel);
	}

}
