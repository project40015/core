package com.decimatepvp.functions.crate.rewards;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.functions.crate.CrateReward;
import com.decimatepvp.functions.crate.Rarity;

public class ItemReward extends CrateReward {

	private ItemStack item;
	
	public ItemReward(ItemStack item, ItemStack anim, Rarity rarity, int chance) {
		super(rarity.getColor() + item.getType().toString().toLowerCase().replaceAll("_", " "), item, anim, rarity, chance);
		this.item = item;
	}

	@Override
	public void reward(Player player) {
		player.getInventory().addItem(item);
	}
	
}
