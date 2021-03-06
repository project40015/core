package com.decimatepvp.minievents;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.decimatepvp.utils.FactionUtils;
import com.decimatepvp.utils.PlayerUtils;
import com.massivecraft.factions.entity.Faction;

import decimatenetworkcore.core.DataUser;
import decimatenetworkcore.core.DecimateNetworkCore;

public class MiniEvents implements Listener {
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onZombieSpawn(SpawnerSpawnEvent event) {
		if(event.getEntityType() == EntityType.ZOMBIE) {
			if(event.getEntity().getVehicle() != null) {
				event.getEntity().getVehicle().remove();
			}
		}
	}
	
	//Statistic Tracking:
	@EventHandler
	public void onStatBlockPlace(BlockPlaceEvent event){
		if(!event.isCancelled()){
			DataUser du = DecimateNetworkCore.getInstance().getDataUserManager().getDataUser(event.getPlayer().getUniqueId().toString());
			du.setBlocksPlaced(du.getBlocksPlaced() + 1);
		}
	}
	
	@EventHandler
	public void onStatBlockBreak(BlockBreakEvent event){
		if(!event.isCancelled()){
			DataUser du = DecimateNetworkCore.getInstance().getDataUserManager().getDataUser(event.getPlayer().getUniqueId().toString());
			du.setBlocksBroken(du.getBlocksBroken() + 1);
		}
	}
	
