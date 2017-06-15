package com.decimatepvp.enchants.enchants;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.decimatepvp.enchants.types.CustomEquipEnchant;
import com.decimatepvp.utils.DecimateUtils;

public class SwiftnessEnchant extends CustomEquipEnchant {
	
	private final PotionEffect speed;
	
	private static String[] lore = new String[] {
			DecimateUtils.color("&b&lThis enchantment allows you to move at"),
			DecimateUtils.color("&b&lspeeds Usain Bolt would laugh at but it is still fast.")
	};

	public SwiftnessEnchant() {
		super("Swiftness", 1, lore, ItemType.BOOTS);
		
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
