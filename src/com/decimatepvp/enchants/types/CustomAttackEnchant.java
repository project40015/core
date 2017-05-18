package com.decimatepvp.enchants.types;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.decimatepvp.enchants.CustomEnchant;

public abstract class CustomAttackEnchant extends CustomEnchant {

	public CustomAttackEnchant(String enchantName, int enchantMaxLevel, ItemType... types) {
		super(enchantName, enchantMaxLevel, types);
	}
	
	public abstract void onAttack(EntityDamageByEntityEvent event, int level);

}
