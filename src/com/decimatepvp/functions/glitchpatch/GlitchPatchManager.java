package com.decimatepvp.functions.glitchpatch;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.Manager;

public class GlitchPatchManager implements Manager, Listener {

	@EventHandler
	public void onLand(PlayerTeleportEvent event){
		if(event.getCause().equals(TeleportCause.ENDER_PEARL)){
			if(event.getTo().getBlock().getLocation().clone().add(0,1,0).getBlock().getType().isSolid()){
				event.getPlayer().sendMessage(ChatColor.RED + "This pearl landed in a glitched spot.");
				event.getPlayer().getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
				event.getPlayer().updateInventory();
				event.setCancelled(true);
			}
		}
	}
	
	@Override
	public void disable() {
		
	}

}
