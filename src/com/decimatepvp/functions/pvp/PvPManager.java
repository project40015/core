package com.decimatepvp.functions.pvp;

import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.decimatepvp.core.utils.Configuration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PvPManager implements Manager, Listener, CommandExecutor {
	
	private List<String> killList = Lists.newArrayList();
	private Map<String, CombatPlayer> players = Maps.newHashMap();
	private Map<Integer, CombatPlayer> entities = Maps.newHashMap();
	
	public PvPManager() {
		loadKillList();
	}
	
//	@EventHandler
//	public void onClick(InventoryClickEvent event) {
//		Bukkit.broadcastMessage(""+event.getSlot());
//	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("Decimate.staff.test")) {
			addHumanEntity((Player) sender);
		}
		return false;
	}

	public boolean addHumanEntity(Player player) {
		if(!players.containsKey(player.getUniqueId().toString())) {
			CombatPlayer combat = new CombatPlayer(player);
			players.put(player.getUniqueId().toString(), combat);
			entities.put(combat.getId(), combat);
			return true;
		}
		
		return false;
	}

	public boolean removeHumanEntity(OfflinePlayer player) {
		if(players.containsKey(player.getUniqueId().toString())) {
			entities.remove(players.get(player.getUniqueId().toString()).getId());
			players.get(player.getUniqueId().toString()).remove();
			players.remove(player.getUniqueId().toString());
			return true;
		}
		
		return false;
	}
	
	public boolean addToList(OfflinePlayer player) {
		if(!killList.contains(player.getUniqueId().toString())) {
			killList.add(player.getUniqueId().toString());
			return true;
		}
		
		return false;
	}
	
	public boolean removeFromList(OfflinePlayer player) {
		if(killList.contains(player.getUniqueId().toString())) {
			killList.remove(player.getUniqueId().toString());
			return true;
		}
		
		return false;
	}
	
	public void remove(CombatPlayer cp) {
		entities.remove(cp.getId());
		players.remove(cp.getUUID(), cp);
		cp.remove();
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if(entities.containsKey(event.getEntity().getEntityId())) {
			CombatPlayer cp = entities.get(event.getEntity().getEntityId());
			cp.onDeath(event);
			remove(cp);
		}
	}
	
	@EventHandler
	public void onKillListJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(killList.contains(player.getUniqueId().toString())) {
			player.getInventory().clear();
			player.damage(Double.MAX_VALUE);
			removeFromList(player);
		}
		if(players.containsKey(player.getUniqueId().toString())) {
			remove(players.get(player.getUniqueId().toString()));
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(!player.hasMetadata("LogoutCommand")) {
			addHumanEntity(player);
		}
	}

	private void loadKillList() {
		Configuration cfg = new Configuration(DecimateCore.getCore(), "KillList.yml");
		FileConfiguration config = cfg.getData();
		
		this.killList = config.getStringList("KillList");
	}

	@Override
	public void disable() {
		Configuration cfg = new Configuration(DecimateCore.getCore(), "KillList.yml");
		cfg.reset();
		FileConfiguration config = cfg.getData();
		
		config.set("KillList", killList);
		
		for(CombatPlayer cp : players.values()) {
			cp.remove();
		}
	}

}
