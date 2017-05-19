package com.decimatepvp.enchants.enchants;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.decimatepvp.enchants.types.CustomEquipEnchant;

public class SwiftnessEnchant extends CustomEquipEnchant {
	
	private final PotionEffect speed;

	public SwiftnessEnchant() {
		super("Swiftness", 1, ItemType.BOOTS);
		
		this.speed = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1);
	}

	@Override
	public void onEquip(LivingEntity entity, int level) {
		entity.addPotionEffect(speed);
	}

	@Override
	public void onDequip(LivingEntity entity, int level) {
		entity.removePotionEffect(speed.getType());
	}

}
