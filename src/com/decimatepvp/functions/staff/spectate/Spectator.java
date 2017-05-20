package com.decimatepvp.functions.staff.spectate;

import org.bukkit.entity.Player;

import com.decimatepvp.utils.PlayerInventory;

public class Spectator {

	private Player player;
	private PlayerInventory inventory;
	
	public Spectator(Player player){
		this.player = player;
		this.inventory = new PlayerInventory(player);
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public PlayerInventory getInventory(){
		return inventory;
	}
	
}
