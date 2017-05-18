package com.decimatepvp.enchants;

import org.bukkit.inventory.ItemStack;

public class CustomEnchant {
	
	private String enchantName;
	
	private int enchantMaxLevel;
	
	private ItemType[] types;
	
	
	public CustomEnchant(String enchantName, int enchantMaxLevel, ItemType... types) {
		this.enchantName = enchantName;
		this.enchantMaxLevel = enchantMaxLevel;
		this.types = types;
	}

	public String getEnchantName() {
		return enchantName;
	}

	public int getEnchantMaxLevel() {
		return enchantMaxLevel;
	}

	public ItemType[] getTypes() {
		return types;
	}
	
	public boolean isItemApplicable(ItemStack item) {
		for(ItemType type : types) {
			if(type.isItemOfType(item)) {
				return true;
			}
		}
		
		return false;
	}

	public enum ItemType {
		SWORD, AXE, HOE, SPADE, PICKAXE, HELMET, CHESTPLATE, LEGGINGS, BOOTS, BOW;
		
		public boolean isItemOfType(ItemStack item) {
			return item.getType().toString().contains("_" + this.toString());
		}
	}

}
