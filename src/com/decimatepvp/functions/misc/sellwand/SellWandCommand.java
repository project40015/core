package com.decimatepvp.functions.misc.sellwand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.decimatepvp.core.DecimateCore;

public class SellWandCommand implements CommandExecutor {
	
	private DecimateCore core;
	
	public SellWandCommand() {
		core = DecimateCore.getCore();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//		if(sender instanceof Player) {
		if(sender.hasPermission("Decimate.sellwand")) {
			if(args.length != 1){
				sender.sendMessage(ChatColor.RED + "Must give a player argument.");
				return false;
			}
			try{
				Player player = Bukkit.getPlayer(args[0]);
				core.getSellWandManager().giveSellWand(player);
				sender.sendMessage(ChatColor.GREEN + "You have given a sell wand!");
				player.sendMessage(ChatColor.GREEN + "You have received a sell wand!");
			}catch(Exception ex){
				sender.sendMessage(ChatColor.RED + "Player not found.");
			}
		}
		else {
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
		}
//		else {
//			sender.sendMessage(ChatColor.RED + "Only players may use this command.");
//		}
		
		return false;
	}
	
}
