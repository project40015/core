package com.decimatepvp.functions.misc;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.decimatepvp.core.utils.Configuration;
import com.decimatepvp.utils.DecimateUtils;
import com.google.common.collect.Maps;

public class PlaytimeManager implements CommandExecutor, Listener, Manager {

	private Map<String, Long> playtime = Maps.newHashMap();
	
	private Map<String, Long> logintime = Maps.newHashMap();
	
	public PlaytimeManager() {
		loadPlaytime();
		loadOnlineLogin();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.hasPermission("Decimate.command.playtime")) {
			OfflinePlayer player = null;
			
			if((sender.hasPermission("Decimate.command.playtime.others")) && !(sender instanceof Player)) {
				if(args.length > 0) {
					player = Bukkit.getOfflinePlayer(args[0]);
				}
				else {
					sender.sendMessage("You must add a player!");
					return false;
				}
			}
			else {
				player = (Player) sender;
			}
			
			if(playtime.containsKey(player.getUniqueId().toString())) {
				update(player);
				
				long playtime = this.playtime.get(player.getUniqueId().toString());
				
				String time = DecimateUtils.longToTime(playtime);
				
				sender.sendMessage(DecimateUtils.color("&ePlayer &a" + player.getName() + " &e has played a total "
						+ "of " + time));
			}
			else {
				sender.sendMessage(ChatColor.RED + "That player has not played.");
			}
			
		}
		return false;
	}

	private void update(OfflinePlayer player) {
		if(playtime.containsKey(player.getUniqueId().toString())) {
			playtime.put(player.getUniqueId().toString(), 
					playtime.get(player.getUniqueId().toString()) + getTimePassed(player));

			
			if(logintime.get(player.getUniqueId().toString()) != -1) {
				logintime.put(player.getUniqueId().toString(), System.currentTimeMillis());
			}
		}
		else {
			playtime.put(player.getUniqueId().toString(), 0l);
		}
	}

	private Long getTimePassed(OfflinePlayer player) {
		return logintime.get(player.getUniqueId().toString()) == -1 ?
				0 : System.currentTimeMillis() - logintime.get(player.getUniqueId().toString());
	}

	private void loadPlaytime() {
		Configuration cfg = new Configuration(DecimateCore.getCore(), "playtime.yml");
		FileConfiguration config = cfg.getData();
		
		for(String player : config.getKeys(false)) {
			playtime.put(player, config.getLong(player));
			logintime.put(player, -1l);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if(!playtime.containsKey(event.getPlayer().getUniqueId().toString())) {
			playtime.put(event.getPlayer().getUniqueId().toString(), 0l);
		}
		
		logintime.put(event.getPlayer().getUniqueId().toString(), System.currentTimeMillis());
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		update(event.getPlayer());

		logintime.put(event.getPlayer().getUniqueId().toString(), -1l);
	}

	private void loadOnlineLogin() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			logintime.put(player.getUniqueId().toString(), System.currentTimeMillis());
		}
	}

	@Override
	public void disable() {
		updatePlayerTimes();
		
		Configuration cfg = new Configuration(DecimateCore.getCore(), "playtime.yml");
		FileConfiguration config = cfg.getData();
		
		for(String player : playtime.keySet()) {
			config.set(player, playtime.get(player));
		}
		
		cfg.saveData();
	}

	private void updatePlayerTimes() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			update(player);
		}
	}

}
