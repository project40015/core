package com.decimatepvp.functions.misc.crophopper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.decimatepvp.core.utils.Configuration;
import com.google.common.collect.Lists;

public class CropHopperManager implements Listener, Manager {

	private List<CropHopper> hoppers = new ArrayList<>();
	
	private ItemStack cropStack, mobStack;
	
	public CropHopperManager(){
		createStacks();
		loadHoppers();
	}
	
	private void loadHoppers(){
		FileConfiguration config = new Configuration(DecimateCore.getCore(), "/crophoppers", "hoppers.yml").getData();
		
		for(String data : config.getStringList("data.hoppers")) {
			this.hoppers.add(new CropHopper(data));
		}
	}
	
	public void saveHoppers(){
		Configuration cfg = new Configuration(DecimateCore.getCore(), "/crophoppers", "hoppers.yml");
		FileConfiguration config = cfg.getData();
		
		List<String> data = Lists.newArrayList();
		for(CropHopper ch : hoppers) {
			data.add(ch.toString());
		}
		
		config.set("data.hoppers", data);
		
		cfg.saveData();
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
	
	public boolean isCropHopper(Chunk chunk, boolean crop){
		for(CropHopper hopper : hoppers){
			if(hopper.isCrop() == crop && hopper.getLocation().getChunk().equals(chunk)){
				return true;
			}
		}
		return false;
	}
	
	public CropHopper getCropHopper(Material drop, int amount, Chunk chunk, boolean crop){
		for(int i = 0; i < hoppers.size(); i++){
			CropHopper hopper = hoppers.get(i);
			if(hopper.getLocation().getChunk().equals(chunk) && hopper.isCrop() == crop){
				if(!(hopper.getLocation().getBlock().getState() instanceof Hopper)){
					hoppers.remove(i--);
					continue;
				}
				Hopper rHopper = (Hopper) hopper.getLocation().getBlock().getState();
				int space = getSpace(drop, rHopper);
				if(space == 0){
					continue;
				}
				if(space >= amount){
					return hopper;
				}
			}
		}
		return null;
	}
	
	private int getSpace(Material drop, Hopper hopper){
		Inventory inv = hopper.getInventory();
		int space = 0;
		for(int i = 0; i < inv.getSize(); i++){
			if(inv.getItem(i) != null && drop.equals(inv.getItem(i).getType())){
				if(inv.getItem(i).getAmount() != drop.getMaxStackSize()){
					space += drop.getMaxStackSize() - inv.getItem(i).getAmount();
				}
			}else if(inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)){
				space += 64;
			}
		}
		return space;
	}
	
	private void createStacks(){
		ItemStack stack = new ItemStack(Material.HOPPER);
		ItemMeta sm = stack.getItemMeta();
		sm.setDisplayName(ChatColor.DARK_GREEN + "Crop Hopper");
		sm.setLore(Arrays.asList("", ChatColor.GRAY + "Collects all dropped cactus in a chunk."));
		stack.setItemMeta(sm);
		this.cropStack = stack;
		
		ItemStack mstack = new ItemStack(Material.HOPPER);
		ItemMeta msm = mstack.getItemMeta();
		msm.setDisplayName(ChatColor.RED + "Mob Hopper");
		msm.setLore(Arrays.asList("", ChatColor.GRAY + "Collects all mob drops in a chunk."));
		mstack.setItemMeta(msm);
		this.mobStack = mstack;
	}
	
	public ItemStack getCropHopperItemStack(){
		return cropStack;
	}
	
	public void giveCropHopper(Player player, boolean crop){
		if(player.getInventory().firstEmpty() == -1){
			player.getLocation().getWorld().dropItemNaturally(player.getLocation(), cropStack);
		}else{
			player.getInventory().addItem(cropStack);
		}
	}
	
	public void giveCropHopperHand(Player player, boolean crop, int amount){
		ItemStack clone = crop ? cropStack.clone() : mobStack.clone();
		clone.setAmount(amount);
		player.getInventory().setItemInHand(clone);
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event){
		ItemStack fake = event.getItemInHand().clone();
		fake.setAmount(1);
		if(fake.equals(cropStack)){
			CropHopper hopper = new CropHopper(event.getBlock().getLocation(), true);
			this.addHopper(hopper);
			event.getPlayer().sendMessage(ChatColor.YELLOW + "You placed a crop hopper!");
		}else if(fake.equals(mobStack)){
			CropHopper hopper = new CropHopper(event.getBlock().getLocation(), false);
			this.addHopper(hopper);
			event.getPlayer().sendMessage(ChatColor.YELLOW + "You placed a mob hopper!");
		}
	}
	
