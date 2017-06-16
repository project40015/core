package com.decimatepvp.core.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.decimatepvp.core.DecimateCore;

public class KillRewardListener implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent event){
		if(event.getEntity().getKiller() != null && event.getEntity().getKiller() instanceof Player){
			Player killer = (Player) event.getEntity().getKiller();
			double amount = DecimateCore.getCore().eco.getBalance(event.getEntity()) * 0.03;
			int an = (int) (amount * 100);
			double cash = an/100.0;
			if(cash == 0){
				return;
			}
			
			DecimateCore.getCore().eco.depositPlayer(killer, cash);
			DecimateCore.getCore().eco.withdrawPlayer(event.getEntity(), cash);
			
			killer.sendMessage(ChatColor.GRAY + "You stole " + ChatColor.RED + "$" + cash + ChatColor.GRAY + " from " + ChatColor.RED + event.getEntity().getName() + ChatColor.GRAY + "!");
			event.getEntity().sendMessage(ChatColor.GRAY + "You lost " + ChatColor.RED + "$" + cash + ChatColor.GRAY + " to " + ChatColor.RED + event.getEntity().getName() + ChatColor.GRAY + "!");
		}
	}
	
}
