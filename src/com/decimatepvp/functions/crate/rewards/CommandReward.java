package com.decimatepvp.functions.crate.rewards;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.functions.crate.CrateReward;
import com.decimatepvp.functions.crate.Rarity;

public class CommandReward extends CrateReward {

	private String command;
	
	public CommandReward(String name, ItemStack icon, Rarity rarity, int chance, String command) {
		super(name, icon, rarity, chance);
		this.command = command;
	}

	@Override
	public void reward(Player player) {
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", player.getName()));
	}

}
