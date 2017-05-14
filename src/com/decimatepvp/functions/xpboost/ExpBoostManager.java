package com.decimatepvp.functions.xpboost;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.core.Manager;
import com.decimatepvp.utils.ItemUtils;
import com.google.common.collect.Lists;

public class ExpBoostManager implements Manager, Listener {
	
	private final String ENCHANT_NAME = "Xp Boost";
	
	private final ItemStack xpBook;
	
	public ExpBoostManager() {
		xpBook = ItemUtils.createItem(Material.ENCHANTED_BOOK, 1, (byte) 0,
				"&b&lXp Boost", "&e* &bAdding this enchant to a &b&lSword &bwill double the exp given!");
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBookDrag(InventoryClickEvent event) {
		if(event.getAction() == InventoryAction.SWAP_WITH_CURSOR) {
			ItemStack dragged = event.getCursor();
			if(ItemUtils.isItemCloned(dragged, xpBook)) {
				ItemStack item = event.getCurrentItem();
				event.setCursor(null);
				addToWeapon(item);
			}
		}
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		LivingEntity living = event.getEntity();
		Player killer = living.getKiller();
		if(killer != null) {
			ItemStack hand = killer.getItemInHand();
			if(doesWeaponHaveEnchant(hand)) {
				int exp = event.getDroppedExp() * 2;
				event.setDroppedExp(exp);
			}
		}
	}
	
	public boolean addToWeapon(ItemStack item) {
		if(item.getType().toString().contains("SWORD") && !doesWeaponHaveEnchant(item)) {
			ItemMeta meta = item.getItemMeta();
			List<String> lore = Lists.newArrayList();
			lore.add(ChatColor.DARK_GRAY + ENCHANT_NAME);
			if(meta.hasLore()) {
				lore.addAll(meta.getLore());
			}
			meta.setLore(lore);
			item.setItemMeta(meta);
			return true;
		}
		return false;
	}
	
	public boolean doesWeaponHaveEnchant(ItemStack item) {
		if(item.getItemMeta().hasLore()) {
			for(String lore : item.getItemMeta().getLore()) {
				lore = ChatColor.stripColor(lore);
				if(lore.equals(ENCHANT_NAME)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void giveXpBook(Player player) {
		player.getInventory().addItem(getXpBook());
	}
	
	public ItemStack getXpBook() {
		return xpBook.clone();
	}

	@Override
	public void disable() {
		
	}

}