	@EventHandler
	public void onStatKill(EntityDeathEvent event){
		if(event.getEntity() instanceof Player){
			DataUser ddu = DecimateNetworkCore.getInstance().getDataUserManager().getDataUser(event.getEntity().getUniqueId().toString());
			ddu.setDeaths(ddu.getDeaths() + 1);
		}
		if(event.getEntity().getKiller() == null){
			return;
		}
		Player player = null;
		if(event.getEntity().getKiller() instanceof Player){
			player = (Player) event.getEntity().getKiller();
		}else if(event.getEntity().getKiller() instanceof Projectile){
			if(((Projectile)event.getEntity().getKiller()).getShooter() instanceof Player){
				player = (Player) ((Projectile)event.getEntity().getKiller()).getShooter();
			}
		}
		if(player != null){
			DataUser du = DecimateNetworkCore.getInstance().getDataUserManager().getDataUser(player.getUniqueId().toString());
			if(event.getEntity() instanceof Player){
				du.setPlayersKilled(du.getPlayersKilled() + 1);
			}else if(event.getEntity() instanceof LivingEntity){
				du.setMobsKilled(du.getMobsKilled() + 1);
			}
		}
	}
	
//	@SuppressWarnings("deprecation")
//	@EventHandler(priority = EventPriority.HIGHEST)
//	public void onMobSpawn(PlayerInteractEvent event) {
//		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
//			Player player = event.getPlayer();
//			ItemStack hand = player.getItemInHand();
//			if((hand != null) && (true)) { //hand.getType() == Material.MONSTER_EGG
//				Block eventBlock = event.getClickedBlock().getRelative(event.getBlockFace());
//				Block actualBlock = player.getLastTwoTargetBlocks((HashSet<Byte>) null, 10).get(1)
//						.getRelative(PlayerUtils.getBlockFace(player));
//				if(!eventBlock.equals(actualBlock)) {
//					Bukkit.broadcastMessage("Cancelled!");
//					event.setCancelled(true);
//				}
//				else {
//					Bukkit.broadcastMessage("Not Cancelled!");
//				}
//			}
//		}
//	}
	
//	@EventHandler
//	public void removeFireResistance(PotionSplashEvent event) {
//		for(PotionEffect pe : Lists.newArrayList(event.getPotion().getEffects())) {
//			if(pe.getType() == PotionEffectType.FIRE_RESISTANCE) {
//				event.getPotion().getEffects().remove(pe);
//			}
//		}
//	}
//	
//	@EventHandler
//	public void removeFireResistance(PlayerItemConsumeEvent event) {
//		ItemStack item = event.getItem();
//		if(item.getType() == Material.POTION) {
//			Potion potion = Potion.fromItemStack(item);
//			for(PotionEffect pe : Lists.newArrayList(potion.getEffects())) {
//				if(pe.getType() == PotionEffectType.FIRE_RESISTANCE) {
//					potion.getEffects().remove(pe);
//				}
//			}
//		}
//	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		event.setJoinMessage("");
		Player player = event.getPlayer();
		PlayerUtils.sendTitle(player, "DECIMATEPVP", 1*10, 2*10, 1*10, ChatColor.LIGHT_PURPLE);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		event.setQuitMessage("");
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event){
		event.setLeaveMessage("");
	}
	
	@EventHandler
	public void onTarget(EntityTargetLivingEntityEvent event){
		if(event.getTarget() instanceof Player){
			if(event.getEntity() instanceof Monster && !event.getEntity().equals(EntityType.WITHER)){
				event.setCancelled(true);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onTable(InventoryOpenEvent event){
		if(event.getInventory().getType().equals(InventoryType.ENCHANTING)){
			event.getInventory().setItem(1, new ItemStack(Material.getMaterial(351), 3, (byte)4));
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event){
		if(event.getInventory().getType().equals(InventoryType.ENCHANTING)){
			event.getInventory().setItem(1, new ItemStack(Material.AIR));
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(InventoryClickEvent event){
		if(event.getInventory().getType().equals(InventoryType.ENCHANTING)){
			if(event.getCurrentItem() == null || event.getCurrentItem().getType() == null){
				return;
			}
			if(event.getCurrentItem().getType().equals(Material.getMaterial(351))){
				event.setCancelled(true);
			}
		}
	}
	
//	@EventHandler
//	public void onMove(PlayerMoveEvent event){
//		if(event.getPlayer().getName().equalsIgnoreCase("_Ug")){
//			NumberFormat formatter = new DecimalFormat("#0.00");
//			double d = event.getTo().distance(event.getFrom())*2 - event.getPlayer().getVelocity().length();
//			PlayerUtils.sendActionbar(event.getPlayer(), (d < 0 ? ChatColor.RED.toString() : ChatColor.GREEN.toString() + "+") + formatter.format(d));
//		}
//	}
	
	@EventHandler
	public void onSpawn(CreatureSpawnEvent event){
		if(event.getSpawnReason().equals(SpawnReason.BUILD_WITHER) ||
				event.getSpawnReason().equals(SpawnReason.CHUNK_GEN) ||
				event.getSpawnReason().equals(SpawnReason.NATURAL)){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onChange(WeatherChangeEvent event){
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event){
		if(event.getBlock().getLocation().getBlockY() == event.getPlayer().getLocation().getBlockY() - 1 &&
				event.getBlock().getLocation().getBlockX() == event.getPlayer().getLocation().getBlockX() &&
				event.getBlock().getLocation().getBlockZ() == event.getPlayer().getLocation().getBlockZ()){
			if(PlayerUtils.isInSpawn(event.getPlayer())) {
				return;
			}
			Faction faction = FactionUtils.getFactionByLoc(event.getBlock().getLocation()); 
			if(faction.equals(FactionUtils.getWilderness())) {
				return;
			}
			if(faction.getOnlinePlayers().contains(event.getPlayer())) {
				return;
			}
			if(event.getPlayer().getGameMode() == GameMode.CREATIVE){
				return;
			}
			event.getPlayer().setVelocity(new Vector(0, -1, 0));
		}
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Creeper){
			if(event.getEntity().getLocation().clone().add(0,1,0).getBlock().getType().isSolid()){
				event.getEntity().setVelocity(event.getEntity().getVelocity().setY(-1));
			}
		}
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event){
		if(event.getCause().equals(TeleportCause.ENDER_PEARL)){
			return;
		}
		if(!event.getPlayer().hasPermission("Decimate.fly-disable-bypass")){
			event.getPlayer().setFallDistance(0);
			if(event.getPlayer().isFlying()){
				event.getPlayer().setFlying(false);
			}
			if(event.getPlayer().getAllowFlight()){
				event.getPlayer().setAllowFlight(false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onDeath(EntityDeathEvent event){
		if(event.getEntity() instanceof Chicken){
			event.getDrops().add(new ItemStack(Material.EGG, event.getDrops().size()/2));
		}else if(event.getEntity() instanceof Creeper){
			if(Math.random() <= 0.25){
				event.getDrops().add(new ItemStack(Material.TNT, (int)Math.ceil(event.getDrops().size()/2.0)));
			}
		}
	}
	
}
