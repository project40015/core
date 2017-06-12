package com.decimatepvp.enchants.types;

import org.bukkit.event.player.PlayerInteractEvent;

import com.decimatepvp.enchants.CustomEnchant;

public abstract class CustomClickEnchant extends CustomEnchant {

	public CustomClickEnchant(String enchantName, int enchantMaxLevel, String[] lore, ItemType... types) {
		super(enchantName, enchantMaxLevel, lore, types);
	}
	
	public abstract void onClick(PlayerInteractEvent event, int level);

}
