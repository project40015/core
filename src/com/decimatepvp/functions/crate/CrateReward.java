package com.decimatepvp.functions.crate;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class CrateReward {

	private String name;
	private ItemStack icon;
	private int chance;
	
	public CrateReward(String name, ItemStack icon, Rarity rarity, int chance){
		this.name = name;
		this.icon = icon;
		this.chance = chance;
	}
	
	public abstract void reward(Player player);
	
	public String getName(){
		return name;
	}
	
	public ItemStack getIcon(){
		return icon;
	}
	
	public int getChance(){
		return chance;
	}
	
}
