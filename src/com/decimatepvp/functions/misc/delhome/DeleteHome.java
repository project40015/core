package com.decimatepvp.functions.misc.delhome;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.utils.FactionUtils;
import com.google.common.collect.Maps;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Relation;

public class DeleteHome implements Listener {

	private Map<String, String> lastHomeCommand = Maps.newHashMap();
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		
		if(lastHomeCommand.containsKey(player.getName())) {
			Faction territory = FactionUtils.getFactionByLoc(event.getTo());
			Relation rel = FactionUtils.getFaction(player).
					getRelationTo(territory);
			
			if((rel != Relation.MEMBER) &&
					(territory != FactionUtils.getWilderness())) {
//				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), 
//						"delhome " + player.getName() + ":" + lastHomeCommand.get(player.getName()));
				
				player.sendMessage(ChatColor.RED + "You cannot teleport there!");
				event.setCancelled(true);
			}
			
			lastHomeCommand.remove(player.getName());
		}
	}
	
	@EventHandler
	public void onHomeCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		
		if(lastHomeCommand.containsKey(player.getName())) {
			return;
		}
		
		String command = event.getMessage();
		command = command.substring(1, command.length());
		
		String[] args = command.split(" ");
		String cmd = args[0];

		if((cmd.equalsIgnoreCase("sethome")) || (cmd.equalsIgnoreCase("esethome")) ||
				(cmd.equalsIgnoreCase("essentials:sethome")) || (cmd.equalsIgnoreCase("essentials:esethome"))) {
			
			if(args.length == 1) {
				player.sendMessage("You must specify a home name.");
				event.setCancelled(true);
				return;
			}
		}
		else if((cmd.equalsIgnoreCase("home")) || (cmd.equalsIgnoreCase("ehome")) ||
				(cmd.equalsIgnoreCase("essentials:home")) || (cmd.equalsIgnoreCase("essentials:ehome"))) {
			
			if(args.length == 1) {
				player.sendMessage("You must specify a home.");
				event.setCancelled(true);
				return;
			}
			
			event.setCancelled(true);
			lastHomeCommand.put(player.getName(), args[1]);
			player.sendMessage(ChatColor.GOLD + "Teleportation will commence in " +
					ChatColor.RED + "5 seconds" + ChatColor.GOLD + ". Don't move.");
			startDelay(player);
		}
	}

	private void startDelay(Player player) {
		OfflinePlayer plyr = player;
		String name = player.getName();
		Location start = player.getLocation();
		BukkitRunnable br = new BukkitRunnable() {
			
			int time = 0;
			
			@Override
			public void run() {
				if(plyr.isOnline()) {
					if(start.distance(player.getLocation()) > 0.5D) {
						player.sendMessage(ChatColor.DARK_RED + "Pending teleportation request cancelled.");
						lastHomeCommand.remove(name);
						cancel();
					}
					
					if(time == 5) {
						Bukkit.broadcastMessage("1");
						player.performCommand("home " + lastHomeCommand.get(player.getName()));
						cancel();
					}
				}
				else {
					lastHomeCommand.remove(name);
					cancel();
				}
				
				time++;
			}
		};
		
		br.runTaskTimer(DecimateCore.getCore(), 0, 20);
	}

}
