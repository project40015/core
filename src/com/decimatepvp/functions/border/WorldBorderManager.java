package com.decimatepvp.functions.border;

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
	
	private final double px = 12001D, nx = -12001D;
	private final double pz = 12001D, nz = -12001D;
	
	private final World world;
	
	public WorldBorderManager() {
		world = Bukkit.getServer().getWorlds().get(0);
		
		getEntityManager().runTaskTimer(DecimateCore.getCore(), 0l, 10l);
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
							if(!entity.hasPermission("Decimate.staff.leaveborder")) {
								((Player) entity).damage(999999999);
							}
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
		if((z >= 11999) || (z <= -11999)) {
			return true;
		}
		if((x >= 11999) || (x <= -11999)) {
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
