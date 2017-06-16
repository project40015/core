package com.decimatepvp.functions.misc.minicommands;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.decimatepvp.core.utils.Configuration;
import com.decimatepvp.utils.DecimateUtils;
import com.google.common.collect.Maps;

public class BlacklistManager implements Manager, CommandExecutor {

	public static final String BAN_MESSAGE_REASON = "Player {BANNER} permanently banned {BANNED}: {REASON}";
	public static final String BAN_MESSAGE = "Player {BANNER} permanently banned {BANNED}";
	
	private Map<OfflinePlayer, String> blacklisted = Maps.newHashMap();
	
	public BlacklistManager() {
		loadBlackListed();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.hasPermission("Decimate.staff.blacklist")) {
			if(command.getName().equalsIgnoreCase("blacklist")) {
				if(args.length == 0) {
					sender.sendMessage(ChatColor.GOLD + "Proper Usage: /blacklist [player] [reason]");
				}
				else if(args.length == 1) {
					OfflinePlayer plyr = Bukkit.getOfflinePlayer(args[0]);
					
					ban(plyr);
					blacklisted.put(plyr, "");
					
					broadcast(sender, plyr, "");
					sender.sendMessage(ChatColor.GREEN + "Player has been banned.");
				}
				else {
					OfflinePlayer plyr = Bukkit.getOfflinePlayer(args[0]);
					String reason = buildArgs(1, args);
					
					ban(plyr);
					blacklisted.put(plyr, reason);
					
					broadcast(sender, plyr, reason);
					sender.sendMessage(ChatColor.GREEN + "Player has been banned.");
				}
			}
			else if(command.getName().equalsIgnoreCase("blacklistpardon")) {
				if(args.length == 0) {
					sender.sendMessage(ChatColor.GOLD + "Proper Usage: /blacklistpardon [player]");
				}
				else if(args.length == 1) {
					OfflinePlayer plyr = Bukkit.getOfflinePlayer(args[0]);
					
					plyr.setBanned(false);
					blacklisted.remove(plyr);
					sender.sendMessage(ChatColor.GREEN + "Player has been pardoned.");
				}
			}
		}
		else {
			sender.sendMessage(ChatColor.RED + "You do not have the proper permissions.");
		}
		
		return false;
	}
	
	@SuppressWarnings("deprecation")
	private void ban(OfflinePlayer plyr) {
		if(plyr.isOnline()) {
			plyr.getPlayer().kickPlayer("You have been blacklisted!");
		}
		
		plyr.setBanned(true);
	}

	private void broadcast(CommandSender sender, OfflinePlayer plyr, String reason) {
		String broadcast = "";
		if(reason.length() > 0) {
			broadcast = BAN_MESSAGE_REASON.replace("{BANNER}", sender.getName()).replace("{BANNED}", plyr.getName())
					.replace("{REASON}", reason);
		}
		else {
			broadcast = BAN_MESSAGE.replace("{BANNER}", sender.getName()).replace("{BANNED}", plyr.getName());
		}
		
		Bukkit.broadcastMessage(DecimateUtils.color(broadcast));
	}

	private String buildArgs(int offset, String[] args) {
		String string = "";
		for(int i = offset; i < args.length; i++) {
			String str = args[i];
			if(i == args.length) {
				string += str;
			}
			else {
				string += str + " ";
			}
		}
		
		return string;
	}

	@SuppressWarnings("deprecation")
	private void loadBlackListed() {
		Configuration cfg = new Configuration(DecimateCore.getCore(), "blacklist.yml");
		FileConfiguration config = cfg.getData();

		for(String line : config.getKeys(false)) {
			String uid = line.split(" - ")[1];
			String reason = config.getString(line + ".reason");
			
			OfflinePlayer plyr = Bukkit.getOfflinePlayer(UUID.fromString(uid));
			if(!plyr.isBanned()) {
				plyr.setBanned(true);
			}
			
			blacklisted.put(plyr, reason);
		}
		
	}

	@Override
	public void disable() {
		Configuration cfg = new Configuration(DecimateCore.getCore(), "blacklist.yml");
		cfg.reset();
		FileConfiguration config = cfg.getData();
		
		for(OfflinePlayer plyr : blacklisted.keySet()) {
			String reason = blacklisted.get(plyr);
			String name = plyr.getName();
			String uid = plyr.getUniqueId().toString();
			
			config.set(name + " - " + uid + ".reason", reason);
		}
		
		cfg.saveData();
	}

}
