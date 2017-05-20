package com.decimatepvp.functions.staff.spectate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.decimatepvp.core.DecimateCore;

public class SpectateCommand implements CommandExecutor {

	private DecimateCore core;
	
	public SpectateCommand(DecimateCore core){
		this.core = core;
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(!(arg0 instanceof Player)){
			arg0.sendMessage("Command only for players.");
			return false;
		}
		
		Player player = (Player) arg0;
		
		if(!player.hasPermission("Decimate.spectate")){
			player.sendMessage(ChatColor.RED + "You do not have permission for this.");
			return false;
		}
		
		if(arg3.length < 1){
			player.sendMessage(ChatColor.RED + "Invalid syntax, try " + ChatColor.YELLOW + "/spectate (toggle/tp)" + ChatColor.RED + "!");
			return false;
		}
		
		if(arg3[0].equalsIgnoreCase("tp")){
			if(arg3.length == 1){
				player.sendMessage(ChatColor.RED + "Invalid syntax, try " + ChatColor.YELLOW + "/spectate tp (name)" + ChatColor.RED + "!");
				return false;
			}
			try {
				if(core.getSpectateManager().isSpectating(player)){
					player.teleport(Bukkit.getServer().getPlayer(arg3[1]).getLocation());
					player.sendMessage(ChatColor.GREEN + "Teleported to " + ChatColor.AQUA + Bukkit.getServer().getPlayer(arg3[1]).getName() + ChatColor.GREEN + "!");
				}else{
					core.getSpectateManager().toggleSpectator(player);
					player.teleport(Bukkit.getServer().getPlayer(arg3[1]).getLocation());
					player.sendMessage(ChatColor.GREEN + "Teleported to " + ChatColor.AQUA + Bukkit.getServer().getPlayer(arg3[1]).getName() + ChatColor.GREEN + "!");
				}
			}catch(Exception ex){
				player.sendMessage(ChatColor.RED + "Player not online.");
				return false;
			}
			return false;
		}
		
		if(arg3[0].equalsIgnoreCase("toggle")){
			core.getSpectateManager().toggleSpectator(player);
			return false;
		}
		
		return false;
	}

}
