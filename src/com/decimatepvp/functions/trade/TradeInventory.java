package com.decimatepvp.functions.trade;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.utils.ItemUtils;

import net.milkbowl.vault.economy.Economy;

public class TradeInventory  {
	
	private Economy eco;
	
	private TradeManager manager;
	
	private final ItemStack bar = ItemUtils.createItem(Material.IRON_FENCE, 1, " ");

	private DecimalFormat df = new DecimalFormat("&2$00.00");

	private Player player1, player2;
	private Inventory trade1, trade2;
	private boolean ready1 = false, ready2 = false;
	private int money1 = 0, money2 = 0;
	
	public TradeInventory(TradeManager manager, Player player1, Player player2) {
		this.eco = DecimateCore.getCore().getEconomy();
		this.manager = manager;
		this.player1 = player1;
		this.player2 = player2;
		
		trade1 = Bukkit.createInventory(null, 54, ChatColor.YELLOW + "Trade with " + player2.getName());

		trade1.setItem(36, ItemUtils.createItem(Material.REDSTONE, 1, "&cDecline Trade"));
		trade1.setItem(30, ItemUtils.createItem(Material.INK_SACK, 1, (byte) 8, "&7Ready?"));
		trade1.setItem(38, ItemUtils.createItem(Material.STAINED_CLAY, 1, (byte) 5, "&aAdd $1000"));
		trade1.setItem(39, ItemUtils.createItem(Material.STAINED_CLAY, 1, (byte) 14, "&cRemove $1000"));
		trade1.setItem(45, ItemUtils.createItem(Material.GOLD_BLOCK, 1, (byte) 0, "&6Money Trading", "&2$0.00"));
		trade1.setItem(46, ItemUtils.createItem(Material.REDSTONE_BLOCK, 1, "&4Reset Money"));
		trade1.setItem(47, ItemUtils.createItem(Material.WOOL, 1, (byte) 5, "&aAdd $100"));
		trade1.setItem(48, ItemUtils.createItem(Material.WOOL, 1, (byte) 14, "&cRemove $100"));
		
		trade1.setItem(32, ItemUtils.createItem(Material.INK_SACK, 1, (byte) 8, "&7Not ready"));
		trade1.setItem(41, ItemUtils.createItem(Material.GOLD_BLOCK, 1, (byte) 0, "&6Money Trading", "&2$0.00"));

		trade2 = Bukkit.createInventory(null, 54, ChatColor.YELLOW + "Trade with " + player1.getName());
		
		trade2.setContents(trade1.getContents().clone());
		
		for(int slot = 4; slot < 54; slot += 9) {
			trade1.setItem(slot, bar);
			trade2.setItem(slot, bar);
		}
	}

	/**
	 * Syncs the inventories of trade1 and trade2
	 */
	public void sync() {
		syncReady();
		syncItems();
		syncMoney();

		player1.updateInventory();
		player2.updateInventory();
	}
	
	private void syncMoney() {
		trade1.setItem(45, ItemUtils.createItem(Material.GOLD_BLOCK, 1, (byte) 0,
				"&6Money Trading", df.format(money1)));
		trade1.setItem(41, ItemUtils.createItem(Material.GOLD_BLOCK, 1, (byte) 0,
				"&6Money Trading", df.format(money2)));

		trade2.setItem(45, ItemUtils.createItem(Material.GOLD_BLOCK, 1, (byte) 0,
				"&6Money Trading", df.format(money2)));
		trade2.setItem(41, ItemUtils.createItem(Material.GOLD_BLOCK, 1, (byte) 0,
				"&6Money Trading", df.format(money1)));
	}

	private void syncItems() {
		for(int slot : manager.itemSlots.keySet()) {
			ItemStack item1 = trade1.getItem(slot);
			ItemStack item2 = trade2.getItem(slot);

			trade1.setItem(manager.itemSlots.get(slot), item2);
			trade2.setItem(manager.itemSlots.get(slot), item1);
		}
	}

	private void syncReady() {
		if(ready1) {
			trade1.setItem(30, ItemUtils.createItem(Material.INK_SACK, 1, (byte) 10, "&aReady!"));
			trade2.setItem(32, ItemUtils.createItem(Material.INK_SACK, 1, (byte) 10, "&aReady!"));
		}
		else {
			trade1.setItem(30, ItemUtils.createItem(Material.INK_SACK, 1, (byte) 8, "&7Not ready"));
			trade2.setItem(32, ItemUtils.createItem(Material.INK_SACK, 1, (byte) 8, "&7Not ready"));
		}
		
		if(ready2) {
			trade2.setItem(30, ItemUtils.createItem(Material.INK_SACK, 1, (byte) 10, "&aReady!"));
			trade1.setItem(32, ItemUtils.createItem(Material.INK_SACK, 1, (byte) 10, "&aReady!"));
		}
		else {
			trade2.setItem(30, ItemUtils.createItem(Material.INK_SACK, 1, (byte) 8, "&7Not ready"));
			trade1.setItem(32, ItemUtils.createItem(Material.INK_SACK, 1, (byte) 8, "&7Not ready"));
		}
	}
	
	public void addMoney(int i, int money) {
		if(i == 1) {
			money1 += 100;
		}
		else {
			money2 += 100;
		}
	}
	
	public void removeMoney(int i, int money) {
		if(i == 1) {
			money1 = (int) Math.min(Math.max(0, money1 - money), eco.getBalance(player1));
		}
		else {
			money2 = (int) Math.min(Math.max(0, money1 - money), eco.getBalance(player2));
		}
	}

	public Inventory getTrade1() {
		return trade1;
	}
	
	public Inventory getTrade2() {
		return trade2;
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public boolean isReady(int i) {
		return i == 1 ? ready1 : ready2;
	}

	public void setReady(int i, boolean bool) {
		if(i == 1) {
			this.ready1 = bool;
		}
		else {
			this.ready2 = bool;
		}
	}
	
	public void resetMoney(int i) {
		if(i == 1) {
			this.money1 = 0;
		}
		else {
			this.money2 = 0;
		}
	}

	public void openInventories() {
		player1.openInventory(trade1);
		player2.openInventory(trade2);
	}
	
	public void endTrade() {
		for(int slot : manager.itemSlots.keySet()) {
			ItemStack i1 = trade1.getItem(slot);
			ItemStack i2 = trade2.getItem(slot);

			if(player1.getInventory().firstEmpty() != -1) {
				player1.getInventory().addItem(i1);
			}
			else {
				player1.getWorld().dropItem(player1.getLocation(), i1);
			}

			if(player2.getInventory().firstEmpty() != -1) {
				player2.getInventory().addItem(i2);
			}
			else {
				player2.getWorld().dropItem(player2.getLocation(), i2);
			}
		}
	}
	
}