	@EventHandler
	public void onSpawn(ItemSpawnEvent event){
//		if(event.getEntity() instanceof Item){
//			Item i = (Item) event.getEntity();
			Material type = event.getEntity().getItemStack().getType();
			if(type == Material.CACTUS){
				event.setCancelled(fill(event.getEntity().getLocation(), event.getEntity().getItemStack(), true));
			}
			if(type == Material.ROTTEN_FLESH || type == Material.IRON_INGOT
					 || type == Material.BONE || type == Material.ARROW ||
					 type == Material.RAW_BEEF || type == Material.LEATHER ||
					 type == Material.PORK || type == Material.SULPHUR ||
					 type == Material.TNT || type == Material.STRING ||
					 type == Material.SPIDER_EYE || type == Material.RAW_CHICKEN ||
					 type == Material.EGG || type == Material.RED_ROSE ||
					 type == Material.GOLD_NUGGET){
				event.setCancelled(fill(event.getEntity().getLocation(), event.getEntity().getItemStack(), false));
			}
//		}
	}
	
	private boolean fill(Location location, ItemStack item, boolean crop){
		CropHopper ch = this.getCropHopper(item.getType(), item.getAmount(), location.getChunk(), crop); 
		if(ch != null){
			Block b = ch.getLocation().getBlock();
			if(b.getState() instanceof Hopper){
				Hopper h = (Hopper) b.getState();
				h.getInventory().addItem(item);
				if(crop){
					h.getWorld().playEffect(h.getBlock().getLocation().clone().add(0.5, 1, 0.5), Effect.HAPPY_VILLAGER, 1);
				}else{
					h.getWorld().playEffect(h.getBlock().getLocation().clone().add(0.5, 1, 0.5), Effect.COLOURED_DUST, 1);
				}
				return true;
			}else{
				this.removeHopper(ch);
			}
		}
		return this.isCropHopper(location.getChunk(), crop);
	}
	
//	@EventHandler
//	public void onInteract(PlayerInteractEvent event){
//		if(event.getAction().equals(Action.RIGHT_CLICK_AIR)){
//			ItemStack fake = event.getItem().clone();
//			fake.setAmount(1);
//			if(fake.equals(cropStack)){
//				ItemStack nF = mobStack.clone();
//				nF.setAmount(event.getPlayer().getItemInHand().getAmount());
//				event.getPlayer().setItemInHand(nF);
//				
//				event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 1, 1);
//				PlayerUtils.sendActionbar(event.getPlayer(), ChatColor.YELLOW + "Switched to mob hopper.");
//			}else if(fake.equals(mobStack)){
//				ItemStack nF = cropStack.clone();
//				nF.setAmount(event.getPlayer().getItemInHand().getAmount());
//				event.getPlayer().setItemInHand(nF);
//				
//				event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 1, 1);
//				PlayerUtils.sendActionbar(event.getPlayer(), ChatColor.YELLOW + "Switched to crop hopper.");
//			}
//		}
//	}
	
	@EventHandler
	public void onExplode(EntityExplodeEvent event){
		for(int i = 0; i < event.blockList().size(); i++){
			Block b = event.blockList().get(i);
			if(b.getType() == Material.HOPPER){
				if(isCropHopper(b.getLocation())){
					CropHopper ch = this.getCropHopper(b.getLocation());
					b.getWorld().dropItemNaturally(b.getLocation(), ch.isCrop() ? this.cropStack : this.mobStack);
					removeHopper(ch);
					b.setType(Material.AIR);
					event.blockList().remove(i--);
				}
			}
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event){
		if(event.getBlock().getType() == Material.HOPPER){
			if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
				return;
			}
			Location location = event.getBlock().getLocation();
			if(this.isCropHopper(location)){
				CropHopper ch = this.getCropHopper(location);
				location.getWorld().dropItemNaturally(location, ch.isCrop() ? this.cropStack : this.mobStack);
				this.removeHopper(ch);
				location.getBlock().setType(Material.AIR);
				event.setCancelled(true);
			}
		}
	}

	@Override
	public void disable() {
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(player.isOp()){
				player.sendMessage(ChatColor.YELLOW + "Hoppers saving.");
			}
		}
		
		saveHoppers();
	}
	
}
