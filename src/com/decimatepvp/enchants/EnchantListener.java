package com.decimatepvp.enchants;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.enchants.types.CustomAttackEnchant;
import com.decimatepvp.enchants.types.CustomClickEnchant;
import com.decimatepvp.enchants.types.CustomDamagedEnchant;
import com.decimatepvp.enchants.types.CustomEquipEnchant;
import com.decimatepvp.enchants.types.CustomKillerEnchant;
import com.decimatepvp.events.PlayerDequipEvent;
import com.decimatepvp.events.PlayerEquipEvent;

public class EnchantListener implements Listener {

	private static final DecimateCore core = DecimateCore.getCore();

//	@SuppressWarnings("deprecation")
//	@EventHandler
//	public void onBookEnchantdd(InventoryClickEvent event) {
//		ItemStack cursor = event.getCursor();
//		ItemStack clicked = event.getCurrentItem();
//		if (cursor == null || clicked == null) {
//			return;
//		}
//		if (event.getClick() == ClickType.SHIFT_LEFT) {
//			if (core.getEnchantManager().isEnchantedBook(cursor)) {
//				CustomEnchant enchantment = core.getEnchantManager()
//						.getEnchantById(cursor.getEnchantmentLevel(Enchantment.ARROW_FIRE));
//				if (enchantment.isItemApplicable(clicked)) {
//					core.getEnchantManager().addEnchantToItem(clicked, enchantment.getEnchantName(),
//							cursor.getEnchantmentLevel(Enchantment.ARROW_DAMAGE));
//					cursor.setAmount(0);
//					event.setCursor(null);
//					if (event.getWhoClicked() instanceof Player) {
//						Player player = (Player) event.getWhoClicked();
//						player.playSound(player.getLocation(), Sound.ANVIL_USE, 1, 1);
//					}
//					event.setCancelled(true);
//				}
//			}
//		}
//	}
	
