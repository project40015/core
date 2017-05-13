package com.decimatepvp.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerUtils {

	public static int getTotal(Inventory inv, Material material) {
		int total = 0;
		for(ItemStack item : inv) {
			if(item != null) {
				if(item.getType() == material) {
					total += item.getAmount();
				}
			}
		}
		return total;
	}
	
	public static void removeItems(Player player, Material material, int amount){
		for(int i = 0; i < player.getInventory().getSize(); i++){
			if(player.getInventory().getItem(i) == null){
				continue;
			}
			if(player.getInventory().getItem(i).getType().equals(material)){
				if(amount - player.getInventory().getItem(i).getAmount() >= 0){
					player.getInventory().setItem(i, new ItemStack(Material.AIR));
				}else{
					player.getInventory().setItem(i, new ItemStack(material, player.getInventory().getItem(i).getAmount() - amount));
					break;
				}
			}
		}
	}
	
}
