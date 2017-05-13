package com.decimatepvp.functions.freeze;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.decimatepvp.core.DecimateCore;

public class FreezeCommand implements CommandExecutor {

	private DecimateCore core;
	
	public FreezeCommand(DecimateCore core){
		this.core = core;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.hasPermission("Decimate.command.freeze")) {
			if(args.length == 0) {
				sender.sendMessage(color("&6Usage: &8/freeze [player]"));
			}
			else {
				OfflinePlayer plyr = Bukkit.getOfflinePlayer(args[0]);
				if(!plyr.isOnline()) {
					sender.sendMessage(color("&6Player &c" + plyr.getName() + " &6is not online!"));
					return true;
				}
				
				Player player = plyr.getPlayer();
				if(!core.getFreezeManager().isFrozen(player)) {
					if(player.hasPermission("Decimate.command.freeze.protect") && (player.isOp() ? sender.isOp() : false)) {
						sender.sendMessage(color("&6Player &c" + plyr.getName() + " &6cannot be frozen!"));
						player.sendMessage(color("&6Player &c" + sender.getName() + " &6tried to freeze you!"));
						return true;
					}
					
					core.getFreezeManager().freeze(player);
					sender.sendMessage(color("&6Player &b" + plyr.getName() + " &6has been frozen!"));
				}
				else {
					core.getFreezeManager().thaw(player);
					sender.sendMessage(color("&6Player &b" + plyr.getName() + " &6has been thawed!"));
				}
			}
		}
		else {
			sender.sendMessage(color("&cYou do not have the proper permissions."));
		}
		return false;
	}


	private String color(String textToTranslate) {
		return ChatColor.translateAlternateColorCodes('&', textToTranslate);
	}


}
