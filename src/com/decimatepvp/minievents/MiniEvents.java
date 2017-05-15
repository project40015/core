package com.decimatepvp.minievents;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

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
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onTable(InventoryOpenEvent event){
		if(event.getInventory().getType().equals(InventoryType.ENCHANTING)){
			event.getInventory().setItem(1, new ItemStack(Material.getMaterial(351), 3, (byte)4));
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event){
		if(event.getInventory().getType().equals(InventoryType.ENCHANTING)){
			event.getInventory().setItem(1, new ItemStack(Material.AIR));
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event){
		if(event.getInventory().getType().equals(InventoryType.ENCHANTING)){
			if(event.getSlot() == 1){
				event.setCancelled(true);
				return;
			}
		}
		if(event.getWhoClicked() instanceof Player){
			Player player = (Player) event.getWhoClicked();
			if(player.getOpenInventory().getType().equals(InventoryType.ENCHANTING)){
				if((event.getInventory().getItem(event.getSlot()) != null &&
						event.getInventory().getItem(event.getSlot()).getType().equals(Material.INK_SACK))){
					event.setCancelled(true);
					return;
				}
			}
		}
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