	@EventHandler
	public void onAnvilClick(InventoryClickEvent event) {
		if(event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			if(event.getInventory().getType() == InventoryType.ANVIL ||
					player.getOpenInventory().getType() == InventoryType.ANVIL) {

				AnvilInventory ai =
						player.getOpenInventory().getType() == InventoryType.ANVIL ?
								(AnvilInventory) player.getOpenInventory().getTopInventory() :
									(AnvilInventory) event.getInventory();

				new BukkitRunnable() {
					
					@Override
					public void run() {
						ItemStack a = ai.getItem(0);
						ItemStack b = ai.getItem(1);
	//					if(slot == 0){
	//						a = e.getCursor();
	//					}
	//					if(slot == 1){
	//						b = e.getCursor();
	//					}
						if(a == null || b == null){
							return;
						}
						
						if(!core.getEnchantManager().isEnchantedBook(b)){
							return;
						}
						
						CustomEnchant enchantment = core.getEnchantManager().
								getEnchantById(b.getEnchantmentLevel(Enchantment.ARROW_FIRE));

						
						if(!enchantment.isItemApplicable(a)){
							return;
						}
						
						ItemStack result = a.clone();
						core.getEnchantManager().addEnchantToItem(result,
								enchantment.getEnchantName(), b.getEnchantmentLevel(Enchantment.ARROW_DAMAGE));

						ai.setItem(2, result);
						player.updateInventory();
						
	//					if(slot == 2){
	//						ItemStack air = new ItemStack(Material.AIR);
	//						event.setCurrentItem(air);
	//						ai.setItem(0, air);
	//						ai.setItem(1, air);
	//						player.setItemOnCursor(result);
	//					}
					}
				}.runTaskLater(core, 1l);
				
			}
		}
	}
	
//	@EventHandler
//	public void onBookEnchant(InventoryClickEvent e) {
//		if (e.getWhoClicked() instanceof Player) {
//			if (e.getView().getType() == InventoryType.ANVIL ||
//					((Player)e.getWhoClicked()).getOpenInventory().getType() == InventoryType.ANVIL) {
//				AnvilInventory anvilInv;
//				if(e.getView().getType() == InventoryType.ANVIL) {
//					anvilInv = (AnvilInventory) e.getInventory();
//				}
//				else {
//					anvilInv = (AnvilInventory) ((Player)e.getWhoClicked()).getOpenInventory();
//				}
//				int slot = e.getRawSlot();				
//
//				if (slot == 2 || slot == 1 || slot == 0 || e.getView().getType() != InventoryType.ANVIL) {
//				
//					
//					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DecimateCore.getCore(), new Runnable(){
//
//						@Override
//						public void run() {
//							ItemStack a = anvilInv.getItem(0);
//							ItemStack b = anvilInv.getItem(1);
////							if(slot == 0){
////								a = e.getCursor();
////							}
////							if(slot == 1){
////								b = e.getCursor();
////							}
//							if(a == null || b == null){
//								return;
//							}
//							
//							if(!core.getEnchantManager().isEnchantedBook(b)){
//								return;
//							}
//							
//							CustomEnchant enchantment = core.getEnchantManager().getEnchantById(b.getEnchantmentLevel(Enchantment.ARROW_FIRE));
//							
//							if(!enchantment.isItemApplicable(a)){
//								return;
//							}
//							
//							ItemStack result = a.clone();
//							core.getEnchantManager().addEnchantToItem(result, enchantment.getEnchantName(), b.getEnchantmentLevel(Enchantment.ARROW_DAMAGE));
//
//							anvilInv.setItem(2, result);
//							((Player)e.getWhoClicked()).updateInventory();
//							
//							if(slot == 2){
//								ItemStack air = new ItemStack(Material.AIR);
//								e.setCurrentItem(air);
//								anvilInv.setItem(0, air);
//								anvilInv.setItem(1, air);
//								((Player)e.getWhoClicked()).setItemOnCursor(result);
//							}
//						}
//						
//					}, 10);					
////					for (Material m : swords) {
////						if (itemsInAnvil[0].getType() == m && itemsInAnvil[1].getType() == m) {
////							ItemStack slot1 = itemsInAnvil[0];
////							ItemStack slot2 = itemsInAnvil[1];
////
////							if (slot1.getEnchantmentLevel(Enchantment.FIRE_ASPECT) == 1
////									&& slot2.getEnchantmentLevel(Enchantment.FIRE_ASPECT) == 1) {
////								ItemStack sword = new ItemStack(m, 1);
////								sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
////								e.setCurrentItem(sword);
////							}
////						}
////					}
//				}
//			}
//		}
//	}

