package com.decimatepvp.functions.bookCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandBook {

	private ItemStack item;
	private String command;
	private String message = "";
	private String noPermission = "";
	
	public CommandBook(ItemStack item, String command){
		this.item = item;
		this.command = command;
	}
	
	public CommandBook(ItemStack item, String command, String message){
		this(item, command);
		this.message = message;
	}
	
	public CommandBook(ItemStack item, String command, String message, String noPermission){
		this(item, command);
		this.message = message;
		this.noPermission = noPermission;
	}
	
	public ItemStack getItem(){
		return item;
	}
	
	public String getCommand(){
		return command;
	}
	
	public void run(Player player){
		if(!noPermission.equals("") && player.hasPermission(noPermission)){
			player.sendMessage(ChatColor.YELLOW + "You already have this.");
			return;
		}
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
		
		if(!this.message.equals("")){
			player.sendMessage(message);
		}
	}
	
	public String getNoPermission(){
		return this.noPermission;
	}
	
}
