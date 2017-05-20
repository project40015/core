package com.decimatepvp.abilities;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.decimatepvp.utils.DecimateUtils;

public class ShockwavePotion extends AbstractPotionAbility {

	private final String name = "&e&lShockwave";
	private final List<String> lore = Arrays.asList(
			DecimateUtils.color("&2This potion throws all nearby entities back.")
	);
	
	private final int radius = 4;
	
	public ShockwavePotion() {
		super("Shockwave", 1);
	}

	@Override
	public void onCrash(PotionSplashEvent event) {
		Location center = event.getEntity().getLocation();
		for(Entity ent : event.getEntity().getNearbyEntities(radius, radius, radius)) {
			if(ent instanceof LivingEntity) {
				LivingEntity entity = (LivingEntity) ent;
				applyEffect(center, entity);
			}
		}
	}

	private void applyEffect(Location center, LivingEntity entity) {
		double d =  4 / center.distance(entity.getLocation());
		Vector direction = center.getDirection().subtract(entity.getLocation().toVector());
		entity.setVelocity(direction.multiply(d));
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
