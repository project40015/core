package com.decimatepvp.enchants.types;

import org.bukkit.event.block.BlockBreakEvent;

import com.decimatepvp.enchants.CustomEnchant;

public abstract class CustomMineEnchant extends CustomEnchant {

	public CustomMineEnchant(String enchantName, int enchantMaxLevel, String[] lore, ItemType... types) {
		super(enchantName, enchantMaxLevel, lore, types);
	}
	
	public abstract void onMine(BlockBreakEvent event, int level);

}
