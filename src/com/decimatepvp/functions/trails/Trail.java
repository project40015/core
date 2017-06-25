package com.decimatepvp.functions.trails;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.utils.EnchantGlow;

public abstract class Trail {

	private String name;
	private String description;
	private String permission;
	private ItemStack activeIcon;
	private ItemStack disabledIcon;
	
	public Trail(String name, String description, String permission, Material iconMaterial){
		this.name = name;
		this.description = description;
		this.permission = permission;
		
		setupIcons(iconMaterial);
	}
	
	private void setupIcons(Material material){
		disabledIcon = new ItemStack(material);
		ItemMeta aim = disabledIcon.getItemMeta();
		aim.setDisplayName(ChatColor.BLUE + name);
		aim.setLore(Arrays.asList("", ChatColor.GRAY + description));
		disabledIcon.setItemMeta(aim);
		
		activeIcon = new ItemStack(material);
		ItemMeta dim = activeIcon.getItemMeta();
		dim.setDisplayName(ChatColor.AQUA + name + ChatColor.GRAY + " (ACTIVE)");
		dim.setLore(Arrays.asList("", ChatColor.GRAY + description));
		activeIcon.setItemMeta(dim);
		EnchantGlow.addGlow(activeIcon);
	}
	
	public ItemStack getIcon(boolean activated){
		return activated ? activeIcon : disabledIcon;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getName(){
		return name;
	}
	
	public String getPermission(){
		return permission;
	}
	
	public boolean hasPermission(Player player){
		return player.hasPermission(permission);
	}
	
	public abstract void display(Location location);
	
}
