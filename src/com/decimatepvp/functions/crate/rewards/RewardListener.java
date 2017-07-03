package com.decimatepvp.functions.crate.rewards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.utils.FactionUtils;
import com.decimatepvp.utils.PlayerUtils;

public class RewardListener implements Listener {

	@EventHandler
	public void onDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player p = (Player) event.getEntity();
			if(p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getItemMeta() != null &&
					p.getInventory().getHelmet().getItemMeta().getLore() != null){
				if(p.getInventory().getHelmet().getItemMeta().getLore().contains(ChatColor.GRAY + "Aestas's Enchant I")){
					if(p.getLocation().getBlock().getLightFromSky() == 15 &&
							p.getLocation().getWorld().getTime() >= 0 &&
							p.getLocation().getWorld().getTime() <= 12000){
						event.setDamage(event.getDamage()*0.8);
						p.getWorld().playSound(p.getLocation(), Sound.DIG_SAND, 1, 1);
					}
				}
			}
		}
	}
	
	private HashMap<Integer, Player> arrowIds = new HashMap<>();
	
	@EventHandler
	public void onLaunch(ProjectileLaunchEvent event){
		if(event.getEntity().getShooter() instanceof Player){
			ItemStack hand = ((Player)event.getEntity().getShooter()).getItemInHand();
			if(hand != null && hand.getItemMeta() != null && hand.getItemMeta().getLore() != null
					&& hand.getItemMeta().getLore().contains(ChatColor.GRAY + "Freedom I")){
				if(!PlayerUtils.isInSpawn((Player)event.getEntity().getShooter())){
					arrowIds.put(event.getEntity().getEntityId(), (Player)event.getEntity().getShooter());
				}
			}
		}
	}
	
	@EventHandler
	public void onDamage(ProjectileHitEvent event){
		if(event.getEntity().getType().equals(EntityType.ARROW)){
			if(arrowIds.containsKey(event.getEntity().getEntityId())){
				if(Math.random() < 0.35){
					return;
				}
				if(!PlayerUtils.isInSpawn(event.getEntity().getLocation())){
					for(Entity entity : event.getEntity().getNearbyEntities(4, 4, 4)){
						if(!(entity instanceof Damageable)){
							continue;
						}
						if(entity instanceof Player){
							if(FactionUtils.getFaction(arrowIds.get(event.getEntity().getEntityId())).equals(FactionUtils.getFaction((Player)entity))){
								continue;
							}
						}
						((Damageable)entity).damage(2);
					}
				}
				arrowIds.remove((Object)event.getEntity().getEntityId());
				FireworkEffect effect = FireworkEffect.builder().trail(false).flicker(false).withColor(Color.RED).withColor(Color.WHITE).withColor(Color.BLUE).with(FireworkEffect.Type.BALL).build();
				final Firework fw = event.getEntity().getWorld().spawn(event.getEntity().getLocation(), Firework.class);
				FireworkMeta meta = fw.getFireworkMeta();
				meta.addEffect(effect);
				meta.setPower(0);
				fw.setFireworkMeta(meta);
				new BukkitRunnable() {
				    @Override
				    public void run() {
				      fw.detonate();
				    }
				}.runTaskLater(DecimateCore.getCore(), 2L);
			}
		}
	}
	
}
