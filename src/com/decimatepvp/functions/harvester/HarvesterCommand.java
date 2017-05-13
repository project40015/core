package com.decimatepvp.functions.harvester;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.decimatepvp.core.DecimateCore;

public class HarvesterCommand implements CommandExecutor {
	
	private DecimateCore core;
	
	public HarvesterCommand() {
		core = DecimateCore.getCore();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("Decimate.trench.hoe")) {
				core.getHarvesterManager().giveHarvester(player);
				player.sendMessage(ChatColor.GREEN + "You have been given a Harvester Hoe!");
			}
			else {
				sender.sendMessage(ChatColor.RED + "Only players may use this command.");
			}
		}
		else {
			sender.sendMessage(ChatColor.RED + "Only players may use this command.");
		}
		
		return false;
	}
	
}
