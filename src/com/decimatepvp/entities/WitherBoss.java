package com.decimatepvp.entities;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Wither;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.functions.animation.sphere.MovementAnimation;
import com.decimatepvp.utils.DecimateUtils;
import com.decimatepvp.utils.ParticleEffect.OrdinaryColor;
import com.decimatepvp.utils.ParticleUtils;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityAnimal;
import net.minecraft.server.v1_8_R3.EntityArrow;
import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLargeFireball;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityMonster;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntitySmallFireball;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.EntityWitherSkull;
import net.minecraft.server.v1_8_R3.EnumDifficulty;
import net.minecraft.server.v1_8_R3.EnumMonsterType;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.IEntitySelector;
import net.minecraft.server.v1_8_R3.IRangedEntity;
import net.minecraft.server.v1_8_R3.Material;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.MobEffect;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.Navigation;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldEvent;
import net.minecraft.server.v1_8_R3.PathfinderGoalArrowAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.WorldServer;

public class WitherBoss extends EntityMonster implements IRangedEntity {
	
  private float[] a = new float[2];
  private float[] b = new float[2];
  private float[] c = new float[2];
  private float[] bm = new float[2];
  private int[] bn = new int[2];
  private int[] bo = new int[2];
  
  private int bp;
	
  	private double[][][] fallAttack = ParticleUtils.createLayeredDisk(6, 24, 1.0D, 1.0D, true);
  	
  	private double[][] attackSphere = ParticleUtils.createSphere(0.75, 8, 16);
	
