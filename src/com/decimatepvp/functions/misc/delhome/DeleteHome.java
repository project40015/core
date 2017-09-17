package com.decimatepvp.functions.misc.delhome;

import java.util.Map;

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
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;

public class DeleteHome implements Listener {

	class Timer extends BukkitRunnable {

		public boolean isRunning = false;

		OfflinePlayer plyr;
		Player player;
		Location start;

		int time = 0;

		public Timer(Player player) {
			plyr = player;
			this.player = player;
			start = player.getLocation();
		}

		@Override
		public synchronized void cancel() throws IllegalStateException {
			isRunning = false;
			super.cancel();
		}

		@Override
		public void run() {
			isRunning = true;
			if(plyr.isOnline()) {
				if(start.distance(player.getLocation()) > 0.5D) {
					player.sendMessage(ChatColor.DARK_RED + "Pending teleportation request cancelled.");
					lastHomeCommand.remove(plyr.getName());
					timer.remove(player.getName());
					this.cancel();
				}

				if(time == 5) {
					player.sendMessage(ChatColor.GOLD + "Teleportation commencing.");
					teleporting.put(player.getName(), lastHomeCommand.get(player.getName()));
					player.performCommand("home " + lastHomeCommand.get(player.getName()));
					lastHomeCommand.remove(plyr.getName());
					timer.remove(player.getName());
					this.cancel();
				}
			}
			else {
				lastHomeCommand.remove(plyr.getName());
				timer.remove(player.getName());
				this.cancel();
			}

			time++;

		}

	}
	private final Map<String, String> lastHomeCommand = Maps.newHashMap();
	private final Map<String, String> teleporting = Maps.newHashMap();

	private final Map<String, Timer> timer = Maps.newHashMap();

	@EventHandler
	public void onHomeCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();

		String command = event.getMessage();
		command = command.substring(1, command.length());

		String[] args = command.split(" ");
		String cmd = args[0];

		if(cmd.equalsIgnoreCase("sethome") || cmd.equalsIgnoreCase("esethome") ||
				cmd.equalsIgnoreCase("essentials:sethome") || cmd.equalsIgnoreCase("essentials:esethome")) {

			if(args.length == 1) {
				player.sendMessage("You must specify a home name.");
				event.setCancelled(true);
				return;
			}
		}
		else if(cmd.equalsIgnoreCase("home") || cmd.equalsIgnoreCase("ehome") ||
				cmd.equalsIgnoreCase("essentials:home") || cmd.equalsIgnoreCase("essentials:ehome")) {

			if(args.length == 1) {
				//				player.sendMessage("You must specify a home.");
				player.performCommand("homes");
				event.setCancelled(true);
				return;
			}

			if(timer.containsKey(player.getName())) {
				player.sendMessage(ChatColor.DARK_RED + "Pending teleportation request cancelled.");
				lastHomeCommand.remove(player);
				timer.get(player.getName()).cancel();
				timer.remove(player.getName());
				lastHomeCommand.remove(player.getName());
			}

			event.setCancelled(true);
			lastHomeCommand.put(player.getName(), args[1]);
			player.sendMessage(ChatColor.YELLOW + "Teleportation will commence in " +
					ChatColor.GOLD + "5 seconds" + ChatColor.YELLOW + ". Don't move.");
			this.startDelay(player);
		}
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		
		if(player.isOp()){
			return;
		}

		if(teleporting.containsKey(player.getName())) {
			Faction territory = FactionUtils.getFactionByLoc(event.getTo());
			Rel rel = FactionUtils.getFaction(player).
					getRelationTo(territory);

			if(!rel.isAtLeast(Rel.MEMBER) &&
					territory != FactionUtils.getWilderness()) {
				//				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
				//						"delhome " + player.getName() + ":" + teleporting.get(player.getName()));

				player.sendMessage(ChatColor.RED + "You cannot teleport there");
				event.setCancelled(true);
			}

			teleporting.remove(player.getName());
		}
	}

	private void startDelay(Player player) {
		Timer br = new Timer(player);

		timer.put(player.getName(), br);

		br.runTaskTimer(DecimateCore.getCore(), 0, 20);
	}

}
