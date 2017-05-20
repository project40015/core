package com.decimatepvp.functions.misc.crophopper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CropHopperManager implements Listener {

	private List<CropHopper> hoppers = new ArrayList<>();
	
	private ItemStack stack;
	
	public CropHopperManager(){
		createStack();
	}
	
	public void addHopper(CropHopper hopper){
		this.hoppers.add(hopper);
	}
	
	public void removeHopper(CropHopper hopper){
		this.hoppers.remove(hopper);
	}
	
	public boolean isCropHopper(Location location){
		for(CropHopper hopper : hoppers){
			if(hopper.getLocation().equals(location)){
				return true;
			}
		}
		return false;
	}
	
	public CropHopper getCropHopper(Location location){
		for(CropHopper hopper : hoppers){
			if(hopper.getLocation().equals(location)){
				return hopper;
			}
		}
		return null;
	}
	
	public boolean isCropHopper(Chunk chunk){
		for(CropHopper hopper : hoppers){
			if(hopper.getLocation().getChunk().equals(chunk)){
				return true;
			}
		}
		return false;
	}
	
	private void createStack(){
		ItemStack stack = new ItemStack(Material.HOPPER);
		ItemMeta sm = stack.getItemMeta();
		sm.setDisplayName(ChatColor.DARK_GREEN + "Crop Hopper");
		sm.setLore(Arrays.asList("", ChatColor.GRAY + "Collects all dropped cactus in a chunk."));
		stack.setItemMeta(sm);
		this.stack = stack;
	}
	
	public ItemStack getCropHopperItemStack(){
		return stack;
	}
	
	@EventHandler
	public void onSpawn(EntitySpawnEvent event){
		if(event.getEntity() instanceof Item){
			Item i = (Item) event.getEntity();
			if(i.getItemStack().getType() == Material.CACTUS){
				
			}
		}
	}
	
	@EventHandler
	public void onExplode(EntityExplodeEvent event){
		for(int i = 0; i < event.blockList().size(); i++){
			Block b = event.blockList().get(i);
			if(b.getType() == Material.HOPPER){
				if(isCropHopper(b.getLocation())){
					removeHopper(getCropHopper(b.getLocation()));
					b.getWorld().dropItemNaturally(b.getLocation(), stack);
					event.blockList().remove(i--);
				}
			}
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event){
		if(event.getBlock().getType() == Material.HOPPER){
			Location location = event.getBlock().getLocation();
			if(this.isCropHopper(location)){
				this.removeHopper(this.getCropHopper(location));
				location.getWorld().dropItemNaturally(location, stack);
				location.getBlock().setType(Material.AIR);
				event.setCancelled(true);
			}
		}
	}
	
}
