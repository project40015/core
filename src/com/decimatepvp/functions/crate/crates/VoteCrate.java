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

public class VoteCrate extends Crate {

	private ItemStack voteKey;
	private ArmorStand stand;
	
	public VoteCrate(List<CrateReward> rewards) {
		super(ChatColor.GRAY + "Vote Crate", rewards);
		setupKey();
	}
	
	private void setupKey(){
		voteKey = new ItemStack(Material.TRIPWIRE_HOOK);
		ItemMeta meta = voteKey.getItemMeta();
		meta.setDisplayName(ChatColor.GRAY + "Vote Crate Key");
		voteKey.setItemMeta(meta);
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
		player.sendMessage(ChatColor.GRAY + "You found a " + reward.getFormatName() + ChatColor.GRAY + "!");
		player.sendMessage("");
		if(reward.getRarity() == Rarity.EPIC || reward.getRarity() == Rarity.MYTHICAL){
			Bukkit.broadcastMessage(ChatColor.GRAY + player.getName() + " has found a " + reward.getRarity().getDisplay() + ChatColor.GRAY + " reward!");
		}
		reward.reward(player);
	}
	
	private void effect(Player player, CrateReward reward, Location location){
		if(stand == null){
			stand = location.getWorld().spawn(location.clone().add(0.5,0.2,0.5), ArmorStand.class);
			stand.setVisible(false);
			stand.setGravity(false);
			stand.setSmall(true);
			stand.setCustomName(super.getName());
			stand.setCustomNameVisible(true);
		}
		stand.setHelmet(new ItemStack(Material.STONE));
		super.opening = true;
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DecimateCore.getCore(), new Runnable(){

			@Override
			public void run() {
				stand.setHelmet(new ItemStack(reward.getRarity().getChange()));
				stand.getLocation().getWorld().playEffect(stand.getLocation(), Effect.STEP_SOUND, reward.getRarity().getChange());
				if(reward.getRarity() == Rarity.EPIC){
					stand.getLocation().getWorld().playSound(stand.getLocation(), Sound.PIG_DEATH, 1, 1);
				}else if(reward.getRarity() == Rarity.MYTHICAL){
					stand.getLocation().getWorld().playSound(stand.getLocation(), Sound.HORSE_SKELETON_DEATH, 1, 1);
				}
				giveReward(player, reward);
			}
				
		}, 25);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DecimateCore.getCore(), new Runnable(){

			@Override
			public void run() {
				stand.setHelmet(new ItemStack(Material.AIR));
				opening = false;
			}
			
		}, 50);
	}

	@Override
	public ItemStack getItemStack() {
		return voteKey;
	}

	@Override
	public void disable() {
		this.stand.remove();
	}
	
}
