package com.decimatepvp.enchants.enchants;

import org.bukkit.event.entity.EntityDeathEvent;

import com.decimatepvp.enchants.types.CustomKillerEnchant;

public class ExpEnchant extends CustomKillerEnchant {

	public ExpEnchant() {
		super("ExpBoost", 1, ItemType.SWORD, ItemType.AXE, ItemType.BOW);
	}

	@Override
	public void onKill(EntityDeathEvent event, int level) {
		event.setDroppedExp(event.getDroppedExp() * 2);
	}

}
