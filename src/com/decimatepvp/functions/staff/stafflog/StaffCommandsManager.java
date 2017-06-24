package com.decimatepvp.functions.staff.stafflog;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.decimatepvp.core.utils.Configuration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class StaffCommandsManager implements Manager, Listener {

	private List<String> ALLOWED = Arrays.asList("home", "spawn", "echest", "warp");

	Map<OfflinePlayer, List<String>> commands = Maps.newHashMap();

	@EventHandler
	public void onStaffCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if(player.hasPermission("Decimate.staff.log")) {
			if(!isAllowed(event.getMessage().toLowerCase())) {

				if(commands.containsKey(player)) {
					commands.get(player).add(event.getMessage());
				}
				else {
					commands.put(player, Lists.newArrayList());
					commands.get(player).add(event.getMessage());
				}
			}
		}
	}

	private boolean isAllowed(String message) {
		for(String string : ALLOWED) {
			if(string.startsWith(message)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void disable() {
		String month = new SimpleDateFormat("MMMM").format(Calendar.getInstance().getTime());
		String day = new SimpleDateFormat("dd").format(Calendar.getInstance().getTime());

		for(Entry<OfflinePlayer, List<String>> set : commands.entrySet()) {
			String username = set.getKey().getName();

			Configuration cfg = new Configuration(DecimateCore.getCore(), "/stafflogs/" + username, month + ".yml");
			FileConfiguration config = cfg.getData();

			config.set(day, config.getStringList(day).addAll(set.getValue()));
			cfg.saveData();
		}

	}
}
