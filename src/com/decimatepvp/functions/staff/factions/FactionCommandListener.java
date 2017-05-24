package com.decimatepvp.functions.staff.factions;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.decimatepvp.utils.FactionUtils;

public class FactionCommandListener implements Listener {
	
	public static final List<String> ALLOWED_COMMANDS = 
			Arrays.asList("spawn", "f ", "home", "feed", "echest",
					"bal", "spectate", "msg", "m ", "r", "shop", "ah", "who",
					"eat", "tpa ", "kick", "tempban", "plugman");

	public static final List<String> ALLY_ONLY_COMMANDS = 
			Arrays.asList("fly");
	
	@EventHandler
	public void onCommandUse(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String command = event.getMessage().substring(1, event.getMessage().length()).toLowerCase();

		if(!player.hasPermission("Decimate.factions.commandbypass")) {
			if(isCommandAllyOnly(command)) {
				if(!FactionUtils.isInAllyTerritory(player)) {
					player.sendMessage(ChatColor.RED + "You can only use this in Ally territory!");
					event.setCancelled(true);
				}
			}
			else if(!FactionUtils.isAreaSafe(player)) {
				if(!isCommandAllowed(command)) {
					player.sendMessage(ChatColor.RED + "You cannot use this in this area!");
					event.setCancelled(true);
				}
			}
		}
	}
	
	private boolean isCommandAllyOnly(String command) {
		for(String allowed :  ALLY_ONLY_COMMANDS) {
			if(command.startsWith(allowed)) {
				return true;
			}
		}
		return false;
	}

	private boolean isCommandAllowed(String command) {
		for(String allowed :  ALLOWED_COMMANDS) {
			if(command.startsWith(allowed)) {
				return true;
			}
		}
		return false;
	}
	
}
