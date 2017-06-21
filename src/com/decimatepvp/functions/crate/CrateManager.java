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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.decimatepvp.functions.crate.crates.DecimateCrate;
import com.decimatepvp.functions.crate.crates.GodCrate;
import com.decimatepvp.functions.crate.crates.SummerCrate;
import com.decimatepvp.functions.crate.crates.TypicalCrate;
import com.decimatepvp.functions.crate.crates.VoteCrate;
import com.decimatepvp.functions.crate.rewards.CashReward;
import com.decimatepvp.functions.crate.rewards.CommandReward;
import com.decimatepvp.functions.crate.rewards.ItemReward;
import com.decimatepvp.utils.EnchantGlow;
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
	
	@EventHandler
	public void onClose(InventoryCloseEvent event){
		if(!(event.getPlayer() instanceof Player)){
			return;
		}
		Player player = (Player) event.getPlayer();
		for(Crate crate : this.crates){
			if(crate instanceof TypicalCrate){
				TypicalCrate tp = (TypicalCrate) crate;
				tp.finish(player, true);
			}
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		for(Crate crate : this.crates){
			if(crate instanceof TypicalCrate){
				TypicalCrate tp = (TypicalCrate) crate;
				tp.finish(event.getPlayer(), true);
			}
		}
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event){
		for(Crate crate : this.crates){
			if(crate instanceof TypicalCrate){
				TypicalCrate tp = (TypicalCrate) crate;
				tp.finish(event.getPlayer(), true);
			}
		}	
	}
	
	private SummerCrate setupSummerCrate(){
		return new SummerCrate(Arrays.asList(
				new CommandReward("Summer Kit (PERM)", Skull.getPlayerSkull("MHF_Present2"), Rarity.EPIC, 14, "manuaddp %player% essentials.kits.summer", true,
						ChatColor.GRAY + "Gives you access to " + ChatColor.GOLD + "Summer Kit" + ChatColor.GRAY + " (47 hours):~ ~" + ChatColor.GRAY +
						"1 x " + ChatColor.GOLD + "Aestas's Helmet" + ChatColor.GRAY + ": Protection V unbreaking V diamond helmet that absorbs 20% of damage while you are in the sun.~" +
								ChatColor.GRAY + "1 x " + ChatColor.GOLD + "Summer Chestplate" + ChatColor.GRAY + ": Protection IV unbreaking III~" +
								ChatColor.GRAY + "1 x " + ChatColor.GOLD + "Summer Leggings" + ChatColor.GRAY + ": Protection IV unbreaking III~" +
								ChatColor.GRAY + "1 x " + ChatColor.GOLD + "Summer Boots" + ChatColor.GRAY + ": Protection IV unbreaking III~" +
								ChatColor.GRAY + "1 x " + ChatColor.GOLD + "Summer Sword" + ChatColor.GRAY + ": Sharpness V unbreaking III fire aspect II",
						ChatColor.GRAY + "You have unlocked the " + ChatColor.GOLD + "Summer " + ChatColor.GRAY + "kit!", "essentials.kits.summer"),
				new CommandReward("Decimate Rank", new ItemStack(Material.BEDROCK), Rarity.MYTHICAL, 1, "manuadd %player% decimate", true, ChatColor.GRAY +
						"Receive the " + DecimateCore.getCore().getColoredDecimate() + ChatColor.GRAY + " rank!",
						ChatColor.GRAY + "You have unlocked the " + DecimateCore.getCore().getColoredDecimate() + ChatColor.GRAY + " rank!", "decimate.rank.decimate"),
				new CommandReward("Blaze Spawner (4)", new ItemStack(Material.MOB_SPAWNER), Rarity.RARE, 25, "es give %player% BLAZE 0 4", false),
				new CommandReward("Iron Golem Spawner (2)", new ItemStack(Material.MOB_SPAWNER), Rarity.RARE, 20, "es give %player% IRON_GOLEM 0 2", false),
				new CashReward("$3,500,000", new ItemStack(Material.PAPER), Rarity.COMMON, 40, 3500000) 
				));
	}
	
	private GodCrate setupGodCrate(){
		return new GodCrate(Arrays.asList(
				new CommandReward("Blaze Spawner (2)", new ItemStack(Material.MOB_SPAWNER), Rarity.COMMON, 25+3+3, "es give %player% BLAZE 0 2", false),
				new CommandReward("Iron Golem Spawner (1)",new ItemStack(Material.MOB_SPAWNER), Rarity.RARE, 22-3, "es give %player% IRON_GOLEM 0 1", false),
				new CommandReward("Creeper Spawner (1)", new ItemStack(Material.MOB_SPAWNER), Rarity.RARE, 22-3, "es give %player% CREEPER 0 1", false),
				new CommandReward("Decimate Key (1)", glowItem(Material.TRIPWIRE_HOOK, 1, true), Rarity.MYTHICAL, 1, "cratekey %player% decimate_key 1", false),
				new CommandReward("Decimate Kit (1)", new ItemStack(Material.BOOK), Rarity.COMMON, 30, "kit decimate %player%", true)
				));
	}
	
	private DecimateCrate setupDecimateCrate(){
		return new DecimateCrate(Arrays.asList(
				new CommandReward("Blaze Spawner (3)", new ItemStack(Material.MOB_SPAWNER), Rarity.RARE, 25, "es give %player% BLAZE 0 3", false),
				new CommandReward("Iron Golem Spawner (2)",new ItemStack(Material.MOB_SPAWNER), Rarity.RARE, 22, "es give %player% IRON_GOLEM 0 2", false),
				new CommandReward("Creeper Spawner (2)", new ItemStack(Material.MOB_SPAWNER), Rarity.RARE, 22, "es give %player% CREEPER 0 2", false),
				new CommandReward("Decimate Rank", new ItemStack(Material.BEDROCK), Rarity.MYTHICAL, 1, "manuadd %player% decimate", true, ChatColor.GRAY +
						"Receive the " + DecimateCore.getCore().getColoredDecimate() + ChatColor.GRAY + " rank!",
						ChatColor.GRAY + "You have unlocked the " + DecimateCore.getCore().getColoredDecimate() + ChatColor.GRAY + " rank!", "decimate.rank.decimate"),
				new CommandReward("Protection V Kit (1)", new ItemStack(Material.BOOK), Rarity.COMMON, 30, "kit p5 %player%", true)
				));
	}
	
	private VoteCrate setupVoteCrate(){
		return new VoteCrate(Arrays.asList(
				new CommandReward("Creeper Spawner (1)", new ItemStack(Material.MOB_SPAWNER), Rarity.MYTHICAL, 1, "es give %player% CREEPER 0 1", false),
				new ItemReward("Hopper (4)", item(Material.HOPPER, 0, 4), Rarity.RARE, 15),
				new CommandReward("God Key (1)", glowItem(Material.TRIPWIRE_HOOK, 1, true), Rarity.MYTHICAL, 2, "cratekey %player% god_crate 1", false),
				new CommandReward("Zombie Spawner (1)", new ItemStack(Material.MOB_SPAWNER), Rarity.COMMON, 20, "es give %player% ZOMBIE 0 1", false),
				new CommandReward("Skeleton Spawner (1)", new ItemStack(Material.MOB_SPAWNER), Rarity.COMMON, 20, "es give %player% SKELETON 0 1", false),
				new CommandReward("Cow Spawner (1)", new ItemStack(Material.MOB_SPAWNER), Rarity.EPIC, 5, "es give %player% COW 0 1", false),
				new ItemReward("God Apple (4)", item(Material.GOLDEN_APPLE, 1, 4), Rarity.COMMON, 20),
				new CashReward("$100,000", new ItemStack(Material.PAPER), Rarity.RARE, 17, 100000)
				));
	}
	
	private ItemStack item(Material material, int data, int amount){
		return new ItemStack(material, amount, (byte) data);
	}
	
	private ItemStack glowItem(Material material, int amount, boolean glow){
		ItemStack i = new ItemStack(material, amount, (byte) 0);
		if(glow){
			EnchantGlow.addGlow(i);
		}
		return i;
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
		List<Crate> groundDisplayCrates = new ArrayList<>();
		for(Crate crate : this.crates){
			if(crate.hasGroundEffect()){
				groundDisplayCrates.add(crate);
			}
		}
		final int t = 3;
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
				for(int i = 0; i < groundDisplayCrates.size(); i++){
					
					//TODO ADD THIS BACK
//					groundDisplayCrates.get(i).getGroundEffect().display(0, 0, 0, 0, 1, groundDisplayCrates.get(i).getLocation().clone().add(x, 0.05, z), 30);
				}
			}
			
		}, t, t);
	}
	
	private void startSeasonalTimer(){
		List<Crate> seasonal = new ArrayList<Crate>();
		for(Crate crate : this.crates){
			if(crate.isSeasonal()){
				seasonal.add(crate);
			}
		}
		if(seasonal.size() == 0){
			return;
		}
		runB = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(DecimateCore.getCore(), new Runnable(){

			@Override
			public void run() {
				for(int i = 0; i < seasonal.size(); i++){
					if(seasonal.get(i).isOver()){
						seasonal.get(i).clearStands();
						seasonal.get(i).disable();
						crates.remove(seasonal);
						seasonal.remove(i--);
					}else{
						seasonal.get(i).setTimeString(ChatColor.GRAY + formatTimeString(seasonal.get(i).getOver()));
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
			ItemStack icon = cr.getIcon(1);
			if(cr == null || icon == null || icon.getItemMeta() == null || item == null || item.getItemMeta() == null){
				continue;
			}
			if(icon.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())){
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
			player.sendMessage(ChatColor.RED + "Please wait 1 second before opening another crate.");
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
