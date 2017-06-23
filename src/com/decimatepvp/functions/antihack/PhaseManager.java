package com.decimatepvp.functions.antihack;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import com.decimatepvp.utils.PlayerUtils;

public class PhaseManager implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent event){
		if(event instanceof PlayerTeleportEvent){
			return;
		}
//		Vector v = event.getPlayer().getVelocity();
//		double speed = event.getPlayer().isFlying() ? event.getPlayer().getFlySpeed() : event.getPlayer().getWalkSpeed();
//		double action = event.getPlayer().isSprinting() ? 1.3 : 1;
//		double leniency = speed*(1.4 + event.getPlayer().getFallDistance())*action;
//		PlayerUtils.sendActionbar(event.getPlayer(), ""+leniency);
		if((event.getTo().distance(event.getFrom()) > 0.4 && (event.getTo().getBlock().getType().isSolid() || event.getFrom().getBlock().getType().isSolid())
				&& event.getTo().getBlock().getType().equals(event.getFrom().getBlock().getType())) || event.getTo().distance(event.getFrom()) > 1.6){
			sendStaffMessage(event.getPlayer().getName());
			event.getPlayer().teleport(event.getFrom());
			return;
		}
//		if(event.getTo().getBlock().getType().isSolid()){
//			sendStaffMessage(event.getPlayer().getName());
//			event.getPlayer().teleport(event.getFrom());
//			return;
//		}
	}
	
	private void sendStaffMessage(String player){
		for(Player p : Bukkit.getServer().getOnlinePlayers()){
			if(p.hasPermission("decimatepvp.hack.view")){
				p.sendMessage(ChatColor.RED + player + ChatColor.GRAY + " may be using phase...");
			}
		}
	}
	
}
