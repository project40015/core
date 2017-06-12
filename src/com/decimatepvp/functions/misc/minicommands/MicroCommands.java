package com.decimatepvp.functions.misc.minicommands;

import org.bukkit.Bukkit;
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
			arg0.sendMessage(ChatColor.GREEN + "  Overworld" + ChatColor.GRAY + ": " + ChatColor.RED + "10k " + ChatColor.GRAY + "by" + ChatColor.RED + " 10k");
			arg0.sendMessage(ChatColor.YELLOW + "  End" + ChatColor.GRAY + ": " + ChatColor.RED + "5k " + ChatColor.GRAY + "by" + ChatColor.RED + " 5k");
			arg0.sendMessage(ChatColor.GRAY + "Website: " + ChatColor.RED + "http://decimatepvp.com");
			arg0.sendMessage(ChatColor.GRAY + "Discord: " + ChatColor.RED + "https://discord.gg/znFvYra");
			arg0.sendMessage(ChatColor.GRAY + "Store: " + ChatColor.RED + "http://shop.decimatepvp.com/");
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
		}else if(arg1.getName().equalsIgnoreCase("dbroadcast")){
			if(arg0.isOp()){
				if(arg3.length < 1){
					return true;
				}
				String message = "";
				for(int i = 0; i < arg3.length; i++){
					message += arg3[i] + " ";
				}
				message = message.trim();
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
			}else{
				arg0.sendMessage(ChatColor.RED + "You do not have permission for this.");
			}
		}
		
		return true;
	}

	private int getPing(Player player) {
	    return ((CraftPlayer) player).getHandle().ping;
	}
	
}
