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
import com.decimatepvp.utils.Skull;

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
		CashReward tenk = new CashReward("$10,000", new ItemStack(Material.PAPER), Rarity.COMMON, 100, 10000);
		
		CrateReward blazeSpawner3 = new CommandReward("Blaze Spawner (3)", new ItemStack(Material.MOB_SPAWNER), Skull.BLAZE.getSkull(), Rarity.RARE, 25, "es give %player% BLAZE 0 3");
		CrateReward ironSpawner2 = new CommandReward("Iron Golem Spawner (2)", new ItemStack(Material.MOB_SPAWNER), Skull.GOLEM.getSkull(), Rarity.RARE, 22, "es give %player% IRON_GOLEM 0 2");
		CrateReward creeperSpawner2 = new CommandReward("Creeper Spawner (2)", new ItemStack(Material.MOB_SPAWNER), Skull.getPlayerSkull("MHF_Creeper"), Rarity.RARE, 22, "es give %player% CREEPER 0 2");
		CrateReward decimateRank = new CommandReward("Decimate Rank", new ItemStack(Material.BOOK), Skull.getPlayerSkull("FarmKiteCarry"), Rarity.MYTHICAL, 1, "manuadd %player% decimate");
		CrateReward decimateKit = new CommandReward("Decimate Kit", new ItemStack(Material.BOOK), new ItemStack(Material.DIAMOND_HELMET), Rarity.COMMON, 30, "kit decimate %player%");


		rewards.add(tenk);
		
		godCrate = new GodCrate(Arrays.asList(tenk));
		voteCrate = new VoteCrate(Arrays.asList(tenk));
		decimateCrate = new DecimateCrate(Arrays.asList(blazeSpawner3, ironSpawner2, creeperSpawner2, decimateRank, decimateKit));
		
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
