package com.decimatepvp.functions.harvester;

import org.bukkit.Bukkit;
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
//		if(sender instanceof Player) {
		if(sender.hasPermission("Decimate.trench.hoe")) {
			if(args.length != 1){
				sender.sendMessage(ChatColor.RED + "Must give a player argument.");
				return false;
			}
			try{
				Player player = Bukkit.getPlayer(args[0]);
				core.getHarvesterManager().giveHarvester(player);
				sender.sendMessage(ChatColor.GREEN + "You have given a Harvester Hoe!");
				player.sendMessage(ChatColor.GREEN + "You have received a Harvester Hoe!");
			}catch(Exception ex){
				sender.sendMessage(ChatColor.RED + "Player not found.");
			}
		}
		else {
			sender.sendMessage(ChatColor.RED + "Only players may use this command.");
		}
//		else {
//			sender.sendMessage(ChatColor.RED + "Only players may use this command.");
//		}
		
		return false;
	}
	
}
