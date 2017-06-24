package com.decimatepvp.functions.misc.enemylogout;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.utils.FactionUtils;

public class EnemyTerritoryLogoutManager implements Listener {

	private List<ULTWrap> logoutLocation = new ArrayList<>();
	
	public EnemyTerritoryLogoutManager(){
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(DecimateCore.getCore(), new Runnable(){

			@Override
			public void run() {
				for(int i = 0; i < logoutLocation.size(); i++){
					if(logoutLocation.get(i).t()){
						logoutLocation.remove(i--);
					}
				}
			}
			
		}, 20, 20);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		if(!FactionUtils.isAreaSafe(event.getPlayer())){
			logoutLocation.add(new ULTWrap(event.getPlayer().getUniqueId().toString(), event.getPlayer().getLocation(), 60));
			event.getPlayer().teleport(new Location(Bukkit.getWorlds().get(0), 0, 80, 0));
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		for(ULTWrap wrap : logoutLocation){
			if(wrap.u.equals(event.getPlayer().getUniqueId().toString())){
				event.getPlayer().teleport(wrap.l);
			}
		}
	}
	
}
