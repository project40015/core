package com.decimatepvp.functions.trade;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.google.common.collect.Maps;

public class TradeManager implements Manager, Listener, CommandExecutor {

	private Map<Player, TradeInventory> trades1 = Maps.newHashMap(); //Trade1 of TradeInventory
	private Map<Player, TradeInventory> trades2 = Maps.newHashMap(); //Trade2 of TradeInventory
	
	/**
	 * Player is the one being requested
	 */
	private Map<OfflinePlayer, TradeRequest> tradeRequests = Maps.newHashMap(); 
	
	public Map<Integer, Integer> itemSlots = Maps.newHashMap();
	
	public TradeManager() {
		createSlots();
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event){
		if(!(event.getPlayer() instanceof Player)){
			return;
		}
		Player player = (Player) event.getPlayer();
		if(trades1.containsKey(player) ||
				trades2.containsKey(player)) {
			TradeInventory trade = trades1.containsKey(player) ?
					trades1.remove(player) :
						trades2.remove(player);
			trade.endTrade(false);
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		if(trades1.containsKey(player) ||
				trades2.containsKey(player)) {
			TradeInventory trade = trades1.containsKey(player) ?
					trades1.get(player) :
						trades2.get(player);
					trade.endTrade(false);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onTradeClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(trades1.containsKey(player) ||
				trades2.containsKey(player)) {
			int slot = event.getRawSlot();
			
//			Bukkit.broadcastMessage(""+(!itemSlots.containsKey(slot) && slot < 54));

			TradeInventory trade = trades1.containsKey(player) ?
					trades1.get(player) :
						trades2.get(player);

			if(!itemSlots.containsKey(slot) && slot < 54) {
				event.setCancelled(true);
			}
			else {
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DecimateCore.getCore(), new Runnable(){

					@Override
					public void run() {
						trade.sync(true);
					}
					
				}, 4);
				return;
			}
			
			int i = (player == trade.getPlayer1() ? 1 : 2);
			
			if(slot == 47) { //Add $100
				trade.addMoney(i, 100);
			}
			else if(slot == 48) { //Remove $100
				trade.removeMoney(i, 100);
			}
			else if(slot == 38) { //Adds $1000
				trade.addMoney(i, 1000);
			}
			else if(slot == 39) { //Removes $1000
				trade.removeMoney(i, 1000);
			}
			else if(slot == 30) { // Toggles ready
				trade.setReady(i, !trade.isReady(i));
			}
			else if(slot == 46) { //Resets money
				trade.resetMoney(i);
			}else if(slot == 36) {
				trade.endTrade(false);
				return;
			}
			
			trade.sync(false);
			
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.hasPermission("Decimate.trade")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				
				if(args.length == 0) {
					sender.sendMessage(ChatColor.RED + "Invalid syntax, try" +ChatColor.YELLOW + " /trade (accept/decline/player)");
				}
				else {
					String arg = args[0];
					
					if(arg.equalsIgnoreCase("accept")) {
						if(tradeRequests.containsKey(player)) {
							accept(player);
						}
						else {
							sender.sendMessage(ChatColor.RED + "You have no trade requests!");
						}
					}
					else if(arg.equalsIgnoreCase("decline")) {
						if(tradeRequests.containsKey(player)) {
							decline(player);
						}
						else {
							sender.sendMessage(ChatColor.RED + "You have no trade requests!");
						}
					}
					else if(arg.length() >= 3) {
						OfflinePlayer sendee = Bukkit.getOfflinePlayer(args[0]);
						
						if(sendee.isOnline()) {
							request(player, sendee.getPlayer());
						}
						else {
							sender.sendMessage(ChatColor.RED + "Player is not online.");
						}
					}
					else {
						sender.sendMessage(ChatColor.RED + "Invalid syntax, try" +ChatColor.YELLOW + " /trade (accept/decline/player)");
					}
				}
			}
			else {
				sender.sendMessage("Only players with inventories may use this.");
			}
		}
		else {
			sender.sendMessage(ChatColor.RED + "You do not have the proper permissions to use this.");
		}
		
		return false;
	}

	public void request(Player sender, Player sendee) {
		TradeRequest request = new TradeRequest(this, sender, sendee);
		
		tradeRequests.put(sendee, request);
		
		request.send();
	}

	public void accept(Player player) {
		TradeRequest request = tradeRequests.get(player);
		request.accept();
		tradeRequests.remove(player);

		TradeInventory trade = new TradeInventory(this, request.getSender(), player);
		
		trades1.put(trade.getPlayer1(), trade);
		trades2.put(trade.getPlayer2(), trade);
		
		trade.openInventories();
	}

	public void decline(OfflinePlayer player) {
		tradeRequests.get(player).decline();
		tradeRequests.remove(player);
	}

	@Override
	public void disable() {
		for(TradeInventory manager : trades1.values()) {
			manager.endTrade(false);
		}
	}
	
	private void createSlots() {
		itemSlots.put(0, 8);
		itemSlots.put(1, 7);
		itemSlots.put(2, 6);
		itemSlots.put(3, 5);

		itemSlots.put(9, 17);
		itemSlots.put(10, 16);
		itemSlots.put(11, 15);
		itemSlots.put(12, 14);

		itemSlots.put(18, 26);
		itemSlots.put(19, 25);
		itemSlots.put(20, 24);
		itemSlots.put(21, 23);
	}

}
