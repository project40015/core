package com.decimatepvp.functions.enchants;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.decimatepvp.utils.ItemUtils;
import com.google.common.collect.Maps;

public class AnvilManager implements Manager, Listener {

	private DecimateCore core = DecimateCore.getCore();
	
	public static AnvilManager instance = new AnvilManager();
	public HashMap<UUID, AnvilUpdateTask> tasks = Maps.newHashMap();
	
	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true)
	public void onHandleAnvil(InventoryClickEvent event) {
		if (event.getInventory().getType() != InventoryType.ANVIL) {
			return;
		}
		AnvilInventory anvilinv = (AnvilInventory) event.getInventory();
		ItemStack[] anvilitems = anvilinv.getContents();
		if (anvilitems.length == 0 || anvilitems.length == 1) {
			return;
		}
		ItemStack slot1 = anvilitems[0];
		ItemStack slot2 = anvilitems[1];
		if (slot1 == null || slot2 == null) {
			return;
		}
		
		if (slot1.getType() == Material.AIR || slot2.getType() == Material.AIR) {
		  return;
		}
		ItemStack slot3 = slot1.clone();
		
		if((slot1.getType().toString().contains("SWORD")) &&
				ItemUtils.isItemCloned(slot2, core.getExpBoostManager().getXpBook())) {
			core.getExpBoostManager().addToWeapon(slot3);
			
			anvilinv.setItem(2, slot3);
		}
		
		if(((event.isShiftClick()) || (event.isRightClick())) && (event.getSlotType() == InventoryType.SlotType.RESULT) && 
	    	      (slot2.getType() == Material.ENCHANTED_BOOK)) {
			slot2.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 0);
			event.setCursor(slot3);
			anvilinv.clear();
			
			return;
		}
	}
  
	@EventHandler
	public void onOpenInventory(InventoryOpenEvent event) {
		Inventory inv = event.getInventory();
		if (inv.getType() != InventoryType.ANVIL) {
			return;
		}
		
		Player p = (Player)event.getPlayer();
		this.tasks.put(p.getUniqueId(), new AnvilUpdateTask(p, inv));
	}
  
	@EventHandler
	public void onCloseInventory(InventoryCloseEvent event) {
		Inventory inv = event.getInventory();
		if (inv.getType() != InventoryType.ANVIL) {
			return;
		}
		Player p = (Player)event.getPlayer();
		this.tasks.remove(p.getUniqueId());
	}
	
	public void setAnvilExp(Player p, Inventory inventory, int cost) {
		AnvilUpdateTask task = (AnvilUpdateTask)this.tasks.get(p.getUniqueId());
		task.setExp(p);
	}

	@Override
	public void disable() {
		for(BukkitRunnable br : tasks.values()) {
			br.cancel();
		}
	}
}