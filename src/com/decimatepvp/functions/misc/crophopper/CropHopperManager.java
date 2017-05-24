package com.decimatepvp.functions.misc.crophopper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.decimatepvp.utils.PlayerUtils;

public class CropHopperManager implements Listener, Manager {

	private List<CropHopper> hoppers = new ArrayList<>();
	
	private YamlConfiguration data;
	
	private ItemStack cropStack, mobStack;
	
	public CropHopperManager(){
		createStacks();
		loadData();
		loadHoppers();
	}
	
	private void loadHoppers(){
		loadData();
		List<String> hoppers = data.getStringList("data.hoppers");
		for(String st : hoppers){
			this.hoppers.add(new CropHopper(st));
		}
	}
	
	private void loadData(){
		data = YamlConfiguration.loadConfiguration(new File(DecimateCore.getCore().getDataFolder().getPath() + "/data", "hopper.yml"));
		
		for(String str : data.getStringList("data.hoppers")){
			hoppers.add(new CropHopper(str));
		}
	}
	
	private void saveHoppers(){
		if(hoppers.size() == 0){
			return;
		}
		data.getStringList("data.hoppers").add(hoppers.get(0).toString());
		hoppers.remove(0);
		saveHoppers();
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
			if(hopper.getLocation().getChunk().equals(chunk) && hopper.isCrop() == crop){
				return true;
			}
		}
		return false;
	}
	
	public CropHopper getCropHopper(Chunk chunk, boolean crop){
		for(CropHopper hopper : hoppers){
			if(hopper.getLocation().getChunk().equals(chunk) && hopper.isCrop() == crop){
				return hopper;
			}
		}
		return null;
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
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event){
		ItemStack fake = event.getItemInHand();
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
	public void onSpawn(EntitySpawnEvent event){
		if(event.getEntity() instanceof Item){
			Item i = (Item) event.getEntity();
			Material type = i.getItemStack().getType();
			if(type == Material.CACTUS){
				event.setCancelled(fill(i.getLocation(), i.getItemStack(), true));
			}
			if(type == Material.ROTTEN_FLESH || type == Material.IRON_INGOT
					 || type == Material.BONE || type == Material.ARROW ||
					 type == Material.RAW_BEEF || type == Material.LEATHER ||
					 type == Material.PORK){
				event.setCancelled(fill(i.getLocation(), i.getItemStack(), false));
			}
		}
	}
	
	private boolean fill(Location location, ItemStack item, boolean crop){
		if(this.isCropHopper(location.getChunk(), crop)){
			CropHopper ch = this.getCropHopper(location.getChunk(), crop); 
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
		return false;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR)){
			ItemStack fake = event.getItem().clone();
			fake.setAmount(1);
			if(fake.equals(cropStack)){
				ItemStack nF = mobStack.clone();
				nF.setAmount(event.getPlayer().getItemInHand().getAmount());
				event.getPlayer().setItemInHand(nF);
				
				event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 1, 1);
				PlayerUtils.sendActionbar(event.getPlayer(), ChatColor.YELLOW + "Switched to mob hopper.");
			}else if(fake.equals(mobStack)){
				ItemStack nF = cropStack.clone();
				nF.setAmount(event.getPlayer().getItemInHand().getAmount());
				event.getPlayer().setItemInHand(nF);
				
				event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 1, 1);
				PlayerUtils.sendActionbar(event.getPlayer(), ChatColor.YELLOW + "Switched to crop hopper.");
			}
		}
	}
	
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
//		for(Player player : Bukkit.getServer().getOnlinePlayers()){
//			if(player.isOp()){
//				player.sendMessage(ChatColor.YELLOW + "Hoppers saving.");
//			}
//		}
//		if(!data.contains("data.hoppers")){
//			data.createSection("data.hoppers");
//		}
//		if(data.getString("data.hoppers") != null){
//			data.getList("data.hoppers").clear();
//		}
//		saveHoppers();
	}
	
}
