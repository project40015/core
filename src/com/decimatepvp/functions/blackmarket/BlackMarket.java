package com.decimatepvp.functions.blackmarket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlackMarket {

	private HashMap<ItemStack, Integer> items = new HashMap<>();
	
	public BlackMarket(){
		
	}
	
	/*
	 * Precondition: enchantments.size() == enchantmentLevels.size()
	 */
	public ItemStack customItem(String name, Material material, int amount, List<Enchantment> enchantments, List<Integer> enchantmentLevels, List<String> lore){
		ItemStack itemstack = customItem(name, material, amount, lore);
		for(int i = 0; i < enchantments.size(); i++){
			itemstack.addEnchantment(enchantments.get(i), enchantmentLevels.get(i));
		}
		return itemstack;
	}
	
	public ItemStack customItem(String name, Material material, int amount, List<String> lore){
		ItemStack itemstack = new ItemStack(material, amount);
		ItemMeta im = itemstack.getItemMeta();
		im.setLore(lore);
		if(!name.equals("")){
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		}
		itemstack.setItemMeta(im);
		return itemstack;
	}
	
}
