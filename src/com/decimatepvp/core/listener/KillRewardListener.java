package com.decimatepvp.core.listener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.utils.Skull;

public class KillRewardListener implements CommandExecutor, Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent event){
		if(event.getEntity().getKiller() != null && event.getEntity().getKiller() instanceof Player){
			Player killer = (Player) event.getEntity().getKiller();
			double amount = DecimateCore.getCore().eco.getBalance(event.getEntity()) * 0.03;
			
			DecimalFormat formatter = new DecimalFormat("#,###.00");
			
			DecimateCore.getCore().eco.withdrawPlayer(event.getEntity(), amount);
			
//			killer.sendMessage(ChatColor.GRAY + "You stole " + ChatColor.RED + "$" + cash + ChatColor.GRAY + " from " + ChatColor.RED + event.getEntity().getName() + ChatColor.GRAY + "!");
//			event.getEntity().sendMessage(ChatColor.GRAY + "You lost " + ChatColor.RED + "$" + cash + ChatColor.GRAY + " to " + ChatColor.RED + killer.getName() + ChatColor.GRAY + "!");
			
			ItemStack head = Skull.getPlayerSkull(event.getEntity().getName());
			ItemMeta hm = head.getItemMeta();
			hm.setDisplayName(ChatColor.GOLD + event.getEntity().getName() + "'s Head");
			List<String> lore = new ArrayList<>();
			lore.add("");
			lore.add(ChatColor.GRAY + "Value:" + ChatColor.RED + " $" + formatter.format(amount));
			hm.setLore(lore);
			head.setItemMeta(hm);
			event.getDrops().add(head);
		}
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(arg0 instanceof Player){
			Player player = (Player) arg0;
			if(player.getInventory().getItemInHand() != null &&
					player.getInventory().getItemInHand().getItemMeta() != null
					&& player.getInventory().getItemInHand().getItemMeta().getDisplayName() != null
					&& player.getInventory().getItemInHand().getItemMeta().getLore() != null &&
					player.getInventory().getItemInHand().getItemMeta().getLore().size() >= 2){
				String headLore = player.getInventory().getItemInHand().getItemMeta().getLore().get(1);
//				Bukkit.broadcastMessage(headLore);
				if(headLore.startsWith(ChatColor.GRAY + "Value:" + ChatColor.RED + " $")){
					headLore = ChatColor.stripColor(headLore.substring(12));
					headLore = headLore.replaceAll(",", "");
//					Bukkit.broadcastMessage(headLore);
					try{
						double n = Double.valueOf(headLore) * player.getInventory().getItemInHand().getAmount();
						DecimateCore.getCore().eco.depositPlayer(player, n);
						player.sendMessage(ChatColor.GRAY + "You have deposited " + ChatColor.YELLOW + "$" + n + ChatColor.GRAY + "!");
						player.getInventory().setItemInHand(new ItemStack(Material.AIR));
						return false;
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
			player.sendMessage(ChatColor.RED + "You must be holding a sellable head.");
		}
		return false;
	}
	
}
