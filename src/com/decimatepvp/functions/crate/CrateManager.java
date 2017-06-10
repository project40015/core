package com.decimatepvp.functions.crate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.decimatepvp.functions.crate.crates.DecimateCrate;
import com.decimatepvp.functions.crate.crates.GodCrate;
import com.decimatepvp.functions.crate.crates.SummerCrate;
import com.decimatepvp.functions.crate.crates.VoteCrate;
import com.decimatepvp.functions.crate.rewards.CashReward;
import com.decimatepvp.functions.crate.rewards.CommandReward;
import com.decimatepvp.utils.Skull;

public class CrateManager implements Manager, Listener {

	private List<Crate> crates = new ArrayList<>();
	
	private int run, runB, i = 1;
	
	public CrateManager(){
		loadCrates();
		startTimer();
		startSeasonalTimer();
	}
	
	private void loadCrates(){
		GodCrate godCrate = setupGodCrate();
		VoteCrate voteCrate = setupVoteCrate();
		DecimateCrate decimateCrate = setupDecimateCrate();
		SummerCrate summerCrate = setupSummerCrate();
		
		this.crates.add(godCrate);
		this.crates.add(voteCrate);
		this.crates.add(decimateCrate);
		this.crates.add(summerCrate);
	}
	
	private SummerCrate setupSummerCrate(){
		return new SummerCrate(Arrays.asList(
				new CommandReward("Summer Kit (PERM)", Skull.getPlayerSkull("MHF_Present2"), Rarity.EPIC, 14, "manuaddp %player% essentials.kits.summer", true,
						ChatColor.GRAY + "Gives you access to " + ChatColor.GOLD + "Summer Kit" + ChatColor.GRAY + " (weekly):~ ~" + ChatColor.GRAY +
						"1 x " + ChatColor.GOLD + "Aestas's Helmet" + ChatColor.GRAY + ": Diamond helmet that absorbs 20% of damage while you are in the sun.",
						ChatColor.GRAY + "You have unlocked the " + ChatColor.GOLD + "Summer " + ChatColor.GRAY + "kit!"),
				new CommandReward("Decimate Rank", Skull.getPlayerSkull("MHF_Present1"), Rarity.MYTHICAL, 1, "manuadd %player% decimate", true, ChatColor.GRAY +
						"Receive the " + DecimateCore.getCore().getColoredDecimate() + ChatColor.GRAY + " rank!",
						ChatColor.GRAY + "You have unlocked the " + DecimateCore.getCore().getColoredDecimate() + ChatColor.GRAY + " rank!"),
				new CommandReward("Blaze Spawner (4)", new ItemStack(Material.MOB_SPAWNER), Rarity.RARE, 25, "es give %player% BLAZE 0 4", false),
				new CommandReward("Iron Golem Spawner (2)", new ItemStack(Material.MOB_SPAWNER), Rarity.RARE, 20, "es give %player% IRON_GOLEM 0 2", false),
				new CashReward("$4,000,000", new ItemStack(Material.PAPER), Rarity.COMMON, 40, 4000000) 
				));
	}
	
	private GodCrate setupGodCrate(){
		return new GodCrate(Arrays.asList(
				new CashReward("$100,000", new ItemStack(Material.PAPER), Rarity.COMMON, 100, 100000)
				));
	}
	
	private DecimateCrate setupDecimateCrate(){
		return new DecimateCrate(Arrays.asList(
				new CommandReward("Blaze Spawner (3)", new ItemStack(Material.MOB_SPAWNER), Rarity.RARE, 25, "es give %player% BLAZE 0 3", false),
				new CommandReward("Iron Golem Spawner (2)",new ItemStack(Material.MOB_SPAWNER), Rarity.RARE, 22, "es give %player% IRON_GOLEM 0 2", false),
				new CommandReward("Creeper Spawner (2)", new ItemStack(Material.MOB_SPAWNER), Rarity.RARE, 22, "es give %player% CREEPER 0 2", false),
				new CommandReward("Decimate Rank", Skull.getPlayerSkull("MHF_Present1"), Rarity.MYTHICAL, 1, "manuadd %player% decimate", true, ChatColor.GRAY +
						"Receive the " + DecimateCore.getCore().getColoredDecimate() + ChatColor.GRAY + " rank!",
						ChatColor.GRAY + "You have unlocked the " + DecimateCore.getCore().getColoredDecimate() + ChatColor.GRAY + " rank!"),
				new CommandReward("Decimate Kit (1)", new ItemStack(Material.BOOK), Rarity.COMMON, 30, "kit decimate %player%", true)
				));
	}
	
