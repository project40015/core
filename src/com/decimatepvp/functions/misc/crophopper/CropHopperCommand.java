package com.decimatepvp.functions.misc.crophopper;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.decimatepvp.core.DecimateCore;

public class CropHopperCommand implements CommandExecutor {

	private DecimateCore core;

	public CropHopperCommand() {
		core = DecimateCore.getCore();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Command for players only.");
			return false;
		}
		Player player = (Player) sender;

		if (player.getItemInHand() == null
				|| !player.getItemInHand().getType().equals(Material.HOPPER)) {
			player.sendMessage(ChatColor.RED
					+ "You must be holding hoppers to use this command.");
			return false;
		}
		if (player.getItemInHand().getItemMeta() != null
				&& player.getItemInHand().getItemMeta().getDisplayName() != null && !player
						.getItemInHand()
						.getItemMeta()
						.getDisplayName()
						.equals(ChatColor.stripColor(player.getItemInHand()
								.getItemMeta().getDisplayName()))) {
			player.sendMessage(ChatColor.RED + "You cannot use this hopper.");
			return false;
		}
		boolean cropHopper = true;
		if(label.equalsIgnoreCase("mobhopper")){
			cropHopper = false;
		}
		int amount = player.getItemInHand().getAmount();
		int cost = (cropHopper ? 25000 : 1000000)  * amount;
		if (!core.eco.has(player, cost)) {
			player.sendMessage(ChatColor.RED + "You do not have the required $"
					+ cost + " for " + amount + " " + (cropHopper ? "crop" : "mob")  + " hopper(s).");
			return false;
		}
		core.eco.withdrawPlayer(player, cost);
		core.getCropHopperManager().giveCropHopperHand(player, cropHopper, amount);
		player.sendMessage(ChatColor.GREEN + "You bought " + amount
				+ " " + (cropHopper ? "crop" : "mob")  + " hopper(s) for $" + cost + "!");
		player.playSound(player.getLocation(), Sound.ANVIL_USE, 1, 1);
		return false;
	}

}
