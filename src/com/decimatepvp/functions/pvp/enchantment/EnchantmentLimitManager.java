package com.decimatepvp.functions.pvp.enchantment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantmentLimitManager implements Listener {

	private List<EnchantmentLimit> limit = new ArrayList<>();

	public EnchantmentLimitManager() {
		fillLimits();
	}

	private void fillLimits() {
		limit.add(new EnchantmentLimit(Enchantment.FIRE_ASPECT, 0));
		limit.add(new EnchantmentLimit(Enchantment.DAMAGE_ALL, 2));
	}

	private boolean isLimited(Enchantment enchantment, int level) {
		for (EnchantmentLimit lim : limit) {
			if (lim.getEnchantment().equals(enchantment)
					&& lim.getLevel() > level) {
				return true;
			}
		}
		return false;
	}

	private int getLimit(Enchantment enchantment) {
		for (EnchantmentLimit lim : limit) {
			if (lim.getEnchantment().equals(enchantment)) {
				return lim.getLevel();
			}
		}
		return -1;
	}

	@EventHandler
	public void onEnchant(EnchantItemEvent event) {
		List<Enchantment> rm = new ArrayList<>();
		for (Enchantment enchant : event.getEnchantsToAdd().keySet()) {
			if (isLimited(enchant, event.getEnchantsToAdd().get(enchant))) {
				if (getLimit(enchant) != 0) {
					event.getEnchantsToAdd().put(enchant, getLimit(enchant));
				} else {
					rm.add(enchant);
				}
			}
		}
		rm.forEach(e -> event.getEnchantsToAdd().remove(e));
		rm.forEach(e -> event.getItem().removeEnchantment(e));
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		// check whether the event has been cancelled by another plugin
		if (!e.isCancelled()) {
			HumanEntity ent = e.getWhoClicked();

			// not really necessary
			if (ent instanceof Player) {
				Player player = (Player) ent;
				Inventory inv = e.getInventory();

				// see if we are talking about an anvil here
				if (inv instanceof AnvilInventory) {
					AnvilInventory anvil = (AnvilInventory) inv;
					InventoryView view = e.getView();
					int rawSlot = e.getRawSlot();

					// compare raw slot to the inventory view to make sure we
					// are in the upper inventory
					if (rawSlot == view.convertSlot(rawSlot)) {
						// 2 = result slot
						if (rawSlot == 2) {
							// all three items in the anvil inventory
							ItemStack[] items = anvil.getContents();

							// item in the left slot
							ItemStack item1 = items[0];

							// item in the right slot
							ItemStack item2 = items[1];

							// I do not know if this is necessary
							if (item1 != null && item2 != null) {
								int id1 = item1.getTypeId();
								int id2 = item2.getTypeId();

								// if the player is repairing something the ids
								// will be the same
//								if (id1 != 0 && id1 == id2) {
									// item in the result slot
									ItemStack item3 = e.getCurrentItem();

									// check if there is an item in the result
									// slot
									if (item3 != null) {
										ItemMeta meta = item3.getItemMeta();

										// meta data could be null
										if (meta != null) {
											// get the repairable interface to
											// obtain the repair cost
											
											List<Enchantment> rm = new ArrayList<>();

											final Map<Enchantment, Integer> map = item3.getEnchantments();
											for(Enchantment enchant : map.keySet()){
												if(isLimited(enchant, item3.getEnchantmentLevel(enchant))){
													if(getLimit(enchant) != 0){
														item3.removeEnchantment(enchant);
														item3.addEnchantment(enchant, this.getLimit(enchant));
													}else{
														rm.add(enchant);
													}
												}
											}
											rm.forEach(en -> item3.getEnchantments().remove(en));

											
//											if (meta instanceof Repairable) {
//												Repairable repairable = (Repairable) meta;
//												int repairCost = repairable
//														.getRepairCost();
//
//												// can the player afford to
//												// repair the item
//												if (player.getLevel() >= repairCost) {
//													// success
//												} else {
//													// bugger
//												}
//											}
										}
//									}
								}
							}
						}
					}
				}
			}
		}
	}

}
