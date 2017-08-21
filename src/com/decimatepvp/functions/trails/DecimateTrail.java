package com.decimatepvp.functions.trails;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;

import com.decimatepvp.utils.ParticleEffect.OrdinaryColor;
import com.decimatepvp.utils.ParticleUtils;

public class DecimateTrail extends Trail {

	public DecimateTrail() {
		super("Decimate Rainbow Trail", "Unlocked in decimate crate", "decimatepvp.trail.decimatetrail", Material.GOLDEN_APPLE);
	}
	
	private Color randomColor(){
		return Color.fromBGR((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
	}

	@Override
	public void display(Location location) {
		ParticleUtils.summonRedstoneParticle(new OrdinaryColor(randomColor()), location, 30);
	}

}
