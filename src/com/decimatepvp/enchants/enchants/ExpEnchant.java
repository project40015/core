package com.decimatepvp.enchants.enchants;

import org.bukkit.ChatColor;
import org.bukkit.event.entity.EntityDeathEvent;

import com.decimatepvp.enchants.types.CustomKillerEnchant;
import com.decimatepvp.utils.DecimateUtils;

public class ExpEnchant extends CustomKillerEnchant {
	
	private static String[] lore = new String[] {
//			DecimateUtils.color("&a&lI'm too lazy to think of a lore right now. I'll do it later. Gives double exp."),
			DecimateUtils.color("&a&lGives double experience.")
	};

	public ExpEnchant() {
		super(ChatColor.GREEN + "ExpBoost", 1, lore, ItemType.SWORD, ItemType.AXE, ItemType.BOW);
	}

	@Override
	public void onKill(EntityDeathEvent event, int level) {
		event.setDroppedExp(event.getDroppedExp() * 2);
	}

}
