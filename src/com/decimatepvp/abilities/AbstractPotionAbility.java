package com.decimatepvp.abilities;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public abstract class AbstractPotionAbility extends Potion {

	@SuppressWarnings("deprecation")
	public AbstractPotionAbility() {
		super(PotionType.WATER, Tier.ONE, true);
	}
	
	public abstract void onCrash(PotionSplashEvent event);
	
	@Override
	public ItemStack toItemStack(int amount) {
		ItemStack potion = super.toItemStack(amount);
		PotionMeta meta = (PotionMeta) potion.getItemMeta();
			meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		potion.setItemMeta(meta);
			
		return super.toItemStack(amount);
	}

}
