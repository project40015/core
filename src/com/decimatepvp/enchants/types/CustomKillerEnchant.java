package com.decimatepvp.enchants.types;

import org.bukkit.event.entity.EntityDeathEvent;

import com.decimatepvp.enchants.CustomEnchant;

public abstract class CustomKillerEnchant extends CustomEnchant {

	public CustomKillerEnchant(String enchantName, int enchantMaxLevel, ItemType... types) {
		super(enchantName, enchantMaxLevel, types);
	}
	
	public abstract void onKill(EntityDeathEvent event, int level);

}
