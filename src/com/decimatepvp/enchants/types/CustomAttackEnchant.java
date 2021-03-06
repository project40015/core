package com.decimatepvp.enchants.types;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.decimatepvp.enchants.CustomEnchant;

public abstract class CustomAttackEnchant extends CustomEnchant {

	public CustomAttackEnchant(String enchantName, int enchantMaxLevel, String[] lore, ItemType... types) {
		super(enchantName, enchantMaxLevel, lore, types);
	}
	
	public abstract void onAttack(EntityDamageByEntityEvent event, int level);

}
