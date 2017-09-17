package com.decimatepvp.functions.rune.runes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.functions.rune.Rune;
import com.decimatepvp.functions.rune.RuneID;

public class HunterRune extends Rune {
	
	private List<String> players = new ArrayList<>();
	
	public HunterRune() {
		super(RuneID.HUNTER_RUNE);
		
		super.createItem(Material.BONE, ChatColor.GREEN + "Hunter Rune", Arrays.asList(
				ChatColor.GRAY + "Right click to receive " + ChatColor.GREEN + "18s",
				ChatColor.GRAY + "of " + ChatColor.GREEN + "2.5x" + ChatColor.GRAY + " damage with bow."));
	}

	@Override
	public ItemStack getItem() {
		return super.item;
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Arrow){
			Arrow damager = (Arrow) event.getDamager();
			if(damager.getShooter() instanceof Player){
				if(players.contains(((Player) damager).getUniqueId().toString())){
					event.setDamage(event.getDamage() * 2.5);
				}
			}
		}
	}

	@Override
	public boolean onClick(Player player) {
		final String uuid = player.getUniqueId().toString();
		if(players.contains(uuid)){
			return false;
		}
		players.add(uuid);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DecimateCore.getCore(), new Runnable(){

			@Override
			public void run() {
				if(players.contains(uuid)){
					players.remove(uuid);
				}
			}
			
		}, 20*18);
		
		return true;
	}

}
