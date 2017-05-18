package com.decimatepvp.enchants.types;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.decimatepvp.enchants.CustomEnchant;

public abstract class CustomDeathEnchant extends CustomEnchant {

	public CustomDeathEnchant(String enchantName, int enchantMaxLevel, ItemType... types) {
		super(enchantName, enchantMaxLevel, types);
	}
	
	public abstract void onDeath(EntityDamageByEntityEvent event, int level);

}
