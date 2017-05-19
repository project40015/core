package com.decimatepvp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDequipEvent extends PlayerEvent implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancelled = false;

	private ItemStack equipment;
	private int inventorySlot;

	public PlayerDequipEvent(Player who, ItemStack equipment, int slot) {
		super(who);
		
		this.equipment = equipment;
		this.inventorySlot = slot;
	}
	
	///////////////////////////////

	public ItemStack getEquipment() {
		return equipment;
	}

	public void setEquipment(ItemStack equipment) {
		this.equipment = equipment;
	}

	public int getInventorySlot() {
		return inventorySlot;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean paramBoolean) {
		this.cancelled = paramBoolean;
	}

}
