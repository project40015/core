package com.decimatepvp.entities;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.util.Vector;

import com.decimatepvp.utils.DecimateUtils;
import com.decimatepvp.utils.ParticleUtils;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityAnimal;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityMonster;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.EntityWitherSkull;
import net.minecraft.server.v1_8_R3.EnumDifficulty;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.IEntitySelector;
import net.minecraft.server.v1_8_R3.Material;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldEvent;
import net.minecraft.server.v1_8_R3.PathfinderGoalArrowAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.WorldServer;

public class WitherBoss extends EntityWither {

	@SuppressWarnings("unused")
		private float[] a = new float[2];
	@SuppressWarnings("unused")
		private float[] b = new float[2];
	@SuppressWarnings("unused")
		private float[] c = new float[2];
	@SuppressWarnings("unused")
		private float[] bm = new float[2];
	
	private int[] bn = new int[2];
	private int[] bo = new int[2];
	private int bp;
  
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static final Predicate<Entity> bq = new Predicate() {
		public boolean a(Entity entity) {
			return ((entity instanceof EntityMonster) && (!(entity instanceof WitherBoss)));
		}
    
		public boolean apply(Object object) {
			return a((Entity) object);
		}
	};
	
	public WitherBoss(Location location) {
		super(((CraftWorld) location.getWorld()).getHandle());
		
		setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		
		resetAI();
		setGoals();
	}
	
//	public void m() {
//		this.motY *= 0.6000000238418579D;
//	    if ((!this.world.isClientSide) && (s(0) > 0)) {
//	    	Entity entity = this.world.a(s(0));
//	    	if (entity != null) {
//	    		if ((this.locY < entity.locY) || ((!cm()) && (this.locY < entity.locY + 5.0D))) {
//	    			if (this.motY < 0.0D) {
//	    				this.motY = 0.0D;
//	    			}
//	    			this.motY += (0.5D - this.motY) * 0.6000000238418579D;
//	    		}
//	    		double d3 = entity.locX - this.locX;
//	    		
//	    		double d0 = entity.locZ - this.locZ;
//	    		double d1 = d3 * d3 + d0 * d0;
//	    		if (d1 > 9.0D) {
//	    			double d2 = MathHelper.sqrt(d1);
//	    			this.motX += (d3 / d2 * 0.5D - this.motX) * 0.6000000238418579D;
//	    			this.motZ += (d0 / d2 * 0.5D - this.motZ) * 0.6000000238418579D;
//	    		}
//	    	}
//	    }
//	    if (this.motX * this.motX + this.motZ * this.motZ > 0.05000000074505806D) {
//	    	this.yaw = ((float)MathHelper.b(this.motZ, this.motX) * 57.295776F - 90.0F);
//	    }
//	    
//	    for (int i = 0; i < 2; i++) {
//	    	this.bm[i] = this.b[i];
//	    	this.c[i] = this.a[i];
//	    }
//	    
//	    for(int i = 0; i < 2; i++) {
//	    	int j = s(i + 1);
//	    	Entity entity1 = null;
//	    	if (j > 0) {
//	    		entity1 = this.world.a(j);
//	    	}
//	    	if (entity1 != null) {
//		        double d0 = t(i + 1);
//		        double d1 = u(i + 1);
//		        double d2 = v(i + 1);
//		        double d4 = entity1.locX - d0;
//		        double d5 = entity1.locY + entity1.getHeadHeight() - d1;
//		        double d6 = entity1.locZ - d2;
//		        double d7 = MathHelper.sqrt(d4 * d4 + d6 * d6);
//		        float f = (float)(MathHelper.b(d6, d4) * 180.0D / 3.1415927410125732D) - 90.0F;
//		        float f1 = (float)-(MathHelper.b(d5, d7) * 180.0D / 3.1415927410125732D);
//		        
//		        this.a[i] = b(this.a[i], f1, 40.0F);
//		        this.b[i] = b(this.b[i], f, 10.0F);
//	    	}
//	    	else {
//	    		this.b[i] = b(this.b[i], this.aI, 10.0F);
//	    	}
//	    }
//	    boolean flag = cm();
//	    for (int j = 0; j < 3; j++) {
//	    	double d8 = t(j);
//	    	double d9 = u(j);
//	    	double d10 = v(j);
//
//	    	Location center = new Location(world.getWorld(), d8, d9, d10);
//	    	
//	    	ParticleEffect.SMOKE_NORMAL.display(new Vector(), 1, center, 32);
//	    	
//	    	this.world.addParticle(EnumParticle.SMOKE_NORMAL, d8 + this.random.nextGaussian() * 0.30000001192092896D,
//	    			d9 + this.random.nextGaussian() * 0.30000001192092896D, d10 + this.random.nextGaussian() * 0.30000001192092896D,
//	    			0.0D, 0.0D, 0.0D, new int[0]);
//	    	if ((flag) && (this.world.random.nextInt(4) == 0)) {
//		    	Location center1 = new Location(world.getWorld(), d8 + this.random.nextGaussian() * 0.30000001192092896D,
//		    				d9 + this.random.nextGaussian() * 0.30000001192092896D, d10 + this.random.nextGaussian() * 0.30000001192092896D);
//		    	
//		    	ParticleEffect.SPELL.display(new OrdinaryColor(getRandomColor()), center1, 32);
//	    		this.world.addParticle(EnumParticle.SPELL_MOB, d8 + this.random.nextGaussian() * 0.30000001192092896D,
//	    				d9 + this.random.nextGaussian() * 0.30000001192092896D, d10 + this.random.nextGaussian() * 0.30000001192092896D,
//	    				0.699999988079071D, 0.699999988079071D, 0.5D, new int[0]);
//	    	}
//	    	if ((flag) && (this.world.random.nextInt(4) == 0)) {
//		    	Location center1 = new Location(world.getWorld(), d8 + this.random.nextGaussian() * 0.30000001192092896D,
//		    				d9 + this.random.nextGaussian() * 0.30000001192092896D, d10 + this.random.nextGaussian() * 0.30000001192092896D);
//		    	
//		    	ParticleEffect.DRIP_LAVA.display(new Vector(), 1, center1, 32);
//	    		this.world.addParticle(EnumParticle.SPELL_MOB, d8 + this.random.nextGaussian() * 0.30000001192092896D,
//	    				d9 + this.random.nextGaussian() * 0.30000001192092896D, d10 + this.random.nextGaussian() * 0.30000001192092896D,
//	    				0.699999988079071D, 0.699999988079071D, 0.5D, new int[0]);
//	    	}
//	    }
//	    
//	    if (cl() > 0) {
//	    	for (j = 0; j < 3; j++) {
//	    		this.world.addParticle(EnumParticle.SPELL_MOB, this.locX + this.random.nextGaussian() * 1.0D, this.locY + this.random.nextFloat() * 3.3F, this.locZ + this.random.nextGaussian() * 1.0D, 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D, new int[0]);
//	    	}
//	    }
//	}
  
