package com.decimatepvp.utils;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.utils.ParticleEffect.OrdinaryColor;

public class ParticleUtils {

	private static final Random random = new Random();

	/**
	 * 
	 * @param color
	 *            Color of beam
	 * @param entity
	 *            Entity shooting beam
	 * @param range
	 *            Range the beam goes
	 * @param damageable
	 *            if the beam damages
	 * @param damage
	 *            damages beam deals if damageable
	 * @param charge
	 *            time until beam launches
	 * @param speed
	 *            speed at which beam moves with 1 being the highest
	 */
	public static void sendBeamFromEntity(Vector vector, Color color, LivingEntity entity, int range,
			boolean damageable, int damage, long charge, long speed) {

		Location start = entity.getEyeLocation();
		Location loc = entity.getEyeLocation();
		loc.add(vector);

		OrdinaryColor particlecolor = new OrdinaryColor(color);

		if(speed != 0) {
			new BukkitRunnable() {

				@Override
				public void run() {
					if(loc.distance(start) <= range) {
						loc.add(vector);
						if(damageable) {
							for(Entity ent : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
								if((ent instanceof Damageable) && (ent != entity)) {
									((Damageable) ent).damage(damage, entity);
								}
							}
						}

						for(int i = 0; i < 10; i++) {
							double x = random.nextDouble() / 3;
							double y = random.nextDouble() / 3;
							double z = random.nextDouble() / 3;

							summonRedstoneParticle(particlecolor, loc.clone().add(x, y, z), 64);
						}
					}
					else {
						cancel();
					}
				}
			}.runTaskTimer(DecimateCore.getCore(), charge, speed);
		}
		else {
			new BukkitRunnable() {

				@Override
				public void run() {
					while (loc.distance(start) < range) {
						loc.add(vector);
						if(damageable) {
							for(Entity ent : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
								if((ent instanceof Damageable) && (ent != entity)) {
									((Damageable) ent).damage(damage, entity);
								}
							}
						}

						for(int i = 0; i < 10; i++) {
							double x = random.nextDouble() / 3;
							double y = random.nextDouble() / 3;
							double z = random.nextDouble() / 3;

							ParticleEffect.REDSTONE.display(particlecolor, loc.clone().add(x, y, z), 64);
						}
					}
				}
			}.runTaskTimer(DecimateCore.getCore(), charge, speed);
		}
	}

	public static void summonRedstoneParticle(OrdinaryColor particlecolor, Location loc, int range) {
		ParticleEffect.REDSTONE.display(particlecolor, loc, range);
	}

	public static double[][] createSphere(double size, int rings, int particlesPerRing) {
		double[][] positions = new double[rings * ((particlesPerRing * 4))][3];

		int posN = 0;
		for(double i = 0; i <= Math.PI; i += Math.PI / rings) {
			double radius = Math.sin(i) * size;
			double y = Math.cos(i) * size;

			for(double a = 0; a < Math.PI * 2; a += Math.PI / particlesPerRing) {
				double x = Math.cos(a) * radius;
				double z = Math.sin(a) * radius;
				positions[posN] = new double[] { x, y, z };
				posN++;
			}
		}

		return positions;
	}

	public static double[][][] createLayeredDisk(int rings, int particlesPerRing, double startingRadius,
			double growthRate, boolean rateAdd) {

		double[][][] layeredDisk = new double[rings][(particlesPerRing * 2) + 1][2];
		double radius = startingRadius;

		int layer = 0;
		for(int ring = 0; ring < rings; ring++) {
			int i = 0;
			for(double a = 0; a < Math.PI * 2; a += Math.PI / particlesPerRing) {
				double x = Math.cos(a) * radius;
				double z = Math.sin(a) * radius;
				layeredDisk[layer][i] = new double[] { x, z };
				i++;
			}
			layer++;

			if(rateAdd) {
				radius += growthRate;
			}
			else {
				radius *= growthRate;
			}
		}

		return layeredDisk;
	}

}
