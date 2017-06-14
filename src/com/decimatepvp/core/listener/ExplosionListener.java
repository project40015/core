package com.decimatepvp.core.listener;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import com.google.common.collect.Maps;

import net.minecraft.server.v1_8_R3.MathHelper;

public class ExplosionListener implements Listener {
	
	private Random random = new Random();
	private HashMap<Integer, Float> entityPowerMap = Maps.newHashMap();
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void OnExplosionPrime(ExplosionPrimeEvent event) {
		if(!event.isCancelled()) {
			this.entityPowerMap.put(Integer.valueOf(event.getEntity().getEntityId()), Float.valueOf(event.getRadius()));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockDestroy(EntityExplodeEvent event) {
		if ((!event.isCancelled()) && (event.getEntityType() != EntityType.ENDER_DRAGON) &&
				(this.entityPowerMap.containsKey(Integer.valueOf(event.getEntity().getEntityId())))) {
	      correctExplosion(event, ((Float)this.entityPowerMap.get(Integer.valueOf(event.getEntity().getEntityId()))).floatValue());
	      this.entityPowerMap.remove(Integer.valueOf(event.getEntity().getEntityId()));
	    }
	}
	
	@SuppressWarnings("deprecation")
	private void correctExplosion(EntityExplodeEvent event, float power) {
		org.bukkit.World world = event.getEntity().getWorld();
	    event.blockList().clear();
	    for (int i = 0; i < 16; i++) {
	    	for (int j = 0; j < 16; j++) {
	    		for (int k = 0; k < 16; k++) {
	    			if ((i == 0) || (i == 15) || (j == 0) || (j == 15) || (k == 0) || (k == 15)) {
	    				double d3 = i / 15.0F * 2.0F - 1.0F;
	    				double d4 = j / 15.0F * 2.0F - 1.0F;
	    				double d5 = k / 15.0F * 2.0F - 1.0F;
	    				double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
	            
	    				d3 /= d6;
	    				d4 /= d6;
	    				d5 /= d6;
	    				float f1 = power * (0.7F + this.random.nextFloat() * 0.6F);
	            
	    				double d0 = event.getLocation().getX();
	    				double d1 = event.getLocation().getY();
	    				double d2 = event.getLocation().getZ();
	    				for (float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F) {
	    					int l = MathHelper.floor(d0);
	    					int i1 = MathHelper.floor(d1);
	    					int j1 = MathHelper.floor(d2);
	    					int k1 = world.getBlockTypeIdAt(l, i1, j1);
	    					if ((k1 > 0) && (k1 != 8) && (k1 != 9) && (k1 != 10) && (k1 != 11)) {
	    						f1 -= (net.minecraft.server.v1_8_R3.Block.getById(k1).a(((CraftEntity)event.getEntity()).getHandle()) + 0.3F) * f2;
	    					}
	    					if ((f1 > 0.0F) && (i1 < 256) && (i1 >= 0) && (k1 != 8) && (k1 != 9) && (k1 != 10)) {
	    						org.bukkit.block.Block block = world.getBlockAt(l, i1, j1);
	    						if(((k1 == (11)) || (k1 == (12))) &&
	    								(!event.blockList().contains(block))) {
	    							event.blockList().add(block);
	    						}
	    					}
	    					d0 += d3 * f2;
	    					d1 += d4 * f2;
	    					d2 += d5 * f2;
	    				}
	    			}
	    		}
	    	}
	    }
	}

}