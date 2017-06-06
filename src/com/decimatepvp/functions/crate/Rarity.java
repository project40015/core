package com.decimatepvp.functions.crate;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum Rarity {

	COMMON("COMMON", ChatColor.WHITE, Material.IRON_ORE),
	RARE("RARE", ChatColor.YELLOW, Material.GOLD_ORE),
	EPIC("EPIC", ChatColor.AQUA, Material.DIAMOND_ORE),
	MYTHICAL("MYTHICAL", ChatColor.GREEN, Material.EMERALD_ORE);
	
	private String name;
	private String display;
	private ChatColor color;
	private Material change;
	
	Rarity(String name, ChatColor color, Material change){
		this.name = name;
		this.color = color;
		this.display = color + "(" + name + ")";
		this.change = change;
	}
	
	public String getName(){
		return name;
	}
	
	public ChatColor getColor(){
		return color;
	}
	
	public String getDisplay(){
		return display;
	}
	
	public Material getChange(){
		return change;
	}
	
	
	
}
