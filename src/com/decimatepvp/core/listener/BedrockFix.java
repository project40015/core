package com.decimatepvp.core.listener;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

import com.decimatepvp.utils.PlayerUtils;

public class BedrockFix implements Listener {

	@EventHandler
	public void onPlace(BlockPlaceEvent event){
		if(event.getBlock().getType().equals(Material.BEDROCK)){
			if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
				PlayerUtils.removeItems(event.getPlayer(), Material.BEDROCK);
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onSpawn(ItemSpawnEvent event){
		if(event.getEntity().getItemStack().getType().equals(Material.BEDROCK)){
			event.setCancelled(true);
		}
	}
	
}
