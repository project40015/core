package com.decimatepvp.functions.misc.enemylogout;

import org.bukkit.Location;

public class ULTWrap {

	public String u;
	public Location l;
	private int t;
	
	public ULTWrap(String u, Location l, int t){
		this.u = u;
		this.l = l;
		this.t = t;
	}
	
	public boolean t(){
		t--;
		if(t < 0){
			return true;
		}
		return false;
	}
	
}
