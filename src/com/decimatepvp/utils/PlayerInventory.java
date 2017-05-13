package com.decimatepvp.utils;

import java.util.Collection;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class PlayerInventory {

	private Location location;
	private Collection<PotionEffect> activePotions;
	private ItemStack helmet, chestplate, leggings, boots;
	private Inventory inventory;
	private double health;
	private int food, fire;
	private GameMode gamemode;
	
	public PlayerInventory(Player player){
		location = player.getLocation();
		activePotions = player.getActivePotionEffects();
		helmet = player.getInventory().getHelmet();
		chestplate = player.getInventory().getChestplate();
		leggings = player.getInventory().getLeggings();
		boots = player.getInventory().getBoots();
		inventory = player.getInventory();
		this.health = player.getHealth();
		this.food = player.getFoodLevel();
		this.fire = player.getFireTicks();
		this.gamemode = player.getGameMode();
	}
	
	public void paste(Player player){
		player.addPotionEffects(activePotions);
		player.getInventory().setHelmet(helmet);
		player.getInventory().setChestplate(chestplate);
		player.getInventory().setLeggings(leggings);
		player.getInventory().setBoots(boots);
		for(int i = 0; i < inventory.getSize(); i++){
			if(inventory.getItem(i) == null){
				player.getInventory().setItem(i, new ItemStack(Material.AIR));
			}else{
				player.getInventory().setItem(i, inventory.getItem(i));
			}
		}
		player.setHealth(health);
		player.setFireTicks(fire);
		player.setFoodLevel(food);
		
		player.setGameMode(gamemode);
		
		player.teleport(location);
	}
	
}
