package com.decimatepvp.enchants.enchants;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.decimatepvp.enchants.types.CustomEquipEnchant;
import com.decimatepvp.utils.DecimateUtils;

public class FishEnchant extends CustomEquipEnchant {
	
	private final PotionEffect waterBreathing;
	
	private static String[] lore = new String[] {
			DecimateUtils.color("&3&lWanna Aquaman?! Of course you don't but you can "),
			DecimateUtils.color("&3&lbreathe under water with this so that's nice.")
	};

	public FishEnchant() {
		super(ChatColor.BLUE + "Fish", 1, lore, ItemType.HELMET);
		
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
