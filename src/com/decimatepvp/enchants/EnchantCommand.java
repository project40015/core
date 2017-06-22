package com.decimatepvp.enchants;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.DecimateCore;
import com.google.common.collect.Lists;

public class EnchantCommand implements CommandExecutor, TabCompleter {
	
	private DecimateCore core = DecimateCore.getCore();

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> enchants = Lists.newArrayList(core.getEnchantManager().getCustomEnchants().keySet());
		List<String> completer = Lists.newArrayList();
		if(args.length == 2) {
			String arg = args[1].toLowerCase();
			for(String enchant : enchants) {
				enchant = ChatColor.stripColor(enchant);
				if(enchant.toLowerCase().startsWith(arg)) {
					completer.add(enchant);
				}
			}
			Collections.sort(completer);
			return completer;
		}
		
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//		if(sender instanceof Player) {
//			Player player = (Player) sender;
			if(sender.hasPermission("Decimate.enchant.book")) {
				if(args.length == 3) {
					OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
					if(op.isOnline()) {
						String enchant = args[1];
						if(core.getEnchantManager().isEnchant(enchant)) {
							if(StringUtils.isNumeric(args[2])) {
								int level = (int) Double.parseDouble(args[2]);
								if(level <= core.getEnchantManager().getEnchant(enchant).getEnchantMaxLevel()) {
									ItemStack book = core.getEnchantManager().getEnchantedBook(enchant, level);
									if(book != null) {
										sender.sendMessage(ChatColor.GOLD + "You have been given an enchanted book!");
										giveBook(book, op.getPlayer());
										op.getPlayer().updateInventory();
									}
									else {
										sender.sendMessage(ChatColor.RED + "This enchantment cannot be set on this item!");
									}
								}
								else {
									sender.sendMessage(ChatColor.RED + "Level is too high for enchant.");
								}
							}
							else {
								sender.sendMessage(ChatColor.RED + "You must enter a proper level.");
							}
						}
						else {
							sender.sendMessage(ChatColor.RED + "That is not a proper enchantment.");
							String enchantments = "";
							for(CustomEnchant ce : core.getEnchantManager().getAllEnchants()) {
								enchantments += ChatColor.AQUA + ce.getEnchantName() + ChatColor.DARK_AQUA + ", ";
							}
							enchantments = enchantments.substring(0, enchantments.length() - 2);
							sender.sendMessage(enchantments);
						}
					}
					else {
						sender.sendMessage(ChatColor.RED + "That player is not online.");
					}
				}
				else {
					sender.sendMessage(ChatColor.GOLD + "Proper Usage: /giveenchantment [player] [enchant] [level]");
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + "You do not have the proper permissions.");
			}
//		}
//		else {
//			sender.sendMessage(ChatColor.RED + "Only players may use this command.");
//		}
		return false;
	}

	private void giveBook(ItemStack book, Player player) {
		if(player.getInventory().firstEmpty() != -1) {
			player.getInventory().addItem(book);
		}
		else {
			player.getWorld().dropItem(player.getLocation(), book);
		}
	}

}