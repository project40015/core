package com.decimatepvp.functions.trails;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;

import com.decimatepvp.utils.ParticleEffect.OrdinaryColor;
import com.decimatepvp.utils.ParticleUtils;

public class WarVictorTrail extends Trail {

	public WarVictorTrail() {
		super("Coming Soon", "???", "decimatepvp.trail.warvictor", Material.GOLDEN_CARROT);
	}
	
	private Color randomColor(){
		double random = Math.random() * 3;
		if(random < 1){
			return Color.RED;
		}else if(random < 2){
			return Color.YELLOW;
		}else{
			return Color.ORANGE;
		}
	}

	@Override
	public void display(Location location) {
		ParticleUtils.summonRedstoneParticle(new OrdinaryColor(randomColor()), location, 30);
	}

}
