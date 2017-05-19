package com.decimatepvp.enchants.enchants;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.decimatepvp.enchants.types.CustomEquipEnchant;

public class FishEnchant extends CustomEquipEnchant {
	
	private final PotionEffect waterBreathing;

	public FishEnchant() {
		super("Fish", 1, ItemType.HELMET);
		
		this.waterBreathing = new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0);
	}

	@Override
	public void onEquip(LivingEntity entity, int level) {
		entity.addPotionEffect(waterBreathing);
	}

	@Override
	public void onDequip(LivingEntity entity, int level) {
		entity.removePotionEffect(waterBreathing.getType());
	}

}