	protected void E() {
		if(cl() > 0) {
			int i = cl() - 1;
			if(i <= 0) {
				ExplosionPrimeEvent event = new ExplosionPrimeEvent(getBukkitEntity(), 7.0F, false);
				this.world.getServer().getPluginManager().callEvent(event);
				if(!event.isCancelled()) {
					this.world.createExplosion(this, this.locX, this.locY + getHeadHeight(), this.locZ, event.getRadius(),
							event.getFire(), this.world.getGameRules().getBoolean("mobGriefing"));
				}
				int viewDistance = ((WorldServer)this.world).getServer().getViewDistance() * 16;
				for(EntityPlayer player : MinecraftServer.getServer().getPlayerList().players) {
					double deltaX = this.locX - player.locX;
					double deltaZ = this.locZ - player.locZ;
					double distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
					if((this.world.spigotConfig.witherSpawnSoundRadius <= 0) ||
							(distanceSquared <=
							this.world.spigotConfig.witherSpawnSoundRadius * this.world.spigotConfig.witherSpawnSoundRadius)) {
						if(distanceSquared > viewDistance * viewDistance)
						{
							double deltaLength = Math.sqrt(distanceSquared);
							double relativeX = player.locX + deltaX / deltaLength * viewDistance;
							double relativeZ = player.locZ + deltaZ / deltaLength * viewDistance;
							player.playerConnection.sendPacket(
									new PacketPlayOutWorldEvent(1013, new BlockPosition(
											(int)relativeX, (int)this.locY, (int)relativeZ), 0, true));
						}
						else {
							player.playerConnection.sendPacket(
									new PacketPlayOutWorldEvent(1013, new BlockPosition(
											(int)this.locX, (int)this.locY, (int)this.locZ), 0, true));
						}
					}
				}
			}
			r(i);
			if(this.ticksLived % 10 == 0) {
				heal(10.0F, EntityRegainHealthEvent.RegainReason.WITHER_SPAWN);
			}
		}
		else {
			for(int i = 1; i < 3; i++) {
				if(this.ticksLived >= this.bn[(i - 1)]) {
					this.bn[(i - 1)] = (this.ticksLived + 10 + this.random.nextInt(10));
					if((this.world.getDifficulty() == EnumDifficulty.NORMAL) || (this.world.getDifficulty() == EnumDifficulty.HARD)) {
						int k = i - 1;
						int l = this.bo[(i - 1)];
            
						this.bo[k] = (this.bo[(i - 1)] + 1);
						if(l > 15) {
							float f = 10.0F;
							float f1 = 5.0F;
							double d0 = MathHelper.a(this.random, this.locX - f, this.locX + f);
							double d1 = MathHelper.a(this.random, this.locY - f1, this.locY + f1);
							double d2 = MathHelper.a(this.random, this.locZ - f, this.locZ + f);
              
							a(null, i + 1, d0, d1, d2, true);
							this.bo[(i - 1)] = 0;
						}
					}
					int j = s(i);
					if(j > 0)  {
						Entity entity = this.world.a(j);
						if((entity != null) && (entity.isAlive()) && (h(entity) <= 900.0D) && (hasLineOfSight(entity)))
						{
							if(((entity instanceof EntityHuman)) && (((EntityHuman)entity).abilities.isInvulnerable)) {
								b(i, 0);
							}
							else {
								a(i + 1, (EntityLiving)entity);
								this.bn[(i - 1)] = (this.ticksLived + 40 + this.random.nextInt(20));
								this.bo[(i - 1)] = 0;
							}
						}
						else {
							b(i, 0);
						}
					}
					else {
						List<Entity> list = this.world.a(EntityLiving.class, getBoundingBox().grow(20.0D, 8.0D, 20.0D),
								Predicates.and(bq, IEntitySelector.d));
						for(int i1 = 0; (i1 < 10) && (!list.isEmpty()); i1++)
						{
							EntityLiving entityliving = (EntityLiving)list.get(this.random.nextInt(list.size()));
							if((entityliving != this) && (entityliving.isAlive()) && (hasLineOfSight(entityliving)))
							{
								if((entityliving instanceof EntityHuman))
								{
									if(((EntityHuman) entityliving).abilities.isInvulnerable) {
										break;
									}
									b(i, entityliving.getId());
									
									break;
								}
								b(i, entityliving.getId());
								
								break;
							}
							list.remove(entityliving);
						}
					}
				}
			}
			if(getGoalTarget() != null) {
				b(0, getGoalTarget().getId());
			} else {
				b(0, 0);
			}
			if(this.bp > 0)
			{
				this.bp -= 1;
				if((this.bp == 0) && (this.world.getGameRules().getBoolean("mobGriefing"))) {
					int i = MathHelper.floor(this.locY);
					int j = MathHelper.floor(this.locX);
					int j1 = MathHelper.floor(this.locZ);
					boolean flag = false;
					for(int k1 = -1; k1 <= 1; k1++) {
						for(int l1 = -1; l1 <= 1; l1++) {
							for(int i2 = 0; i2 <= 3; i2++)
							{
								int j2 = j + k1;
								int k2 = i + i2;
								int l2 = j1 + l1;
								BlockPosition blockposition = new BlockPosition(j2, k2, l2);
								Block block = this.world.getType(blockposition).getBlock();
								if((block.getMaterial() != Material.AIR) && (EntityWither.a(block))) {
									if(!CraftEventFactory.callEntityChangeBlockEvent(this, j2, k2, l2, Blocks.AIR, 0).isCancelled()) {
										flag = (this.world.setAir(blockposition, true)) || (flag);
									}
								}
							}
						}
					}
					if(flag) {
						this.world.a(null, 1012, new BlockPosition(this), 0);
					}
				}
			}
			if(this.ticksLived % 20 == 0) {
				heal(1.0F, EntityRegainHealthEvent.RegainReason.REGEN);
			}
		}
	}
  
