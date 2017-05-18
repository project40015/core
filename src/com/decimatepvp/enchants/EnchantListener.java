package com.decimatepvp.enchants;

import java.util.List;

import org.bukkit.entity.Creature;
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
		if(event.getEntity() instanceof Creature) {
			Creature creature = (Creature) event.getEntity();
			for(ItemStack armor : creature.getEquipment().getArmorContents()) {
				List<CustomEnchant> enchantments = core.getEnchantManager().getEnchantsOnItem(armor);
				for(CustomEnchant enchantment : enchantments) {
					if(enchantment instanceof CustomDamagedEnchant) {
						CustomDamagedEnchant damagedEnchant = (CustomDamagedEnchant) enchantment;
						int level = core.getEnchantManager().getLevelFromItem(enchantment, armor);
						damagedEnchant.onDamageTaken(event, level);
					}
				}
			}
		}
	}
	
}
