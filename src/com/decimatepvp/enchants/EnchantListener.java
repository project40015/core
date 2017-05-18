package com.decimatepvp.enchants;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.enchants.types.CustomAttackEnchant;
import com.decimatepvp.enchants.types.CustomClickEnchant;
import com.decimatepvp.enchants.types.CustomDamagedEnchant;
import com.decimatepvp.enchants.types.CustomEquipEnchant;
import com.decimatepvp.enchants.types.CustomKillerEnchant;
import com.decimatepvp.events.PlayerDequipEvent;
import com.decimatepvp.events.PlayerEquipEvent;

public class EnchantListener implements Listener {
	
	private final DecimateCore core = DecimateCore.getCore();
	
	/*
	 * CustomDeathEnchant
	 */
	@EventHandler
	public void onEntityKilled(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			if((entity.getHealth() - event.getFinalDamage()) <= 0) { //If Entities health is below or equal to 0
				for(ItemStack armour : entity.getEquipment().getArmorContents()) {
					if(armour != null) {
						List<CustomEnchant> enchantments = core.getEnchantManager().getEnchantsOnItem(armour);
						for(CustomEnchant enchantment : enchantments) {
							if(enchantment instanceof CustomDamagedEnchant) {
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
		if(player != null) {
			ItemStack hand = player.getItemInHand();
			if((hand != null) && (hand.getType() != Material.AIR)) {
				List<CustomEnchant> enchantments = core.getEnchantManager().getEnchantsOnItem(hand);
				for(CustomEnchant enchantment : enchantments) {
					if(enchantment instanceof CustomKillerEnchant) {
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
		if((hand != null) && (hand.getType() != Material.AIR)) {
			List<CustomEnchant> enchantments = core.getEnchantManager().getEnchantsOnItem(hand);
			for(CustomEnchant enchantment : enchantments) {
				if(enchantment instanceof CustomClickEnchant) {
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
		for(CustomEnchant enchantment : enchantments) {
			if(enchantment instanceof CustomEquipEnchant) {
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
		for(CustomEnchant enchantment : enchantments) {
			if(enchantment instanceof CustomEquipEnchant) {
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
		if(event.getDamager() instanceof LivingEntity) {
			ItemStack hand = ((LivingEntity) event.getDamager()).getEquipment().getItemInHand();
			if((hand != null) && (hand.getType() != Material.AIR)) {
				List<CustomEnchant> enchantments = core.getEnchantManager().getEnchantsOnItem(hand);
				for(CustomEnchant enchantment : enchantments) {
					if(enchantment instanceof CustomAttackEnchant) {
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
		if(event.getEntity() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			for(ItemStack armour : entity.getEquipment().getArmorContents()) {
				if(armour != null) {
					List<CustomEnchant> enchantments = core.getEnchantManager().getEnchantsOnItem(armour);
					for(CustomEnchant enchantment : enchantments) {
						if(enchantment instanceof CustomDamagedEnchant) {
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
