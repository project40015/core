package com.decimatepvp.enchants.types;

import org.bukkit.event.entity.EntityDamageEvent;

import com.decimatepvp.enchants.CustomEnchant;

public abstract class CustomDamagedEnchant extends CustomEnchant {

	public CustomDamagedEnchant(String enchantName, int enchantMaxLevel, String[] lore, ItemType... types) {
		super(enchantName, enchantMaxLevel, lore, types);
	}
	
	public abstract void onDamageTaken(EntityDamageEvent event, int level);

}
