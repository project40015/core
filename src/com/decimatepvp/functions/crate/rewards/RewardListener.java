package com.decimatepvp.functions.crate.rewards;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

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
	
}
