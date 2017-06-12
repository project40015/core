package com.decimatepvp.functions.misc.trench;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.utils.ItemUtils;

public class TrenchPick implements Listener, CommandExecutor{
	
	private static final int RADIUS = 2;
	
	private final ItemStack pickaxe;
	
	public TrenchPick() {
		pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
		pickaxe.addEnchantment(Enchantment.DIG_SPEED, 5);
		pickaxe.addEnchantment(Enchantment.DURABILITY, 3);
		ItemUtils.setDisplayName(pickaxe, "&l&eTrench Pickaxe");
		ItemUtils.setLore(pickaxe, new String[] { "&eMines all blocks in a 5x5 area around the block." });
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		ItemStack item = event.getPlayer().getItemInHand();
		if(isTrenchPickaxe(item) && !event.isCancelled()) {
			breakArea(event.getBlock().getLocation().add(0.5D, 0.5D, 0.5D));
		}
	}
	
	private void breakArea(Location add) {
		for(int x = -RADIUS; x <= RADIUS; x++) {
			for(int y = -RADIUS; y <= RADIUS; y++) {
				for(int z = -RADIUS; z <= RADIUS; z++) {
					Location location = add.clone().add(x, y, z);
					location.getBlock().breakNaturally(pickaxe);
				}
			}
		}
	}

	public boolean isTrenchPickaxe(ItemStack item) {
		if(item == null){
			return false;
		}
		return (item.hasItemMeta() ? item.getItemMeta().hasLore() ? 
				item.getItemMeta().getLore().get(0).equals("Mines all blocks in a 5x5 area around the block.")
				: false : false);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("Decimate.trench.pickaxe")) {
			if(args.length == 1) {
				OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
				if(op.isOnline()) {
					Player player = op.getPlayer();
					give(player);
					sender.sendMessage(ChatColor.GOLD + "Player has been given a Trench Pickaxe.");
				}
				else {
					sender.sendMessage(ChatColor.RED + "Player is not online.");
				}
			}
			else {
				sender.sendMessage(ChatColor.GOLD + "Usage: /trenchpickaxe [player]");
			}
		}
		return false;
	}

	private void give(Player player) {
		if(player.getInventory().firstEmpty() != -1) {
			player.getInventory().addItem(pickaxe.clone());
		}
		else {
			player.getWorld().dropItem(player.getLocation(), pickaxe.clone());
		}
	}

}
