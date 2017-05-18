package com.decimatepvp.enchants;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.enchants.types.CustomDamagedEnchant;
import com.decimatepvp.enchants.types.CustomEquipEnchant;
import com.decimatepvp.events.PlayerDequipEvent;
import com.decimatepvp.events.PlayerEquipEvent;

public class EnchantListener implements Listener {
	
	private final DecimateCore core = DecimateCore.getCore();
	
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

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
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
