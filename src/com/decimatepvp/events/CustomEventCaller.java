package com.decimatepvp.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.enchants.CustomEnchant.ItemType;

public class CustomEventCaller implements Listener {
	
	private final DecimateCore core = DecimateCore.getCore();

	@EventHandler
	public void onPlayerEquip(PlayerInteractEvent event) {
		if((event.getAction() == Action.RIGHT_CLICK_AIR) ||
				(event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			Player player = event.getPlayer();
			ItemStack hand = event.getItem();
			if((hand != null) && (hand.getType() != Material.AIR)) {
				ItemType type = getArmour(hand);
				if(type != null) {
					ItemStack armour = null;
					if(type == ItemType.BOOTS) {
						armour = player.getEquipment().getBoots();
					}
					else if(type == ItemType.LEGGINGS) {
						armour = player.getEquipment().getLeggings();
						}
					else if(type == ItemType.CHESTPLATE) {
						armour = player.getEquipment().getChestplate();
						}
					else if(type == ItemType.HELMET) {
						armour = player.getEquipment().getHelmet();
					}

					if((armour == null) || (armour.getType() == Material.AIR)) {
						callEquip(player, hand, player.getInventory().getHeldItemSlot());
//						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerEquip(InventoryClickEvent event) {
		if(!(event.getWhoClicked() instanceof Player)){
			return;
		}
		ItemStack current = event.getCurrentItem();
		ItemStack cursor = event.getCursor();
		int slot = event.getRawSlot();
		boolean num = event.getClick().equals(ClickType.NUMBER_KEY);
		if(event.getClick().equals(ClickType.SHIFT_LEFT) || event.getClick().equals(ClickType.SHIFT_RIGHT) || event.getClick().equals(num)){
			Player player = (Player) event.getWhoClicked();
			ItemType type = getArmour(current);
			if(type.equals(ItemType.HELMET) && (num || player.getInventory().getHelmet() == null || player.getInventory().getHelmet().getType().equals(Material.AIR))){
				if(!callEquip((Player) event.getWhoClicked(), current, 5)) {
					event.setCancelled(true);
					return;
				}
			}
			if(type.equals(ItemType.CHESTPLATE) && (num || player.getInventory().getChestplate() == null || player.getInventory().getChestplate().getType().equals(Material.AIR))){
				if(!callEquip((Player) event.getWhoClicked(), current, 6)) {
					event.setCancelled(true);
					return;
				}
			}
			if(type.equals(ItemType.LEGGINGS) && (num || player.getInventory().getLeggings() == null || player.getInventory().getLeggings().getType().equals(Material.AIR))){
				if(!callEquip((Player) event.getWhoClicked(), current, 7)) {
					event.setCancelled(true);
					return;
				}
			}
			if(type.equals(ItemType.BOOTS) && (num || player.getInventory().getBoots() == null || player.getInventory().getBoots().getType().equals(Material.AIR))){
				if(!callEquip((Player) event.getWhoClicked(), current, 8)) {
					event.setCancelled(true);
					return;
				}
			}
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
