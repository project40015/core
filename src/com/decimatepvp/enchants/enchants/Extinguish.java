package com.decimatepvp.enchants.enchants;

import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityDamageEvent;

import com.decimatepvp.enchants.types.CustomDamagedEnchant;

public class Extinguish extends CustomDamagedEnchant {

	public Extinguish() {
		super("Extinguish", 1, ItemType.HELMET, ItemType.CHESTPLATE, ItemType.LEGGINGS, ItemType.BOOTS);
	}

	@Override
	public void onDamageTaken(EntityDamageEvent event, int level) {
		Bukkit.broadcastMessage("Enchantment Success!");
	}

}
