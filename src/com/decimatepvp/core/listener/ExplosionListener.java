package com.decimatepvp.core.listener;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.functions.extraexplodables.ExplodableManager;
import com.google.common.collect.Maps;

import net.minecraft.server.v1_8_R3.MathHelper;

public class ExplosionListener implements Listener {
	
	private Random random = new Random();
	private HashMap<Integer, Float> entityPowerMap = Maps.newHashMap();
	private ExplodableManager explodableManager = DecimateCore.getCore().getExplodableManager();
	private DecimateCore plugin = DecimateCore.getCore();
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void OnExplosionPrime(ExplosionPrimeEvent event) {
		if(!event.isCancelled()) {
			this.entityPowerMap.put(Integer.valueOf(event.getEntity().getEntityId()), Float.valueOf(event.getRadius()));
		}
	}

//	@EventHandler(priority = EventPriority.HIGHEST)
//	public void onBlockDestroy(EntityExplodeEvent event) {
//		if ((!event.isCancelled()) && (event.getEntityType() != EntityType.ENDER_DRAGON) &&
//				(this.entityPowerMap.containsKey(Integer.valueOf(event.getEntity().getEntityId())))) {
//	      correctExplosion(event, ((Float)this.entityPowerMap.get(Integer.valueOf(event.getEntity().getEntityId()))).floatValue());
//	      this.entityPowerMap.remove(Integer.valueOf(event.getEntity().getEntityId()));
//	    }
//	}
	
	private boolean makeBlowable(Material m){
		return m == Material.OBSIDIAN ;
	}
	
	private boolean instantBlow(Material m){
		return m == Material.LAVA || m == Material.STATIONARY_LAVA || m == Material.BEDROCK;
	}
	
