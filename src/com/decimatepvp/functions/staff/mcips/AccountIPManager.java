package com.decimatepvp.functions.staff.mcips;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class AccountIPManager implements Listener, CommandExecutor {
	
	private Map<InetAddress, List<OfflinePlayer>> sharedIps = Maps.newHashMap();
	
	public AccountIPManager() {
		loadOnlinePlayers();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.hasPermission("Decimate.staff.iplist")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.GOLD + "Proper Usage: /iplist [player]");
			}
			OfflinePlayer plyr = Bukkit.getOfflinePlayer(args[0]);
			InetAddress ip = plyr.isOnline() ? plyr.getPlayer().getAddress().getAddress() : getPlayerAddress(plyr);
			if(ip != null) {
				List<OfflinePlayer> list = getAccountsOnIP(ip);
				sender.sendMessage(ChatColor.GOLD + "-------------------------------");
				sender.sendMessage(ChatColor.RED + "IP: " + ChatColor.GOLD + ip.toString() + ChatColor.RED + ": ");
				sender.sendMessage(ChatColor.RED + "Accounts on IP: " + ChatColor.GOLD + list.size());
				for(OfflinePlayer offp : list) {
					sender.sendMessage(ChatColor.GREEN + offp.getName() + ":");
					sender.sendMessage(ChatColor.YELLOW + "    UUID: " + ChatColor.GREEN + offp.getUniqueId().toString());
					if(offp.isOnline()) {
						sender.sendMessage(ChatColor.YELLOW + "    Display: " + offp.getPlayer().getDisplayName());
						sender.sendMessage(ChatColor.YELLOW + "    Online: " + ChatColor.GREEN + "True");
					}
					else {
						sender.sendMessage(
								ChatColor.YELLOW + "    Last Online: "  + ChatColor.AQUA + longToDate(offp.getLastPlayed()));
						sender.sendMessage(ChatColor.YELLOW + "    Online: " + ChatColor.RED + "False");
					}
				}
				sender.sendMessage(ChatColor.GOLD + "-------------------------------");
			}
			else {
				sender.sendMessage(ChatColor.RED + "This player has not joined.");
			}
		}
		else {
			sender.sendMessage(ChatColor.RED + "You do not have the proper permissions to use this.");
		}
			
		return false;
	}
	
	private InetAddress getPlayerAddress(OfflinePlayer plyr) {
		for(Entry<InetAddress, List<OfflinePlayer>> set : sharedIps.entrySet()) {
			if(set.getValue().contains(plyr)) {
				return set.getKey();
			}
		}
		return null;
	}

	private void loadOnlinePlayers() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(!isInList(player)) {
				addPlayerToList(player);
			}
		}
	}
	
	public List<OfflinePlayer> getAccountsOnIP(InetAddress ip) {
		return sharedIps.get(ip);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(!isInList(player)) {
			addPlayerToList(player);
		}
	}

	private void addPlayerToList(Player player) {
		if(sharedIps.containsKey(player.getAddress().getAddress())) {
			if(sharedIps.get(player.getAddress().getAddress()).contains(player)) {
				sharedIps.get(player.getAddress().getAddress()).add(player);
			}
		}
		else {
			List<OfflinePlayer> list = Lists.newArrayList();
			list.add(player);
//			list.add(Bukkit.getOfflinePlayer("Notch"));
			sharedIps.put(player.getAddress().getAddress(), list);
		}
	}
	
	private boolean isInList(Player player) {
		if(sharedIps.containsKey(player.getAddress().getAddress())) {
			return true;
		}
		return false;
	}
	
	private String longToDate(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.US);

		GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		calendar.setTimeInMillis(time);
		
		return sdf.format(calendar.getTime());
	}
	
}
