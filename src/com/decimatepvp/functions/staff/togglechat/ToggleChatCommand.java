package com.decimatepvp.functions.staff.togglechat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.decimatepvp.core.DecimateCore;

public class ToggleChatCommand implements CommandExecutor {

	private DecimateCore core;
	
	public ToggleChatCommand(DecimateCore core){
		this.core = core;
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(arg0.hasPermission("Decimate.togglechat")){
			String name = "CONSOLE";
			if(arg0 instanceof Player){
				name = ((Player)arg0).getName();
			}
			core.getToggleChatManager().toggle(name);
		}
		
		return false;
	}

}
