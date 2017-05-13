package com.decimatepvp.core.listener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.decimatepvp.core.DecimateCore;

public class PlayerMoveListener implements Listener {
	
	private DecimateCore core;
	
	public PlayerMoveListener() {
		core = DecimateCore.getCore();
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		freeze(event);
	}

	private void freeze(PlayerMoveEvent event) {
		Location from = event.getFrom();
		Location to = event.getTo();
		if(from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
			from.setYaw(to.getYaw());
			from.setPitch(to.getPitch());
			if(core.freeze.frozen.contains(event.getPlayer())) {
				event.setTo(from);
			}
		}
	}
	
}
