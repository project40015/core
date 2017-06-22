package com.decimatepvp.enchants;

import java.util.Random;

import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.DecimateCore;

public class CustomEnchant {
	
	protected final Random random;
	
	private String enchantName;
	
	private int enchantMaxLevel;
	
	private String[] lore;
	
	private ItemType[] types;
	
	public CustomEnchant(String enchantName, int enchantMaxLevel, String[] lore, ItemType... types) {
		this.enchantName = enchantName;
		this.enchantMaxLevel = enchantMaxLevel;
		this.lore = lore;
		this.types = types;
		
		this.random = new Random();
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
		if(item.getEnchantments() != null && DecimateCore.getCore().getEnchantManager().getEnchantsOnItem(item).contains(this)){
			return false;
		}
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
			if(item == null){
				return false;
			}
			return item.getType().toString().contains("_" + this.toString());
		}
	}

	public String[] getLore() {
		return lore;
	}

}
