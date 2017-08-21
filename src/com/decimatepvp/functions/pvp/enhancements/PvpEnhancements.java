package com.decimatepvp.functions.pvp.enhancements;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.decimatepvp.core.DecimateCore;

public class PvpEnhancements implements Listener {

	@EventHandler
	public void onEnchant(EnchantItemEvent event){
		if(event.getEnchantsToAdd().containsKey(Enchantment.FIRE_ASPECT)){
			event.getEnchantsToAdd().remove(Enchantment.FIRE_ASPECT);
			if(event.getEnchantsToAdd().size() == 0){
				event.getEnchantsToAdd().put(Enchantment.DAMAGE_ALL, 1);
			}
//			if(event.getItem().getType().equals(Material.FISHING_ROD)){
//				double d = Math.random()*100;
//				ItemMeta im = event.getItem().getItemMeta();
//				List<String> lore = im.getLore() == null ? new ArrayList<>() : im.getLore();
//				if(d < 2 && event.getExpLevelCost() >= 30){
//					lore.add(ChatColor.GRAY + "Harpoon III");
//				}else if(d < 4 && event.getExpLevelCost() >= 10){
//					lore.add(ChatColor.GRAY + "Harpoon II");
//				}else if(d < 6){
//					lore.add(ChatColor.GRAY + "Harpoon I");
//				}
//			}
		}
	}
	
	@EventHandler
	public void onFish(PlayerFishEvent event){
		if(event.getCaught() instanceof Player){
			Player caught = (Player) event.getCaught();
			ItemStack hand = event.getPlayer().getItemInHand();
			double mult = 0;
			if(hand != null && hand.getItemMeta() != null && hand.getItemMeta().getLore() != null){
				if(hand.getItemMeta().getLore().contains(ChatColor.GRAY + "Harpoon III")){
					mult = 0.5;
				}else if(hand.getItemMeta().getLore().contains(ChatColor.GRAY + "Harpoon II")){
					mult = 0.25;
				}else if(hand.getItemMeta().getLore().contains(ChatColor.GRAY + "Harpoon I")){
					mult = 0.1;
				}
			}
			if(mult == 0){
				return;
			}
			Vector v = new Vector(-1*caught.getLocation().getX() + event.getPlayer().getLocation().getX(),
					-1*caught.getLocation().getY() + event.getPlayer().getLocation().getY(),
					-1*caught.getLocation().getZ() + event.getPlayer().getLocation().getZ());
			v.normalize().multiply(mult);
			caught.setVelocity(caught.getVelocity().add(v));
		}
	}
	
	@EventHandler
	public void onRod(EntityDamageByEntityEvent event){
		if(event.isCancelled()){
			return;
		}
		if(event.getDamager().getType().equals(EntityType.FISHING_HOOK)){
			if(event.getEntity() instanceof Player){
				Player hit = (Player) event.getEntity();
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DecimateCore.getCore(), new Runnable(){

					@Override
					public void run() {
						Vector velocity = hit.getVelocity();
						velocity.setX(velocity.getX()/1.15);
						velocity.setZ(velocity.getZ()/1.15);
						velocity.setY(velocity.getY()+0.05);
						hit.setVelocity(velocity);
					}
					
				}, 1);
			}
		}
	}
	
}
