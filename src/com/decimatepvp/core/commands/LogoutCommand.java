package com.decimatepvp.core.commands;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.decimatepvp.core.DecimateCore;
import com.google.common.collect.Maps;

public class LogoutCommand implements CommandExecutor {

	public Map<Player, Integer> logoutT = Maps.newHashMap();
	public Map<Player, Location> logoutL = Maps.newHashMap();
	
	public LogoutCommand() {
		getLogoutTask().runTaskTimer(DecimateCore.getCore(), 0L, 20L);
	}

	private BukkitRunnable getLogoutTask() {
		return new BukkitRunnable() {

			@Override
			public void run() {
				for(Player player : logoutT.keySet()) {
					if(player != null) {
						int time = logoutT.get(player);
						double distance = logoutL.get(player).distance(player.getLocation());
						
						if(distance > 0.25D) {
							player.sendMessage(color("&cCancelling Logout. Please try again."));
							logoutT.remove(player);
							logoutL.remove(player);
							continue;
						}
						
						if(time == 0) {
							logout(player);
							logoutT.remove(player);
							logoutL.remove(player);
						}
						else if(time != 5){
							player.sendMessage(color("&6" + time + " seconds..."));
						}

						time--;
						logoutT.replace(player, time);
					}
					else {
						logoutT.remove(player);
						logoutL.remove(player);
					}
				}
			}
			
		};
	}

	private void logout(OfflinePlayer player) {
		if(player.isOnline()) {
			try {
				player.getPlayer().setMetadata("LogoutCommand", new FixedMetadataValue(DecimateCore.getCore(), true));
				((CraftPlayer) player.getPlayer()).getHandle().playerConnection.disconnect("You have logged out!");
				DecimateCore.getCore().getPvpManager().removeFromList(player);
			}
			catch(Exception e) {
				player.getPlayer().sendMessage(ChatColor.RED + "Something went wrong. Please try again.");
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.hasPermission("Decimate.command.logout")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(logoutT.containsKey(player)) {
					logoutT.remove(player);
					logoutL.remove(player);
				}
				
				player.sendMessage(color("&6Logging you out in 5 seconds. Please stay still..."));
				logoutT.put(player, 5);
				logoutL.put(player, player.getLocation());
			}
			else {
				sender.sendMessage(color("&cOnly players may use this command."));
			}
		}
		else {
			sender.sendMessage(color("&cYou do not have the proper permissions."));
		}
		return false;
	}


	private String color(String textToTranslate) {
		return ChatColor.translateAlternateColorCodes('&', textToTranslate);
	}

}
