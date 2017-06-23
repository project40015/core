package com.decimatepvp.functions.staff.factions;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.utils.FactionUtils;

public class FactionCommandListener implements Listener {

	private final List<String> allowedPvPCommands = Arrays.asList("msg", "m", "r", "f c", "f chat", "f map", "feed",
			"f show");
	
	public static final List<String> ALLOWED_COMMANDS = 
			Arrays.asList("spawn", "f ", "home", "feed", "echest",
					"bal", "spectate", "msg", "m ", "r", "shop", "ah", "who",
					"eat", "tpa ", "kick", "tempban", "plugman", "echest", "enderchest", "ftop");

	public static final List<String> ALLY_ONLY_COMMANDS = 
			Arrays.asList("fly");
	
	private DecimateCore core = DecimateCore.getCore();
	
	@EventHandler
	public void onCommandUse(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String command = event.getMessage().substring(1, event.getMessage().length()).toLowerCase();

		if(!player.hasPermission("Decimate.factions.commandbypass")) {
			if(core.getPvpManager().isPlayerInCombat(player)) {
				if(!isCommandAllowed(allowedPvPCommands, command)) {
					player.sendMessage(ChatColor.RED + "You cannot use this command in combat!");
					event.setCancelled(true);
				}
			}
			else if(isCommandAllyOnly(command)) {
				if(!FactionUtils.isInAllyTerritory(player)) {
					player.sendMessage(ChatColor.RED + "You can only use this in Ally territory!");
					event.setCancelled(true);
				}
			}
			else if(!FactionUtils.isAreaSafe(player)) {
				if(!isCommandAllowed(ALLOWED_COMMANDS, command)) {
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

	private boolean isCommandAllowed(List<String> commands, String command) {
		for(String allowed : commands) {
			if(command.startsWith(allowed)) {
				return true;
			}
		}
		return false;
	}
	
}
