package com.decimatepvp.functions.crate;

import org.bukkit.ChatColor;

public enum Rarity {

	COMMON(ChatColor.WHITE + "COMMON"),
	RARE(ChatColor.YELLOW + "RARE"),
	EPIC(ChatColor.LIGHT_PURPLE + "EPIC"),
	MYTHICAL(ChatColor.RED + "MYTHICAL");
	
	private String display;
	
	Rarity(String display){
		this.display = display;
	}
	
	public String getDisplay(){
		return display;
	}
	
}
