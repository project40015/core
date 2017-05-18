package com.decimatepvp.enchants.enchants;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.decimatepvp.enchants.types.CustomEquipEnchant;

public class WarriorEnchant extends CustomEquipEnchant {
	
	private final PotionEffect strength;

	public WarriorEnchant() {
		super("Warrior", 1, ItemType.CHESTPLATE);
		
		this.strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0);
	}

	@Override
	public void onEquip(LivingEntity entity, int level) {
		entity.addPotionEffect(strength);
	}

	@Override
	public void onDequip(LivingEntity entity, int level) {
		entity.removePotionEffect(strength.getType());
	}

}
