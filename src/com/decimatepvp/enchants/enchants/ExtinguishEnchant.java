package com.decimatepvp.enchants.enchants;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.decimatepvp.enchants.types.CustomDamagedEnchant;
import com.decimatepvp.utils.DecimateUtils;

public class ExtinguishEnchant extends CustomDamagedEnchant {
	
	private static String[] lore = new String[] {
			DecimateUtils.color("&2&lOH NO!! I'M ON FIRE!! Oh wait, as soon as I take damage it goes away. Neat.")
	};

	public ExtinguishEnchant() {
		super("Extinguish", 1, lore, ItemType.HELMET, ItemType.CHESTPLATE, ItemType.LEGGINGS, ItemType.BOOTS);
	}

	@Override
	public void onDamageTaken(EntityDamageEvent event, int level) {
		if((event.getCause() == DamageCause.FIRE) || (event.getCause() == DamageCause.FIRE_TICK)) {
			event.getEntity().setFireTicks(0);
		}
	}

}
