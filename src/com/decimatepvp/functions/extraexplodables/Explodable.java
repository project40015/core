package com.decimatepvp.functions.extraexplodables;

import org.bukkit.Bukkit;

import com.decimatepvp.core.DecimateCore;

public class Explodable {

	private int x, y, z, material, life = 10;
	private String world;
	
	public Explodable(String world, int x, int y, int z, int material){
		this.x  = x;
		this.y = y;
		this.z = z;
		this.world = world;
		this.material = material;
//		this.life = DecimateCore.getCore().getExplodableManager().getExplodableType(material).getLives();
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getZ(){
		return z;
	}
	
	public String getWorld(){
		return world;
	}
	
	public int getMaterial(){
		return material;
	}
	
	public int getLife(){
		return life;
	}
	
	public boolean hit(){
		life--;
		if(life <= 0){
			Bukkit.getWorld(world).getBlockAt(x, y, z).breakNaturally();
			DecimateCore.getCore().getExplodableManager().deleteExplodable(this);
			return true;
		}
		return false;
	}
	
}
