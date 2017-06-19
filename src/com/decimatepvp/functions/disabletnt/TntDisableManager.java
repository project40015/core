package com.decimatepvp.functions.disabletnt;

import java.util.Date;

import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class TntDisableManager implements Listener {

	private final long then;
	
	@SuppressWarnings("deprecation")
	public TntDisableManager(){
		then = (new Date(2017-1900, 6-1, 16+7, 18-2, 0)).getTime();
	}
	
	@EventHandler
	public void onExplode(EntityExplodeEvent event){
		if(event.getEntity() instanceof Creeper){
			Creeper cr = (Creeper) event.getEntity();
			if(cr.isPowered()){
				return;
			}
		}
		if(then > System.currentTimeMillis()){
			event.setCancelled(true);
		}
	}
	
}
