package com.decimatepvp.functions.staff.staffchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.decimatepvp.core.DecimateCore;

public class StaffChatCommand implements CommandExecutor {
	
	private DecimateCore core;
	
	public StaffChatCommand() {
		core = DecimateCore.getCore();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("Decimate.staff.chat")) {
				if(core.getStaffChatManager().togglePlayer(player)) {
					player.sendMessage(ChatColor.GREEN + "You have enabled staff chat!");
				}else {
					player.sendMessage(ChatColor.RED + "You have disabled staff chat!");
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			}
		}
		else {
			sender.sendMessage(ChatColor.RED + "Only players may use this command.");
		}
		
		return false;
	}
	
}
