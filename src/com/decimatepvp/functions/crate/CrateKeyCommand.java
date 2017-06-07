package com.decimatepvp.functions.crate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.decimatepvp.core.DecimateCore;

public class CrateKeyCommand implements CommandExecutor {
	
	private DecimateCore core;
	
	public CrateKeyCommand() {
		core = DecimateCore.getCore();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.hasPermission("Decimate.key.give")) {
			if(args.length < 2){
				sender.sendMessage(ChatColor.RED + "Correct format: /cratekey (player) (crate name) [amount]");
				return false;
			}
			int num = 1;
			if(args.length == 3){
				try{
					num = Integer.parseInt(args[2]);
				}catch(Exception ex){
					sender.sendMessage(ChatColor.RED + "Invalid number.");
					return false;
				}
			}
			try{
				Player player = Bukkit.getPlayer(args[0]);
				if(core.getCrateManager().isCrate(args[1])){
					core.getCrateManager().getCrate(args[1]).giveKey(player, num);
					sender.sendMessage(ChatColor.GREEN + "Key given!");
				}else{
					sender.sendMessage(ChatColor.RED + "That is not a valid crate. List of crates:");
					sender.sendMessage(ChatColor.YELLOW + core.getCrateManager().cratesString());
				}
			}catch(Exception ex){
				sender.sendMessage(ChatColor.RED + "Player not found.");
			}
		}
		else {
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
		}
		
		return false;
	}
	
}
