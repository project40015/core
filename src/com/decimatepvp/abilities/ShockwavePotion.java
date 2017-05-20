package com.decimatepvp.abilities;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.decimatepvp.utils.DecimateUtils;

public class ShockwavePotion extends AbstractPotionAbility {

	private final String name = "&e&lShockwave";
	private final List<String> lore = Arrays.asList(
			DecimateUtils.color("&2This potion throws back all nearby entities.")
	);
	
	public ShockwavePotion() {
		super("Shockwave", 1);
	}

	@Override
	public void onCrash(PotionSplashEvent event) {
		Vector center = event.getEntity().getLocation().toVector();
		for(LivingEntity entity : event.getAffectedEntities()) {
			applyEffect(center, entity, event.getIntensity(entity));
			event.setIntensity(entity, 0.0D);
		}
	}

	private void applyEffect(Vector center, LivingEntity entity, double effectiveness) {
		Vector direction = entity.getLocation().toVector().subtract(center);
		entity.setVelocity(direction.multiply(1.5D).multiply(effectiveness));
	}
	
	@Override
	public ItemStack toItemStack(int amount) {
		ItemStack potion = super.toItemStack(amount);
		ItemMeta meta = potion.getItemMeta();
			meta.setDisplayName(DecimateUtils.color(name));
			meta.setLore(lore);
		potion.setItemMeta(meta);
		
		return potion;
	}

}
