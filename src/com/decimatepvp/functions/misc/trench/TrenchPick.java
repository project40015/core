package com.decimatepvp.functions.misc.trench;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.utils.FactionUtils;
import com.decimatepvp.utils.ItemUtils;
import com.massivecraft.factions.Faction;

public class TrenchPick implements Listener, CommandExecutor{
	
	private static final int RADIUS = 1;
	
	private final ItemStack pickaxe;
	private final String lore = ChatColor.YELLOW + "Mines all blocks in a 3x3 area around the block.";
	
	public TrenchPick() {
		pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
		pickaxe.addEnchantment(Enchantment.DIG_SPEED, 5);
		pickaxe.addEnchantment(Enchantment.DURABILITY, 3);
		ItemUtils.setDisplayName(pickaxe, "&l&eTrench Pickaxe");
		ItemUtils.setLore(pickaxe, new String[] { lore });
	}
	
	private BlockFace getBlockFace(double yaw, double pitch){
		if(yaw > 0){
			yaw -= 360;
		}
//		yaw = yaw + 180;
		if(pitch < -45){
			return BlockFace.UP;
		}
		if(pitch > 45){
			return BlockFace.DOWN;
		}
		if(between(yaw, -135, -45)){
			return BlockFace.EAST;
		}
		if(between(yaw, -225, -135)){
			return BlockFace.NORTH;
		}
		if(between(yaw, -315, -225)){
			return BlockFace.WEST;
		}
		return BlockFace.SOUTH;
	}
	
	private boolean between(double n, int low, int high){
		return n >= low && n <= high;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event) {
		ItemStack item = event.getPlayer().getItemInHand();
		if(isTrenchPickaxe(item) && !event.isCancelled()) {
			Player player = event.getPlayer();
			Faction faction = FactionUtils.getFactionByLoc(event.getBlock().getLocation()); 
			if(faction.isWilderness() || FactionUtils.getFaction(player).equals(faction)){
				breakArea(event.getBlock().getRelative(getBlockFace(event.getPlayer().getLocation().getYaw(),
						event.getPlayer().getLocation().getPitch())).getLocation().clone());
			}
		}
	}
	
	private void breakArea(Location add) {
		for(int x = -RADIUS; x <= RADIUS; x++) {
			for(int y = -RADIUS; y <= RADIUS; y++) {
				for(int z = -RADIUS; z <= RADIUS; z++) {
					Location location = add.clone().add(x, y, z);
					if(location.getBlock().getType().equals(Material.BEDROCK)){
						continue;
					}
					Block block = location.getBlock();
					block.breakNaturally(pickaxe);
				}
			}
		}
	}

	public boolean isTrenchPickaxe(ItemStack item) {
		if(item == null){
			return false;
		}
		return (item.hasItemMeta() ? item.getItemMeta().hasLore() ? 
				item.getItemMeta().getLore().contains(lore)
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
