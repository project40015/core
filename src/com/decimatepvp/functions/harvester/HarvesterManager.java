package com.decimatepvp.functions.harvester;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.Manager;
import com.decimatepvp.utils.ItemUtils;

public class HarvesterManager implements Manager, Listener {
	
	private final ItemStack harvesterItem;
	
	public HarvesterManager() {
		harvesterItem = ItemUtils.createItem(Material.DIAMOND_HOE, 1, (byte) 0, "&6&l* &2&lHarvester Hoe &l&6*",
				"&6* Allows you to harvest multiple sugarcane stacks");
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSugarCaneBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		ItemStack hand = player.getItemInHand();
		if((hand != null) && (hand.equals(harvesterItem))) {
			Block block = event.getBlock();
			if(block.getType() == Material.SUGAR_CANE_BLOCK) {
				player.getLocation().setYaw(0);
				List<Block> line = player.getLineOfSight((HashSet<Byte>) null, 6);
				
				//Gets the sugarcane on the side of the main line of sight
				for(Block b : line) {
					Block b1 = b.getRelative(BlockFace.WEST);
					Block b2 = b.getRelative(BlockFace.EAST);
					if(b1.getType() == Material.SUGAR_CANE_BLOCK) {
						line.add(b1);
					}
					else if(b2.getType() == Material.SUGAR_CANE_BLOCK) {
						line.add(b2);
					}
				}
				
				// Removes non-sugar
				for(Block b : new ArrayList<>(line)) {
					if(b.getType() != Material.SUGAR_CANE_BLOCK) {
						line.remove(b);
					}
				}
				
				int amount = breakStalks(line);
				ItemStack sugarcane = new ItemStack(Material.SUGAR_CANE_BLOCK, amount);
				player.getInventory().addItem(sugarcane);
			}
		}
	}
	
	private int breakStalks(List<Block> line) {
		int amountBroken = 0;
		for(Block block : line) {
			if(block.getRelative(BlockFace.DOWN).getType() == Material.SUGAR_CANE_BLOCK) {
				amountBroken++;
				block.setType(Material.AIR);
			}
		}
		
		return amountBroken;
	}

	public void giveHarvester(Player player) {
		player.getInventory().addItem(harvesterItem);
	}
	
	public ItemStack getHarvesterItem() {
		return harvesterItem.clone();
	}

	@Override
	public void disable() {
		
	}

}
