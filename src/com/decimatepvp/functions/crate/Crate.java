package com.decimatepvp.functions.crate;

import java.util.List;

import org.bukkit.Location;

public abstract class Crate {

	private String name;
	private List<CrateReward> rewards;
	
	public Crate(String name, List<CrateReward> rewards){
		this.name = name;
		this.rewards = rewards;
	}
	
	public String getName(){
		return name;
	}
	
	public List<CrateReward> getRewards(){
		return rewards;
	}
	
	public abstract void open(Location location);
	
}
