package com.decimatepvp.functions.misc.enemylogout;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.decimatepvp.utils.FactionUtils;

public class EnemyTerritoryLogoutManager implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		if(!FactionUtils.isAreaSafe(event.getPlayer())){
			event.getPlayer().teleport(new Location(Bukkit.getWorlds().get(0), 0, 80, 0));
		}
	}
	
}
