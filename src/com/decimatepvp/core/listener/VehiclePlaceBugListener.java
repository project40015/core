package com.decimatepvp.core.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class VehiclePlaceBugListener implements Listener {

	@EventHandler
	public void onPlace(PlayerInteractEvent event){
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if(event.getPlayer().getItemInHand().getType().equals(Material.MINECART) &&
					!event.getClickedBlock().getType().equals(Material.RAILS) &&
					!event.getClickedBlock().getType().equals(Material.POWERED_RAIL) &&
					!event.getClickedBlock().getType().equals(Material.DETECTOR_RAIL) &&
					!event.getClickedBlock().getType().equals(Material.ACTIVATOR_RAIL)){
				event.getPlayer().sendMessage(ChatColor.RED + "You cannot place a minecart on this block.");
				event.setCancelled(true);
			}else if(event.getPlayer().getItemInHand().getType().equals(Material.BOAT) &&
					!event.getClickedBlock().getType().equals(Material.WATER) &&
					!event.getClickedBlock().getType().equals(Material.STATIONARY_WATER)){
				event.getPlayer().sendMessage(ChatColor.RED + "You cannot place a boat on this block.");
				event.setCancelled(true);
			}
		}
	}
	
}
