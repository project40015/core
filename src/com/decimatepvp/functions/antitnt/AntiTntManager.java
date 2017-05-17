package com.decimatepvp.functions.antitnt;

import java.util.List;

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

import com.decimatepvp.core.Manager;
import com.decimatepvp.utils.DecimateUtils;
import com.google.common.collect.Lists;

public class AntiTntManager implements Manager, Listener {
	
	private List<TNTPrimed> primedTNT = Lists.newArrayList();
	
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
		double d = loc.distance(player.getLocation());
		if(d < 4.00D) {
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
