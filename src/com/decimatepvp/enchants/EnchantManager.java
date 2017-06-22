package com.decimatepvp.enchants;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.enchants.enchants.ExpEnchant;
import com.decimatepvp.enchants.enchants.ExtinguishEnchant;
import com.decimatepvp.enchants.enchants.FishEnchant;
import com.decimatepvp.enchants.enchants.SwiftnessEnchant;
import com.decimatepvp.enchants.enchants.WarriorEnchant;
import com.decimatepvp.utils.DecimateUtils;
import com.decimatepvp.utils.EnchantGlow;
import com.decimatepvp.utils.ItemUtils;
import com.decimatepvp.utils.RomanNumeralUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.md_5.bungee.api.ChatColor;

public class EnchantManager {

	private final Map<String, CustomEnchant> customEnchants = Maps.newHashMap();
	private final Map<CustomEnchant, Integer> customEnchantIds = Maps.newHashMap();
	private final Map<Integer, CustomEnchant> customEnchantIdsR = Maps.newHashMap();
	
	public EnchantManager() {
		this.registerEnchant(new ExpEnchant(), 0);
		this.registerEnchant(new FishEnchant(), 1);
		this.registerEnchant(new SwiftnessEnchant(), 2);
		this.registerEnchant(new WarriorEnchant(), 3);
		this.registerEnchant(new ExtinguishEnchant(), 4);
	}
	
	public boolean addEnchantToItem(ItemStack item, String enchant, int level) {
		CustomEnchant enchantment = this.getEnchant(ChatColor.stripColor(enchant));
		if(item != null && enchantment != null && enchantment.isItemApplicable(item)) {
			level = Math.min(enchantment.getEnchantMaxLevel(), Math.max(level, 0));
			
			if(!this.doesWeaponHaveEnchant(enchant, item)) {
				ItemMeta meta = item.getItemMeta();
				List<String> lore = Lists.newArrayList();
				lore.add(DecimateUtils.color("&7" + ChatColor.stripColor(enchantment.getEnchantName()) + " " + this.toRoman(level)));
				if(meta.hasLore()) {
					lore.addAll(meta.getLore());
				}
//				for(String str : enchantment.getLore()) {
//					lore.add(str);
//				}
				meta.setLore(lore);
				item.setItemMeta(meta);
				EnchantGlow.addGlow(item);
				return true;
			}
		}
		
		return false;
	}

	public boolean doesWeaponHaveEnchant(String enchant, ItemStack item) {
		if(item != null && item.getItemMeta().hasLore() && this.isEnchant(enchant)) {
			for(String lore : item.getItemMeta().getLore()) {
				if(lore.split(" ")[0].equals(enchant)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private int fromRoman(String roman) {
		return RomanNumeralUtils.valueOf(roman);
	}

	public Collection<CustomEnchant> getAllEnchants() {
		return customEnchants.values();
	}

	public Map<String, CustomEnchant> getCustomEnchants() {
		return customEnchants;
	}

	public CustomEnchant getEnchant(String enchant) {
		for(String ec : this.customEnchants.keySet()){
			if(ChatColor.stripColor(ec).equalsIgnoreCase(enchant)){
				return customEnchants.get(ec);
			}
		}
		return null;
	}
	
	public CustomEnchant getEnchantById(int enchantmentLevel) {
		return customEnchantIdsR.get(enchantmentLevel);
	}

	public ItemStack getEnchantedBook(String enchant, int level) {
		ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
		CustomEnchant enchantment = this.getEnchant(enchant);
		if(enchantment != null) {
			ItemUtils.setDisplayName(book, enchantment.getEnchantName() + " " + this.toRoman(level));
//			ItemUtils.setLore(book, enchantment.getLore());
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
	
	public List<CustomEnchant> getEnchantsOnItem(ItemStack item) {
		List<CustomEnchant> enchantments = Lists.newArrayList();
		if(item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
			for(String lore : item.getItemMeta().getLore()) {
				lore = ChatColor.stripColor(lore);
				String enchant = lore.split(" ")[0];
				if(this.isEnchant(enchant) && !enchantments.contains(enchant)) {
					enchantments.add(this.getEnchant(enchant));
				}
			}
		}
		
		return enchantments;
	}
	
	public int getLevelFromItem(CustomEnchant enchantment, ItemStack item) {
		if(item != null && item.getItemMeta().hasLore() && enchantment != null) {
			for(String lore : item.getItemMeta().getLore()) {
				if(lore.split(" ")[0].equals(enchantment.getEnchantName())) {
					return this.fromRoman(lore.split(" ")[1]);
				}
			}
		}
		
		return -1;
	}

	public boolean isEnchant(String enchant) {
		for(String ec : this.customEnchants.keySet()){
			if(ChatColor.stripColor(ec).equalsIgnoreCase(enchant)){
				return true;
			}
		}
		return false;
	}

	public boolean isEnchantedBook(ItemStack item) {
		return item.getEnchantmentLevel(Enchantment.ARROW_INFINITE) == 69 && 
				item.getType() == Material.ENCHANTED_BOOK &&
				customEnchantIds.containsValue(item.getEnchantmentLevel(Enchantment.ARROW_FIRE));
	}

	private void registerEnchant(CustomEnchant enchantment, int id) {
		customEnchants.put(enchantment.getEnchantName(), enchantment);
		customEnchantIds.put(enchantment, id);
		customEnchantIdsR.put(id, enchantment);
	}

	private String toRoman(int number) {
		return RomanNumeralUtils.convertToRoman(number);
	}

}