	/*
	 * CustomDeathEnchant
	 */
	@EventHandler
	public void onEntityKilled(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			if ((entity.getHealth() - event.getFinalDamage()) <= 0) { // If
																		// Entities
																		// health
																		// is
																		// below
																		// or
																		// equal
																		// to 0
				for (ItemStack armour : entity.getEquipment().getArmorContents()) {
					if (armour != null) {
						List<CustomEnchant> enchantments = core.getEnchantManager().getEnchantsOnItem(armour);
						for (CustomEnchant enchantment : enchantments) {
							if (enchantment instanceof CustomDamagedEnchant) {
								CustomDamagedEnchant deathEnchant = (CustomDamagedEnchant) enchantment;
								int level = core.getEnchantManager().getLevelFromItem(enchantment, armour);
								deathEnchant.onDamageTaken(event, level);
							}
						}
					}
				}
			}
		}
	}

	/*
	 * CustomKillerEnchant
	 */
	@EventHandler
	public void onEntityKill(EntityDeathEvent event) {
		Player player = event.getEntity().getKiller();
		if (player != null) {
			ItemStack hand = player.getItemInHand();
			if ((hand != null) && (hand.getType() != Material.AIR)) {
				List<CustomEnchant> enchantments = core.getEnchantManager().getEnchantsOnItem(hand);
				for (CustomEnchant enchantment : enchantments) {
					if (enchantment instanceof CustomKillerEnchant) {
						CustomKillerEnchant killerEnchant = (CustomKillerEnchant) enchantment;
						int level = core.getEnchantManager().getLevelFromItem(enchantment, hand);
						killerEnchant.onKill(event, level);
					}
				}
			}
		}
	}

	/*
	 * CustomClickEnchant
	 */
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent event) {
		ItemStack hand = event.getItem();
		if ((hand != null) && (hand.getType() != Material.AIR)) {
			List<CustomEnchant> enchantments = core.getEnchantManager().getEnchantsOnItem(hand);
			for (CustomEnchant enchantment : enchantments) {
				if (enchantment instanceof CustomClickEnchant) {
					CustomClickEnchant clickEnchantment = (CustomClickEnchant) enchantment;
					int level = core.getEnchantManager().getLevelFromItem(enchantment, hand);
					clickEnchantment.onClick(event, level);
				}
			}
		}
	}

	/*
	 * CustomEquipEnchant
	 */
	@EventHandler
	public void onPlayerEquip(PlayerEquipEvent event) {
		List<CustomEnchant> enchantments = core.getEnchantManager().getEnchantsOnItem(event.getEquipment());
		for (CustomEnchant enchantment : enchantments) {
			if (enchantment instanceof CustomEquipEnchant) {
				CustomEquipEnchant equipEnchantment = (CustomEquipEnchant) enchantment;
				int level = core.getEnchantManager().getLevelFromItem(enchantment, event.getEquipment());
				equipEnchantment.onEquip(event.getPlayer(), level);
			}
		}
	}

	/*
	 * CustomEquipEnchant
	 */
	@EventHandler
	public void onPlayerDequip(PlayerDequipEvent event) {
		List<CustomEnchant> enchantments = core.getEnchantManager().getEnchantsOnItem(event.getEquipment());
		for (CustomEnchant enchantment : enchantments) {
			if (enchantment instanceof CustomEquipEnchant) {
				CustomEquipEnchant equipEnchantment = (CustomEquipEnchant) enchantment;
				int level = core.getEnchantManager().getLevelFromItem(enchantment, event.getEquipment());
				equipEnchantment.onDequip(event.getPlayer(), level);
			}
		}
	}

	/*
	 * CustomAttackEnchant
	 */
	@EventHandler
	public void onEntityAttack(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof LivingEntity) {
			ItemStack hand = ((LivingEntity) event.getDamager()).getEquipment().getItemInHand();
			if ((hand != null) && (hand.getType() != Material.AIR)) {
				List<CustomEnchant> enchantments = core.getEnchantManager().getEnchantsOnItem(hand);
				for (CustomEnchant enchantment : enchantments) {
					if (enchantment instanceof CustomAttackEnchant) {
						CustomAttackEnchant attackEnchantment = (CustomAttackEnchant) enchantment;
						int level = core.getEnchantManager().getLevelFromItem(enchantment, hand);
						attackEnchantment.onAttack(event, level);
					}
				}
			}
		}
	}

	/*
	 * CustomDamagedEnchant
	 */
	@EventHandler
	public void onEntityDamaged(EntityDamageEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			for (ItemStack armour : entity.getEquipment().getArmorContents()) {
				if (armour != null) {
					List<CustomEnchant> enchantments = core.getEnchantManager().getEnchantsOnItem(armour);
					for (CustomEnchant enchantment : enchantments) {
						if (enchantment instanceof CustomDamagedEnchant) {
							CustomDamagedEnchant damagedEnchant = (CustomDamagedEnchant) enchantment;
							int level = core.getEnchantManager().getLevelFromItem(enchantment, armour);
							damagedEnchant.onDamageTaken(event, level);
						}
					}
				}
			}
		}
	}

}
