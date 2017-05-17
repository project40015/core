package com.decimatepvp.functions.freeze;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;


public class FreezeManager implements Manager, Listener {
		
	private DecimateCore core;
	private List<String> frozen = new ArrayList<>();
	
	public FreezeManager(DecimateCore core){
		this.core = core;
	}
	
	public void freeze(Player player){
		if(!this.frozen.contains(player.getUniqueId().toString())){
			this.frozen.add(player.getUniqueId().toString());
		}
	}
	
	public void thaw(Player player){
		if(this.frozen.contains(player.getUniqueId().toString())){
			this.frozen.remove(player.getUniqueId().toString());
		}
	}
	
	public boolean isFrozen(Player player){
		return this.frozen.contains(player.getUniqueId().toString());
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		freeze(event);
	}

	private void freeze(PlayerMoveEvent event) {
		if(!core.getFreezeManager().isFrozen(event.getPlayer())){
			return;
		}
		Location from = event.getFrom();
		Location to = event.getTo();
		if(from.getX() != to.getX() || (from.getY() < to.getY()) || from.getZ() != to.getZ()) {
			from.setYaw(to.getYaw());
			from.setPitch(to.getPitch());
			event.setTo(from);
		}
	}
	
	@EventHandler
	public void onPlayerTakeDamage(EntityDamageEvent event) {
		if(event.getEntityType() == EntityType.PLAYER) {
			Player player = (Player) event.getEntity();
			freeze(player, event);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if(core.getFreezeManager().isFrozen(event.getPlayer())) {
			core.getFreezeManager().thaw(event.getPlayer());
		}
	}

	private void freeze(Player player, EntityDamageEvent event) {
		if(core.getFreezeManager().isFrozen(player)) {
			event.setCancelled(true);
		}
	}
	
	@Override
	public void disable() {
		this.frozen.clear();
	}
	
}
