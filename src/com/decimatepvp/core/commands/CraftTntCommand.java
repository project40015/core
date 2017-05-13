package com.decimatepvp.core.commands;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.DecimateCore;

public class CraftTntCommand implements CommandExecutor {

	private final int GUNPOWDER = 5, SAND = 4;
	
	private DecimateCore core;

	private DecimalFormat df;
	
	private double cost;
	
	public CraftTntCommand() {
		this.core = DecimateCore.getCore();
		this.df = new DecimalFormat("0.00");
		this.cost = core.config.COST_OF_SAND;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			
			if(player.hasPermission("Decimate.command.tnt")) {
				Inventory inv = player.getInventory();

				int totalPowder = getTotal(inv, Material.SULPHUR);

				if(totalPowder < (64 * GUNPOWDER)*2) {
					if(hasBalance(player, (SAND*cost))) {
						if(totalPowder >= (64 * GUNPOWDER)) {
							ItemStack tnt = new ItemStack(Material.TNT, 64);
							removeType(inv, Material.SULPHUR, (64*GUNPOWDER));
							if(inv.firstEmpty() != -1) {
								inv.addItem(tnt);
							}
							else {
								player.getWorld().dropItem(player.getLocation(), tnt);
							}
							player.sendMessage(ChatColor.GREEN + "You have crafted 64 tnt charging you " + df.format(SAND*cost) + "!");
							core.eco.withdrawPlayer(player, SAND*cost);
						}
						else {
							player.sendMessage(ChatColor.RED + "You do not have enough gunpowder.");
						}
					}
					else {
						player.sendMessage(ChatColor.RED + "You do not have sufficient funds.");
					}
				}
				else {
					int tntCrafted = 0;
					for(int i = 0; i < 25; i++) {
						if(hasBalance(player, (SAND*cost))) {
							if(totalPowder >= (64 * GUNPOWDER)) {
								ItemStack tnt = new ItemStack(Material.TNT, 64);
								removeType(inv, Material.SULPHUR, (64*GUNPOWDER));
								if(inv.firstEmpty() != -1) {
									inv.addItem(tnt);
								}
								else {
									player.getWorld().dropItem(player.getLocation(), tnt);
								}
								tntCrafted += tnt.getAmount();
								totalPowder -= (64*GUNPOWDER);
								core.eco.withdrawPlayer(player, (SAND*cost));
							}
						}
						else {
							break;
						}
					}
					player.sendMessage(ChatColor.GREEN + "You have crafted " + tntCrafted + " tnt charging you $" +
									df.format((tntCrafted/64) * (SAND*cost)) + "!");
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + "You do not have the proper permissions.");
			}
		}
		else {
			sender.sendMessage("Only players may use this command.");
		}
		
		return false;
	}

	private boolean hasBalance(Player player, double d) {
		return core.eco.has(player, d);
	}

	private void removeType(Inventory inv, Material material, final int amount) {
		for(ItemStack item : inv.getContents()) {
			if(item != null) {
				if(item.getType() == material) {
					if(item.getAmount() <= amount) {
						inv.remove(item);
					}
					else {
						item.setAmount (item.getAmount()-amount);
					}
				}
			}
		}
	}

	private int getTotal(Inventory inv, Material material) {
		int total = 0;
		for(ItemStack item : inv) {
			if(item != null) {
				if(item.getType() == material) {
					total += item.getAmount();
				}
			}
		}
		return total;
	}
	
}
