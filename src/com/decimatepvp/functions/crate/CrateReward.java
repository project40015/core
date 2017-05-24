package com.decimatepvp.functions.crate;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class CrateReward {

	private String name;
	private ItemStack icon;
	private Rarity rarity;
	private int chance;
	
	public CrateReward(String name, ItemStack icon, Rarity rarity, int chance){
		this.name = name;
		this.icon = icon.clone();
		this.rarity = rarity;
		this.chance = chance;
		ItemMeta im = this.icon.getItemMeta();
		im.setDisplayName(getFormatName());
		this.icon.setItemMeta(im);
	}
	
	public abstract void reward(Player player);
	
	public String getName(){
		return name;
	}
	
	public String getFormatName(){
		return rarity.getColor() + name + " " + rarity.getDisplay();
	}
	
	public Rarity getRarity(){
		return rarity;
	}
	
	public ItemStack getIcon(){
		return icon;
	}
	
	public int getChance(){
		return chance;
	}
	
}
