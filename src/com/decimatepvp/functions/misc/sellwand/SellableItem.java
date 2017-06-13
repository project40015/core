package com.decimatepvp.functions.misc.sellwand;

import org.bukkit.Material;

public class SellableItem {

	private Material material;
	double cost;
	
	public SellableItem(Material material, double cost){
		this.material = material;
		this.cost = cost;
	}
	
	public Material getMaterial(){
		return material;
	}
	
	public double getCost(){
		return this.cost;
	}
	
}
