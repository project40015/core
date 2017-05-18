package com.decimatepvp.enchants;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
		if(args.length == 1) {
			String arg = args[0].toLowerCase();
			for(String enchant : enchants) {
				if(enchant.toLowerCase().startsWith(arg)) {
					completer.add(enchant);
				}
			}
			
			return completer;
		}
		else if(args.length == 0) {
			return enchants;
		}
		
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("Decimate.enchant.apply")) {
				ItemStack hand = player.getItemInHand();
				if((hand != null) && (hand.getType() != Material.AIR)) {
					if(args.length == 2) {
						String enchant = args[0];
						if(core.getEnchantManager().isEnchant(enchant)) {
							if(StringUtils.isNumeric(args[1])) {
								int level = (int) Double.parseDouble(args[1]);
								if(core.getEnchantManager().addEnchantToItem(hand, enchant, level)) {
									sender.sendMessage(ChatColor.GOLD + "Item successfully enchanted!");
									player.updateInventory();
								}
								else {
									sender.sendMessage(ChatColor.RED + "This enchantment cannot be set on this item!");
								}
							}
							else {
								sender.sendMessage(ChatColor.RED + "You must enter a proper number.");
							}
						}
						else {
							sender.sendMessage(ChatColor.RED + "That is not a proper enchantment.");
						}
					}
					else {
						sender.sendMessage(ChatColor.GOLD + "Proper Usage: /applyenchant [enchant] [level]");
					}
				}
				else {
					sender.sendMessage(ChatColor.RED + "You must be holding an item!");
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + "You do not have the proper permissions.");
			}
		}
		else {
			sender.sendMessage(ChatColor.RED + "Only players may use this command.");
		}
		return false;
	}

}
