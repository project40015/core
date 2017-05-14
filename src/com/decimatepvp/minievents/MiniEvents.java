package com.decimatepvp.minievents;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.decimatepvp.utils.PlayerUtils;

public class MiniEvents implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		event.setJoinMessage("");
		PlayerUtils.sendTitle(event.getPlayer(), "DECIMATEPVP", 1*20, 2*20, 1*20, ChatColor.LIGHT_PURPLE);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		event.setQuitMessage("");
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event){
		event.setLeaveMessage("");
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event){
		if(event.getCause().equals(TeleportCause.ENDER_PEARL)){
			return;
		}
		if(!event.getPlayer().hasPermission("Decimate.fly-disable-bypass")){
			event.getPlayer().setFallDistance(0);
			if(event.getPlayer().isFlying()){
				event.getPlayer().setFlying(false);
			}
			if(event.getPlayer().getAllowFlight()){
				event.getPlayer().setAllowFlight(false);
			}
		}
	}
	
}
