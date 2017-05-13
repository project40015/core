package com.decimatepvp.functions.staffchat;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.decimatepvp.core.Manager;
import com.google.common.collect.Lists;

public class StaffChatManager implements Manager, Listener {
	
	private List<String> usernames = Lists.newArrayList();
	
	@EventHandler
	public void changeChatMessage(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if(isInAdminChat(player)) {
			
		}
	}

	private boolean isInAdminChat(Player player) {
		return usernames.contains(player.getUniqueId().toString());
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
		// TODO Auto-generated method stub
		
	}
	
}
