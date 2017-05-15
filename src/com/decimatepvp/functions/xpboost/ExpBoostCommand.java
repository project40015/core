package com.decimatepvp.functions.xpboost;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.decimatepvp.core.DecimateCore;

public class ExpBoostCommand implements CommandExecutor {
	
	private DecimateCore core = DecimateCore.getCore();

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { 
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("Decimate.enchant.xpboost")) {
				if(args.length == 0) {
					sender.sendMessage(ChatColor.GOLD + "Proper Usage: /expboost [player]");
				}
				else {
					OfflinePlayer plyr = Bukkit.getOfflinePlayer(args[0]);
					if(plyr.isOnline()) {
						core.getExpBoostManager().giveXpBook(plyr.getPlayer());
					}
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "You do not have the proper permissions to use this.");
			}
		}
		else {
			sender.sendMessage(ChatColor.RED + "Only players may use this command");
		}
		
		return false;
	}

}
