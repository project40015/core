package com.decimatepvp.functions.crate.crates;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.functions.crate.Crate;
import com.decimatepvp.functions.crate.CrateReward;
import com.decimatepvp.functions.crate.Rarity;
import com.decimatepvp.utils.ParticleEffect;

public class GodCrate extends Crate {

	private ItemStack godKey;
	private ArmorStand stand;
	
	public GodCrate(List<CrateReward> rewards) {
		super(ChatColor.YELLOW + "God Crate", rewards);
		setupKey();
	}
	
	private void setupKey(){
		godKey = new ItemStack(Material.TRIPWIRE_HOOK);
		ItemMeta meta = godKey.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "God Crate Key");
		godKey.setItemMeta(meta);
	}

	@Override
	public boolean open(Player player, CrateReward reward, Location location) {
		effect(player, reward, location);
		return true;
	}
	
	@Override
	public void giveReward(Player player, CrateReward reward) {
		player.sendMessage("");
		player.sendMessage(ChatColor.GRAY + "Found: " + reward.getFormatName() + ChatColor.GRAY + "!");
		player.sendMessage("");
		if(reward.getRarity() == Rarity.EPIC || reward.getRarity() == Rarity.MYTHICAL){
			Bukkit.broadcastMessage(ChatColor.GRAY + player.getName() + " found: " + reward.getRarity().getDisplay() + ChatColor.GRAY + " reward!");
		}
		reward.reward(player);
	}
	
	private void postEffect(Player player, Rarity rarity, Location location){
		if(rarity.equals(Rarity.COMMON)){
			ParticleEffect.CLOUD.display(0, 0, 0, 0, 2, location.clone().add(0,1,0), 20);
		}else if(rarity.equals(Rarity.RARE)){
			ParticleEffect.ENCHANTMENT_TABLE.display(1, 1, 1, 0, 24, location.clone().add(0,1,0), 20);
		}else if(rarity.equals(Rarity.EPIC)){
			for(int i = 0; i < 50; i++){
				final int q = i;
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DecimateCore.getCore(), new Runnable(){

					@Override
					public void run() {
						location.getWorld().playSound(location, Sound.WATER, 1, 1);
						ParticleEffect.DRIP_WATER.display(0, 0, 0, 0, 1, location.clone().add(.5*Math.cos((2*Math.PI)*(q/50.0)), 0.5 + q/40.0, .5*Math.sin((2*Math.PI)*(q/50.0))), 20);
					}
					
				}, i);
			}
		}else if(rarity.equals(Rarity.MYTHICAL)){
			
			Location[] locs = {new Location(Bukkit.getWorlds().get(0), 22, 76, 15), new Location(Bukkit.getWorlds().get(0), 22, 76, 23),
					new Location(Bukkit.getWorlds().get(0), 14, 76, 23)};
			for(int i = 0; i < 10; i++){
				final int q = i;
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DecimateCore.getCore(), new Runnable(){

					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						if(q <= 3){
							for(Location loc : locs){
								loc.clone().add(0, q, 0).getBlock().setTypeIdAndData(95, (byte) 5, true);
								loc.clone().add(0, q + 1, 0).getBlock().setType(Material.EMERALD_BLOCK);
								loc.getWorld().playSound(loc, Sound.PISTON_EXTEND, 1, 1);
							}
						}else if(q == 4){
							if(player != null && player.isOnline() && player.getLocation().distance(locs[0]) < 40){
								player.getLocation().getWorld().playSound(player.getLocation(), Sound.WITHER_SPAWN, 1, 1);
								for(Location loc : locs){
									for(int i = 1; i <= 30; i++){
										ParticleEffect.VILLAGER_HAPPY.display(0, 0, 0, 0, 1,
												loc.clone().add((player.getLocation().getX() - loc.getX())*(i/30.0), (player.getEyeLocation().getY() - (loc.getY() + 4) + 4)*(i/30.0), (player.getLocation().getZ() - loc.getZ())*(i/30.0)), 50);
									}
								}
							}
						}else if(q >= 6){
							for(Location loc : locs){
								loc.clone().add(0, 10-(q+1), 0).getBlock().setType(Material.EMERALD_BLOCK);
								loc.clone().add(0, 10 - (q+1) + 1, 0).getBlock().setType(Material.AIR);
								loc.getWorld().playSound(loc, Sound.PISTON_RETRACT, 1, 1);
							}
						}
					}
					
				}, i*10);
			}
		}
	}
	
	private void effect(Player player, CrateReward reward, Location location){
		Inventory inventory = Bukkit.getServer().createInventory(player, 27, ChatColor.UNDERLINE + super.getName());
		ItemStack it = new ItemStack(Material.HOPPER);
		ItemMeta im = it.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "Your Reward:");
		it.setItemMeta(im);
		inventory.setItem(4, it);
		
		
		for(int i = 9; i < 18; i++){
			inventory.setItem(i, reward().getIcon(super.getTotalChance()));
		}
		
		player.openInventory(inventory);
		final int p = 19;
		for(int i = 0; i < p; i++){
			final int q = i;
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DecimateCore.getCore(), new Runnable(){

				@Override
				public void run() {
					player.playSound(player.getLocation(), Sound.FIRE_IGNITE, 1, 1);
					for(int i = 0; i < 8; i++){
						inventory.setItem(17-i, inventory.getItem(16-i));
					}
					if(q != p - 5){
						inventory.setItem(9, reward().getIcon(getTotalChance()));
					}else{
						inventory.setItem(9, reward.getIcon(getTotalChance()));
					}
					if(q == p - 1){
						giveReward(player, reward);
						postEffect(player, reward.getRarity(), location.clone().add(0.5,0.5,0.5));
					}
				}
				
			}, (long) (q + ((0.001*Math.pow(q, 4)) < 2 ? 2 : (0.001*Math.pow(q, 4)))));
		}
	}

	@Override
	public ItemStack getItemStack() {
		return godKey;
	}

	@Override
	public void disable() {
	}
	
}
