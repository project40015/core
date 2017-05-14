package com.decimatepvp.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.utils.ExperienceUtils;
import com.decimatepvp.utils.ItemUtils;

public class BottleExpCommand implements CommandExecutor, Listener {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { 
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("Decimate.misc.bottle")) {
				if(player.getLevel() >= 1){
					int exp = ExperienceUtils.getTotalExperience(player);
					ItemStack bottleExp = bottleExp(player, exp);
				
					if(player.getInventory().firstEmpty() == -1) {
						player.getWorld().dropItem(player.getLocation(), bottleExp);
					}
					else {
						player.getInventory().addItem(bottleExp);
					}
				
					sender.sendMessage(ChatColor.GREEN + "You have bottled " + exp + " exp!");
				}else{
					player.sendMessage(ChatColor.RED + "You do not have the minimum 1 level to bottle!");
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + "You do not have the proper permissions to use this.");
			}
		}
		else {
			sender.sendMessage(ChatColor.RED + "Only players may use this command.");
		}
		
		return false;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onBottleThrow(ProjectileLaunchEvent event) {
		Projectile proj = event.getEntity();
		if((proj.getShooter() != null) &&
				(proj.getShooter() instanceof Player)) {
			Player player = (Player) proj.getShooter();
			int exp = getExpFromItemStack(player.getItemInHand());
			proj.setCustomName("" + exp);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBottleCrash(ExpBottleEvent event) {
		ThrownExpBottle bottle = event.getEntity();
		if((bottle.getShooter() != null) &&
				(bottle.getShooter() instanceof Player)) {
			if(bottle.getCustomName() != null) {
				event.setExperience(Integer.parseInt(bottle.getCustomName()));
			}
		}
	}
	
	private Integer getExpFromItemStack(ItemStack hand) {
		int exp = 0;
		try {
			exp = Integer.parseInt(ChatColor.stripColor(hand.getItemMeta().getLore().get(0))
					.replace("Throwing this bottle will release ", "").replace(" exp!", ""));
		}
		catch(Exception e) { }
		
		return exp;
	}

	private ItemStack bottleExp(Player player, int exp) {
		ItemStack bottle = ItemUtils.createItem(Material.EXP_BOTTLE, 1, (byte) 0, "&6Bottled EXP",
				"&bThrowing this bottle will release &a&l" + exp + " &bexp!");
		player.setLevel(0);
		player.setExp(0);
		player.setTotalExperience(0);
		
		return bottle;
	}
	
}