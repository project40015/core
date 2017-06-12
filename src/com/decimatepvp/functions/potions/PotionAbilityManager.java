package com.decimatepvp.functions.potions;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.abilities.AbstractPotionAbility;
import com.decimatepvp.abilities.ShockwavePotion;
import com.decimatepvp.core.DecimateCore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PotionAbilityManager implements Listener, CommandExecutor, TabCompleter {
	
	private Map<String, AbstractPotionAbility> potionsString = Maps.newHashMap();
	
	private Map<Integer, AbstractPotionAbility> potionsId = Maps.newHashMap();
	
	public PotionAbilityManager() {
		registerPotion(new ShockwavePotion());
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPotionThrow(ProjectileLaunchEvent event) {
		Projectile proj = event.getEntity();
		if((proj.getShooter() != null) &&
				(proj.getShooter() instanceof Player)) {
			Player player = (Player) proj.getShooter();
			int slot = player.getInventory().getHeldItemSlot();
			ItemStack hand = player.getItemInHand().clone();
			if((hand != null) && (hand.getType() == Material.POTION)) {
				if(hand.containsEnchantment(Enchantment.ARROW_INFINITE)) {
					Location loc = player.getLocation();
					if(loc.getX() > -75 && loc.getX() < 77){
						if(loc.getY() > 64){
							if(loc.getZ() > -75 && loc.getZ() < 77){
								if(!player.getGameMode().equals(GameMode.CREATIVE)){
									Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DecimateCore.getCore(), new Runnable(){

										@Override
										public void run() {
											hand.setAmount(hand.getAmount() + 1);
											player.getInventory().setItem(slot, hand);
										}
										
									}, 1);
								}
								event.getEntity().remove();
								return;
							}
						}
					}
					int id = hand.getEnchantmentLevel(Enchantment.ARROW_INFINITE);
					proj.setCustomName("Custom Effect Id: " + id);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPotionSplash(PotionSplashEvent event) {
		Projectile proj = event.getEntity();
		if((proj.getShooter() != null) &&
				(proj.getShooter() instanceof Player)) {
			if((proj.getCustomName() != null) &&
					(proj.getCustomName().startsWith("Custom Effect Id: "))) {
				String name = proj.getCustomName().substring(18, proj.getCustomName().length());
				int id = Integer.parseInt(name);
				if(isPotionReal(id)) {
					AbstractPotionAbility potion = getPotion(id);
					potion.onCrash(event);
				}
			}
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> potions = Lists.newArrayList(potionsString.keySet());
		List<String> completer = Lists.newArrayList();
		if(args.length == 1) {
			String arg = args[0].toLowerCase();
			for(String start : potions) {
				if(start.toLowerCase().startsWith(arg.toLowerCase())) {
					completer.add(start);
				}
			}
			
			return completer;
		}
		else if(args.length == 0) {
			return potions;
		}
		
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.hasPermission("Decimate.potions.give")) {
			if(args.length == 2) {
				AbstractPotionAbility potion = null;
				if((NumberUtils.isNumber(args[0])) && (isPotionReal(Integer.parseInt(args[0])))) {
					potion = getPotion(Integer.parseInt(args[0]));
				}
				else if(isPotionReal(args[0])) {
					potion = getPotion(args[0]);
				}
				else {
					sender.sendMessage(ChatColor.RED + "That is not a valid potion.");
					return true;
				}
				
				OfflinePlayer plyr = Bukkit.getOfflinePlayer(args[1]);
				if(plyr.isOnline()) {
					plyr.getPlayer().getInventory().addItem(potion.toItemStack(1));
					sender.sendMessage(ChatColor.GREEN + "Potion " + ChatColor.GOLD + potion.getName() +
							ChatColor.GREEN + " has been received!");
					return true;
				}
				else {
					sender.sendMessage(ChatColor.RED + "Player is not online.");
					return true;
				}
			}
			else {
				sender.sendMessage(ChatColor.GOLD + "Proper Usage: /potionability [potion] [player]");
				return true;
			}
		}
		else {
			sender.sendMessage(ChatColor.RED + "You do not have the proper permissions.");
			return true;
		}
	}
	
	public AbstractPotionAbility getPotion(String string) {
		return potionsString.get(string);
	}
	
	public AbstractPotionAbility getPotion(int id) {
		return potionsId.get(id);
	}

	public boolean isPotionReal(String potion) {
		return potionsString.containsKey(potion);
	}

	public boolean isPotionReal(int potion) {
		return potionsId.containsKey(potion);
	}

	private void registerPotion(AbstractPotionAbility... potions) {
		for(AbstractPotionAbility potion : potions) {
			this.potionsString.put(potion.getName(), potion);
			this.potionsId.put(potion.getItemId(), potion);
		}
	}
	
}
