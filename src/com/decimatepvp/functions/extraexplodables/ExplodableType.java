package com.decimatepvp.functions.extraexplodables;

public class ExplodableType {

	private int lives, material;
	
	public ExplodableType(int material, int lives){
		this.lives = lives;
		this.material = material;
	}
	
	public int getLives(){
		return lives;
	}
	
	public int getMaterial(){
		return material;
	}
	
}