	private double t(int i) {
		if (i <= 0) {
			return this.locX;
		}
		float f = (this.aI + 180 * (i - 1)) / 180.0F * 3.1415927F;
		float f1 = MathHelper.cos(f);
    
		return this.locX + f1 * 1.3D;
	}
  
	private double u(int i) {
  	 return i <= 0 ? this.locY + 3.0D : this.locY + 2.2D;
	}
  
	private double v(int i) {
		if (i <= 0) {
			return this.locZ;
		}
		float f = (this.aI + 180 * (i - 1)) / 180.0F * 3.1415927F;
		float f1 = MathHelper.sin(f);
		
		return this.locZ + f1 * 1.3D;
	}
  
//	private float b(float f, float f1, float f2) {
//		float f3 = MathHelper.g(f1 - f);
//		if (f3 > f2) {
//			f3 = f2;
//		}
//		if (f3 < -f2) {
//			f3 = -f2;
//		}
//		
//		return f + f3;
//	}
  
	private void a(int i, EntityLiving entityliving) {
		a(entityliving, i, entityliving.locX, entityliving.locY + entityliving.getHeadHeight() * 0.5D, entityliving.locZ,
				(i == 0) && (this.random.nextFloat() < 0.001F));
	}
  
	private void a(EntityLiving target, int i, double d0, double d1, double d2, boolean flag) {
		world.a(null, 1014, new BlockPosition(this), 0);
		double d3 = t(i);
		double d4 = u(i);
		double d5 = v(i);
		double d6 = d0 - d3;
		double d7 = d1 - d4;
		double d8 = d2 - d5;
		
		int rnd = random.nextInt(2);
		if(rnd == 0) {
			EntityWitherSkull entity = new EntityWitherSkull(world, this, d6, d7, d8);
			
		    if(flag) {
		    	entity.setCharged(true);
		    }
		    
		    world.addEntity(entity);
		}
		else if(rnd == 1) {
			Location entityLocation = new Location(world.getWorld(), d0, d1, d2);
			if(target != null) {
				entityLocation.setPitch(0);
				entityLocation.setYaw(0);
				
				if(target instanceof EntityAnimal) {
					entityLocation = ((LivingEntity) target.getBukkitEntity()).getEyeLocation().subtract(entityLocation.getDirection());
				}
				else {
					entityLocation = ((LivingEntity) target.getBukkitEntity()).getEyeLocation().subtract(0, 1, 0);
				}
			}
			
			Vector vector = entityLocation.toVector().subtract(new Location(world.getWorld(), d3, d4 + 1, d5).toVector());
			vector.multiply(0.0625);
			
			ParticleUtils.sendBeamFromEntity(vector, getRandomColor(), ((LivingEntity) getBukkitEntity()), 20, true, 5, 1l, 1l);
			Bukkit.broadcastMessage(getMaxHealth() + " " + getHealth());
		}
		
		locX = d3;
		locY = d4;
		locZ = d5;
	}

