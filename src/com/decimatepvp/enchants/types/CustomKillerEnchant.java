package com.decimatepvp.enchants.types;

import org.bukkit.event.entity.EntityDeathEvent;

import com.decimatepvp.enchants.CustomEnchant;

public abstract class CustomKillerEnchant extends CustomEnchant {

	public CustomKillerEnchant(String enchantName, int enchantMaxLevel, String[] lore, ItemType... types) {
		super(enchantName, enchantMaxLevel, lore, types);
	}
	
	public abstract void onKill(EntityDeathEvent event, int level);

}
