package com.decimatepvp.functions.misc.minicommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.decimatepvp.utils.BungeeUtils;
import com.decimatepvp.utils.PlayerUtils;

public class HubCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(!(arg0 instanceof Player)){
			arg0.sendMessage("This command is for players.");
			return false;
		}
		
		if(!PlayerUtils.isInSpawn((Player)arg0)){
			arg0.sendMessage(ChatColor.RED + "This command is only usable in safezones. If you wish to use it here please type " + ChatColor.YELLOW + "/logout" + ChatColor.RED + "!");
			return false;
		}
		
		((Player)arg0).sendMessage(ChatColor.YELLOW + "Returning you to the hub...");
		BungeeUtils.send((Player) arg0, "lobby");
		
		return false;
	}

}
