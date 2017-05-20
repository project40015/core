package com.decimatepvp.functions.staff.togglechat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.decimatepvp.core.Manager;

public class ToggleChatManager implements Listener, Manager {

	private boolean disabled = false;
	
	public void toggle(String name){
		if(!disabled){
			disabled = true;
			Bukkit.broadcastMessage(ChatColor.RED + "Chat has been disabled by " + ChatColor.YELLOW + name + ChatColor.RED + "!");
		}else{
			disabled = false;
			Bukkit.broadcastMessage(ChatColor.GREEN + "Chat has been enabled by " + ChatColor.AQUA + name + ChatColor.GREEN + "!");
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		if(disabled && !event.getPlayer().hasPermission("Decimate.togglechat.bypass")){
			event.getPlayer().sendMessage(ChatColor.RED + "Chat is currently disabled.");
			event.setCancelled(true);
		}
	}
	
	@Override
	public void disable() {
		disabled = false;
	}

}
