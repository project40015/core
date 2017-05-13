package com.decimatepvp.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class PlayerInventory {

	private Location location;
	private Collection<PotionEffect> activePotions;
	private ItemStack helmet, chestplate, leggings, boots;
	private List<ItemStack> inventory = new ArrayList<>();
	private double health;
	private int food, fire;
	private GameMode gamemode;
	private Vector velocity;
	private float fallDistance;
	
	public PlayerInventory(Player player){
		location = player.getLocation();
		activePotions = player.getActivePotionEffects();
		helmet = player.getInventory().getHelmet();
		chestplate = player.getInventory().getChestplate();
		leggings = player.getInventory().getLeggings();
		boots = player.getInventory().getBoots();
		for(int i = 0; i < player.getInventory().getSize(); i++){
			inventory.add(player.getInventory().getItem(i) == null ? new ItemStack(Material.AIR) : player.getInventory().getItem(i));
		}
		this.health = player.getHealth();
		this.food = player.getFoodLevel();
		this.fire = player.getFireTicks();
		this.gamemode = player.getGameMode();
		
		this.fallDistance = player.getFallDistance();
		this.velocity = player.getVelocity();
	}
	
	public void paste(Player player){
		player.addPotionEffects(activePotions);
		player.getInventory().setHelmet(helmet);
		player.getInventory().setChestplate(chestplate);
		player.getInventory().setLeggings(leggings);
		player.getInventory().setBoots(boots);
		for(int i = 0; i < inventory.size(); i++){
			player.getInventory().setItem(i, inventory.get(i));
		}
		player.setHealth(health);
		player.setFireTicks(fire);
		player.setFoodLevel(food);
		
		player.setGameMode(gamemode);
		
		player.teleport(location);
		
		player.setFallDistance(fallDistance);
		player.setVelocity(velocity);
	}
	
}
