package com.decimatepvp.functions.crate;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class CrateReward {

	private String name;
	private ItemStack icon;
	private Rarity rarity;
	private int chance;
	private String description;
	
	public CrateReward(String name, ItemStack icon, Rarity rarity, int chance){
		this(name, icon, rarity, chance, "");
	}
	
	public CrateReward(String name, ItemStack icon, Rarity rarity, int chance, String description){
		this.name = name;
		this.icon = icon.clone();
		this.rarity = rarity;
		this.chance = chance;
		ItemMeta im = this.icon.getItemMeta();
		im.setDisplayName(getFormatName());
		this.icon.setItemMeta(im);
		this.description = description;
	}
	
	private double chance(int total){
		double d = (chance + 0.0)/total;
		int a = (int) (d * 10000);
		return a/100.0;
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
	
	public ItemStack getIcon(int total){
		ItemStack n = icon.clone();
		ItemMeta nm = n.getItemMeta();
		List<String> lore = nm.getLore() == null ? new ArrayList<>() : nm.getLore();
		lore.add("");
		lore.add(ChatColor.GRAY + "Chance: " + ChatColor.YELLOW + chance(total) + "%");
		nm.setLore(lore);
		n.setItemMeta(nm);
		return n;
	}
	
	public int getChance(){
		return chance;
	}
	
	public String getDescription(){
		return this.description;
	}
	
}
