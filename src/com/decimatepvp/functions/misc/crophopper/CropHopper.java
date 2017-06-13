package com.decimatepvp.functions.misc.crophopper;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class CropHopper {

	private boolean crop;
	private Location location;
	
	public CropHopper(Location location, boolean crop){
		this.location = location;
		this.crop = crop;
	}
	
	public CropHopper(String saved){
		String[] args = saved.split(" ");
		location = new Location(Bukkit.getWorld(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
		this.crop = Boolean.valueOf(args[4]);
	}
	
	public Location getLocation(){
		return location;
	}
	
	public boolean isCrop(){
		return crop;
	}
	
	@Override
	public String toString(){
		return location.getWorld().getName() + " " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + " " + (crop ? "true" : "false");
	}
	
}
