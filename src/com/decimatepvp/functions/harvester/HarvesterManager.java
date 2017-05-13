package com.decimatepvp.functions.harvester;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.Manager;
import com.decimatepvp.utils.ItemUtils;

public class HarvesterManager implements Manager, Listener {
	
	public static ItemStack harvesterItem;
	
	public HarvesterManager() {
		harvesterItem = ItemUtils.createItem(Material.DIAMOND_HOE, 1, (byte) 0, "&6&l* &2&lHarvester Hoe &l&6*",
				"&6* Allows you to harvest multiple sugarcane stacks");
	}

	@Override
	public void disable() {
		
	}

}
