package com.decimatepvp.core.listener;

import org.bukkit.event.Listener;

public class PlayerUseItemListener implements Listener {

//	@SuppressWarnings("deprecation")
//	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
//	public void onPlayerInteract(PlayerInteractEvent event) {
//		if (!event.isCancelled()) {
//			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
//				Player player = event.getPlayer();
//				ItemStack hand = player.getItemInHand();
//				if ((hand.getType() == Material.MONSTER_EGG) && (!player.isSneaking())) {
//					Block eventBlock = event.getClickedBlock().getRelative(event.getBlockFace());
//					Block actualBlock = player.getLastTwoTargetBlocks((HashSet<Byte>) null, 10).get(1)
//							.getRelative(PlayerUtils.getBlockFace(player));
//					if (eventBlock.equals(actualBlock)) {
//						SpawnEgg egg = (SpawnEgg) hand.getData();
//						Location center = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation()
//								.add(0.5D, 0, 0.5D);
//						player.getWorld().spawnEntity(center, egg.getSpawnedType());
//
//						if (player.getGameMode() != GameMode.CREATIVE) {
//							hand.setAmount(hand.getAmount() - 1);
//							if (hand.getAmount() <= 0) {
//								player.getInventory().setItem(player.getInventory().getHeldItemSlot(), null);
//							}
//						}
//						event.setCancelled(true);
//					}
//
//				}
//			}
//		}
//	}

}