	public boolean isInFallingAttack = false;
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  private static final Predicate<Entity> bq = new Predicate()
  {
    public boolean a(Entity entity)
    {
      return ((entity instanceof EntityLiving)) && (((EntityLiving)entity).getMonsterType() != EnumMonsterType.UNDEAD);
    }
    
    public boolean apply(Object object)
    {
      return a((Entity)object);
    }
  };
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public WitherBoss(Location location)
  {
    super(((CraftWorld) location.getWorld()).getHandle());
    setHealth(getMaxHealth());
    setSize(0.9F, 3.5F);
    setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    this.fireProof = true;
    ((Navigation)getNavigation()).d(true);
    this.goalSelector.a(0, new PathfinderGoalFloat(this));
    this.goalSelector.a(2, new PathfinderGoalArrowAttack(this, 1.0D, 40, 20.0F));
    this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
    this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
    this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
    this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
    this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityInsentient.class, 0, false, false, bq));
    this.b_ = 50;
  }
  
  public void spawn() {
	  world.addEntity(this);
  }
  
  protected void h()
  {
    super.h();
    this.datawatcher.a(17, new Integer(0));
    this.datawatcher.a(18, new Integer(0));
    this.datawatcher.a(19, new Integer(0));
    this.datawatcher.a(20, new Integer(0));
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.setInt("Invul", cl());
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    r(nbttagcompound.getInt("Invul"));
  }
  
  protected String z()
  {
    return "mob.wither.idle";
  }
  
  protected String bo()
  {
    return "mob.wither.hurt";
  }
  
  protected String bp()
  {
    return "mob.wither.death";
  }
  
  public void m()
  {
    this.motY *= 0.42069;
    if ((!this.world.isClientSide) && (s(0) > 0))
    {
      Entity entity = this.world.a(s(0));
      if (entity != null)
      {
        if ((this.locY < entity.locY) || ((!cm()) && (this.locY < entity.locY + 5.0D)) && (!isInFallingAttack))
        {
          if (this.motY < 0.0D) {
            this.motY = 0.0D;
          }
          
        }
        double d3 = entity.locX - this.locX;
        
        double d0 = entity.locZ - this.locZ;
        double d1 = d3 * d3 + d0 * d0;
        if (d1 > 9.0D)
        {
          double d2 = MathHelper.sqrt(d1);
          this.motX += (d3 / d2 * 0.5D - this.motX) * 0.6000000238418579D;
          this.motZ += (d0 / d2 * 0.5D - this.motZ) * 0.6000000238418579D;
        }
      }
    }
    if (this.motX * this.motX + this.motZ * this.motZ > 0.05000000074505806D) {
      this.yaw = ((float)MathHelper.b(this.motZ, this.motX) * 57.295776F - 90.0F);
    }
    super.m();
    for (int i = 0; i < 2; i++)
    {
      this.bm[i] = this.b[i];
      this.c[i] = this.a[i];
    }
    for (int i = 0; i < 2; i++)
    {
      int j = s(i + 1);
      Entity entity1 = null;
      if (j > 0) {
        entity1 = this.world.a(j);
      }
      if (entity1 != null)
      {
        double d0 = t(i + 1);
        double d1 = u(i + 1);
        double d2 = v(i + 1);
        double d4 = entity1.locX - d0;
        double d5 = entity1.locY + entity1.getHeadHeight() - d1;
        double d6 = entity1.locZ - d2;
        double d7 = MathHelper.sqrt(d4 * d4 + d6 * d6);
        float f = (float)(MathHelper.b(d6, d4) * 180.0D / 3.1415927410125732D) - 90.0F;
        float f1 = (float)-(MathHelper.b(d5, d7) * 180.0D / 3.1415927410125732D);
        
        this.a[i] = b(this.a[i], f1, 40.0F);
        this.b[i] = b(this.b[i], f, 10.0F);
      }
      else
      {
        this.b[i] = b(this.b[i], this.aI, 10.0F);
      }
    }
    boolean flag = cm();
    for (int j = 0; j < 3; j++)
    {
      double d8 = t(j);
      double d9 = u(j);
      double d10 = v(j);
      
      this.world.addParticle(EnumParticle.SMOKE_NORMAL, d8 + this.random.nextGaussian() * 0.30000001192092896D, d9 + this.random.nextGaussian() * 0.30000001192092896D, d10 + this.random.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D, new int[0]);
      if ((flag) && (this.world.random.nextInt(4) == 0)) {
        this.world.addParticle(EnumParticle.SPELL_MOB, d8 + this.random.nextGaussian() * 0.30000001192092896D, d9 + this.random.nextGaussian() * 0.30000001192092896D, d10 + this.random.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D, new int[0]);
      }
    }
    if (cl() > 0) {
      for (j = 0; j < 3; j++) {
        this.world.addParticle(EnumParticle.SPELL_MOB, this.locX + this.random.nextGaussian() * 1.0D, this.locY + this.random.nextFloat() * 3.3F, this.locZ + this.random.nextGaussian() * 1.0D, 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D, new int[0]);
      }
    }
  }
  
  protected void E()
  {
    if (cl() > 0)
    {
      int i = cl() - 1;
      if (i <= 0)
      {
        ExplosionPrimeEvent event = new ExplosionPrimeEvent(getBukkitEntity(), 7.0F, false);
        this.world.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
          this.world.createExplosion(this, this.locX, this.locY + getHeadHeight(), this.locZ, event.getRadius(), event.getFire(), this.world.getGameRules().getBoolean("mobGriefing"));
        }
        int viewDistance = ((WorldServer)this.world).getServer().getViewDistance() * 16;
        for (EntityPlayer player : MinecraftServer.getServer().getPlayerList().players)
        {
          double deltaX = this.locX - player.locX;
          double deltaZ = this.locZ - player.locZ;
          double distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
          if ((this.world.spigotConfig.witherSpawnSoundRadius <= 0) || (distanceSquared <= this.world.spigotConfig.witherSpawnSoundRadius * this.world.spigotConfig.witherSpawnSoundRadius)) {
            if (distanceSquared > viewDistance * viewDistance)
            {
              double deltaLength = Math.sqrt(distanceSquared);
              double relativeX = player.locX + deltaX / deltaLength * viewDistance;
              double relativeZ = player.locZ + deltaZ / deltaLength * viewDistance;
              player.playerConnection.sendPacket(new PacketPlayOutWorldEvent(1013, new BlockPosition((int)relativeX, (int)this.locY, (int)relativeZ), 0, true));
            }
            else
            {
              player.playerConnection.sendPacket(new PacketPlayOutWorldEvent(1013, new BlockPosition((int)this.locX, (int)this.locY, (int)this.locZ), 0, true));
            }
          }
        }
      }
      r(i);
      if (this.ticksLived % 10 == 0) {
        heal(10.0F, EntityRegainHealthEvent.RegainReason.WITHER_SPAWN);
      }
    }
    else
    {
      super.E();
      for (int i = 1; i < 3; i++) {
        if (this.ticksLived >= this.bn[(i - 1)])
        {
          this.bn[(i - 1)] = (this.ticksLived + 10 + this.random.nextInt(10));
          if ((this.world.getDifficulty() == EnumDifficulty.NORMAL) || (this.world.getDifficulty() == EnumDifficulty.HARD))
          {
            int k = i - 1;
            int l = this.bo[(i - 1)];
            
            this.bo[k] = (this.bo[(i - 1)] + 1);
            if (l > 15)
            {
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
          if (j > 0)
          {
            Entity entity = this.world.a(j);
            if ((entity != null) && (entity.isAlive()) && (h(entity) <= 900.0D) && (hasLineOfSight(entity)))
            {
              if (((entity instanceof EntityHuman)) && (((EntityHuman)entity).abilities.isInvulnerable))
              {
                b(i, 0);
              }
              else
              {
                a(i + 1, (EntityLiving)entity);
                this.bn[(i - 1)] = (this.ticksLived + 40 + this.random.nextInt(20));
                this.bo[(i - 1)] = 0;
              }
            }
            else {
              b(i, 0);
            }
          }
          else
          {
            List<EntityLiving> list = this.world.a(EntityLiving.class, getBoundingBox().grow(20.0D, 8.0D, 20.0D), Predicates.and(bq, IEntitySelector.d));
            for (int i1 = 0; (i1 < 10) && (!list.isEmpty()); i1++)
            {
              EntityLiving entityliving = (EntityLiving)list.get(this.random.nextInt(list.size()));
              if ((entityliving != this) && (entityliving.isAlive()) && (hasLineOfSight(entityliving)))
              {
                if ((entityliving instanceof EntityHuman))
                {
                  if (((EntityHuman) entityliving).abilities.isInvulnerable) {
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
      if (getGoalTarget() != null) {
        b(0, getGoalTarget().getId());
      } else {
        b(0, 0);
      }
      if (this.bp > 0)
      {
        this.bp -= 1;
        if ((this.bp == 0) && (this.world.getGameRules().getBoolean("mobGriefing")))
        {
          int i = MathHelper.floor(this.locY);
          int j = MathHelper.floor(this.locX);
          int j1 = MathHelper.floor(this.locZ);
          boolean flag = false;
          for (int k1 = -1; k1 <= 1; k1++) {
            for (int l1 = -1; l1 <= 1; l1++) {
              for (int i2 = 0; i2 <= 3; i2++)
              {
                int j2 = j + k1;
                int k2 = i + i2;
                int l2 = j1 + l1;
                BlockPosition blockposition = new BlockPosition(j2, k2, l2);
                Block block = this.world.getType(blockposition).getBlock();
                if ((block.getMaterial() != Material.AIR) && (a(block))) {
                  if (!CraftEventFactory.callEntityChangeBlockEvent(this, j2, k2, l2, Blocks.AIR, 0).isCancelled()) {
                    flag = (this.world.setAir(blockposition, true)) || (flag);
                  }
                }
              }
            }
          }
          if (flag) {
            this.world.a(null, 1012, new BlockPosition(this), 0);
          }
        }
      }
      if (this.ticksLived % 20 == 0) {
        heal(1.0F, EntityRegainHealthEvent.RegainReason.REGEN);
      }
    }
  }
  
  public static boolean a(Block block)
  {
    return (block != Blocks.BEDROCK) && (block != Blocks.END_PORTAL) && (block != Blocks.END_PORTAL_FRAME) && (block != Blocks.COMMAND_BLOCK) && (block != Blocks.BARRIER);
  }
  
  public void n()
  {
    r(220);
    setHealth(getMaxHealth() / 3.0F);
  }
  
  public void aA() {}
  
  public int br()
  {
    return 4;
  }
  
  private double t(int i)
  {
    if (i <= 0) {
      return this.locX;
    }
    float f = (this.aI + 180 * (i - 1)) / 180.0F * 3.1415927F;
    float f1 = MathHelper.cos(f);
    
    return this.locX + f1 * 1.3D;
  }
  
  private double u(int i)
  {
    return i <= 0 ? this.locY + 3.0D : this.locY + 2.2D;
  }
  
  private double v(int i)
  {
    if (i <= 0) {
      return this.locZ;
    }
    float f = (this.aI + 180 * (i - 1)) / 180.0F * 3.1415927F;
    float f1 = MathHelper.sin(f);
    
    return this.locZ + f1 * 1.3D;
  }
  
  private float b(float f, float f1, float f2)
  {
    float f3 = MathHelper.g(f1 - f);
    if (f3 > f2) {
      f3 = f2;
    }
    if (f3 < -f2) {
      f3 = -f2;
    }
    return f + f3;
  }
  
  private void a(int i, EntityLiving entityliving)
  {
    a(entityliving, i, entityliving.locX, entityliving.locY + entityliving.getHeadHeight() * 0.5D, entityliving.locZ, (i == 0) && (this.random.nextFloat() < 0.001F));
  }
  
  private void a(EntityLiving target, int i, double d0, double d1, double d2, boolean flag) {
		world.a(null, 1014, new BlockPosition(this), 0);
		double d3 = t(i);
		double d4 = u(i);
		double d5 = v(i);
		double d6 = d0 - d3;
		double d7 = d1 - d4;
		double d8 = d2 - d5;
		
		Location eyelocation = ((Wither) getBukkitEntity()).getEyeLocation();
		
		int rnd = random.nextInt(5);
		if(rnd == 0) {
			EntityWitherSkull entity = new EntityWitherSkull(world, this, d6, d7, d8);
			
		    if(flag) {
		    	entity.setCharged(true);
		    }
		    
		    world.addEntity(entity);
		}
		else if(rnd == 1) {
			EntityFireball entity = random.nextBoolean() ?
					new EntityLargeFireball(world, this, d6, d7, d8) : new EntitySmallFireball(world, this, d6, d7, d8);
		    
		    world.addEntity(entity);
		}
		else if(rnd == 2) {
			Location entityLocation = new Location(world.getWorld(), d0, d1, d2);
			if(target != null) {
				if((target != null) && (target instanceof EntityAnimal)) {
					entityLocation = ((LivingEntity) target.getBukkitEntity()).getEyeLocation().subtract(entityLocation.getDirection());
				}
				else {
					entityLocation = ((LivingEntity) target.getBukkitEntity()).getEyeLocation().subtract(0, 1, 0);
				}
			}
			
			Vector vector = entityLocation.toVector().subtract(new Location(world.getWorld(), d3, d4 + 1, d5).toVector());
			vector.multiply(0.0625);
			
			ParticleUtils.sendBeamFromEntity(vector, getRandomColor(), ((LivingEntity) getBukkitEntity()), 20, true, 5, 1l, 1l);
		}
		else if(rnd == 3) {
			Location entityLocation = new Location(world.getWorld(), d0, d1, d2);
			if(target != null) {
				if((target != null) && (target instanceof EntityAnimal)) {
					entityLocation = ((LivingEntity) target.getBukkitEntity()).getEyeLocation().subtract(entityLocation.getDirection());
				}
				else {
					entityLocation = ((LivingEntity) target.getBukkitEntity()).getEyeLocation().subtract(0, 1, 0);
				}
			}
			Vector direction = eyelocation.getDirection();
			
			MovementAnimation animation = getRasenganAttack(new OrdinaryColor(getRandomColor()), eyelocation, direction, 16);
			animation.start(0l, random.nextInt(2)+1);
		}
		else if(rnd == 4) {
			if(target != null) {
				world.getWorld().strikeLightning(target.getBukkitEntity().getLocation());
			}
		}

		double chance = random.nextDouble();
		if(chance >= 0.10D) {
			isInFallingAttack = true;
			this.motY = 1.0D;
			getFallAttack().runTaskTimer(DecimateCore.getCore(), 20l, 20l);
		}
		chance = random.nextDouble();
		if(chance >= 0.1D) {
			this.setLocation(d0, d1, d2, yaw, pitch);
			makeSound("mob.endermen.portal", 1.0f, 1.0f);
		}
		chance = random.nextDouble();
		
		
		locX = d3;
		locY = d4;
		locZ = d5;
	}
  
  	private MovementAnimation getRasenganAttack(OrdinaryColor particlecolor, Location start,
			Vector direction, double range) {
  		return new MovementAnimation(attackSphere, particlecolor, start, direction, range,
  				10.0D, getBukkitEntity(), 0.50D);
  	}
	
	private BukkitRunnable getFallAttack() {
		return new BukkitRunnable() {
			
			private int ticks = 0;
			
			@Override
			public void run() {
				if(ticks < 3) {
					if(isInFallingAttack) {
						BlockPosition blockposition = new BlockPosition(locX, locY - 1.0D, locZ);
						Block block = world.getType(blockposition).getBlock();
						
						if(block.getMaterial() != Material.AIR) {
							Location loc = new Location(world.getWorld(), locX, locY, locZ);
							summonFallAttackParticles();
							DecimateUtils.damageNearbyEntities(loc, 6.0D, 10, getBukkitEntity());
							world.getWorld().playSound(loc, Sound.ENDERDRAGON_WINGS, 1.0f, 2.0f);
							isInFallingAttack = false;
							cancel();
						}
						
						ticks++;
					}
					else {
						cancel();
					}
				}
				else {
					motY = -5.0D;
//					BukkitRunnable br = new BukkitRunnable() {
//						
//						int i = 0;
//						
//						@Override
//						public void run() {
//							if(i < 20) {
//								i++;
//							}
//							else {
//								cancel();
//							}
//						}
//					};
//					
//					br.runTaskTimer(DecimateCore.getCore(), 0l, 1l);
				}
			}
		};
	}
  
  public void a(EntityLiving entityliving, float f)
  {
    a(0, entityliving);
  }
  
  public boolean damageEntity(DamageSource damagesource, float f)
  {
    if (isInvulnerable(damagesource)) {
      return false;
    }
    if ((damagesource != DamageSource.DROWN) && (!(damagesource.getEntity() instanceof EntityWither)))
    {
      if ((cl() > 0) && (damagesource != DamageSource.OUT_OF_WORLD)) {
        return false;
      }
      if (cm())
      {
        Entity entity = damagesource.i();
        if ((entity instanceof EntityArrow)) {
          return false;
        }
      }
      Entity entity = damagesource.getEntity();
      if ((entity != null) && (!(entity instanceof EntityHuman)) && ((entity instanceof EntityLiving)) && (((EntityLiving)entity).getMonsterType() == getMonsterType())) {
        return false;
      }
      if (this.bp <= 0) {
        this.bp = 20;
      }
      for (int i = 0; i < this.bo.length; i++) {
        this.bo[i] += 3;
      }
      return super.damageEntity(damagesource, f);
    }
    return false;
  }
	
	protected void summonFallAttackParticles() {
		for (int i = 0; i < fallAttack.length; i++) {
			double[][] positions = fallAttack[i];
			for(double[] pos : positions) {
				double x = locX + pos[0];
				double y = locY + 0.1D;
				double z = locX + pos[1];
				
				ParticleUtils.summonRedstoneParticle(new OrdinaryColor(Color.PURPLE), new Location(world.getWorld(), x, y, z), 64);
			}
		}
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
  
  protected void dropDeathLoot(boolean flag, int i)
  {
//    EntityItem entityitem = a(Items.NETHER_STAR, 1);
//    if (entityitem != null) {
//      entityitem.u();
//    }
//    if (!this.world.isClientSide)
//    {
//      Iterator iterator = this.world.a(EntityHuman.class, getBoundingBox().grow(50.0D, 100.0D, 50.0D)).iterator();
//      while (iterator.hasNext())
//      {
//        EntityHuman entityhuman = (EntityHuman)iterator.next();
//        
//        entityhuman.b(AchievementList.J);
//      }
//    }
  }
  
  protected void D()
  {
    this.ticksFarFromPlayer = 0;
  }
  
  public void e(float f, float f1) {}
  
  public void addEffect(MobEffect mobeffect) {}
  
  protected void initAttributes()
  {
    super.initAttributes();
    getAttributeInstance(GenericAttributes.maxHealth).setValue(1000);
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.6000000238418579D);
    getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(40.0D);
    setCustomName(ChatColor.RED + "Wither Boss");
    setCustomNameVisible(true);
  }
  
  public int cl()
  {
    return this.datawatcher.getInt(20);
  }
  
  public void r(int i)
  {
    this.datawatcher.watch(20, Integer.valueOf(i));
  }
  
  public int s(int i)
  {
    return this.datawatcher.getInt(17 + i);
  }
  
  public void b(int i, int j)
  {
    this.datawatcher.watch(17 + i, Integer.valueOf(j));
  }
  
  public boolean cm()
  {
    return getHealth() <= getMaxHealth() / 2.0F;
  }
  
  public EnumMonsterType getMonsterType()
  {
    return EnumMonsterType.UNDEAD;
  }
  
  public void mount(Entity entity)
  {
    this.vehicle = null;
  }
  
  	@Override
	public String toString() {
	  return "DecimateWitherBoss";
	}
  	
  	public static ItemStack[] equipment = new ItemStack[] {
  			new ItemStack(org.bukkit.Material.DIAMOND_SWORD),
  			new ItemStack(org.bukkit.Material.DIAMOND_BOOTS),
  			new ItemStack(org.bukkit.Material.DIAMOND_LEGGINGS),
  			new ItemStack(org.bukkit.Material.DIAMOND_CHESTPLATE),
  			new ItemStack(org.bukkit.Material.DIAMOND_HELMET)
  	};

	public static void spawnMinion(EntityDamageByEntityEvent event) {
		if((event.getDamager() instanceof LivingEntity) &&
				(event.getEntity() instanceof LivingEntity)) {
			LivingEntity damaged = (LivingEntity) event.getEntity();
			LivingEntity damager = (LivingEntity) event.getDamager();
			if((damager.toString().equals("DecimateWitherBoss")) && //If the killer is a WitherBoss
					(damaged.getHealth() - event.getFinalDamage() <= 0) && //If the damaged entity will be killed
					(Math.random() < 0.25D)) {
				damaged.getWorld().strikeLightning(damaged.getLocation());
				Skeleton witherskeleton = (Skeleton) damaged.getWorld().spawnEntity(damaged.getLocation(), EntityType.SKELETON);
				witherskeleton.setSkeletonType(SkeletonType.WITHER);
				EntityEquipment equip = witherskeleton.getEquipment();
				
				equip.setArmorContents(equipment.clone());

				equip.setBootsDropChance(0);
				equip.setLeggingsDropChance(0);
				equip.setChestplateDropChance(0);
				equip.setHelmetDropChance(0);
				equip.setItemInHandDropChance(0);
			}
		}
	}
}
