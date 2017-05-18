package com.decimatepvp.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.DecimateCore;

public class CustomEventCaller implements Listener {
	
	private final DecimateCore core = DecimateCore.getCore();

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerEquip(InventoryClickEvent event) {
		ItemStack current = event.getCurrentItem();
		ItemStack cursor = event.getCursor();
		int slot = event.getRawSlot();
		if((slot >= 5) && (slot <= 8)) {
			if(current.getType() == Material.AIR) {
//				Bukkit.broadcastMessage("EQUIP: " + cursor.getType().toString() + " " + slot);
				PlayerEquipEvent equipEvent = new PlayerEquipEvent((Player) event.getWhoClicked(), cursor, slot);
				core.getServer().getPluginManager().callEvent(equipEvent);
				if(equipEvent.isCancelled()) {
					event.setCancelled(true);
					return;
				}
			}
		}
		else if(current.getType() == Material.AIR) {
//			Bukkit.broadcastMessage("DEQUIP: " + cursor.getType().toString() + " " + slot);
			PlayerDequipEvent equipEvent = new PlayerDequipEvent((Player) event.getWhoClicked(), cursor, slot);
			core.getServer().getPluginManager().callEvent(equipEvent);
			if(equipEvent.isCancelled()) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
}
