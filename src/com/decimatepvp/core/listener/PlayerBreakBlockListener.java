package com.decimatepvp.core.listener;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.decimatepvp.core.DecimateCore;

public class PlayerBreakBlockListener implements Listener {
	
	private DecimateCore core;
	
	public PlayerBreakBlockListener() {
		core = DecimateCore.getCore();
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		spawners(event);
	}
	
	@SuppressWarnings("deprecation")
	private void spawners(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if(block.getType() == Material.MOB_SPAWNER) {
			CreatureSpawner spawner = (CreatureSpawner) block.getState();
			double cost = core.config.getValueForSpawner(spawner.getSpawnedType().getTypeId());
			
			if(core.eco.has(player, cost) || player.getGameMode() == GameMode.CREATIVE) {
				if(player.getGameMode() != GameMode.CREATIVE) {
					core.eco.withdrawPlayer(player, cost);
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "You do have have sufficient funds to break this.");
				event.setCancelled(true);
			}
		}
	}

}
