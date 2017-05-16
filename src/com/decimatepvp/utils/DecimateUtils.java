package com.decimatepvp.utils;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class DecimateUtils {
	
	private static final Random random = new Random();

	public static Location generateRandomLocation(int i, int j, int k) {
		int x = random.nextInt(i*2) - i;
		int y = random.nextInt(j*2) - j;
		int z = random.nextInt(k*2) - k;
		
		return new Location(null, x, y, z);
	}
	
	public static String color(String textToTranslate) {
		return ChatColor.translateAlternateColorCodes('&', textToTranslate);
	}

}
