package com.decimatepvp.functions.misc.harvester;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.decimatepvp.utils.ItemUtils;
import com.decimatepvp.utils.PlayerUtils;

public class HarvesterManager implements Manager, Listener {
	
	private Random random;
	
	private final ItemStack harvesterItem;
	
	public HarvesterManager() {
		harvesterItem = ItemUtils.createItem(Material.DIAMOND_HOE, 1, (byte) 0, "&eHarvester Hoe",
				ChatColor.GRAY + "Mode: " + ChatColor.GOLD + "SELL");
		harvesterItem.addEnchantment(Enchantment.DURABILITY, 3);
		random = new Random();
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(ItemUtils.isItemCloned(event.getPlayer().getItemInHand(), harvesterItem)) {
			if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
				if(event.getPlayer().getItemInHand().getItemMeta().getLore().get(0).contains("SELL")){
					event.getPlayer().sendMessage(ChatColor.GRAY + "Harvester mode: " + ChatColor.GREEN + "GATHER");
				}else{
					event.getPlayer().sendMessage(ChatColor.GRAY + "Harvester mode: " + ChatColor.GOLD + "SELL");
				}
				event.getPlayer().setItemInHand(updateWand(event.getPlayer().getItemInHand(), 0, true));
				event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 1, 1);
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onSugarCaneBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		ItemStack hand = player.getItemInHand();
		if(ItemUtils.isItemCloned(hand, harvesterItem)) {
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.PIG_WALK, 1, 1);
			Block block = event.getBlock();
			if(block.getType() == Material.SUGAR_CANE_BLOCK) {	
				event.setCancelled(true);
				Location loc = player.getLocation().clone();
				loc.setPitch(0);
				List<Block> line = new ArrayList<>();
				for(int i = 1; i <= 6; i++){
					if(!line.contains(loc.clone().add(loc.getDirection().normalize().multiply(i).toLocation(loc.getWorld())).getBlock())){
						line.add(loc.clone().add(loc.getDirection().normalize().multiply(i).toLocation(loc.getWorld())).getBlock());
					}
				}
				
				//Gets the sugarcane on the side of the main line of sight
				for(Block b : new ArrayList<>(line)) {
					Block b1 = b.getRelative(BlockFace.WEST);
					Block b2 = b.getRelative(BlockFace.EAST);
					
					if((player.getLocation().getYaw() > -135 && player.getLocation().getYaw() < -45) || 
							(player.getLocation().getYaw() < 135 && player.getLocation().getYaw() > 45)){
						b1 = b.getRelative(BlockFace.NORTH);
						b2 = b.getRelative(BlockFace.SOUTH);
					}
					
					if(b1.getType() == Material.SUGAR_CANE_BLOCK) {
						line.add(b1);
					}
					else if(b2.getType() == Material.SUGAR_CANE_BLOCK) {
						line.add(b2);
					}
				}
				
				List<Block> toAdd = new ArrayList<>();
				
				for(int i = 0; i < 3; i++) {
					for(Block b : new ArrayList<>(line)) {
						Block up = b.getLocation().clone().add(0, i+1, 0).getBlock();
						
						if(up.getType() == Material.SUGAR_CANE_BLOCK) {
							toAdd.add(up);
						}
					}
				}
				
				toAdd.forEach(bl -> {
					if(!line.contains(bl)){
						line.add(bl);
					}
				});
				
				// Removes non-sugarcane
				for(Block b : new ArrayList<>(line)) {
					if(b.getType() != Material.SUGAR_CANE_BLOCK) {
						line.remove(b);
					}
				}

				int amount = breakStalks(line);
				ItemStack updated = updateWand(hand, amount, false);
				event.getPlayer().setItemInHand(updated);
				if(amount > 0) {
					if(hand.getItemMeta().getLore().get(0).contains("GATHER")){
						ItemStack sugarcane = new ItemStack(Material.SUGAR_CANE, amount);
						player.getInventory().addItem(sugarcane);
					}else{
						String str = ChatColor.stripColor(updated.getItemMeta().getLore().get(1).split(" ")[2]);
						str = str.substring(1);
						PlayerUtils.sendActionbar(event.getPlayer(), ChatColor.GREEN + "+$" + (amount*Double.parseDouble(str)));
						DecimateCore.getCore().eco.depositPlayer(event.getPlayer(), amount*DecimateCore.getCore().getDecimateConfig().getCostOfSugarcane());
					}
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
			Block bl = tree.get(d);
			bl.getLocation().getWorld().playEffect(bl.getLocation().add(0.5, 0.5, 0.5), Effect.HAPPY_VILLAGER, 0);
			bl.setType(Material.AIR);
		}
		
		return amountBroken;
	}

	private ItemStack updateWand(ItemStack wand, int mined, boolean flop){
		List<String> lore = wand.getItemMeta().getLore();
		if(flop){
			if(lore.get(0).contains("SELL")){
				lore.set(0, ChatColor.GRAY + "Mode: " + ChatColor.GREEN + "GATHER");
			}else{
				lore.set(0, ChatColor.GRAY + "Mode: " + ChatColor.GOLD + "SELL");
			}
		}
		if(lore.size()>=3){
			int totalMined = (Integer.parseInt(ChatColor.stripColor(lore.get(2).split(" ")[2])) + mined);
			double value = (((int)((totalMined > 2500000 ? 2500000 : totalMined)/500.0)))/500.0;
			value = value*100;
			value = Math.round(value);
			value = value/100;
			lore.set(1, ChatColor.GRAY + "Sugarcane Value:" + ChatColor.GOLD + " $" + (DecimateCore.getCore().getDecimateConfig().getCostOfSugarcane()+value) + ChatColor.GRAY + 
					" (" + ChatColor.GREEN + "+$" + value + ChatColor.GRAY + ")");
			lore.set(2, ChatColor.GRAY + "Sugarcane Harvested: " + ChatColor.YELLOW + totalMined);
		}else{	
			lore.add(1, ChatColor.GRAY + "Sugarcane Value:" + ChatColor.GOLD + " $" + DecimateCore.getCore().getDecimateConfig().getCostOfSugarcane() + ChatColor.GRAY + 
					" (" + ChatColor.GREEN + "+$0.00" + ChatColor.GRAY + ")");
			lore.add(2, ChatColor.GRAY + "Sugarcane Harvested: " + ChatColor.YELLOW + "0");
		}
		ItemMeta im = wand.getItemMeta();
		im.setLore(lore);
		wand.setItemMeta(im);
		return wand;
	}
	
	public void giveHarvester(Player player) {
		if(player.getInventory().firstEmpty() != -1){
			player.getInventory().addItem(harvesterItem);
		}else{
			player.getWorld().dropItem(player.getLocation(), harvesterItem);
			player.sendMessage(ChatColor.YELLOW + "Inventory full. Harvester Item dropped beneath you.");
		}
	}

	public ItemStack getHarvesterItem(boolean sell) {
		return updateWand(harvesterItem.clone(), 0, !sell);
	}

	@Override
	public void disable() {
		
	}

}
