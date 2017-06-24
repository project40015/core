package com.decimatepvp.functions.tntfill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;

public class TntFillManager implements Manager {

	private HashMap<String, Integer> onCooldown = new HashMap<String, Integer>();
	private int cooldown = 2;
	private int runnable;
	
	public TntFillManager(){
		startTimer();
	}
	
	public void putOnCooldown(Player player){
		this.onCooldown.put(player.getUniqueId().toString(), cooldown);
	}
	
	public boolean isOnCooldown(Player player){
		return onCooldown.containsKey(player.getUniqueId().toString());
	}
	
	public int getCooldown(Player player){
		if(isOnCooldown(player)){
			return onCooldown.get(player.getUniqueId().toString());
		}
		return -1;
	}
	
	private void startTimer(){
		runnable = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(DecimateCore.getCore(), new Runnable(){

			@Override
			public void run() {
				List<String> toRemove = new ArrayList<>();
				for(String str : onCooldown.keySet()){
					if(onCooldown.get(str) <= 1){
						toRemove.add(str);
					}else{
						onCooldown.put(str, onCooldown.get(str)-1);
					}
				}
				for(String str : toRemove){
					onCooldown.remove(str);
				}
			}
			
		}, 20, 20);
	}
	
	/**
	 * @return number of TNT to return to inventory
	 */
	public int fill(List<Location> dispensers, int total){
		if(dispensers.size() == 0){
			return total;
		}
		if(dispensers.get(0).getBlock().getState() instanceof Dispenser){
			Dispenser d = (Dispenser) dispensers.get(0).getBlock().getState();
			dispensers.remove(0);
			int space = 0;
			for(int i = 0; i < 9; i++){
				if(d.getInventory().getItem(i) == null){
					space+=64;
				}else
				if(d.getInventory().getItem(i).getType().equals(Material.TNT)){
					space += 64-d.getInventory().getItem(i).getAmount();
				}
			}
			int amount = total/(dispensers.size()+1);
			if(space <= amount){
				if(space > 0){
					d.getInventory().addItem(new ItemStack(Material.TNT, space));
				}
				return fill(dispensers, total-space);
			}else{
				if(amount > 0){
					d.getInventory().addItem(new ItemStack(Material.TNT, amount));
				}
				return fill(dispensers, total-amount);
			}
		}
		return fill(dispensers, total);
	}

	@Override
	public void disable() {
		Bukkit.getServer().getScheduler().cancelTask(this.runnable);
		this.onCooldown.clear();
	}
	
}
