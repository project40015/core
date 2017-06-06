package com.decimatepvp.functions.bookCommand;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandBook {

	private ItemStack item;
	private String command;
	
	public CommandBook(ItemStack item, String command){
		this.item = item;
		this.command = command;
	}
	
	public ItemStack getItem(){
		return item;
	}
	
	public String getCommand(){
		return command;
	}
	
	public void run(Player player){
		int am = player.getItemInHand().getAmount();
		if(am <= 1){
			player.getInventory().setItemInHand(new ItemStack(Material.AIR));
		}else{
			ItemStack hand = player.getItemInHand();
			hand.setAmount(am-1);
			player.setItemInHand(hand);
		}
		player.playSound(player.getLocation(), Sound.HORSE_SADDLE, 1, 1);
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", player.getName()));
	}
	
}
