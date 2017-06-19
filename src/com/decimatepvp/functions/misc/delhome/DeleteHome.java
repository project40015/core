package com.decimatepvp.functions.misc.delhome;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.decimatepvp.utils.FactionUtils;
import com.google.common.collect.Maps;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Relation;

public class DeleteHome implements Listener {
	
	private Map<String, Home> lastHomeCommand = Maps.newHashMap();
	
	public static void main(String[] args) {
		String command = "/home     end";
		command = command.substring(1, command.length()).replaceAll(" +", " ");
		
		String[] arguments = command.split(" ");
		String cmd = arguments[0];
		
		System.out.println(((cmd.equalsIgnoreCase("home")) || (cmd.equalsIgnoreCase("ehome")))
				&& (arguments.length > 1));
		System.out.println(cmd + " " + arguments[1]);
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		
		if(lastHomeCommand.containsKey(player.getName())) {
			Faction territory = FactionUtils.getFactionByLoc(event.getTo());
			Relation rel = FactionUtils.getFaction(player).
					getRelationTo(territory);
			if(rel != Relation.MEMBER && (territory != FactionUtils.getWilderness()) 
					&& (!player.hasPermission("Decimate.staff.home"))) {
				if((System.currentTimeMillis() - lastHomeCommand.get(player.getName()).l)/1000 <= 6){
//				player.performCommand("delhome " + lastHomeCommand.get(player.getName()).n);
					player.sendMessage(ChatColor.RED + "You cannot teleport there!");
					event.setCancelled(true);
				}
				lastHomeCommand.remove(player.getName());
			}
		}
	}
	
	@EventHandler
	public void onHomeCommand(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage();
		command = command.substring(1, command.length());
		
		String[] args = command.split(" ");
		String cmd = args[0];
		
		if(((cmd.equalsIgnoreCase("home")) || (cmd.equalsIgnoreCase("ehome")))
				&& (args.length > 1)) {
			lastHomeCommand.put(event.getPlayer().getName(), new Home(args[1], System.currentTimeMillis()));
		}
	}

}
