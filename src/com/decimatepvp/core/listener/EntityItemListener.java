package com.decimatepvp.core.listener;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftItem;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityItemListener implements Listener {
	
	private Material[] allow = {Material.LADDER, Material.SIGN, Material.SIGN_POST, Material.LAVA, Material.WATER, Material.AIR};

	
	@EventHandler
	public void onLavaDestroySpawner(EntityDamageEvent event) {
		try {
			if(event.getEntityType() == EntityType.DROPPED_ITEM &&
					(event.getCause() == DamageCause.ENTITY_EXPLOSION ||
					event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.FIRE)) {
				Item item = (Item) event.getEntity();
				// This is needed as otherwise the item will jump upwards afterwards making it stay in the air/
				if(item.getItemStack().getType() == Material.MOB_SPAWNER) {
					Material mat = item.getLocation().clone().add(0,-1,0).getBlock().getType();
					if(!contains(mat)){
						return;
					}
					if(event.getCause() == DamageCause.LAVA){
						net.minecraft.server.v1_8_R3.Entity nms = ((CraftItem) item).getHandle();
						nms.motY -= 1;
						nms.move(nms.motX, nms.motY, nms.motZ);
					}
					event.setCancelled(true);
				}
			}
		}
		catch(Exception e) { }
	}
	
	private boolean contains(Material material){
		for(Material mat : allow){
			if(mat == material){
				return true;
			}
		}
		return false;
	}

}
