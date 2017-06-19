package com.decimatepvp.functions.extraexplodables;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import com.decimatepvp.core.Manager;

public class ExplodableManager implements Manager {
	
	@SuppressWarnings("deprecation")
	private ExplodableType[] explodableTypes = new ExplodableType[] {new ExplodableType(Material.OBSIDIAN.getId(), 5)};
	private List<Explodable> explodables = new ArrayList<>();
	
	public boolean isExplodable(int material){
		for(ExplodableType type : explodableTypes){
			if(type.getMaterial() == material){
				return true;
			}
		}
		return false;
	}
	
	public ExplodableType getExplodableType(int material){
		for(ExplodableType type : explodableTypes){
			if(type.getMaterial() == material){
				return type;
			}
		}
		return null;
	}
	
	public Explodable getExplodable(String world, int x, int y, int z, int material){
		for(Explodable explodable : explodables){
			if(explodable.getWorld().equals(world) && explodable.getX() == x && explodable.getY() == y && explodable.getZ() == z){
				return explodable;
			}
		}
		Explodable ex = new Explodable(world, x, y, z, material);
		this.explodables.add(ex);
		return ex;
	}
	
	public ExplodableType[] getExplodableTypes(){
		return explodableTypes;
	}

	@Override
	public void disable() {
	}
	
}
