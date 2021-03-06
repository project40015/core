package com.decimatepvp.functions.misc.minicommands;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.utils.DecimateUtils;

import decimatenetworkcore.core.DataUser;
import decimatenetworkcore.core.DecimateNetworkCore;

public class MicroCommands implements CommandExecutor {

	long then;
	
	@SuppressWarnings("deprecation")
	public MicroCommands(){
		then = (new Date(2017-1900, 10-1, 7, 15, 0)).getTime();
	}
	
	private String tntEnable(){
		//PST
		if(System.currentTimeMillis() >= then){
			return "enabled!";
		}
		long time = then - System.currentTimeMillis();
	
		return DecimateUtils.longToTime(time);
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(arg1.getName().equalsIgnoreCase("discord")){
			arg0.sendMessage(ChatColor.GRAY + "Discord: " + ChatColor.BLUE + "" + ChatColor.UNDERLINE + 
					"https://discord.gg/znFvYra");
		}else if(arg1.getName().equalsIgnoreCase("map")){
			arg0.sendMessage(DecimateCore.getCore().getColoredDecimate() + ChatColor.WHITE + "PVP " + ChatColor.GRAY + "(season III):");
			arg0.sendMessage("");
			arg0.sendMessage(ChatColor.GRAY + "TNT Enable: " + ChatColor.RED + tntEnable());
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
		}else if(arg1.getName().equalsIgnoreCase("dpurchase")){
			if(arg0.equals(Bukkit.getConsoleSender())){
				if(arg3.length == 2){
					String uuid = arg3[0];
					double payment = Double.parseDouble(arg3[1]);
					DataUser du = DecimateNetworkCore.getInstance().getDataUserManager().getDataUser(uuid); 
					if(du != null){
						du.setDonations(du.getTotalDonations() + payment);
					}
				}
			}else{
				arg0.sendMessage(ChatColor.RED + "Only the console can run this command.");
			}
		}else if(arg1.getName().equalsIgnoreCase("dsetrank")){
			if(arg0.equals(Bukkit.getConsoleSender())){
				if(arg3.length == 2){
					String uuid = arg3[0];
					String rank = arg3[1].toUpperCase();
					DecimateNetworkCore.getInstance().getDataUserManager().setRank(uuid, rank);
				}
			}else{
				arg0.sendMessage(ChatColor.RED + "Only the console can run this command.");
			}
		}
		
		return true;
	}

	private int getPing(Player player) {
	    return ((CraftPlayer) player).getHandle().ping;
	}
	
}
