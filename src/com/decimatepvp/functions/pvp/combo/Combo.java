package com.decimatepvp.functions.pvp.combo;


public class Combo {

	private String attacker, attacked;
	private int combo, time;
//	private ArmorStand stand;
	
	public Combo(String attacker, String attacked){
		this.attacker = attacker;
		this.attacked = attacked;
		this.time = 20*3;
		this.combo = 1;
	}
	
	public double hit(double damage, boolean strength){
		this.combo++;
		this.time = 20*3;
		
		return damage*(strength ? (1.1 + combo*0.025) : (1.2 + combo*0.05));
	}
	
	public boolean time(int n){
		this.time -= n;
		if(this.time <= 0){
			return true;
		}
		return false;
	}
	
	public String getAttacker(){
		return attacker;
	}
	
	public String getAttacked(){
		return attacked;
	}
	
	public int getCombo(){
		return combo;
	}
	
	public int getTime(){
		return time;
	}
	
//	public void setStand(ArmorStand stand){
//		this.stand = stand;
//	}
	
//	public void removeCombo(){
//		stand.remove();
//	}
	
//	public ArmorStand getStand(){
//		return stand;
//	}
	
}
