package com.decimatepvp.functions.staff.factions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.decimatepvp.utils.FactionUtils;

public class FactionDamageListener implements Listener {

	@EventHandler
	public void onDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			if(event.getCause().equals(DamageCause.FALL)){
				Player player = (Player) event.getEntity();
				if(FactionUtils.getFactionByLoc(player.getLocation()).equals(FactionUtils.getFaction(player))){
					event.setCancelled(true);
				}
			}
		}
	}
	
}
