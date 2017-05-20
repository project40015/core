package com.decimatepvp.abilities;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public abstract class AbstractPotionAbility extends Potion {
	
	private final String name;
	private final int itemId;

	@SuppressWarnings("deprecation")
	public AbstractPotionAbility(String name, int itemId) {
		super(PotionType.WATER, Tier.ONE, true);
		
		this.name = name;
		this.itemId = itemId;
	}
	
	public abstract void onCrash(PotionSplashEvent event);

	public String getName() {
		return name;
	}
	
	public int getItemId() {
		return itemId;
	}

	@Override
	public ItemStack toItemStack(int amount) {
		ItemStack potion = super.toItemStack(amount);
		PotionMeta meta = (PotionMeta) potion.getItemMeta();
			meta.addEnchant(Enchantment.ARROW_DAMAGE, itemId, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		potion.setItemMeta(meta);
			
		return super.toItemStack(amount);
	}

}
