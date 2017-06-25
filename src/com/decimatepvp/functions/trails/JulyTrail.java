package com.decimatepvp.functions.trails;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;

import com.decimatepvp.utils.ParticleEffect.OrdinaryColor;
import com.decimatepvp.utils.ParticleUtils;

public class JulyTrail extends Trail {

	public JulyTrail() {
		super("Freedom Trail", "Unlocked in July 2017 crate", "decimatepvp.trail.fourthofjuly", Material.FIREWORK);
	}
	
	private Color randomColor(){
		double random = Math.random() * 3;
		if(random < 1){
			return Color.RED;
		}else if(random < 2){
			return Color.BLUE;
		}else{
			return Color.WHITE;
		}
	}

	@Override
	public void display(Location location) {
		ParticleUtils.summonRedstoneParticle(new OrdinaryColor(randomColor()), location, 30);
	}

}
