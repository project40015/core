package com.decimatepvp.functions.mcips;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class AccountIPManager implements Listener, CommandExecutor {
	
	private Map<InetSocketAddress, List<OfflinePlayer>> sharedIps = Maps.newHashMap();
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.hasPermission("Decimate.staff.iplist")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.GOLD + "Proper Usage: /iplist [player]");
			}
			else {
				OfflinePlayer plyr = Bukkit.getOfflinePlayer(args[0]);
				if(plyr.isOnline()) {
					Player player = plyr.getPlayer();
					List<OfflinePlayer> list = getAccountsOnIP(player.getAddress());
					sender.sendMessage(ChatColor.GOLD + "-------------------------------");
					sender.sendMessage(ChatColor.RED + "Accounts on IP: " + player.getAddress().toString() + ": ");
					for(OfflinePlayer offp : list) {
						sender.sendMessage(ChatColor.GREEN + offp.getName() + ":");
						sender.sendMessage(ChatColor.YELLOW + "    UUID: " + offp.getUniqueId().toString());
						if(offp.isOnline()) {
							sender.sendMessage(ChatColor.AQUA + "    Display: " + offp.getPlayer().getDisplayName());
						}
					}
					sender.sendMessage(ChatColor.GOLD + "-------------------------------");
				}
			}
		}
		else {
			sender.sendMessage(ChatColor.RED + "You do not have the proper permissions to use this.");
		}
			
		return false;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(!isInList(player)) {
			addPlayerToList(player);
		}
	}
	
	public List<OfflinePlayer> getAccountsOnIP(InetSocketAddress ip) {
		return sharedIps.get(ip);
	}

	@SuppressWarnings("deprecation")
	private void addPlayerToList(Player player) {
		if(sharedIps.containsKey(player.getAddress())) {
			sharedIps.get(player.getAddress()).add(player);
		}
		else {
			List<OfflinePlayer> list = Lists.newArrayList();
			list.add(player);
			list.add(Bukkit.getOfflinePlayer("Notch"));
			list.add(Bukkit.getOfflinePlayer("Herobrine"));
			sharedIps.put(player.getAddress(), list);
		}
	}
	
	private boolean isInList(Player player) {
		for(List<OfflinePlayer> list : sharedIps.values()) {
			if(list.contains(player)) {
				return true;
			}
		}
		return false;
	}
	
	
}
