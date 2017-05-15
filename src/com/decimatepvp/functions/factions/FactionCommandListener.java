package com.decimatepvp.functions.factions;

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
			Arrays.asList("spawn", "f show", "home", "feed", "echest",
					"bal", "f c", "spectate", "f top", "msg", "r", "shop", "ah", "who", "f map");
	@EventHandler
	public void onCommandUse(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if(!FactionUtils.isAreaSafe(player)) {
			if(!player.hasPermission("Decimate.factions.commandbypass")) {
				String command = event.getMessage().substring(1, event.getMessage().length()).toLowerCase();
				
				if(!isCommandAllowed(command)) {
					player.sendMessage(ChatColor.RED + "You cannot use this in this area!");
					event.setCancelled(true);
				}
			}
		}
	}

	private boolean isCommandAllowed(String command) {
		for(String allowed :  ALLOWED_COMMANDS) {
			if(allowed.startsWith(command)) {
				return true;
			}
		}
		return false;
	}
	
}
