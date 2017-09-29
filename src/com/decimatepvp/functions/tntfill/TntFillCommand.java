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

import decimatenetworkcore.core.DataUser;
import decimatenetworkcore.core.DecimateNetworkCore;

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
			player.sendMessage(ChatColor.RED + "Invalid syntax. Try " + ChatColor.YELLOW + "/tntfill (# of tnt per dispenser) (range)" + ChatColor.RED + "!");
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
		
		int amount = Integer.parseInt(arg3[0]) > 9*64 ? 9*64 : Integer.parseInt(arg3[0]);
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
				int total = 0, extra = 0;
				int inventory = PlayerUtils.getTotal(player.getInventory(), Material.TNT);
				DataUser du = DecimateNetworkCore.getInstance().getDataUserManager().getDataUser(player.getUniqueId().toString());
				if(amount*size > inventory){
					int poExtra = du.getFac1TNT();
					if(poExtra > 1){
						if(amount*size > poExtra + inventory){
							extra = poExtra;
							total = extra + inventory;
							du.removeFac1TNT(extra);
						}else{
							int needed = amount*size - inventory;
							extra = needed;
							du.removeFac1TNT(extra);
						}
					}else{
						total = inventory;
					}
				}else{
					total = amount*size;
				}
				int saved = 0;
				if(total - extra > 0){
					core.getTntFillManager().fill(dispensers, total - extra);
				}
				Bukkit.broadcastMessage(ChatColor.YELLOW.toString() + saved + ", " + total + ", " + extra);
				int extraSaved = 0;
				if(extra > 0){
					List<Location> dispensers2 = new ArrayList<>();
					dispensers.forEach(l -> dispensers2.add(l));
					extraSaved = core.getTntFillManager().fill(dispensers2, extra);
					Bukkit.broadcastMessage(ChatColor.GOLD.toString() + extraSaved);
				}
				PlayerUtils.removeItems(player, Material.TNT, total - saved);
				du.addFac1TNT(extraSaved);
				if(extra - extraSaved > 0){
					player.sendMessage(ChatColor.YELLOW + "Added " + ChatColor.GOLD + (extra - extraSaved) + ChatColor.YELLOW + " TNT from your TNT bank.");
				}
				player.sendMessage(ChatColor.GREEN + "Spread " + ChatColor.AQUA + (total - (saved + extraSaved)) + " TNT " + ChatColor.GREEN
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
