package com.decimatepvp.core.commands;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.utils.ItemUtils;
import com.google.common.collect.Maps;

public class BottleExpCommand implements CommandExecutor, Listener {
	
	private Map<String, Integer> thrower = Maps.newHashMap();
	private Map<Integer, Integer> exp = Maps.newHashMap();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("Decimate.misc.bottle")) {
				int exp = (int) player.getTotalExperience();
				ItemStack bottleExp = bottleExp(player);
				
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
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void getExpInBottle(PlayerInteractEvent event) {
		if((event.getAction() == Action.RIGHT_CLICK_AIR) ||
				(event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			Player player = event.getPlayer();
			ItemStack hand = event.getItem();
			
			if((hand != null) && (hand.getType() == Material.EXP_BOTTLE)) {
				thrower.put(player.getName(), getExpFromItemStack(hand));
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
		player.setTotalExperience(0);
		
		return bottle;
	}
	
}
