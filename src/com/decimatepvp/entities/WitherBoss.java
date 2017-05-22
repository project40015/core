package com.decimatepvp.entities;

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityRabbit;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.EnumMonsterType;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class WitherBoss extends EntityWither {

	public final BlockPosition SPAWN = new BlockPosition(0, 0, 0);
	
	public final int MIN_DISTANCE_FROM_SPAWN = 64, MAX_DISTANCE_FROM_SPAWN = 128;
	
	private Map<String, Double> damageDealt = Maps.newHashMap();
	
	public WitherBoss(World world) {
		super(((CraftWorld) world).getHandle());
		
		resetAI();
		setGoals();
	}
	
	private void a(int i, EntityLiving entityliving) {
		a(i, locX, locY + entityliving.getHeadHeight() * 0.5D, locZ, (i == 0) && (random.nextFloat() < 0.001F));
	}
  
	private void a(int i, double d0, double d1, double d2, boolean flag) {
		world.a(null, 1014, new BlockPosition(this), 0);
		double d3 = t(i);
		double d4 = u(i);
		double d5 = v(i);
		double d6 = d0 - d3;
		double d7 = d1 - d4;
		double d8 = d2 - d5;
		
		Entity entity = new EntityRabbit(world);
		entity.locX = d6; entity.locZ = d7; entity.locZ = d8;
		entity.getBukkitEntity().setVelocity(this.getBukkitEntity().getLocation().getDirection());
		entity.fireTicks = 100;
		
//		int rnd = random.nextInt(4);
//		if(rnd == 1) {
//			EntityWitherSkull entitywitherskull = new EntityWitherSkull(world, this, d6, d7, d8);
//		
//			if(flag) {
//				entitywitherskull.setCharged(true);
//			}
//			
//			entity = entitywitherskull;
//		}
//		else if(rnd == 2) {
//			entity = new EntityFallingBlock(world, d0, d1, d2, getRandomBlock());
//		}
//		else {
//			entity = new EntityTNTPrimed(new Location(world.getWorld(), d6, d7, d8), world);
//		}

		entity.setCustomName("WITHER_BOSS_SPAWNED");
		locY = d4;
		locX = d3;
		locZ = d5;
		world.addEntity(entity);
	}
	 
	private IBlockData getRandomBlock() {
		
		switch(this.random.nextInt(4)) {
		
		case 1 : return Blocks.SOUL_SAND.getBlockData();
		case 2 : return Blocks.OBSIDIAN.getBlockData();
		case 3 : return Blocks.BEDROCK.getBlockData();
		case 4 : return Blocks.BOOKSHELF.getBlockData();
		
		}
		
		return Blocks.SOUL_SAND.getBlockData();
	}

	public void a(EntityLiving entityliving, float f) {
		a(0, entityliving);
	}
	
	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {
		if(damagesource.getEntity() != null) {
			if(damageDealt.containsKey(damagesource.getEntity().getName())) {
				damageDealt.replace(damagesource.getEntity().getName(), damageDealt.get(damagesource.getEntity().getName()) + f);
			}
			else {
				damageDealt.put(damagesource.getEntity().getName(), (double) f);
			}
		}
		return super.damageEntity(damagesource, f);
	}
	
	@Override
	protected void dropDeathLoot(boolean flag, int i) {
		/*
		 * Items to drop on death
		 */
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		
		this.setCustomName(ChatColor.DARK_RED + "Wither Boss");
		this.setCustomNameVisible(true);
		
	    getAttributeInstance(GenericAttributes.maxHealth).setValue(3000.0D);
	    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.75D);
	    getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(40.0D);
	}

	public boolean spawn(Location spawn) {
		this.locX = spawn.getX();
		this.locY = spawn.getY();
		this.locZ = spawn.getZ();
		this.pitch = spawn.getPitch();
		this.yaw = spawn.getYaw();
		return world.addEntity(this, SpawnReason.CUSTOM);
	}
	
	////////////////////////////////

	private double t(int i) {
		if (i <= 0) {
			return locX;
		}
		float f = (aI + 180 * (i - 1)) / 180.0F * 3.1415927F;
		float f1 = MathHelper.cos(f);
		 
		return locX + f1 * 1.3D;
	}
	
	private double u(int i) {
		return i <= 0 ? locY + 3.0D : locY + 2.2D;
	}
	
	private double v(int i) {
		if (i <= 0) {
			return locZ;
		}
		float f = (aI + 180 * (i - 1)) / 180.0F * 3.1415927F;
		float f1 = MathHelper.sin(f);
		   
		return locZ + f1 * 1.3D;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static final Predicate<Entity> bq = new Predicate() {
		public boolean a(Entity entity) {
			return ((entity instanceof EntityLiving)) && (((EntityLiving) entity).getMonsterType() == EnumMonsterType.UNDEAD);
		}
		
		public boolean apply(Object object) {
			return a((Entity)object);
		}
	};

	private void setGoals() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
	    this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1.0D, true));
//		this.goalSelector.a(2, new PathfinderGoalLinger(this, 16));
//		this.goalSelector.a(2, new PathfinderGoalStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalRandomLookaround(this));
	    this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true, new Class[0]));
	    this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityInsentient.class, 0, false, false, bq));
		this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, true));
	}
	
	private void resetAI() {
		try {
			Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
			bField.setAccessible(true);
			Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);
			bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
