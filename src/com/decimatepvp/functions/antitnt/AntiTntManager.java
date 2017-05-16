package com.decimatepvp.functions.antitnt;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
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
import com.decimatepvp.utils.PlayerUtils;
import com.google.common.collect.Lists;

import net.minecraft.server.v1_8_R3.EntityTNTPrimed;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;

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
//		Location area = player.getLocation().add(generateRandomLocation(64, 64, 64));
		Location area = player.getLocation().add(player.getLocation().getDirection().multiply(8));
//		sendFakeExplosion(player, area);
		sendFakeEntityPrimedTnt(player, area);
	}

	private void sendFakeEntityPrimedTnt(Player player, Location area) {
//		TNTPrimed primed = (TNTPrimed) area.getWorld().spawnEntity(area, EntityType.PRIMED_TNT);
//		primed.setYield(0.0f);
//		primed.setCustomName("EntityTnt");
//		primedTNT.add(primed);
		
		EntityTNTPrimed tnt = new EntityTNTPrimed(area, ((CraftWorld) area.getWorld()).getHandle());
		PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(tnt, tnt.getId());
		new BukkitRunnable() {
			
			@Override
			public void run() {
				PlayerUtils.sendPacket(player, packet);
			}
		}.runTaskTimer(DecimateCore.getCore(), 0, 1l);
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
