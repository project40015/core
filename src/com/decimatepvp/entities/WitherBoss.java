package com.decimatepvp.entities;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.decimatepvp.functions.animation.sphere.MovementAnimation;
import com.decimatepvp.utils.DecimateUtils;
import com.decimatepvp.utils.ParticleEffect.OrdinaryColor;
import com.decimatepvp.utils.ParticleUtils;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;

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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static final Predicate<Entity> bq = new Predicate() {
		public boolean a(Entity entity) {
			return entity instanceof EntityLiving && ((EntityLiving) entity).getMonsterType() != EnumMonsterType.UNDEAD;
		}

		@Override
		public boolean apply(Object object) {
			return this.a((Entity) object);
		}
	};
	public static ItemStack[] equipment = new ItemStack[] { new ItemStack(org.bukkit.Material.DIAMOND_SWORD),
			new ItemStack(org.bukkit.Material.DIAMOND_BOOTS), new ItemStack(org.bukkit.Material.DIAMOND_LEGGINGS),
			new ItemStack(org.bukkit.Material.DIAMOND_CHESTPLATE), new ItemStack(org.bukkit.Material.DIAMOND_HELMET) };

	public static boolean a(Block block) {
		return block != Blocks.BEDROCK && block != Blocks.END_PORTAL && block != Blocks.END_PORTAL_FRAME
				&& block != Blocks.COMMAND_BLOCK && block != Blocks.BARRIER;
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		return map.entrySet().stream().sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	public static void spawnMinion(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof LivingEntity && event.getEntity() instanceof LivingEntity) {
			LivingEntity damaged = (LivingEntity) event.getEntity();
			LivingEntity damager = (LivingEntity) event.getDamager();
			if (damager.toString().equals("DecimateWitherBoss") && // If the
					// killer
					// is a
					// WitherBoss
					damaged.getHealth() - event.getFinalDamage() <= 0 && // If
					// the
					// damaged
					// entity
					// will
					// be
					// killed
					Math.random() < 0.25D) {
				damaged.getWorld().strikeLightning(damaged.getLocation());
				Skeleton witherskeleton = (Skeleton) damaged.getWorld().spawnEntity(damaged.getLocation(),
						EntityType.SKELETON);
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

	private final float[] a = new float[2];

	private final float[] b = new float[2];

	private final float[] c = new float[2];

	private final float[] bm = new float[2];

	private final int[] bn = new int[2];

	private final int[] bo = new int[2];

	private int bp;

	private final double[][][] fallAttack = ParticleUtils.createLayeredDisk(6, 24, 1.0D, 1.0D, true);

	private final double[][] attackSphere = ParticleUtils.createSphere(0.75, 8, 16);

	public boolean isInFallingAttack = false;

	public Map<String, Float> playerDamage = Maps.newHashMap();

	private final DecimalFormat df = new DecimalFormat("00.00");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public WitherBoss(net.minecraft.server.v1_8_R3.World world) {
		super(world);
		this.setSize(0.9F, 3.5F);
		fireProof = true;
		((Navigation) this.getNavigation()).d(true);
		goalSelector.a(0, new PathfinderGoalFloat(this));
		goalSelector.a(2, new PathfinderGoalArrowAttack(this, 1.0D, 40, 20.0F));
		goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
		goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
		targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
		targetSelector.a(2,
				new PathfinderGoalNearestAttackableTarget(this, EntityInsentient.class, 0, false, false, bq));
		b_ = 50;
	}

	public WitherBoss(World world) {
		this(((CraftWorld) world).getHandle());
	}

	@Override
	public void a(EntityLiving entityliving, float f) {
		this.a(0, entityliving);
	}

	private void a(EntityLiving target, int i, double d0, double d1, double d2, boolean flag) {
		world.a(null, 1014, new BlockPosition(this), 0);
		double d3 = this.t(i);
		double d4 = this.u(i);
		double d5 = this.v(i);
		double d6 = d0 - d3;
		double d7 = d1 - d4;
		double d8 = d2 - d5;

		Location eyelocation = ((LivingEntity) this.getBukkitEntity()).getEyeLocation();

		int rnd = random.nextInt(5);
		if (rnd == 0) {
			EntityWitherSkull entity = new EntityWitherSkull(world, this, d6, d7, d8);

			if (flag) {
				entity.setCharged(true);
			}

			world.addEntity(entity);
		} else if (rnd == 1) {
			EntityFireball entity = random.nextBoolean() ? new EntityLargeFireball(world, this, d6, d7, d8)
					: new EntitySmallFireball(world, this, d6, d7, d8);

			world.addEntity(entity);
		} else if (rnd == 2) {
			Location entityLocation = new Location(world.getWorld(), d0, d1, d2);
			if (target != null) {
				if (target != null && target instanceof EntityAnimal) {
					entityLocation = ((LivingEntity) target.getBukkitEntity()).getEyeLocation()
							.subtract(entityLocation.getDirection());
				} else {
					entityLocation = ((LivingEntity) target.getBukkitEntity()).getEyeLocation().subtract(0, 1, 0);
				}
			}

			Vector vector = entityLocation.toVector()
					.subtract(new Location(world.getWorld(), d3, d4 + 1, d5).toVector());
			vector.multiply(0.0625);

			ParticleUtils.sendBeamFromEntity(vector, this.getRandomColor(), (LivingEntity) this.getBukkitEntity(), 20,
					true, 5, 1l, 1l);
		} else if (rnd == 3) {
			Location entityLocation = new Location(world.getWorld(), d0, d1, d2);
			if (target != null) {
				if (target != null && target instanceof EntityAnimal) {
					entityLocation = ((LivingEntity) target.getBukkitEntity()).getEyeLocation()
							.subtract(entityLocation.getDirection());
				} else {
					entityLocation = ((LivingEntity) target.getBukkitEntity()).getEyeLocation().subtract(0, 1, 0);
				}
			}
			Vector direction = eyelocation.getDirection();

			MovementAnimation animation = this.getRasenganAttack(new OrdinaryColor(this.getRandomColor()), eyelocation,
					direction, 16);
			animation.start(0l, random.nextInt(2) + 1);
		}

		double chance = random.nextDouble();
		// if(chance <= 0.10D) {
		// isInFallingAttack = true;
		// this.motY = 1.0D;
		// getFallAttack().runTaskTimer(DecimateCore.getCore(), 20l, 20l);
		// }
		chance = random.nextDouble();
		if (chance <= 0.10D) {
			this.setLocation(d0, d1, d2, yaw, pitch);
			this.makeSound("mob.endermen.portal", 1.0f, 1.0f);
		}
		chance = random.nextDouble();
		if (chance <= 0.05D) {
			if (target != null) {
				world.getWorld().strikeLightning(target.getBukkitEntity().getLocation());
				Bukkit.broadcastMessage(this.getHealth() + "/" + this.getMaxHealth());
			}
		}
		chance = random.nextDouble();

		locX = d3;
		locY = d4;
		locZ = d5;
	}

	private void a(int i, EntityLiving entityliving) {
		this.a(entityliving, i, entityliving.locX, entityliving.locY + entityliving.getHeadHeight() * 0.5D,
				entityliving.locZ, i == 0 && random.nextFloat() < 0.001F);
	}

	@Override
	public void a(NBTTagCompound nbttagcompound) {
		super.a(nbttagcompound);
		this.r(nbttagcompound.getInt("Invul"));
	}

	@Override
	public void aA() {
	}

	@Override
	public void addEffect(MobEffect mobeffect) {
	}

	private float b(float f, float f1, float f2) {
		float f3 = MathHelper.g(f1 - f);
		if (f3 > f2) {
			f3 = f2;
		}
		if (f3 < -f2) {
			f3 = -f2;
		}
		return f + f3;
	}

	public void b(int i, int j) {
		datawatcher.watch(17 + i, Integer.valueOf(j));
	}

	@Override
	public void b(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		nbttagcompound.setInt("Invul", this.cl());
	}

	@Override
	protected String bo() {
		return "mob.wither.hurt";
	}

	@Override
	protected String bp() {
		return "mob.wither.death";
	}

	@Override
	public int br() {
		return 4;
	}

	public int cl() {
		return datawatcher.getInt(20);
	}

	public boolean cm() {
		return false;
	}

	@Override
	protected void D() {
		ticksFarFromPlayer = 0;
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float damage) {
		damage = damage / 10;
		if (this.isInvulnerable(damagesource)) {
			return false;
		}
		if (damagesource != DamageSource.DROWN && !(damagesource.getEntity() instanceof EntityWither)) {
			if (this.cl() > 0 && damagesource != DamageSource.OUT_OF_WORLD) {
				return false;
			}
			if (this.cm()) {
				Entity entity = damagesource.i();
				if (entity instanceof EntityArrow) {
					return false;
				}
			}
			Entity entity = damagesource.getEntity();
			if (entity != null && !(entity instanceof EntityHuman) && entity instanceof EntityLiving
					&& ((EntityLiving) entity).getMonsterType() == this.getMonsterType()) {
				return false;
			}
			if (bp <= 0) {
				bp = 20;
			}
			for (int i = 0; i < bo.length; i++) {
				bo[i] += 3;
			}

			return super.damageEntity(damagesource, damage);
		}

		return false;
	}

	@Override
	public void die() {
		super.die();

		playerDamage = sortByValue(playerDamage);

		Bukkit.broadcastMessage(ChatColor.GOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		Bukkit.broadcastMessage(ChatColor.GREEN + "The Wither has been defeated! Here are the top players:");

		int position = 1;
		for (Entry<String, Float> set : playerDamage.entrySet()) {
			if (position <= 3) {
				String damage = df.format(set.getValue());
				Bukkit.broadcastMessage("D: " + damage);
				damage = df.format(1000 / set.getValue() * 100) + "%";
				String message = DecimateUtils.color(position + ".) &b&l" + set.getKey() + "&2 with &b&l" + damage);
				message = this.getPlaceColor(position) + message;

				Bukkit.broadcastMessage(message);
			} else {
				break;
			}
		}

		Bukkit.broadcastMessage(ChatColor.GOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

	}

	@Override
	protected void dropDeathLoot(boolean flag, int i) {
		// EntityItem entityitem = a(Items.NETHER_STAR, 1);
		// if (entityitem != null) {
		// entityitem.u();
		// }
		// if (!this.world.isClientSide)
		// {
		// Iterator iterator = this.world.a(EntityHuman.class,
		// getBoundingBox().grow(50.0D, 100.0D, 50.0D)).iterator();
		// while (iterator.hasNext())
		// {
		// EntityHuman entityhuman = (EntityHuman)iterator.next();
		//
		// entityhuman.b(AchievementList.J);
		// }
		// }
	}

	@Override
	public void e(float f, float f1) {
	}

	@Override
	protected void E() {
		if (this.cl() > 0) {
			int i = this.cl() - 1;
			if (i <= 0) {
				ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), 7.0F, false);
				world.getServer().getPluginManager().callEvent(event);
				if (!event.isCancelled()) {
					world.createExplosion(this, locX, locY + this.getHeadHeight(), locZ, event.getRadius(),
							event.getFire(), world.getGameRules().getBoolean("mobGriefing"));
				}
				int viewDistance = ((WorldServer) world).getServer().getViewDistance() * 16;
				for (EntityPlayer player : MinecraftServer.getServer().getPlayerList().players) {
					double deltaX = locX - player.locX;
					double deltaZ = locZ - player.locZ;
					double distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
					if (world.spigotConfig.witherSpawnSoundRadius <= 0
							|| distanceSquared <= world.spigotConfig.witherSpawnSoundRadius
							* world.spigotConfig.witherSpawnSoundRadius) {
						if (distanceSquared > viewDistance * viewDistance) {
							double deltaLength = Math.sqrt(distanceSquared);
							double relativeX = player.locX + deltaX / deltaLength * viewDistance;
							double relativeZ = player.locZ + deltaZ / deltaLength * viewDistance;
							player.playerConnection.sendPacket(new PacketPlayOutWorldEvent(1013,
									new BlockPosition((int) relativeX, (int) locY, (int) relativeZ), 0, true));
						} else {
							player.playerConnection.sendPacket(new PacketPlayOutWorldEvent(1013,
									new BlockPosition((int) locX, (int) locY, (int) locZ), 0, true));
						}
					}
				}
			}
			this.r(i);
			if (ticksLived % 10 == 0) {
				this.heal(10.0F, EntityRegainHealthEvent.RegainReason.WITHER_SPAWN);
			}
		} else {
			super.E();
			for (int i = 1; i < 3; i++) {
				if (ticksLived >= bn[i - 1]) {
					bn[i - 1] = ticksLived + 10 + random.nextInt(10);
					if (world.getDifficulty() == EnumDifficulty.NORMAL
							|| world.getDifficulty() == EnumDifficulty.HARD) {
						int k = i - 1;
						int l = bo[i - 1];

						bo[k] = bo[i - 1] + 1;
						if (l > 15) {
							float f = 10.0F;
							float f1 = 5.0F;
							double d0 = MathHelper.a(random, locX - f, locX + f);
							double d1 = MathHelper.a(random, locY - f1, locY + f1);
							double d2 = MathHelper.a(random, locZ - f, locZ + f);

							this.a(null, i + 1, d0, d1, d2, true);
							bo[i - 1] = 0;
						}
					}
					int j = this.s(i);
					if (j > 0) {
						Entity entity = world.a(j);
						if (entity != null && entity.isAlive() && this.h(entity) <= 900.0D
								&& this.hasLineOfSight(entity)) {
							if (entity instanceof EntityHuman && ((EntityHuman) entity).abilities.isInvulnerable) {
								this.b(i, 0);
							} else {
								this.a(i + 1, (EntityLiving) entity);
								bn[i - 1] = ticksLived + 40 + random.nextInt(20);
								bo[i - 1] = 0;
							}
						} else {
							this.b(i, 0);
						}
					} else {
						List<EntityLiving> list = world.a(EntityLiving.class,
								this.getBoundingBox().grow(20.0D, 8.0D, 20.0D), Predicates.and(bq, IEntitySelector.d));
						for (int i1 = 0; i1 < 10 && !list.isEmpty(); i1++) {
							EntityLiving entityliving = list.get(random.nextInt(list.size()));
							if (entityliving != this && entityliving.isAlive() && this.hasLineOfSight(entityliving)) {
								if (entityliving instanceof EntityHuman) {
									if (((EntityHuman) entityliving).abilities.isInvulnerable) {
										break;
									}
									this.b(i, entityliving.getId());

									break;
								}
								this.b(i, entityliving.getId());

								break;
							}
							list.remove(entityliving);
						}
					}
				}
			}
			if (this.getGoalTarget() != null) {
				this.b(0, this.getGoalTarget().getId());
			} else {
				this.b(0, 0);
			}
			if (bp > 0) {
				bp -= 1;
				if (bp == 0 && world.getGameRules().getBoolean("mobGriefing")) {
					int i = MathHelper.floor(locY);
					int j = MathHelper.floor(locX);
					int j1 = MathHelper.floor(locZ);
					boolean flag = false;
					for (int k1 = -1; k1 <= 1; k1++) {
						for (int l1 = -1; l1 <= 1; l1++) {
							for (int i2 = 0; i2 <= 3; i2++) {
								int j2 = j + k1;
								int k2 = i + i2;
								int l2 = j1 + l1;
								BlockPosition blockposition = new BlockPosition(j2, k2, l2);
								Block block = world.getType(blockposition).getBlock();
								if (block.getMaterial() != Material.AIR && a(block)) {
									if (!CraftEventFactory.callEntityChangeBlockEvent(this, j2, k2, l2, Blocks.AIR, 0)
											.isCancelled()) {
										flag = world.setAir(blockposition, true) || flag;
									}
								}
							}
						}
					}
					if (flag) {
						world.a(null, 1012, new BlockPosition(this), 0);
					}
				}
			}
			if (ticksLived % 20 == 0) {
				this.heal(1.0F, EntityRegainHealthEvent.RegainReason.REGEN);
			}
		}
	}

	@SuppressWarnings("unused")
	private BukkitRunnable getFallAttack() {
		return new BukkitRunnable() {

			private int ticks = 0;

			@Override
			public void run() {
				if (ticks < 3) {
					if (isInFallingAttack) {
						BlockPosition blockposition = new BlockPosition(locX, locY - 1.0D, locZ);
						Block block = world.getType(blockposition).getBlock();

						if (block.getMaterial() != Material.AIR) {
							Location loc = new Location(world.getWorld(), locX, locY, locZ);
							WitherBoss.this.summonFallAttackParticles();
							DecimateUtils.damageNearbyEntities(loc, 6.0D, 10, WitherBoss.this.getBukkitEntity());
							world.getWorld().playSound(loc, Sound.ENDERDRAGON_WINGS, 1.0f, 2.0f);
							isInFallingAttack = false;
							this.cancel();
						}

						ticks++;
					} else {
						this.cancel();
					}
				} else {
					motY = -5.0D;
					// BukkitRunnable br = new BukkitRunnable() {
					//
					// int i = 0;
					//
					// @Override
					// public void run() {
					// if(i < 20) {
					// i++;
					// }
					// else {
					// cancel();
					// }
					// }
					// };
					//
					// br.runTaskTimer(DecimateCore.getCore(), 0l, 1l);
				}
			}
		};
	}

	@Override
	public EnumMonsterType getMonsterType() {
		return EnumMonsterType.UNDEAD;
	}

	private ChatColor getPlaceColor(int position) {

		switch (position) {
		case 1:
			return ChatColor.GOLD;

		case 2:
			return ChatColor.GRAY;

		case 3:
			return ChatColor.DARK_AQUA;
		}

		return ChatColor.GRAY;
	}

	private Color getRandomColor() {

		switch (random.nextInt(6)) {

		case 0:
			return Color.RED;
		case 1:
			return Color.BLACK;
		case 2:
			return Color.GRAY;
		case 3:
			return Color.BLUE;
		case 4:
			return Color.PURPLE;
		case 5:
			return Color.FUCHSIA;

		}

		return Color.PURPLE;
	}

	private MovementAnimation getRasenganAttack(OrdinaryColor particlecolor, Location start, Vector direction,
			double range) {
		return new MovementAnimation(attackSphere, particlecolor, start, direction, range, 10.0D,
				this.getBukkitEntity(), 0.50D);
	}

	@Override
	protected void h() {
		super.h();
		datawatcher.a(17, new Integer(0));
		datawatcher.a(18, new Integer(0));
		datawatcher.a(19, new Integer(0));
		datawatcher.a(20, new Integer(0));
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		this.getAttributeInstance(GenericAttributes.maxHealth).setValue(100);
		this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.6000000238418579D);
		this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(40.0D);
		this.setCustomName(DecimateUtils.color("&4&lWither Boss"));
		this.setCustomNameVisible(true);
	}

	@Override
	public void m() {
		motY *= 0.42069;
		if (!world.isClientSide && this.s(0) > 0) {
			Entity entity = world.a(this.s(0));
			if (entity != null) {
				if (locY < entity.locY || !this.cm() && locY < entity.locY + 5.0D && !isInFallingAttack) {
					if (motY < 0.0D) {
						motY = 0.0D;
					}

				}
				double d3 = entity.locX - locX;

				double d0 = entity.locZ - locZ;
				double d1 = d3 * d3 + d0 * d0;
				if (d1 > 9.0D) {
					double d2 = MathHelper.sqrt(d1);
					motX += (d3 / d2 * 0.5D - motX) * 0.6000000238418579D;
					motZ += (d0 / d2 * 0.5D - motZ) * 0.6000000238418579D;
				}
			}
		}
		if (motX * motX + motZ * motZ > 0.05000000074505806D) {
			yaw = (float) MathHelper.b(motZ, motX) * 57.295776F - 90.0F;
		}
		super.m();
		for (int i = 0; i < 2; i++) {
			bm[i] = b[i];
			c[i] = a[i];
		}
		for (int i = 0; i < 2; i++) {
			int j = this.s(i + 1);
			Entity entity1 = null;
			if (j > 0) {
				entity1 = world.a(j);
			}
			if (entity1 != null) {
				double d0 = this.t(i + 1);
				double d1 = this.u(i + 1);
				double d2 = this.v(i + 1);
				double d4 = entity1.locX - d0;
				double d5 = entity1.locY + entity1.getHeadHeight() - d1;
				double d6 = entity1.locZ - d2;
				double d7 = MathHelper.sqrt(d4 * d4 + d6 * d6);
				float f = (float) (MathHelper.b(d6, d4) * 180.0D / 3.1415927410125732D) - 90.0F;
				float f1 = (float) -(MathHelper.b(d5, d7) * 180.0D / 3.1415927410125732D);

				a[i] = this.b(a[i], f1, 40.0F);
				b[i] = this.b(b[i], f, 10.0F);
			} else {
				b[i] = this.b(b[i], aI, 10.0F);
			}
		}
		boolean flag = this.cm();
		for (int j = 0; j < 3; j++) {
			double d8 = this.t(j);
			double d9 = this.u(j);
			double d10 = this.v(j);

			world.addParticle(EnumParticle.SMOKE_NORMAL, d8 + random.nextGaussian() * 0.30000001192092896D,
					d9 + random.nextGaussian() * 0.30000001192092896D,
					d10 + random.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D, new int[0]);
			if (flag && world.random.nextInt(4) == 0) {
				world.addParticle(EnumParticle.SPELL_MOB, d8 + random.nextGaussian() * 0.30000001192092896D,
						d9 + random.nextGaussian() * 0.30000001192092896D,
						d10 + random.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D,
						0.5D, new int[0]);
			}
		}
		if (this.cl() > 0) {
			for (j = 0; j < 3; j++) {
				world.addParticle(EnumParticle.SPELL_MOB, locX + random.nextGaussian() * 1.0D,
						locY + random.nextFloat() * 3.3F, locZ + random.nextGaussian() * 1.0D, 0.699999988079071D,
						0.699999988079071D, 0.8999999761581421D, new int[0]);
			}
		}
	}

	@Override
	public void mount(Entity entity) {
		vehicle = null;
	}

	public void n() {
		this.r(220);
		this.setHealth(this.getMaxHealth() / 3.0F);
	}

	public void r(int i) {
		datawatcher.watch(20, Integer.valueOf(i));
	}

	public int s(int i) {
		return datawatcher.getInt(17 + i);
	}

	public void spawn(Location location) {
		this.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		world.addEntity(this);
	}

	protected void summonFallAttackParticles() {
		for (int i = 0; i < fallAttack.length; i++) {
			double[][] positions = fallAttack[i];
			for (double[] pos : positions) {
				double x = locX + pos[0];
				double y = locY + 0.1D;
				double z = locX + pos[1];

				ParticleUtils.summonRedstoneParticle(new OrdinaryColor(Color.PURPLE),
						new Location(world.getWorld(), x, y, z), 64);
			}
		}
	}

	private double t(int i) {
		if (i <= 0) {
			return locX;
		}
		float f = (aI + 180 * (i - 1)) / 180.0F * 3.1415927F;
		float f1 = MathHelper.cos(f);

		return locX + f1 * 1.3D;
	}

	@Override
	public String toString() {
		return "DecimateWitherBoss";
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

	@Override
	protected String z() {
		return "mob.wither.idle";
	}
	
	
}
