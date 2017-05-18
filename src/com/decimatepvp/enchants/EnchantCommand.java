package com.decimatepvp.enchants;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.DecimateCore;

public class EnchantCommand implements CommandExecutor {
	
	private DecimateCore core = DecimateCore.getCore();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("Decimate.enchant.apply")) {
				ItemStack hand = player.getItemInHand();
				if((hand != null) && (hand.getType() != Material.AIR)) {
					if(args.length == 2) {
						String enchant = args[0];
						if(StringUtils.isNumeric(args[1])) {
							int level = (int) Double.parseDouble(args[1]);
							if(core.getEnchantManager().addEnchantToItem(hand, enchant, level)) {
								sender.sendMessage(ChatColor.GOLD + "Item successfully enchanted!");
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
