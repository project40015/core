package com.decimatepvp.functions.misc.crophopper;

import org.bukkit.Location;

public class Chunk {

	private int x, z;
	
	public Chunk(int x, int z){
		this.x = x;
		this.z = z;
	}
	
	public Chunk(Location location){
		this.x = ((location.getBlockX() + 16)/16);
		this.z = ((location.getBlockZ() + 16)/16);
	}
	
	public int getX(){
		return x;
	}
	
	public int getZ(){
		return z;
	}
	
	public boolean equals(Chunk chunk){
		return this.x == chunk.x && this.z == chunk.z;
	}
	
}
