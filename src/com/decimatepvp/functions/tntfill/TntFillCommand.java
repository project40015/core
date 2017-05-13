package com.decimatepvp.functions.tntfill;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.utils.PlayerUtils;

public class TntFillCommand implements CommandExecutor {

	DecimateCore core;
	
	public TntFillCommand(DecimateCore core){
		this.core = core;
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(!(arg0 instanceof Player)){
			arg0.sendMessage("Command only for players.");
			return false;
		}
		
		Player player = (Player) arg0;
		
		if(!player.hasPermission("Decimate.command.tntfill")){
			player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return false;
		}
		
		if(core.getTntFillManager().isOnCooldown(player)){
			player.sendMessage(ChatColor.RED + "You can use this command again in " + ChatColor.YELLOW
					+ core.getTntFillManager().getCooldown(player) + "s" + ChatColor.RED + "!");
			return false;
		}
		
		if(arg3.length != 2){
			player.sendMessage(ChatColor.RED + "Invalid syntax. Try " + ChatColor.YELLOW + "/tntfill (# of tnt) (range)" + ChatColor.RED + "!");
			return false;
		}
		
		for(int i = 0; i < arg3[0].length(); i++){
			if(!Character.isDigit(arg3[0].charAt(i))){
				player.sendMessage(ChatColor.RED + "Invalid amount of tnt: " + ChatColor.YELLOW + arg3[0] + ChatColor.RED + "!");
				return false;
			}
		}
		
		for(int i = 0; i < arg3[1].length(); i++){
			if(!Character.isDigit(arg3[1].charAt(i))){
				player.sendMessage(ChatColor.RED + "Invalid range: " + ChatColor.YELLOW + arg3[1] + ChatColor.RED + "!");
				return false;
			}
		}
		
		int amount = Integer.parseInt(arg3[0]);
		int range = Integer.parseInt(arg3[1]);
		
		if(range > core.getDecimateConfig().getTntFillRange()){
			player.sendMessage(ChatColor.RED + "Range too large, maximum: " + ChatColor.YELLOW
					+ core.getDecimateConfig().getTntFillRange() + ChatColor.RED + "!");
			return false;
		}
		
		Bukkit.getServer().getScheduler().runTaskAsynchronously(DecimateCore.getCore(), new Runnable(){

			@Override
			public void run() {
				List<Location> dispensers = getLocations(player.getLocation(), range);
				
				int size = dispensers.size();
				int total = 0;
				int inventory = PlayerUtils.getTotal(player.getInventory(), Material.TNT);
				if(amount*size > inventory){
					total = inventory;
				}else{
					total = amount*size;
				}
				int saved = core.getTntFillManager().fill(dispensers, total);
				PlayerUtils.removeItems(player, Material.TNT, total - saved);
				
				player.sendMessage(ChatColor.GREEN + "Spread " + ChatColor.AQUA + (total - saved) + " TNT " + ChatColor.GREEN
						+ "over " + ChatColor.AQUA + size + " dispensers" + ChatColor.GREEN + "!");
				
				core.getTntFillManager().putOnCooldown(player);
			}
			
		});
		
		return false;
	}
	
	private List<Location> getLocations(Location loc, int range){
		List<Location> dispensers = new ArrayList<Location>();
		verticalLoop: for(int y = -1*(range/2); y <= range/2; y++){
			if(loc.getY() + y < 0){
				continue verticalLoop;
			}
			for(int x = -1*(range/2); x <= range/2; x++){
				for(int z = -1*(range/2); z <= range/2; z++){

					Location dis = loc.clone().add(x, y, z);
					if(dis.getBlock().getState() instanceof Dispenser){
						dispensers.add(dis);
					}
				}
			}
		}
		return dispensers;
	}

}
