package com.decimatepvp.functions.minicommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OnlineCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		List<String> owners, developers, moderators, helpers, donors, regular;
		
		owners = new ArrayList<>();
		developers = new ArrayList<>();
		moderators = new ArrayList<>();
		helpers = new ArrayList<>();
		donors = new ArrayList<>();
		regular = new ArrayList<>();
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(player.hasPermission("Decimate.owner")){
				owners.add(player.getName());
			}else if(player.hasPermission("Decimate.developer")){
				developers.add(player.getName());
			}else if(player.hasPermission("Decimate.moderator")){
				moderators.add(player.getName());
			}else if(player.hasPermission("Decimate.helper")){
				helpers.add(player.getName());
			}else if(player.hasPermission("Decimate.donor")){
				donors.add(player.getName());
			}else{
				regular.add(player.getName());
			}
		}
		
		arg0.sendMessage(ChatColor.GRAY + "Players online " + ChatColor.LIGHT_PURPLE + "DecimatePVP" + ChatColor.GRAY + ":");
		arg0.sendMessage("");
		arg0.sendMessage("      " + ChatColor.GRAY + "Total: " + ChatColor.WHITE +
				"" + ChatColor.BOLD + Bukkit.getServer().getOnlinePlayers().size());
		arg0.sendMessage("");
		arg0.sendMessage("      " + ChatColor.GRAY + "Owners: " + formatList(owners, ChatColor.RED));
		arg0.sendMessage("      " + ChatColor.GRAY + "Developers: " + formatList(developers, ChatColor.DARK_GREEN));
		arg0.sendMessage("      " + ChatColor.GRAY + "Moderators: " + formatList(moderators, ChatColor.BLUE));
		arg0.sendMessage("      " + ChatColor.GRAY + "Helpers: " + formatList(helpers, ChatColor.GREEN));
		arg0.sendMessage("      " + ChatColor.GRAY + "Donors: " + formatList(donors, ChatColor.GOLD));

		
		return false;
	}

	private String formatList(List<String> strings, ChatColor color){
		if(strings.size() > 5 || strings.size() == 0){
			return color + "" + strings.size();
		}else{
			int num = 1;
			String result = color + strings.get(0);
			while(strings.size() > num){
				result += ChatColor.GRAY + ", " + color + strings.get(num++);
			}
			return result;
		}
	}
	
}
