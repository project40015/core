package com.decimatepvp.functions.staffchat;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.google.common.collect.Lists;

public class StaffChatManager implements Manager, Listener {
	
	private DecimateCore core;
	
	private List<String> usernames = Lists.newArrayList();
	
	public StaffChatManager() {
		core = DecimateCore.getCore();
	}
	
	@EventHandler
	public void changeChatMessage(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if(isInAdminChat(player)) {
			broadcastMessage(player, event.getMessage());
			event.setCancelled(true);
		}
	}

	private void broadcastMessage(Player player, String message) {
		Bukkit.broadcast(core.getDecimateConfig().formatStaffChatMessage(player, message), "Decimate.staff.chat");
	}

	private boolean isInAdminChat(Player player) {
		return usernames.contains(player.getUniqueId().toString());
	}
	
	public boolean togglePlayer(Player player) {
		if(isInAdminChat(player)) {
			return removePlayer(player);
		}
		
		return addPlayer(player);
	}

	public boolean addPlayer(Player player) {
		if(!usernames.contains(player.getUniqueId().toString())) {
			usernames.add(player.getUniqueId().toString());
			return true;
		}
		
		return false;
	}
	
	public boolean removePlayer(Player player) {
		if(usernames.contains(player.getUniqueId().toString())) {
			usernames.remove(player.getUniqueId().toString());
			return true;
		}
		
		return false;
	}

	@Override
	public void disable() {
		
	}
	
}
