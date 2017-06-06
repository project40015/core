package com.decimatepvp.functions.pvp.enchantment;

import org.bukkit.enchantments.Enchantment;

public class EnchantmentLimit {

	private Enchantment enchantment;
	private int level;
	
	public EnchantmentLimit(Enchantment enchantment, int level){
		this.enchantment = enchantment;
		this.level = level;
	}
	
	public Enchantment getEnchantment(){
		return enchantment;
	}
	
	public int getLevel(){
		return level;
	}
	
}
