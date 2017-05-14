package com.decimatepvp.core.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import com.decimatepvp.utils.FactionUtils;

public class AnvilDamageListener implements Listener {
	
	@EventHandler
	public void onAnvilDamage(EntityChangeBlockEvent event) {
		if((event.getBlock().getType() == Material.ANVIL) &&
				(FactionUtils.getWarzone() == FactionUtils.getFactionByLoc(event.getBlock().getLocation()))) {
			event.setCancelled(true);
		}
	}

}
