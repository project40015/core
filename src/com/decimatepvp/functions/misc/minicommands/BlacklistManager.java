package com.decimatepvp.functions.misc.minicommands;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.decimatepvp.core.utils.Configuration;
import com.decimatepvp.utils.DecimateUtils;
import com.google.common.collect.Maps;

public class BlacklistManager implements Manager, CommandExecutor {

	public static final String BAN_MESSAGE_REASON = "Player {BANNER} permanently banned {BANNED}: {REASON}";
	public static final String BAN_MESSAGE = "Player {BANNER} permanently banned {BANNED}";

	private Map<OfflinePlayer, String> blacklisted = Maps.newHashMap();

	private Map<OfflinePlayer, String> ipaddresses = Maps.newHashMap();

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

					if(!plyr.isOnline()) {
						sender.sendMessage(ChatColor.RED + "Player not found...");
						return false;
					}

					ban(plyr.getPlayer());
					blacklisted.put(plyr, "");

					broadcast(sender, plyr, "");
					sender.sendMessage(ChatColor.GREEN + "Player has been banned.");
				}
				else {
					OfflinePlayer plyr = Bukkit.getOfflinePlayer(args[0]);
					String reason = buildArgs(1, args);

					if(!plyr.isOnline()) {
						sender.sendMessage(ChatColor.RED + "Player not found...");
						return false;
					}

					ban(plyr.getPlayer());
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
					OfflinePlayer plyr = null;
					String ip = null;
					
					try {
						if(InetAddress.getByName(args[0]) != null) {
							ip = InetAddress.getByName(args[0]).getHostAddress();
						}
						else {
							plyr = Bukkit.getOfflinePlayer(args[0]);
						}
					} catch (UnknownHostException e) { }

					if(ip == null) {
						if(ipaddresses.containsKey(plyr)) {
							ip = getIp(plyr);
						}
						else if(plyr.isOnline()) {
							ip = plyr.getPlayer().getAddress().getAddress().getHostAddress();
						}
						else {
							sender.sendMessage(ChatColor.RED + "Player not in database. Cannot pardon ip. "
									+ "Wait until the player is online and try again");
						}
					}

					if(ip != null) {
						Bukkit.unbanIP(ip);
					}

					if(plyr != null) {
						plyr.setBanned(false);
					}

					if((plyr != null) || (ip != null)) {
						blacklisted.remove(plyr);
						sender.sendMessage(ChatColor.GREEN + "Player has been pardoned.");
					}
					else {
						sender.sendMessage(ChatColor.RED + "I'll be honest - I have no idea what happened.");
					}
				}
			}
		}
		else {
			sender.sendMessage(ChatColor.RED + "You do not have the proper permissions.");
		}

		return false;
	}

	private String getIp(OfflinePlayer plyr) {
		return ipaddresses.get(plyr);
	}

	@SuppressWarnings("deprecation")
	private void ban(Player plyr) {
		if(!plyr.isOp()) {
			Bukkit.banIP(plyr.getAddress().getAddress().getHostAddress());
			plyr.setBanned(true);
			ipaddresses.put(plyr, plyr.getAddress().getAddress().getHostAddress());
		}
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
			String ip = null;
			
			try {
				ip = InetAddress.getByName(config.getString(line + ".ip")).getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

			OfflinePlayer plyr = Bukkit.getOfflinePlayer(UUID.fromString(uid));
			if(!plyr.isBanned() && !plyr.isOp()) {
				plyr.setBanned(true);
			}

			blacklisted.put(plyr, reason);
			ipaddresses.put(plyr, ip);
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
			String ip = ipaddresses.get(plyr);

			config.set(name + " - " + uid + ".reason", reason);
			config.set(name + " - " + uid + ".ip", ip);
		}

		cfg.saveData();
	}

}
