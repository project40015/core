package com.decimatepvp.functions.patch.border;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.decimatepvp.core.DecimateCore;

public class WorldBorderManager implements Listener {
	
	private double px, nx;
	private double pz, nz;
	
	private final World world;

	public WorldBorderManager(World world,
			double px, double nx,
			double pz, double nz) {
		this.px = px;
		this.nx = nx;
		this.pz = pz;
		this.nz = nz;
		this.world = world;

		Bukkit.broadcastMessage("-");
		Bukkit.broadcastMessage(px + " " + pz);
		Bukkit.broadcastMessage(nx + " " + nz);
		
		getEntityManager().runTaskTimer(DecimateCore.getCore(), 0, 10l);
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if(isOutsideBorder(event.getTo())) {
			event.setCancelled(true);
		}
	}
	
	private BukkitRunnable getEntityManager() {
		return new BukkitRunnable() {
			
			@Override
			public void run() {
				for(Entity entity : world.getEntities()) {
					if((isOutsideBorder(entity.getLocation()))) {
						if(entity instanceof Player) {
//							if(entity.hasPermission("Decimate.staff.leaveborder")) {
//							}
							entity.teleport(world.getHighestBlockAt(9999, 9999).getLocation());
							
//							else {
//								entity.sendMessage("Outside the border!");
//							}
						}
						else {
							entity.remove();
						}
					}
					else if((entity instanceof FallingBlock)) {
						if(isNearBorder(entity.getLocation())) {
							entity.remove();
						}
					}
				}
			}
		};
	}
	
	private boolean isNearBorder(Location location) {
		double x = location.getBlockX();
		double z = location.getBlockZ();
		if(x < 0) {
			x--;
		}
		if(z < 0) {
			z--;
		}
		
		if((z >= pz - 2) || (z <= nz + 2)) {
			return true;
		}
		if((x >= px - 2) || (x <= nx + 2)) {
			return true;
		}
		
		return false;
	}

	private boolean isOutsideBorder(Location location) {
		double x = location.getX();
		double z = location.getZ();
		if((z > pz) || (z < nz)) {
			return true;
		}
		if((x > px) || (x < nx)) {
			return true;
		}
		
		return false;
	}

}
