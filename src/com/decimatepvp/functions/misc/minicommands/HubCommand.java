package com.decimatepvp.functions.misc.minicommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.decimatepvp.utils.BungeeUtils;

public class HubCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(!(arg0 instanceof Player)){
			arg0.sendMessage("This command is for players.");
			return false;
		}
		
		((Player)arg0).sendMessage(ChatColor.YELLOW + "Returning you to the hub...");
		BungeeUtils.send((Player) arg0, "lobby");
		
		return false;
	}

}
