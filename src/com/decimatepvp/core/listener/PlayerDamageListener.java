package com.decimatepvp.core.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.decimatepvp.core.DecimateCore;

public class PlayerDamageListener implements Listener {
	
	private DecimateCore core;
	
	public PlayerDamageListener() {
		core = DecimateCore.getCore();
	}
	
	@EventHandler
	public void onPlayerTakeDamage(EntityDamageEvent event) {
		if(event.getEntityType() == EntityType.PLAYER) {
			Player player = (Player) event.getEntity();
			freeze(player, event);
		}
	}

	private void freeze(Player player, EntityDamageEvent event) {
		if(core.freeze.frozen.contains(player)) {
			event.setCancelled(true);
		}
	}

}
