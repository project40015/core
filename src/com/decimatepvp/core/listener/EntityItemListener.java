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
	
	@EventHandler
	public void onSpawnerCombust(EntityDamageEvent event) {
		if(event.getEntityType() == EntityType.DROPPED_ITEM) {
			Item item = (Item) event.getEntity();
			if(item.getType() == EntityType.MINECART_MOB_SPAWNER) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onLavaDestroySpawner(EntityDamageEvent event) {
		try {
			if(event.getEntityType() == EntityType.DROPPED_ITEM &&
					(event.getCause() == DamageCause.ENTITY_EXPLOSION ||
					event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.FIRE)) {
				
				Item item = (Item) event.getEntity();
				// This is needed as otherwise the item will jump upwards afterwards making it stay in the air/
				if(item.getItemStack().getType() == Material.MOB_SPAWNER) {
					net.minecraft.server.v1_8_R3.Entity nms = ((CraftItem) item).getHandle();
					nms.motY -= 0.05;
					nms.move(nms.motX, nms.motY, nms.motZ);
//					setInvulnerable(item);
					event.setCancelled(true);
				}
			}
		}
		catch(Exception e) { }
	}

}
