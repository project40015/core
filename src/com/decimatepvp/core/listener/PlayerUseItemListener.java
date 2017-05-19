package com.decimatepvp.core.listener;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;

import com.decimatepvp.utils.PlayerUtils;

public class PlayerUseItemListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(!event.isCancelled()) {
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Player player = event.getPlayer();
				ItemStack hand = player.getItemInHand();
				Block eventBlock = event.getClickedBlock().getRelative(event.getBlockFace());
				Block actualBlock = player.getLastTwoTargetBlocks((HashSet<Byte>) null, 10).get(1)
						.getRelative(PlayerUtils.getBlockFace(player));
				if(eventBlock.equals(actualBlock)) {
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

}
