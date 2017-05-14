package com.decimatepvp.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
<<<<<<< HEAD
import org.bukkit.entity.ThrownExpBottle;
=======
>>>>>>> origin/master
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
<<<<<<< HEAD
=======
import org.bukkit.event.player.PlayerInteractEvent;
>>>>>>> origin/master
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.utils.ExperienceUtils;
import com.decimatepvp.utils.ItemUtils;

public class BottleExpCommand implements CommandExecutor, Listener {
<<<<<<< HEAD
=======
	
	private Map<String, Integer> thrower = Maps.newHashMap();
	private Map<Integer, Integer> exp = Maps.newHashMap();
>>>>>>> origin/master

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { 
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("Decimate.misc.bottle")) {
<<<<<<< HEAD
				int exp = ExperienceUtils.getTotalExperience(player);
				ItemStack bottleExp = bottleExp(player, exp);
=======
				int exp = player.getTotalExperience();
				ItemStack bottleExp = bottleExp(player);
>>>>>>> origin/master
				
				if(player.getInventory().firstEmpty() == -1) {
					player.getWorld().dropItem(player.getLocation(), bottleExp);
				}
				else {
					player.getInventory().addItem(bottleExp);
				}
				
				sender.sendMessage(ChatColor.GREEN + "You have bottled " + exp + " exp!");
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

<<<<<<< HEAD
	private ItemStack bottleExp(Player player, int exp) {
		ItemStack bottle = ItemUtils.createItem(Material.EXP_BOTTLE, 1, (byte) 0, "&6&lBottled EXP",
				"&bThrowing this bottle will release &a&l" + exp + " &bexp!");
		player.setLevel(0);
=======
	@EventHandler
	public void bottleHitEvent(ExpBottleEvent event) {
		Projectile entity = event.getEntity();
		if((entity.getShooter() != null) &&
				(entity.getShooter() instanceof Player)) {
			if(exp.containsKey(entity.getEntityId())) {
				event.setExperience(exp.get(entity.getEntityId()));
				exp.remove(entity.getEntityId());
			}
		}
	}

	@EventHandler
	public void bottleThrownEvent(ProjectileLaunchEvent event) {
		Projectile entity = event.getEntity();
		if((entity.getShooter() != null) &&
				(entity.getType() == EntityType.THROWN_EXP_BOTTLE) &&
				(entity.getShooter() instanceof Player)) {
			Player player = (Player) entity.getShooter();
			if(thrower.containsKey(player.getName())) {
				exp.put(entity.getEntityId(), getExpFromBottle(player));
				thrower.remove(player.getName());
			}
		}
	}
	
	private int getExpFromBottle(Player player) {
		return thrower.get(player.getName());
	}

	private ItemStack bottleExp(Player player) {
		ItemStack bottle = ItemUtils.createItem(Material.EXP_BOTTLE, 1, (byte) 0, "&6lBottled EXP",
				"&bThrowing this bottle will release &a&l" + player.getTotalExperience() + " &bexp!");
>>>>>>> origin/master
		player.setExp(0);
		player.setTotalExperience(0);
		
		return bottle;
	}
	
}
