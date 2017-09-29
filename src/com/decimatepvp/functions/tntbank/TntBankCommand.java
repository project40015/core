package com.decimatepvp.functions.tntbank;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import decimatenetworkcore.core.DataUser;
import decimatenetworkcore.core.DecimateNetworkCore;

public class TntBankCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		Player player;
		if(arg0 instanceof Player){
			player = (Player) arg0;
		}else{
			arg0.sendMessage("Only players can run this command.");
			return false;
		}
		DataUser du = DecimateNetworkCore.getInstance().getDataUserManager().getDataUser(player.getUniqueId().toString());
		if(arg3.length == 0){
			player.sendMessage(ChatColor.RED + "You own " + ChatColor.YELLOW + du.getFac1TNT() + ChatColor.RED + " TNT!");
			return false;
		}
		if(arg3.length >= 1){
			if(arg3[0].equalsIgnoreCase("help")){
				player.sendMessage(ChatColor.RED + "Decimate TNT Bank" + ChatColor.GRAY + ":");
				player.sendMessage(ChatColor.GRAY + " - " + ChatColor.YELLOW + "/tntbank deposit (amount)");
				player.sendMessage(ChatColor.GRAY + " - " + ChatColor.YELLOW + "/tntbank withdraw (amount)");
				return false;
			}
			if(arg3[0].equalsIgnoreCase("withdraw") || arg3[0].equalsIgnoreCase("w")){
				if(arg3.length != 1){
					try {
						int amount = Integer.parseInt(arg3[1]);
						if(du.getFac1TNT() >= amount){
							if(!(amount <= 0)){
								int am = fillUp(player, amount);
								du.removeFac1TNT(am);
								player.sendMessage(ChatColor.GREEN + "You withdrew " + ChatColor.AQUA + am + ChatColor.GREEN + " TNT!");
							}else{
								player.sendMessage(ChatColor.RED + "Error: You may only withdraw a positive amount of TNT.");
							}
						}else{
							player.sendMessage(ChatColor.RED + "Error: You do not own this much TNT.");
						}
					}catch(Exception ex){
						player.sendMessage(ChatColor.RED + "Error: " + ChatColor.YELLOW + arg3[1] + " is not a valid number.");
					}
				}else{
					player.sendMessage(ChatColor.RED + "Invalid Syntax, try: " + ChatColor.YELLOW + "/tntbank withdraw (amount)");
				}
				return false;
			}
			if(arg3[0].equalsIgnoreCase("deposit") || arg3[0].equalsIgnoreCase("d") || arg3[0].equalsIgnoreCase("add")){
				if(arg3.length != 1){
					try {
						int amount = 0;
						if(arg3[1].equalsIgnoreCase("all") || arg3[1].equalsIgnoreCase("*")){
							amount = 10000;
						}else{
							amount = Integer.parseInt(arg3[1]);
						}
						if(amount > 0){
							int am = empty(amount, player);
							du.addFac1TNT(am);
							player.sendMessage(ChatColor.GREEN + "You deposited " + ChatColor.AQUA + am + ChatColor.GREEN + " TNT!");
						}else{
							player.sendMessage(ChatColor.RED + "Error: You may only deposit a positive amount of TNT.");
						}
					}catch(NumberFormatException ex){
						player.sendMessage(ChatColor.RED + "Error: " + ChatColor.YELLOW + arg3[1] + " is not a valid number.");
					}
				}else{
					player.sendMessage(ChatColor.RED + "Invalid Syntax, try: " + ChatColor.YELLOW + "/tntbank deposit (amount)");
				}
				return false;
			}
		}
		return false;
	}
	
	public int empty(int amount, Player player){
		int total = 0;
		for(int i = 0; i < 36; i++){
			ItemStack im = player.getInventory().getItem(i);
			if(im == null || im.getType() == null){
				continue;
			}
			if(im.getType().equals(Material.TNT)){
				if(amount > im.getAmount()){
					amount -= im.getAmount();
					total += im.getAmount();
					player.getInventory().setItem(i, new ItemStack(Material.AIR));
				}else{
					player.getInventory().setItem(i, new ItemStack(Material.TNT, im.getAmount() - amount));
					return total;
				}
			}
		}
		return total;
	}
	
	public int fillUp(Player player, int amount){
		if(player.getInventory().firstEmpty() != -1){
			if(amount > 64){
				player.getInventory().addItem(new ItemStack(Material.TNT, 64));
				return 64 + fillUp(player, amount - 64);
			}else{
				player.getInventory().addItem(new ItemStack(Material.TNT, amount));
				return amount;
			}
		}
		return 0;
	}

}
