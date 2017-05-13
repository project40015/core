package com.decimatepvp.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.decimatepvp.core.DecimateCore;

public class PlayerQuitListener implements Listener {
	
	private DecimateCore core;
	
	public PlayerQuitListener() {
		core = DecimateCore.getCore();
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if(core.freeze.frozen.contains(event.getPlayer())) {
			core.freeze.frozen.remove(event.getPlayer());
		}
	}

}
