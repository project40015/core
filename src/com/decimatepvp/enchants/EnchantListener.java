package com.decimatepvp.enchants;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.enchants.types.CustomDamagedEnchant;

public class EnchantListener implements Listener {
	
	private final DecimateCore core = DecimateCore.getCore();

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
