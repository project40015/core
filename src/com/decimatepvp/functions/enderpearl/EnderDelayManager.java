package com.decimatepvp.functions.enderpearl;

import java.text.DecimalFormat;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.decimatepvp.utils.PlayerUtils;
import com.google.common.collect.Maps;

public class EnderDelayManager implements Manager, Listener {
	
	public static final String ACTION_MESSAGE = "&2You cannot Enderpearl for &b{TIME} &2seconds";
	
	private final int TIME_AFFECTED = 10000;
	
	private final DecimalFormat df = new DecimalFormat("##.00");
	
	private Map<OfflinePlayer, Long> disabledPlayers = Maps.newHashMap();
	
	public int endertask;
	
	public EnderDelayManager() {
		startDelayRunnable();
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onEnderTeleport(PlayerInteractEvent event) {
		if((event.getAction() == Action.RIGHT_CLICK_AIR) ||
				(event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			Player player = event.getPlayer();
			if(contains(player)) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "You are currently under an anti-enderpearl potion! You cannot use this!");
			}
		}
	}

	public boolean disablePlayer(Player player) {
		if(contains(player)) {
			return false;
		}
		else {
			disabledPlayers.put(player, System.currentTimeMillis());
			return true;
		}
	}

	public boolean allowPlayer(Player player) {
		if(!contains(player)) {
			return false;
		}
		else {
			disabledPlayers.remove(player);
			return true;
		}
	}

	private boolean contains(Player player) {
		return disabledPlayers.containsKey(player);
	}
	
	@SuppressWarnings("deprecation")
	private void startDelayRunnable() {
		this.endertask = Bukkit.getServer().getScheduler().runTaskTimer(DecimateCore.getCore(), new BukkitRunnable() {
			
			@Override
			public void run() {
				long now = System.currentTimeMillis();
				for(OfflinePlayer plyr : disabledPlayers.keySet()) {
					long start = disabledPlayers.get(plyr.getName());
					long timeLeft = (start + TIME_AFFECTED) - now;
					if(timeLeft <= 0) {
						disabledPlayers.remove(plyr.getName());
					}
					else {
						if(plyr.isOnline()) {
							PlayerUtils.sendActionbar(plyr.getPlayer(), createActionMessage(timeLeft));
						}
					}
				}
			}
		}, 0L, 10L).getTaskId();
	}
	
	private String createActionMessage(long timeLeft) {
		String message = ACTION_MESSAGE;
		message = message.replace("{TIME}", formatTime(timeLeft));
		return message;
	}

	private String formatTime(long timeLeft) {
		double seconds = (double) timeLeft/1000;
		return df.format(seconds);
	}

	@Override
	public void disable() {
		Bukkit.getServer().getScheduler().cancelTask(this.endertask);
	}
	
}