	private Color getRandomColor() {
	
		switch(this.random.nextInt(6)) {
		
		case 0 : return Color.RED;
		case 1 : return Color.BLACK;
		case 2 : return Color.GRAY;
		case 3 : return Color.BLUE;
		case 4 : return Color.PURPLE;
		case 5 : return Color.FUCHSIA;
	
		}
	
		return Color.PURPLE;
	}
  
	@Override
	public void a(EntityLiving entityliving, float f) {
		a(0, entityliving);
	}
  
	public boolean damageEntity(DamageSource damagesource, float f) {
		return super.damageEntity(damagesource, f);
	}
  
	protected void dropDeathLoot(boolean flag, int i) {
//		EntityItem entityitem = a(Items.NETHER_STAR, 1);
//		if (entityitem != null) {
//			entityitem.u();
//		}
//		if (!this.world.isClientSide) {
//			Iterator iterator = this.world.a(EntityHuman.class, getBoundingBox().grow(50.0D, 100.0D, 50.0D)).iterator();
//			while (iterator.hasNext())  {
//				EntityHuman entityhuman = (EntityHuman)iterator.next();
//				
//				entityhuman.b(AchievementList.J);
//			}
//		}
	}
	  
	public int cl() {
		return this.datawatcher.getInt(20);
	}
  
	protected void initAttributes() {
		super.initAttributes();
		
		getAttributeInstance(GenericAttributes.maxHealth).setValue(200);
		getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.6000000238418579D);
		getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(40.0D);
		
		setCustomName(DecimateUtils.color("&4&lWither Boss"));
		setCustomNameVisible(true);
	}
	
	public void r(int i) {
		this.datawatcher.watch(20, Integer.valueOf(i));
	}
	
	public void b(int i, int j) {
		this.datawatcher.watch(17 + i, Integer.valueOf(j));
	}
	
	public int s(int i) {
		return this.datawatcher.getInt(17 + i);
	}

	public void spawn() {
		world.addEntity(this, SpawnReason.CUSTOM);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setGoals() {
		goalSelector.a(0, new PathfinderGoalFloat(this));
		goalSelector.a(2, new PathfinderGoalArrowAttack(this, 1.0D, 40, 20.0F));
		goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
		goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
		targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
		targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityInsentient.class, 0, false, false, bq));
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
