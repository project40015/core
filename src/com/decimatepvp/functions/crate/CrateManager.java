package com.decimatepvp.functions.crate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.Manager;
import com.decimatepvp.functions.crate.crates.DecimateCrate;
import com.decimatepvp.functions.crate.crates.GodCrate;
import com.decimatepvp.functions.crate.crates.VoteCrate;
import com.decimatepvp.functions.crate.rewards.CashReward;
import com.decimatepvp.functions.crate.rewards.CommandReward;
import com.decimatepvp.functions.crate.rewards.ItemReward;

public class CrateManager implements Manager, Listener {

	private List<Crate> crates = new ArrayList<>();
	private List<CrateReward> rewards = new ArrayList<>();
	
	private GodCrate godCrate;
	private VoteCrate voteCrate;
	private DecimateCrate decimateCrate;
	
	public CrateManager(){
		loadCrates();
	}
	
	private void loadCrates(){
		ItemReward coal = new ItemReward(new ItemStack(Material.COAL, 10), Rarity.COMMON, 200);
		ItemReward iron = new ItemReward(new ItemStack(Material.IRON_INGOT, 10), Rarity.COMMON, 100);
		ItemReward gold = new ItemReward(new ItemStack(Material.GOLD_INGOT, 10), Rarity.RARE, 50);
		ItemReward diamond = new ItemReward(new ItemStack(Material.DIAMOND, 10), Rarity.EPIC, 10);
		ItemReward emerald = new ItemReward(new ItemStack(Material.EMERALD, 10), Rarity.MYTHICAL, 5);
		CashReward tenk = new CashReward("$10,000", new ItemStack(Material.PAPER), Rarity.COMMON, 100, 10000);
		CommandReward harvester = new CommandReward("Harvester Hoe", new ItemStack(Material.DIAMOND_HOE), Rarity.MYTHICAL, 10, "harvester %player%");
		CommandReward cropHopper = new CommandReward("Crop Hopper", new ItemStack(Material.HOPPER), Rarity.MYTHICAL, 10, "crophopper %player%");

		rewards.add(coal);
		rewards.add(iron);
		rewards.add(gold);
		rewards.add(diamond);
		rewards.add(emerald);
		rewards.add(tenk);
		rewards.add(harvester);
		rewards.add(cropHopper);
		
		godCrate = new GodCrate(Arrays.asList(tenk, gold, diamond, emerald, harvester));
		voteCrate = new VoteCrate(Arrays.asList(coal, iron, gold, harvester));
		decimateCrate = new DecimateCrate(Arrays.asList(diamond, emerald, harvester, cropHopper));
		
		this.crates.add(godCrate);
		this.crates.add(voteCrate);
		this.crates.add(decimateCrate);
	}
	
	public boolean isCrate(String name){
		for(Crate crate : this.crates){
			if(ChatColor.stripColor(crate.getName()).replaceAll(" ", "_").equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
	}
	
	public String cratesString(){
		String str = "";
		for(Crate crate : this.crates){
			str += ChatColor.stripColor(crate.getName()).toLowerCase().replaceAll(" ", "_") + " ";
		}
		return str.trim();
	}
	
	public Crate getCrate(String name){
		for(Crate crate : this.crates){
			if(ChatColor.stripColor(crate.getName()).replaceAll(" ", "_").equalsIgnoreCase(name)){
				return crate;
			}
		}
		return null;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if(event.getClickedBlock().getType().equals(Material.CHEST) &&
					event.getClickedBlock().getLocation().clone().add(0,-1,0).getBlock().getType().equals(Material.IRON_BLOCK)){
				tryToOpen(event.getPlayer(), event.getClickedBlock().getLocation(), voteCrate);
				event.setCancelled(true);
			}else if(event.getClickedBlock().getType().equals(Material.CHEST) &&
					event.getClickedBlock().getLocation().clone().add(0,-1,0).getBlock().getType().equals(Material.GOLD_BLOCK)){
				tryToOpen(event.getPlayer(), event.getClickedBlock().getLocation(), godCrate);
				event.setCancelled(true);
			}else if(event.getClickedBlock().getType().equals(Material.CHEST) &&
					event.getClickedBlock().getLocation().clone().add(0,-1,0).getBlock().getType().equals(Material.EMERALD_BLOCK)){
				tryToOpen(event.getPlayer(), event.getClickedBlock().getLocation(), decimateCrate);
				event.setCancelled(true);
			}
		}else if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
			if(event.getClickedBlock().getType().equals(Material.CHEST) &&
					event.getClickedBlock().getLocation().clone().add(0,-1,0).getBlock().getType().equals(Material.IRON_BLOCK)){
				viewRewards(event.getPlayer(), voteCrate);
				event.setCancelled(true);
			}else if(event.getClickedBlock().getType().equals(Material.CHEST) &&
					event.getClickedBlock().getLocation().clone().add(0,-1,0).getBlock().getType().equals(Material.GOLD_BLOCK)){
				viewRewards(event.getPlayer(), godCrate);
				event.setCancelled(true);
			}else if(event.getClickedBlock().getType().equals(Material.CHEST) &&
					event.getClickedBlock().getLocation().clone().add(0,-1,0).getBlock().getType().equals(Material.EMERALD_BLOCK)){
				viewRewards(event.getPlayer(), decimateCrate);
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event){
		if(!(event.getWhoClicked() instanceof Player)){
			return;
		}
		if(event.getInventory().equals(voteCrate.getRewardPage()) ||
				event.getInventory().equals(godCrate.getRewardPage())){
			event.setCancelled(true);
		}
	}
	
	private void viewRewards(Player player, Crate crate){
		player.openInventory(crate.getRewardPage());
	}
	
	private void tryToOpen(Player player, Location location, Crate crate){
		if(!crate.isCrateKey(player.getItemInHand())){
			player.sendMessage(ChatColor.RED + "You must be holding a crate key.");
			return;
		}
		if(!crate.open(player, crate.reward(), location)){
			player.sendMessage(ChatColor.RED + "This crate is already being opened.");
			return;
		}
		if(player.getItemInHand().getAmount() > 1){
			ItemStack n = player.getItemInHand();
			n.setAmount(n.getAmount()-1);
			player.setItemInHand(n);
		}else{
			player.setItemInHand(new ItemStack(Material.AIR));
		}
	}
	
	@Override
	public void disable() {
		for(Crate crate : this.crates){
			crate.disable();
		}
		crates.clear();
		rewards.clear();
	}

}
