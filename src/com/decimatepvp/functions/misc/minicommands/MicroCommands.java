package com.decimatepvp.functions.misc.minicommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.decimatepvp.core.DecimateCore;

public class MicroCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(arg1.getName().equalsIgnoreCase("discord")){
			arg0.sendMessage(ChatColor.GRAY + "Discord: " + ChatColor.BLUE + "" + ChatColor.UNDERLINE + 
					"https://discord.gg/znFvYra");
		}else if(arg1.getName().equalsIgnoreCase("map")){
			arg0.sendMessage(DecimateCore.getCore().getColoredDecimate() + ChatColor.WHITE + "PVP " + ChatColor.GRAY + "(season II):");
			arg0.sendMessage("");
			arg0.sendMessage(ChatColor.GRAY + "Worldborder:");
			arg0.sendMessage(ChatColor.GREEN + "  Overworld" + ChatColor.GRAY + ": " + ChatColor.DARK_AQUA + "10k " + ChatColor.GRAY + "by" + ChatColor.DARK_AQUA + " 10k");
			arg0.sendMessage(ChatColor.YELLOW + "  End" + ChatColor.GRAY + ": " + ChatColor.DARK_AQUA + "5k " + ChatColor.GRAY + "by" + ChatColor.DARK_AQUA + " 5k");
			arg0.sendMessage(ChatColor.GRAY + "Website: " + ChatColor.DARK_AQUA + "http://decimatepvp.com");
			arg0.sendMessage(ChatColor.GRAY + "Discord: " + ChatColor.DARK_AQUA + "https://discord.gg/znFvYra");
			arg0.sendMessage(ChatColor.GRAY + "Store: " + ChatColor.DARK_AQUA + "http://shop.decimatepvp.com/");
		}else if(arg1.getName().equalsIgnoreCase("store")){
			arg0.sendMessage(ChatColor.GRAY + "Store: " + ChatColor.GREEN.toString() + ChatColor.UNDERLINE + "http://shop.decimatepvp.com/");
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
		
		return true;
	}

	private int getPing(Player player) {
	    return ((CraftPlayer) player).getHandle().ping;
	}
	
}
