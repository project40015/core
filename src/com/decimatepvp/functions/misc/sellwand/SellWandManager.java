package com.decimatepvp.functions.misc.sellwand;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;

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
		this.sellableItems.add(new SellableItem(Material.CACTUS, 12.50));
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
				if(block.getState() instanceof Chest){
					Chest chest = (Chest) block.getState();
					double total = 0;
					int totalItems = 0;
					for(int i = 0; i < chest.getBlockInventory().getSize(); i++){
						if(chest.getBlockInventory().getItem(i) != null &&
								isSellable(chest.getBlockInventory().getItem(i).getType())){
							total += getValue(chest.getBlockInventory().getItem(i));
							totalItems += chest.getBlockInventory().getItem(i).getAmount();
							chest.getBlockInventory().setItem(i, new ItemStack(Material.AIR));
						}
					}
					if(total > 0){
						DecimateCore.getCore().eco.depositPlayer(event.getPlayer(), total);
						total = total*100;
						int fixed = (int) total;
						total = fixed/100.0;
						event.getPlayer().sendMessage(ChatColor.GREEN + "Sold " + totalItems + " item(s) for $" + total + "!");
					}
				}
			}
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