	private boolean instantBlowDrop(Material m){
		return m == Material.ENCHANTMENT_TABLE || m == Material.ENDER_CHEST;
	}
	
	 
	  @EventHandler(priority=EventPriority.MONITOR)
	  public void onEntityExplode(EntityExplodeEvent e)
	  {
	    if ((!e.isCancelled()) && (e.getEntity() != null))
	    {
//	      if ((e.getEntityType() != EntityType.PRIMED_TNT)) {
//	        return;
//	      }
	      Location source = e.getLocation();
//	      double dmgRadius = this.plugin.getConfig().getDouble("Damage Radius");
	      double dmgRadius = 3;
	      if (e.getYield() > 1.0F) {
	        dmgRadius += e.getYield() / 10.0F;
	      }
	      int radius = (int)Math.ceil(dmgRadius);
	      for (int x = -radius; x <= radius; x++) {
	        for (int y = -radius; y <= radius; y++) {
	          for (int z = -radius; z <= radius; z++)
	          {
	            Location loc = new Location(source.getWorld(), x + source.getX(), y + source.getY(), z + source.getZ());
	            if (source.distance(loc) <= dmgRadius)
	            {
	              Block block = loc.getBlock();
//	              if(block.getType() != Material.AIR){
//	            	  Bukkit.broadcastMessage(block.getType().toString());
//	              }
	              if(!block.getLocation().getBlock().isLiquid() && instantBlow(block.getType())){
	            	  if(block.getType() == Material.BEDROCK && block.getLocation().getY() < 5){
	            		  continue;
	            	  }
	            	  block.setType(Material.AIR);
	            	  continue;
	              }
	              if(!block.getLocation().getBlock().isLiquid() && instantBlowDrop(block.getType())){
	            	  block.breakNaturally();
	            	  continue;
	              }
	              if (makeBlowable(block.getType()))
	              {
//	            	 Bukkit.broadcastMessage(ChatColor.GOLD + block.getType().toString());
	                double distance = loc.distance(source);
	                double damage = 2;
//	                if (block.getType() != Material.OBSIDIAN) {
//	                  damage += 2.0D;
//	                }
	                if (e.getYield() > 0.5D) {
	                  damage += 2.0D;
	                }
	                if (e.getYield() > 8.0F) {
	                  damage += 2.0D;
	                }
	                if (e.getYield() > 16.0F) {
	                  damage += 2.0D;
	                }
	                if (e.getYield() > 22.0F) {
	                  damage += 2.0D;
	                }
	                if (e.getYield() > 28.0F) {
	                  damage += 2.0D;
	                }
	                if (source.getBlock().isLiquid()) {
	                	damage*=0;
//	                  damage *= this.plugin.getConfig().getDouble("Liquid Multiplier");
	                }
	                if (damage > 0.0D)
	                {
////	                  if (this.plugin.getConfig().getBoolean("Scan Through Blocks"))
////	                  {
//	                    Vector v = new Vector(loc.getBlockX() - source.getBlockX(), loc.getBlockY() - source.getBlockY(), loc.getBlockZ() - source.getBlockZ());
//	                    BlockIterator it = new BlockIterator(source.getWorld(), source.toVector(), v, 0.0D, (int)source.distance(loc));
//	                    while (it.hasNext())
//	                    {
////	                      Block b = it.next();
//	                      if ((source.getBlock().getType() == Material.AIR) && (b.isLiquid())) {
////	                        damage *= this.plugin.getConfig().getDouble("Liquid Multiplier");
////	                      }
////	                      if (b.getType() == Material.BEDROCK) {
////	                        damage *= 0.5D;
////	                      } else if (b.getType() == Material.OBSIDIAN) {
////	                        damage *= 0.7D;
////	                      } else if (b.getType() != Material.AIR) {
////	                        damage *= 0.9D;
////	                      }
//	                    }
//	                  }
	                  if (damage <= 0.0D)
	                  {
	                    if (e.getYield() > 0.5D) {
	                      damage += 0.5D;
	                    }
	                    if (e.getYield() > 8.0F) {
	                      damage += 0.5D;
	                    }
	                    if (e.getYield() > 16.0F) {
	                      damage += 0.5D;
	                    }
	                    if (e.getYield() > 22.0F) {
	                      damage += 0.5D;
	                    }
	                    if (e.getYield() > 28.0F) {
	                      damage += 0.5D;
	                    }
	                  }
	                  if (damage > 0.0D)
	                  {
	                    damage /= distance;
	                    @SuppressWarnings("deprecation")
						int id = block.getType().getId();
//	                    if(
	                    plugin.getExplodableManager().getExplodable(block.getWorld().getName(), block.getX(), block.getY(), block.getZ(), id).hit();
//	                    		){
//	                    	block.setType(Material.AIR);
//	                    }
//	                    if (this.healthMap.containsKey(id)) {
//	                      this.healthMap.put(id, Double.valueOf(((Double)this.healthMap.get(id)).doubleValue() - damage));
//	                    } else {
//	                      this.healthMap.put(id, Double.valueOf(this.plugin.getConfig().getDouble("Block Health") - damage));
//	                    }
//	                    if (((Double)this.healthMap.get(id)).doubleValue() <= 0.0D)
//	                    {
//	                      block.breakNaturally();
//	                      this.healthMap.remove(id);
//	                    }
	                  }
	                }
	              }
	            }
	          }
	        }
	      }
	    }
	  }
	
	
	
//	@SuppressWarnings("deprecation")
//	private void correctExplosion(EntityExplodeEvent event, float power) {
//		org.bukkit.World world = event.getEntity().getWorld();
////	    event.blockList().clear();
//	    for (int i = 0; i < 16; i++) {
//	    	for (int j = 0; j < 16; j++) {
//	    		for (int k = 0; k < 16; k++) {
//	    			if ((i == 0) || (i == 15) || (j == 0) || (j == 15) || (k == 0) || (k == 15)) {
//	    				double d3 = i / 15.0F * 2.0F - 1.0F;
//	    				double d4 = j / 15.0F * 2.0F - 1.0F;
//	    				double d5 = k / 15.0F * 2.0F - 1.0F;
//	    				double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
//	            
//	    				d3 /= d6;
//	    				d4 /= d6;
//	    				d5 /= d6;
//	    				float f1 = power * (0.7F + this.random.nextFloat() * 0.6F);
//	            
//	    				double d0 = event.getLocation().getX();
//	    				double d1 = event.getLocation().getY();
//	    				double d2 = event.getLocation().getZ();
//	    				for (float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F) {
//	    					int l = MathHelper.floor(d0);
//	    					int i1 = MathHelper.floor(d1);
//	    					int j1 = MathHelper.floor(d2);
//	    					int k1 = world.getBlockTypeIdAt(l, i1, j1);
//	    					if ((k1 > 0) && (k1 != 8) && (k1 != 9) && (k1 != 10) && (k1 != 11)) {
//	    						f1 -= (net.minecraft.server.v1_8_R3.Block.getById(k1).a(((CraftEntity)event.getEntity()).getHandle()) + 0.3F) * f2;
//	    					}
//	    					if ((f1 > 0.0F) && (i1 < 256) && (i1 >= 0) && (k1 != 8) && (k1 != 9) && (k1 != 10) && k1 != 0) {
//	    						org.bukkit.block.Block block = world.getBlockAt(l, i1, j1);
//	    						if(!event.blockList().contains(block)){
//	    							Bukkit.broadcastMessage(ChatColor.RED.toString() + k1);
//	    							if(k1 == 7){
//	    								block.setType(Material.AIR);
//	    								Bukkit.broadcastMessage("l");
//	    							}else if(k1 == 10 || k1 == 11){
//	    								Bukkit.broadcastMessage("n");
//	    								event.blockList().add(block);
//	    							}else if(explodableManager.isExplodable(k1)){
//	    								Bukkit.broadcastMessage("d");
//	    								if(explodableManager.getExplodable(world.getName(), l, i1, j1, k1).hit()){
//	    									Bukkit.broadcastMessage("c");
//	    									event.blockList().add(block);
//	    								}
//	    							}
//	    						}
//	    					}
//	    					d0 += d3 * f2;
//	    					d1 += d4 * f2;
//	    					d2 += d5 * f2;
//	    				}
//	    			}
//	    		}
//	    	}
//	    }
//	}

}