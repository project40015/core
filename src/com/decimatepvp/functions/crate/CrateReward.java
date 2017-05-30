package com.decimatepvp.functions.crate;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class CrateReward {

	private String name;
	private ItemStack icon, anim;
	private Rarity rarity;
	private int chance;
	
	public CrateReward(String name, ItemStack icon, ItemStack anim, Rarity rarity, int chance){
		this.name = name;
		this.icon = icon.clone();
		this.rarity = rarity;
		this.chance = chance;
		ItemMeta im = this.icon.getItemMeta();
		im.setDisplayName(getFormatName());
		this.icon.setItemMeta(im);
		this.anim = anim;
	}
	
	private double chance(int total){
		double d = chance/total;
		int a = (int) d * 100;
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
		return icon;
	}
	
	public int getChance(){
		return chance;
	}
	
	public ItemStack getAnimationItem(){
		return this.anim;
	}
	
}
