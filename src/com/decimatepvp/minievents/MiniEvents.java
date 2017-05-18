package com.decimatepvp.minievents;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.utils.PlayerUtils;

public class MiniEvents implements Listener {
	
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
		PlayerUtils.sendTitle(event.getPlayer(), "DECIMATEPVP", 1*20, 2*20, 1*20, ChatColor.LIGHT_PURPLE);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		event.setQuitMessage("");
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event){
		event.setLeaveMessage("");
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
	
	@EventHandler
	public void onClick(InventoryClickEvent event){
		if(event.getInventory().getType().equals(InventoryType.ENCHANTING)){
			if(event.getSlot() == 1){
				event.setCancelled(true);
				return;
			}
		}
		if(event.getCurrentItem() != null && event.getCurrentItem().getType().equals(Material.INK_SACK)){
			if(event.getAction().equals(InventoryAction.COLLECT_TO_CURSOR) ||
					event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)){
				event.setCancelled(true);
			}
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
	
	@EventHandler
	public void onDeath(EntityDeathEvent event){
		if(event.getEntity() instanceof Chicken){
			event.getDrops().add(new ItemStack(Material.EGG));
		}else if(event.getEntity() instanceof Creeper){
			if(Math.random() <= 0.25){
				event.getDrops().add(new ItemStack(Material.TNT));
			}
		}
	}
	
}
