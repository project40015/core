package com.decimatepvp.utils;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;

public class DecimateUtils {
	
	private static final Random random = new Random();
	
	public static String longToTime(long time){
		int seconds = (int) ((time / 1000) % 60);
		int minutes = (int) ((time / (1000*60)) % 60);
		int hours   = (int) ((time / (1000*60*60)) % 24);
		int days = (int) ((time / (1000*60*60*24)));
	
		return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
	}

	public static Location generateRandomLocation(int i, int j, int k) {
		int x = random.nextInt(i*2) - i;
		int y = random.nextInt(j*2) - j;
		int z = random.nextInt(k*2) - k;
		
		return new Location(null, x, y, z);
	}
	
	public static String color(String textToTranslate) {
		return ChatColor.translateAlternateColorCodes('&', textToTranslate);
	}

	public static void damageNearbyEntities(Location loc, double d, double damage) {
		damageNearbyEntities(loc, d, damage, null);
	}

	public static void damageNearbyEntities(Location loc, double d, double damage, Entity attacker) {
		for(Entity entity : loc.getWorld().getNearbyEntities(loc, d, d, d)) {
			if(entity instanceof Damageable) {
				if(attacker != null) {
					((Damageable) entity).damage(damage, attacker);
				}
				else {
					((Damageable) entity).damage(damage);
				}
			}
		}
	}

}
