package com.decimatepvp.functions.minicommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.decimatepvp.utils.PlayerUtils;

public class MicroCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(arg1.getName().equalsIgnoreCase("discord")){
			arg0.sendMessage(ChatColor.GRAY + "Discord: " + ChatColor.BLUE + "" + ChatColor.UNDERLINE + 
					"https://discord.gg/znFvYra");
		}else if(arg1.getName().equalsIgnoreCase("website")){
			arg0.sendMessage(ChatColor.GRAY + "Website: " + ChatColor.LIGHT_PURPLE + "" + ChatColor.UNDERLINE + 
					"http://decimatepvp.com");
		}else if(arg1.getName().equalsIgnoreCase("ping")){
			if(arg0 instanceof Player){
				arg0.sendMessage(ChatColor.GRAY + "Ping: " + ChatColor.YELLOW + getPing((Player)arg0) + "ms");
			}else{
				arg0.sendMessage("Players only.");
			}
		}
		else if(arg1.getName().equalsIgnoreCase("antitnt")) {
			Player player = (Player) arg0;
			PlayerUtils.sendFakeExplosion(player, player.getLocation().add(player.getLocation().getDirection().multiply(6)));
		}
		
		return true;
	}

	private int getPing(Player player) {
	    return ((CraftPlayer) player).getHandle().ping;
	}
	
}
