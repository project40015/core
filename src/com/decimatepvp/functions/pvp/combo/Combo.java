package com.decimatepvp.functions.pvp.combo;

public class Combo {

	private String attacker, attacked;
	private int combo, time;
	
	public Combo(String attacker, String attacked){
		this.attacker = attacker;
		this.attacked = attacked;
		this.time = 20*3;
		this.combo = 1;
	}
	
	public double hit(double damage){
		this.combo++;
		this.time = 20*3;
		return damage*(1 + combo*0.05);
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
	
}
