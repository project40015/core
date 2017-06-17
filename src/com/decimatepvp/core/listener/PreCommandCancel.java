package com.decimatepvp.core.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PreCommandCancel implements Listener {

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event){
//		if(event.getMessage().contains("list") || event.getMessage().contains("who") || event.getMessage().contains("online")){
//			event.getPlayer().sendMessage(ChatColor.RED + "This command is disabled for now to prevent lag.");
//			event.setCancelled(true);
//		}
	}
	
}
