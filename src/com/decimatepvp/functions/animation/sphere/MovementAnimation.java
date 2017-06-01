package com.decimatepvp.functions.animation.sphere;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.decimatepvp.functions.animation.Animation;
import com.decimatepvp.utils.ParticleEffect.OrdinaryColor;
import com.decimatepvp.utils.ParticleUtils;

public class MovementAnimation extends Animation {
	
	private Location start, current;
	private Vector direction;
	private double range;
	private OrdinaryColor particlecolor;
	
	private boolean attacks;
	private double damage, attackRange;
	private Entity damager;

	public MovementAnimation(double[][] positions, OrdinaryColor particlecolor, Location start,
			Vector direction, double range) {
		this(positions, particlecolor, start, direction, range, 0.0D, null, 0.0D);
	}

	public MovementAnimation(double[][] positions, OrdinaryColor particlecolor, Location start,
			Vector direction, double range,
			double damage, Entity damager, double attackRange) {
		super(positions);
		this.start = start.clone();
		this.current = start.clone();
		this.range = range;
		this.particlecolor = particlecolor;
		
		if(damage > 0) {
			this.attacks = true;
			this.damager = damager;
			this.attackRange = attackRange;
		}
	}

	@Override
	public void run() {
		if(start.distance(current) <= range) {
			current.add(direction);
			for(double[] pos : positions) {
				ParticleUtils.summonRedstoneParticle(particlecolor, current.clone().add(pos[0], pos[1], pos[2]), 64);
			}
			if(attacks) {
				for(Entity entity : current.getWorld().getNearbyEntities(current, attackRange, attackRange, attackRange)) {
					if((entity instanceof Damageable) && (entity != damager)) {
						((Damageable) entity).damage(damage, damager);
					}
				}
			}
		}
		else {
			super.stop();
		}
	}

}
