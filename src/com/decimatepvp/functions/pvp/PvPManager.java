package com.decimatepvp.functions.pvp;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.decimatepvp.core.utils.Configuration;
import com.decimatepvp.utils.PlayerUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PvPManager implements Manager, Listener, CommandExecutor {

	private final long DAMAGE_TIME = 200;

	private Map<Player, Long> pvp = Maps.newHashMap();

	private List<String> killList = Lists.newArrayList();
	private Map<String, CombatPlayer> players = Maps.newHashMap();
	private Map<Integer, CombatPlayer> entities = Maps.newHashMap();

	public PvPManager() {
		loadKillList();

		loadCombatRunnable();
	}

	private void loadCombatRunnable() {
		BukkitRunnable br = new BukkitRunnable() {

			@Override
			public void run() {
				for(Entry<Player, Long> set : pvp.entrySet()) {
					Player player = set.getKey();
					long time = set.getValue();
					time -= 10;

					if(time > 0) {
						pvp.put(player, time);
					}
					else {
						player.sendMessage(ChatColor.GREEN + "You have been taken out of combat!");
						pvp.remove(player);
					}
				}
			}
		};

		br.runTaskTimer(DecimateCore.getCore(), 0l, 10l);
	}

	public boolean isPlayerInCombat(Player player) {
		return pvp.containsKey(player);
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if((event.getDamager() instanceof Player) && (event.getEntity() instanceof Player)) {
			Player damagee = (Player) event.getEntity();
			if(!isPlayerInCombat(damagee)) {
				damagee.sendMessage(ChatColor.RED + "You have been put into combat!");
			}
			pvp.put(damagee, DAMAGE_TIME);
		}
	}

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

	public boolean addToKillList(OfflinePlayer player) {
		if(!killList.contains(player.getUniqueId().toString())) {
			killList.add(player.getUniqueId().toString());
			return true;
		}

		return false;
	}

	public boolean removeFromKillList(OfflinePlayer player) {
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
	public void onEntityDamage(EntityDamageEvent event) {
		if(this.entities.containsKey(event.getEntity().getEntityId())) {
			event.setCancelled(true);
			if(!event.getCause().equals(DamageCause.ENTITY_ATTACK)) {
				return;
			}
			LivingEntity livingEntity = (LivingEntity) event.getEntity();
			livingEntity.damage(event.getDamage());
			livingEntity.playEffect(EntityEffect.HURT);
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {

		if(entities.containsKey(event.getEntity().getEntityId())) {
			CombatPlayer cp = entities.get(event.getEntity().getEntityId());
			killList.add(cp.getUUID());
			cp.onDeath(event);
			remove(cp);
		}
	}

	@EventHandler
	public void onKillListJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(killList.contains(player.getUniqueId().toString())) {
			player.getInventory().clear();
			player.getInventory().setHelmet(new ItemStack(Material.AIR));
			player.getInventory().setChestplate(new ItemStack(Material.AIR));
			player.getInventory().setLeggings(new ItemStack(Material.AIR));
			player.getInventory().setBoots(new ItemStack(Material.AIR));
			player.setHealth(0);
			removeFromKillList(player);
		}
		if(players.containsKey(player.getUniqueId().toString())) {
			remove(players.get(player.getUniqueId().toString()));
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(!player.hasMetadata("LogoutCommand")) {
			if(!shouldCancelLogger(player)) {
				addHumanEntity(player);
			}
		}
	}

	private boolean shouldCancelLogger(Player player) {
		if(player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) {
			return true;
		}
		if(PlayerUtils.isInSpawn(player)) {
			return true;
		}
		return false;
	}

	private void loadKillList() {
		Configuration cfg = new Configuration(DecimateCore.getCore(), "KillList.yml");
		FileConfiguration config = cfg.getData();

		this.killList = Lists.newArrayList();
		this.killList.addAll(config.getStringList("KillList"));
	}

	@Override
	public void disable() {
		Configuration cfg = new Configuration(DecimateCore.getCore(), "KillList.yml");
		cfg.reset();
		FileConfiguration config = cfg.getData();

		config.set("KillList", killList);
		cfg.saveData();

		for(CombatPlayer cp : players.values()) {
			cp.remove();
		}

	}

}
