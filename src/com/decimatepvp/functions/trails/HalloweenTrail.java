package com.decimatepvp.functions.trails;

import org.bukkit.Location;
import org.bukkit.Material;

import com.decimatepvp.utils.ParticleEffect;

public class HalloweenTrail extends Trail {

	public HalloweenTrail() {
		super("Coming Soon", "???", "decimatepvp.trail.halloween", Material.COAL);
	}
	
//	private Color randomColor(){
//		double random = Math.random() * 3;
//		
//		// 2/3 black, 1/3 purple
//		if(random < 1){
//			return Color.PURPLE;
//		}else{
//			return Color.BLACK;
//		}
//	}

	@Override
	public void display(Location location) {
//		ParticleUtils.summonRedstoneParticle(new OrdinaryColor(randomColor()), location, 30);
		ParticleEffect.SPELL_WITCH.display(0, 0, 0, 0, 1, location, 30);
	}

}
