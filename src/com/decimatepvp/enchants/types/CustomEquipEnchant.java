package com.decimatepvp.enchants.types;

import org.bukkit.entity.LivingEntity;

import com.decimatepvp.enchants.CustomEnchant;

public abstract class CustomEquipEnchant extends CustomEnchant {

	public CustomEquipEnchant(String enchantName, int enchantMaxLevel, String[] lore, ItemType... types) {
		super(enchantName, enchantMaxLevel, lore, types);
	}
	
	public abstract void onEquip(LivingEntity entity, int level);
	
	public abstract void onDequip(LivingEntity entity, int level);

}
