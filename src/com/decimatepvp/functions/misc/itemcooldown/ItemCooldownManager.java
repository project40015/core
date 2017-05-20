package com.decimatepvp.functions.misc.itemcooldown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;

public class ItemCooldownManager implements Manager, Listener {

	private int runnable;
	private HashMap<String, Integer> goldenAppleCooldown = new HashMap<String, Integer>();
	private HashMap<String, Integer> godAppleCooldown = new HashMap<String, Integer>();
	
	private int goldenAppleTime = 45, godAppleTime = 120;
	
	public ItemCooldownManager(){
		start();
	}
	
	private void start(){
		runnable = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(DecimateCore.getCore(), new Runnable(){

			@Override
			public void run() {
				List<String> toRemove = new ArrayList<>();
				for(String str : goldenAppleCooldown.keySet()){
					if(goldenAppleCooldown.get(str) - 1 == 0){
						toRemove.add(str);
					}else{
						goldenAppleCooldown.put(str, goldenAppleCooldown.get(str)-1);
					}
				}
				toRemove.forEach(str -> goldenAppleCooldown.remove(str));
				toRemove.clear();
				for(String str : godAppleCooldown.keySet()){
					if(godAppleCooldown.get(str) - 1 == 0){
						toRemove.add(str);
					}else{
						godAppleCooldown.put(str, godAppleCooldown.get(str)-1);
					}
				}
				toRemove.forEach(str -> godAppleCooldown.remove(str));
			}
			
		}, 20, 20);
	}
	
	public void addGodAppleCooldown(Player player){
		this.godAppleCooldown.put(player.getUniqueId().toString(), godAppleTime);
	}
	
	public void addGoldenAppleCooldown(Player player){
		this.goldenAppleCooldown.put(player.getUniqueId().toString(), goldenAppleTime);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onConsume(PlayerItemConsumeEvent event){
		if(event.getItem().getType().equals(Material.GOLDEN_APPLE)){
			if(event.getItem().getData().getData() == 0){
				addGoldenAppleCooldown(event.getPlayer());
			}else if(event.getItem().getData().getData() == 1){
				addGodAppleCooldown(event.getPlayer());
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if(event.getPlayer().getItemInHand().getType().equals(Material.GOLDEN_APPLE) &&
					event.getPlayer().getItemInHand().getData().getData() == 0){
				if(goldenAppleCooldown.containsKey(event.getPlayer().getUniqueId().toString())){
					event.getPlayer().sendMessage(ChatColor.RED + "You cannot eat a golden apple for another " +
							ChatColor.YELLOW + goldenAppleCooldown.get(event.getPlayer().getUniqueId().toString()) + "s" + ChatColor.RED + "!");
					event.setCancelled(true);
				}
			}else if(event.getPlayer().getItemInHand().getType().equals(Material.GOLDEN_APPLE) &&
					event.getPlayer().getItemInHand().getData().getData() == 1){
				if(godAppleCooldown.containsKey(event.getPlayer().getUniqueId().toString())){
					event.getPlayer().sendMessage(ChatColor.RED + "You cannot eat a god apple for another " +
							ChatColor.YELLOW + godAppleCooldown.get(event.getPlayer().getUniqueId().toString()) + "s" + ChatColor.RED + "!");
					event.setCancelled(true);
				}
			}
		}
	}
	
	@Override
	public void disable() {
		Bukkit.getServer().getScheduler().cancelTask(runnable);
	}

}
