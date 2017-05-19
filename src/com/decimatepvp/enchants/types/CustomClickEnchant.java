package com.decimatepvp.enchants.types;

import org.bukkit.event.player.PlayerInteractEvent;

import com.decimatepvp.enchants.CustomEnchant;

public abstract class CustomClickEnchant extends CustomEnchant {

	public CustomClickEnchant(String enchantName, int enchantMaxLevel, ItemType... types) {
		super(enchantName, enchantMaxLevel, types);
	}
	
	public abstract void onClick(PlayerInteractEvent event, int level);

}
