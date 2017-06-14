package com.decimatepvp.functions.misc.minicommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.utils.PlayerUtils;

public class OnlineCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		List<String> staff, donors, youtuber, regular;
		
		staff = new ArrayList<>();
		donors = new ArrayList<>();
		youtuber = new ArrayList<>();
		regular = new ArrayList<>();
		
		int specs = 0;
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(DecimateCore.getCore().getSpectateManager().isSpectating(player)){
				specs++;
				continue;
			}
			if(PlayerUtils.hasPermission(player, "Decimate.owner")){
				staff.add(player.getName());
			}else if(PlayerUtils.hasPermission(player, "Decimate.developer")){
				staff.add(player.getName());
			}else if(PlayerUtils.hasPermission(player, "Decimate.youtuber")){
				youtuber.add(player.getName());
			}else if(PlayerUtils.hasPermission(player, "Decimate.moderator")){
				staff.add(player.getName());
			}else if(PlayerUtils.hasPermission(player, "Decimate.helper")){
				staff.add(player.getName());
			}else if(PlayerUtils.hasPermission(player, "Decimate.donor")){
				donors.add(player.getName());
			}else{
				regular.add(player.getName());
			}
		}

		//_Ug's clean version:
		
		String serverName = ChatColor.translateAlternateColorCodes('&', "&cD&6E&eC&aI&bM&9A&5T&cE&FP&fV&fP");
		
		arg0.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------" + ChatColor.LIGHT_PURPLE + " " + serverName + " " 
		+ ChatColor.GRAY + "(" + ChatColor.WHITE + (Bukkit.getServer().getOnlinePlayers().size() - specs) + ChatColor.GRAY + ") " + ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");
		arg0.sendMessage("");
		arg0.sendMessage(" " + ChatColor.GRAY + "Staff: " + formatList(staff, ChatColor.YELLOW));
		arg0.sendMessage(" " + ChatColor.GRAY + "Youtubers: " + formatList(youtuber, ChatColor.RED));
		arg0.sendMessage(" " + ChatColor.GRAY + "Donors: " + formatList(donors, ChatColor.GOLD));
		arg0.sendMessage(" " + ChatColor.GRAY + "Players: " + formatList(regular, ChatColor.WHITE));
		
		//Farm's ugly version:
		
//		send(arg0, "&8==============&c&lD&6&lE&e&lC&a&lI&b&lM&9&lA&5&lT&c&lE&F&lP&f&lV&f&lP&8[&a" + (Bukkit.getServer().getOnlinePlayers().size() - specs) + "&7/&a400&8]&8==============");
//		send(arg0, "");
//		send(arg0, "&aStaff&8 >> " + formatList(staff, ChatColor.DARK_RED));
//		send(arg0, "&4You&fTubers&8 >> " + formatList(youtuber, ChatColor.RED));
//		send(arg0, "&7Donors&8 >> " + formatList(donors, ChatColor.GOLD));
//		send(arg0, "&7Players&8 >> " + formatList(regular, ChatColor.GRAY));
//		send(arg0, "");
//		send(arg0, "&8===============================================");
		
		return false;
	}
	
	@SuppressWarnings("unused")
	private void send(CommandSender sender, String message){
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	private String formatList(List<String> strings, ChatColor color){
		if(strings.size() > 5 || strings.size() == 0){
			return color + "" + strings.size();
		}else{
			int num = 1;
			String result = color + strings.get(0);
			while(strings.size() > num){
				result += ChatColor.DARK_GRAY + ", " + color + strings.get(num++);
			}
			return result;
		}
	}
	
}
