package com.decimatepvp.functions.harvester;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.Manager;
import com.decimatepvp.utils.ItemUtils;

public class HarvesterManager implements Manager, Listener {
	
	private Random random;
	
	private final ItemStack harvesterItem;
	
	public HarvesterManager() {
		harvesterItem = ItemUtils.createItem(Material.DIAMOND_HOE, 1, (byte) 0, "&6&l* &2&lHarvester Hoe &l&6*",
				"&6* Allows you to harvest multiple sugarcane stacks");
		harvesterItem.addEnchantment(Enchantment.DURABILITY, 3);
		random = new Random();
	}
	
	@EventHandler
	public void onSugarCaneBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		ItemStack hand = player.getItemInHand();
		if(ItemUtils.isItemCloned(hand, harvesterItem)) {
			Block block = event.getBlock();
			if(block.getType() == Material.SUGAR_CANE_BLOCK) {	
				event.setCancelled(true);
				Location loc = player.getLocation(); loc.setPitch(0);
				player.teleport(loc);
				List<Block> line = new ArrayList<>();
				for(int i = 1; i <= 6; i++){
					line.add(loc.add(loc.getDirection()).getBlock());
				}
				
				//Gets the sugarcane on the side of the main line of sight
				for(Block b : new ArrayList<>(line)) {
					Block b1 = b.getRelative(BlockFace.WEST);
					Block b2 = b.getRelative(BlockFace.EAST);
					
					if(b1.getType() == Material.SUGAR_CANE_BLOCK) {
						line.add(b1);
					}
					else if(b2.getType() == Material.SUGAR_CANE_BLOCK) {
						line.add(b2);
					}
				}
				
				for(int i = 0; i < 2; i++) {
					for(Block b : new ArrayList<>(line)) {
						Block up = b.getRelative(BlockFace.UP);
						
						if(up.getType() == Material.SUGAR_CANE_BLOCK) {
							line.add(up);
						}
					}
				}
				
				// Removes non-sugarcane
				for(Block b : new ArrayList<>(line)) {
					if(b.getType() != Material.SUGAR_CANE_BLOCK) {
						line.remove(b);
					}
				}

				int amount = breakStalks(line);
				if(amount > 0) {
					ItemStack sugarcane = new ItemStack(Material.SUGAR_CANE, amount);
					player.getInventory().addItem(sugarcane);
				}
			}
		}
	}
	
	private int breakStalks(List<Block> line) {
		TreeMap<Double, Block> tree = new TreeMap<>(Collections.reverseOrder());
		int amountBroken = 0;
		for(Block block : line) {
			if(block.getRelative(BlockFace.DOWN).getType() == Material.SUGAR_CANE_BLOCK) {
				amountBroken++;
				
				tree.put(block.getY() + random.nextDouble(), block);
			
			}
		}
		
		for(double d : tree.keySet()) {
			tree.get(d).setType(Material.AIR);
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
