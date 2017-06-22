package com.decimatepvp.enchants.enchants;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.decimatepvp.enchants.types.CustomEquipEnchant;
import com.decimatepvp.utils.DecimateUtils;

public class WarriorEnchant extends CustomEquipEnchant {
	
	private final PotionEffect strength;
	
	private static String[] lore = new String[] {
			DecimateUtils.color("&c&lThis enchantment channels your"),
			DecimateUtils.color("&c&linner warrior giving you permanent Strength II.")
	};

	public WarriorEnchant() {
		super(ChatColor.RED + "Warrior", 1, lore, ItemType.CHESTPLATE);
		
		this.strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1);
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
