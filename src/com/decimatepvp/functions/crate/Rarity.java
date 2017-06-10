package com.decimatepvp.functions.crate;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum Rarity {

	COMMON("COMMON", ChatColor.WHITE, Material.IRON_ORE, 1),
	RARE("RARE", ChatColor.YELLOW, Material.GOLD_ORE, 2),
	EPIC("EPIC", ChatColor.AQUA, Material.DIAMOND_ORE, 3),
	MYTHICAL("MYTHICAL", ChatColor.GREEN, Material.EMERALD_ORE, 4);
	
	private String name;
	private String display;
	private ChatColor color;
	private Material change;
	private int val;
	
	Rarity(String name, ChatColor color, Material change, int val){
		this.name = name;
		this.color = color;
		this.display = color + "(" + name + ")";
		this.change = change;
		this.val = val;
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
	
	public int getValue(){
		return val;
	}
	
	public boolean isRarer(Rarity r){
		return this.val > r.val;
	}
	
	
	
}
