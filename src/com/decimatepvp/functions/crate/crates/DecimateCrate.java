package com.decimatepvp.functions.crate.crates;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.functions.crate.Crate;
import com.decimatepvp.functions.crate.CrateReward;
import com.decimatepvp.functions.crate.Rarity;

public class DecimateCrate extends Crate {

	private ItemStack decimateKey;
	private ArmorStand stand;
	private CrateReward last = null;
	
	public DecimateCrate(List<CrateReward> rewards) {
		super(ChatColor.LIGHT_PURPLE + "Decimate Crate", rewards);
		setupKey();
	}
	
	private void setupKey(){
		decimateKey = new ItemStack(Material.TRIPWIRE_HOOK);
		ItemMeta meta = decimateKey.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Decimate Crate Key");
		decimateKey.setItemMeta(meta);
	}

	@Override
	public boolean open(Player player, CrateReward reward, Location location) {
		boolean b = super.opening;
		if(!b){
			effect(player, reward, location);
		}
		return !b;
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
	
	private void effect(Player player, CrateReward reward, Location location){
		if(stand == null){
			Location loc = location.clone().add(0.5,0.2,0.5);
			loc.setYaw(-90);
			stand = location.getWorld().spawn(loc, ArmorStand.class);
			stand.setVisible(false);
			stand.setGravity(false);
			stand.setSmall(true);
			stand.setCustomName(super.getName());
			stand.setCustomNameVisible(true);
		}
		super.opening = true;
		
		final int cycle = 30;
		
		
		for(int i = 0; i < cycle; i++){
			final int f = i;
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DecimateCore.getCore(), new Runnable(){

				@Override
				public void run() {

					if(f == cycle-1){
						stand.setHelmet(reward.getAnimationItem());
						stand.setCustomName(reward.getFormatName());
						giveReward(player, reward);
						if(reward.getRarity() == Rarity.EPIC){
							stand.getLocation().getWorld().playSound(stand.getLocation(), Sound.PIG_DEATH, 1, 1);
						}else if(reward.getRarity() == Rarity.MYTHICAL){
							stand.getLocation().getWorld().playSound(stand.getLocation(), Sound.HORSE_SKELETON_DEATH, 1, 1);
						}
					}else{
						CrateReward random = last == null ? reward() : reward(last);
						last = random;
						stand.setHelmet(random.getAnimationItem());
						stand.setCustomName(random.getFormatName());
						player.getWorld().playSound(location, Sound.NOTE_PIANO, 1, 1);
					}
				}
					
			}, (int) Math.round(i + Math.pow(1.12, i)));
		}
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DecimateCore.getCore(), new Runnable(){

			@Override
			public void run() {
				stand.setHelmet(new ItemStack(Material.AIR));
				stand.setCustomName(getName());
				opening = false;
			}
			
		}, 20*8);
	}

	@Override
	public ItemStack getItemStack() {
		return decimateKey;
	}

	@Override
	public void disable() {
		System.out.println("Decimate Crate disabled.");
		this.stand.remove();
	}
	
}
