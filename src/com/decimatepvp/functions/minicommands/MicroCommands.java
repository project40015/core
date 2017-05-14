package com.decimatepvp.functions.minicommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MicroCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(arg1.getName().equalsIgnoreCase("discord")){
			arg0.sendMessage(ChatColor.GRAY + "Discord: " + ChatColor.BLUE + "" + ChatColor.UNDERLINE + 
					"https://discord.gg/znFvYra");
		}else if(arg1.getName().equalsIgnoreCase("website")){
			arg0.sendMessage(ChatColor.GRAY + "Website: " + ChatColor.LIGHT_PURPLE + "" + ChatColor.UNDERLINE + 
					"http://decimatepvp.com");
		}
		
		return false;
	}

}