	private VoteCrate setupVoteCrate(){
		return new VoteCrate(Arrays.asList(
				new CashReward("$10,000", new ItemStack(Material.PAPER), Rarity.COMMON, 100, 10000)
				));
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
	
	private void startTimer(){
		final int t = 2;
		run = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(DecimateCore.getCore(), new Runnable(){

			@Override
			public void run() {
				double x = 0, z = 0;
				i = i >= 40 ? 1 : i + 1;
				if(i <= 10){
					x = i/10.0;
				}else if(i <= 20){
					x = 1;
					z = (i-10)/10.0;
				}else if(i <= 30){
					x = 1 - ((i-20)/10.0);
					z = 1;
				}else{
					z = 1 - ((i-30)/10.0);
				}
				for(int i = 0; i < crates.size(); i++){
					if(crates.get(i).hasGroundEffect()){
						crates.get(i).getGroundEffect().display(0, 0, 0, 0, 1, crates.get(i).getLocation().clone().add(x, 0.05, z), 30);
					}
				}
			}
			
		}, t, t);
	}
	
	private void startSeasonalTimer(){
		runB = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(DecimateCore.getCore(), new Runnable(){

			@Override
			public void run() {
				for(int i = 0; i < crates.size(); i++){
					if(crates.get(i).isSeasonal()){
						if(crates.get(i).isOver()){
							crates.get(i).clearStands();
							crates.get(i).disable();
							crates.remove(i--);
						}else{
							crates.get(i).setTimeString(ChatColor.GRAY + formatTimeString(crates.get(i).getOver()));
						}
					}
				}
			}
			
		}, 20, 20);
	}
	
	private String formatTimeString(Date then){
		long time = then.getTime() - System.currentTimeMillis();
		
		int seconds = (int) ((time / 1000) % 60);
		int minutes = (int) ((time / (1000*60)) % 60);
		int hours   = (int) ((time / (1000*60*60)) % 24);
		int days = (int) ((time / (1000*60*60*24)));
		
		return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
			for(Crate crate : crates){
				if(crate.getLocation().getBlock().getLocation().equals(event.getClickedBlock().getLocation())){
					if(crate.isComingSoon()){
						event.getPlayer().sendMessage("");
						event.getPlayer().sendMessage(crate.getName() + ChatColor.GRAY + " is coming soon to " + ChatColor.DARK_AQUA.toString() + ChatColor.UNDERLINE + "shop.decimatepvp.com" + ChatColor.GRAY + "!");
						event.getPlayer().sendMessage("");
					}else{
						if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
							tryToOpen(event.getPlayer(), event.getClickedBlock().getLocation(), crate);
						}else{
							viewRewards(event.getPlayer(), crate);
						}
					}
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onInteractAtEntity(PlayerInteractAtEntityEvent event){
		if(event.getRightClicked() instanceof ArmorStand){
			ArmorStand stand = (ArmorStand) event.getRightClicked();
			for(Crate crate : this.crates){
				if(crate.getNameStand().equals(stand)){
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof ArmorStand){
			ArmorStand stand = (ArmorStand) event.getEntity();
			for(Crate crate : this.crates){
				if(crate.getNameStand().equals(stand)){
					event.setCancelled(true);
				}
			}
		}
	}
	
	private boolean isOpening(String title){
		for(Crate crate : this.crates){
			if(ChatColor.stripColor(crate.getName()).equals(ChatColor.stripColor(title))){
				return true;
			}
		}
		return false;
	}
	
	private CrateReward getCrateReward(Crate crate, ItemStack item){
		for(CrateReward cr : crate.getRewards()){
			if(cr.getIcon(1).getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())){
				return cr;
			}
		}
		return null;
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event){
		if(!(event.getWhoClicked() instanceof Player)){
			return;
		}
		Player p = (Player) event.getWhoClicked();
		for(Crate crate : crates){
			if(event.getInventory().equals(crate.getRewardPage())){
				CrateReward cr = getCrateReward(crate, event.getCurrentItem());
				if(cr != null){
					if(!cr.getDescription().equals("")){
						String[] msgs = cr.getDescription().split("~");
						for(String s : msgs){
							p.sendMessage(s);
						}
					}
				}
				event.setCancelled(true);
				return;
			}
		}
		if(isOpening(event.getInventory().getTitle())){
			event.setCancelled(true);
			return;
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
		if(Bukkit.getServer().getScheduler().isCurrentlyRunning(run)){
			Bukkit.getServer().getScheduler().cancelTask(run);
		}
		if(Bukkit.getServer().getScheduler().isCurrentlyRunning(runB)){
			Bukkit.getServer().getScheduler().cancelTask(runB);
		}
		for(Crate crate : this.crates){
			crate.clearStands();
			crate.disable();
		}
		crates.clear();
	}

}
