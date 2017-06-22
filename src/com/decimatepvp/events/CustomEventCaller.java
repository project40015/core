package com.decimatepvp.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.enchants.CustomEnchant.ItemType;

public class CustomEventCaller implements Listener {
	
	private final DecimateCore core = DecimateCore.getCore();

	@EventHandler
	public void onPlayerEquip(PlayerInteractEvent event) {
		if((event.getAction() == Action.RIGHT_CLICK_AIR) ||
				(event.getAction() == Action.RIGHT_CLICK_BLOCK)){
			Player player = event.getPlayer();
			ItemStack hand = event.getItem();
			if((hand != null) && (hand.getType() != Material.AIR)) {
				ItemType type = getArmour(hand);
				if(type != null) {
					ItemStack armour = null;
					int id = 0;
					if(type == ItemType.BOOTS) {
						armour = player.getEquipment().getBoots();
						id = 8;
					}
					else if(type == ItemType.LEGGINGS) {
						armour = player.getEquipment().getLeggings();
						id = 7;
						}
					else if(type == ItemType.CHESTPLATE) {
						armour = player.getEquipment().getChestplate();
						id = 6;
						}
					else if(type == ItemType.HELMET) {
						armour = player.getEquipment().getHelmet();
						id = 5;
					}

					if((armour == null) || (armour.getType() == Material.AIR)) {
						final ItemStack f = hand;
						final int fi = id;
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DecimateCore.getCore(), new Runnable(){

							@Override
							public void run() {
								if(type == ItemType.BOOTS && player.getInventory().getBoots() != null ||
										type == ItemType.LEGGINGS && player.getInventory().getLeggings() != null ||
										type == ItemType.CHESTPLATE && player.getInventory().getChestplate() != null ||
										type == ItemType.HELMET && player.getInventory().getHelmet() != null){
									callEquip(player, f, fi);
								}
							}
							
						}, 1L);
//						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerEquip(InventoryClickEvent event) {
		if(!(event.getWhoClicked() instanceof Player)){
			return;
		}
		ItemStack current = event.getCurrentItem();
		ItemStack cursor = event.getCursor();
		int slot = event.getRawSlot();
		if(event.getClick().equals(ClickType.DOUBLE_CLICK)){
			return;
		}
	
		ItemType type = getArmour((cursor != null && !cursor.getType().equals(Material.AIR)) ? cursor : current);
		if(event.getClick().equals(ClickType.SHIFT_LEFT) || event.getClick().equals(ClickType.SHIFT_RIGHT)){
			Player player = (Player) event.getWhoClicked();
			if(event.getInventory().getType().equals(InventoryType.CRAFTING)){
			if(type != null && type.equals(ItemType.HELMET) && (player.getInventory().getHelmet() == null || player.getInventory().getHelmet().getType().equals(Material.AIR))){
				if(!callEquip((Player) event.getWhoClicked(), current, 5)) {
					event.setCancelled(true);
					return;
				}
			}
			if(type != null && type.equals(ItemType.CHESTPLATE) && (player.getInventory().getChestplate() == null || player.getInventory().getChestplate().getType().equals(Material.AIR))){
				if(!callEquip((Player) event.getWhoClicked(), current, 6)) {
					event.setCancelled(true);
					return;
				}
			}
			if(type != null && type.equals(ItemType.LEGGINGS) && (player.getInventory().getLeggings() == null || player.getInventory().getLeggings().getType().equals(Material.AIR))){
				if(!callEquip((Player) event.getWhoClicked(), current, 7)) {
					event.setCancelled(true);
					return;
				}
			}
			if(type != null && type.equals(ItemType.BOOTS) && (player.getInventory().getBoots() == null || player.getInventory().getBoots().getType().equals(Material.AIR))){
				if(!callEquip((Player) event.getWhoClicked(), current, 8)) {
					event.setCancelled(true);
					return;
				}
			}
			}
		}
		if(type != null && ((slot == 5 && type != ItemType.HELMET) || (slot == 6 && type != ItemType.CHESTPLATE) || (slot == 7 && type != ItemType.LEGGINGS) || (slot == 8 && type != ItemType.BOOTS))){
			return;
		}
		if((slot >= 5) && (slot <= 8)) {
//			if(current.getType() == Material.AIR) {
//				Bukkit.broadcastMessage("EQUIP: " + cursor.getType().toString() + " " + slot);
				if(!callEquip((Player) event.getWhoClicked(), cursor, slot)) {
					event.setCancelled(true);
				}
//			}
			else {
//				Bukkit.broadcastMessage("DEQUIP: " + cursor.getType().toString() + " " + current.getType().toString() + " " + slot);
				if(!callDequip((Player) event.getWhoClicked(), current, slot)) {
					event.setCancelled(true);
				}
			}
		}
	}

	private boolean callEquip(Player who, ItemStack cursor, int slot) {
		PlayerEquipEvent equipEvent = new PlayerEquipEvent(who, cursor, slot);
		core.getServer().getPluginManager().callEvent(equipEvent);
		if(equipEvent.isCancelled()) {
			return false;
		}
		
		return true;
	}

	private boolean callDequip(Player who, ItemStack item, int slot) {
		PlayerDequipEvent equipEvent = new PlayerDequipEvent(who, item, slot);
		core.getServer().getPluginManager().callEvent(equipEvent);
		if(equipEvent.isCancelled()) {
			return false;
		}
		
		return true;
	}

	private ItemType getArmour(ItemStack item) {
		if(ItemType.BOOTS.isItemOfType(item)) {
			return ItemType.BOOTS;
		}
		if(ItemType.LEGGINGS.isItemOfType(item)) {
			return ItemType.LEGGINGS;
		}
		if(ItemType.CHESTPLATE.isItemOfType(item)) {
			return ItemType.CHESTPLATE;
		}
		if(ItemType.HELMET.isItemOfType(item)) {
			return ItemType.HELMET;
		}
		
		return null;
	}
	
}
