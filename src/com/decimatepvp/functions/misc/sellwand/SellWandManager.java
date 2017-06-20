package com.decimatepvp.functions.misc.sellwand;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.decimatepvp.utils.FactionUtils;

public class SellWandManager implements Manager, Listener {

	private List<SellableItem> sellableItems = new ArrayList<>();
	private ItemStack wand;
	
	public SellWandManager(){
		fillSellableItems();
		setupWand();
	}
	
	private void fillSellableItems(){
		this.sellableItems.add(new SellableItem(Material.ROTTEN_FLESH, 1.56));
		this.sellableItems.add(new SellableItem(Material.BONE, 1.56));
		this.sellableItems.add(new SellableItem(Material.SULPHUR, 7.81));
		this.sellableItems.add(new SellableItem(Material.SPIDER_EYE, 1.64));
		this.sellableItems.add(new SellableItem(Material.ENDER_PEARL, 15.31));
		this.sellableItems.add(new SellableItem(Material.BLAZE_ROD, 17.19));
		this.sellableItems.add(new SellableItem(Material.LEATHER, 1.56));
		this.sellableItems.add(new SellableItem(Material.COOKED_BEEF, 3.51));
		this.sellableItems.add(new SellableItem(Material.ARROW, 1.00));
		this.sellableItems.add(new SellableItem(Material.RED_ROSE, 1.00));
		this.sellableItems.add(new SellableItem(Material.STRING, 1.56));
		this.sellableItems.add(new SellableItem(Material.FEATHER, 1.00));
		this.sellableItems.add(new SellableItem(Material.EGG, 4.00));
		this.sellableItems.add(new SellableItem(Material.IRON_INGOT, 26.00));
		this.sellableItems.add(new SellableItem(Material.GOLD_INGOT, 24.00));
		this.sellableItems.add(new SellableItem(Material.RAW_CHICKEN, 1.00));
		this.sellableItems.add(new SellableItem(Material.CACTUS, 4.29));
		this.sellableItems.add(new SellableItem(Material.SUGAR_CANE, 5.08));

	}
	
	private void setupWand(){
		wand = new ItemStack(Material.DIAMOND_HOE);
		ItemMeta wandMeta = wand.getItemMeta();
		wandMeta.setDisplayName(ChatColor.GOLD + "Sell Wand");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GRAY + "Left click a chest to sell:");
		for(SellableItem si : sellableItems){
			lore.add(ChatColor.GREEN + si.getMaterial().toString().toLowerCase().replaceAll("_", " ") + " ($" + si.getCost() + ")");
		}
		wandMeta.setLore(lore);
		wand.setItemMeta(wandMeta);
	}
	
	public void giveSellWand(Player player){
		if(player.getInventory().firstEmpty() != -1){
			player.getInventory().addItem(this.wand.clone());
		}else{
			player.getWorld().dropItem(player.getLocation(), this.wand.clone());
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event){
		if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
			if(event.getItem() != null && event.getItem().getItemMeta() != null
					&& event.getItem().getItemMeta().getDisplayName() != null &&
					event.getItem().getItemMeta().getDisplayName().equals(wand.getItemMeta().getDisplayName())){
				Block block = event.getClickedBlock();
				if(!FactionUtils.getFactionByLoc(block.getLocation()).equals(FactionUtils.getFaction(event.getPlayer()))){
					event.getPlayer().sendMessage(ChatColor.RED + "You can only use a sell wand in your territory.");
					return;
				}
//				if(!FactionUtils.isOwner(event.getPlayer(), block.getLocation())){
//					event.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to sell an inventory in this claim.");
//					return;
//				}
				if(block.getState() instanceof Chest){
					BlockFace[] faces = new BlockFace[] {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
					Chest chest = (Chest) block.getState();
					Inventory nextTo = null;
					for(BlockFace face : faces){
						if(block.getRelative(face).getType().equals(block.getType()) && block.getRelative(face).getState() instanceof Chest){
							nextTo = ((Chest)block.getRelative(face).getState()).getBlockInventory();
							break;
						}
					}
					if(nextTo == null){
						work(event.getPlayer(), chest.getBlockInventory());
					}else{
						work(event.getPlayer(), chest.getBlockInventory(), nextTo);
					}
				}
				if(block.getState() instanceof Hopper){
					Hopper hopper = (Hopper) block.getState();
					work(event.getPlayer(), hopper.getInventory());
				}
			}
		}
	}
	
	private void work(Player player, Inventory... invs){
		double total = 0;
		int totalItems = 0;
		
		for(Inventory inventory : invs){
			for(int i = 0; i < inventory.getSize(); i++){
				if(inventory.getItem(i) != null &&
						isSellable(inventory.getItem(i).getType())){
					total += getValue(inventory.getItem(i));
					totalItems += inventory.getItem(i).getAmount();
					inventory.setItem(i, new ItemStack(Material.AIR));
				}
			}
		}
		if(total > 0){
			DecimateCore.getCore().eco.depositPlayer(player, total);
			total = total*100;
			int fixed = (int) total;
			total = fixed/100.0;
			player.sendMessage(ChatColor.GREEN + "Sold " + totalItems + " item(s) for $" + total + "!");
		}
	}
	
	private boolean isSellable(Material material){
		for(SellableItem si : this.sellableItems){
			if(si.getMaterial().equals(material)){
				return true;
			}
		}
		return false;
	}
	
	private double getValue(ItemStack item){
		for(SellableItem si : this.sellableItems){
			if(si.getMaterial().equals(item.getType())){
				return si.getCost() * item.getAmount();
			}
		}
		return 0;
	}
	
	@Override
	public void disable() {
		
	}

	
	
}
