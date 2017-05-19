package com.decimatepvp.core.listener;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;

public class PlayerUseItemListener implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		useSpawnEgg(event);
	}

	private void useSpawnEgg(PlayerInteractEvent event) {
		if(!event.isCancelled()) {
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Player player = event.getPlayer();
				ItemStack hand = player.getItemInHand();
				if((hand.getType() == Material.MONSTER_EGG) && (!player.isSneaking())) {
					SpawnEgg egg = (SpawnEgg) hand.getData();
					Location center = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation().add(0.5D, 0, 0.5D);
					player.getWorld().spawnEntity(center, egg.getSpawnedType());
					
					if(player.getGameMode() != GameMode.CREATIVE) {
						hand.setAmount(hand.getAmount() - 1);
						if(hand.getAmount() <= 0) {
							player.getInventory().setItem(player.getInventory().getHeldItemSlot(), null);
						}
					}
					event.setCancelled(true);
				}
			}
		}
	}

}
