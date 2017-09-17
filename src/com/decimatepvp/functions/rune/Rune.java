package com.decimatepvp.functions.rune;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Rune implements Listener {
	
	private RuneID id;
	protected ItemStack item;
	
	public Rune(RuneID id){
		this.id = id;
	}
	
	public RuneID getRuneID(){
		return id;
	}
	
	protected void createItem(Material material, String name, List<String> lore){
		item = new ItemStack(material);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
		im.setLore(lore);
		item.setItemMeta(im);
	}
	
	public abstract ItemStack getItem();
	public abstract boolean onClick(Player player);
	
}
