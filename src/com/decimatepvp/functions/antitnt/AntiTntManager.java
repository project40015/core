package com.decimatepvp.functions.antitnt;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.decimatepvp.utils.DecimateUtils;
import com.google.common.collect.Lists;

public class AntiTntManager implements Manager, Listener {
	
	private final Random random = new Random();
	
	private List<TNTPrimed> primedTNT = Lists.newArrayList();
	
	public AntiTntManager() {
		getAntiTntRunnable().runTaskTimer(DecimateCore.getCore(), 0L, 2400l);
	}
	
	private BukkitRunnable getAntiTntRunnable() {
		return new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Bukkit.getServer().getOnlinePlayers().size() == 0) {
					return;
				}
				int i = random.nextInt(Bukkit.getServer().getOnlinePlayers().size());
				int c = 0;
				for(Player player : Bukkit.getServer().getOnlinePlayers()) {
					if(i == c) {
						DecimateCore.getCore().getAntiTntManager().antiTntPlayer(player);
						break;
					}
					c++;
				}
			}
		};
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onTntExplode(EntityExplodeEvent event) {
		if(event.getEntityType() == EntityType.PRIMED_TNT) {
			Entity entity = event.getEntity();
			if(primedTNT.contains(entity)) {
				entity.remove();
				event.setCancelled(true);
				event.blockList().clear();
				primedTNT.remove(entity);
				entity.getWorld().playEffect(entity.getLocation(), Effect.EXPLOSION_HUGE, 1);
			}
		}
	}
	
	public void antiTntPlayer(Player player) {
		Location loc = DecimateUtils.generateRandomLocation(64, 64, 64);
		loc.setWorld(player.getWorld());
		double d = loc.distance(player.getLocation());
		if(d < 16.00D) {
			if((int) d % 2 == 0) {
				loc.setX(loc.getX()*2);
			}
			else {
				loc.setZ(loc.getZ()*2);
			}
		}
		Location area = player.getLocation().add(loc);
		
//		sendFakeExplosion(player, area);
		sendFakeEntityPrimedTnt(player, area);
	}

	private void sendFakeEntityPrimedTnt(Player player, Location area) {
		TNTPrimed primed = (TNTPrimed) area.getWorld().spawnEntity(area, EntityType.PRIMED_TNT);
		primed.setYield(0.0f);
		primed.setCustomName("EntityTnt");
		primed.setFuseTicks(30);
		primedTNT.add(primed);
	}

//	private static void sendFakeExplosion(Player player, Location location) {
//		double d0 = location.getX();
//		double d1 = location.getY();
//		double d2 = location.getZ();
//		PacketPlayOutExplosion explosion = 
//        		new PacketPlayOutExplosion(d0, d1, d2, 5.0f, Lists.newArrayList(), new Vec3D(0, 0, 0));
//		
//		PlayerUtils.sendPacket(player, explosion);
//	}

	@Override
	public void disable() {
		primedTNT.forEach(primed -> primed.remove());
	}
}
