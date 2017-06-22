package com.decimatepvp.enchants.enchants;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.decimatepvp.enchants.types.CustomEquipEnchant;
import com.decimatepvp.utils.DecimateUtils;

public class ExtinguishEnchant extends CustomEquipEnchant {
	
	private static String[] lore = new String[] {
			DecimateUtils.color("&2&lOH NO!! I'M ON FIRE!!"),
			DecimateUtils.color("&2&lOh... It feels nice. Nvm.")
	};
	
	private final PotionEffect fire;

	public ExtinguishEnchant() {
		super(ChatColor.GOLD + "Extinguish", 1, lore, ItemType.HELMET, ItemType.CHESTPLATE, ItemType.LEGGINGS, ItemType.BOOTS);
		
		fire = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1);
	}

	@Override
	public void onDequip(LivingEntity entity, int level) {
		entity.removePotionEffect(fire.getType());
	}

	@Override
	public void onEquip(LivingEntity entity, int level) {
		entity.addPotionEffect(fire);
	}

}
