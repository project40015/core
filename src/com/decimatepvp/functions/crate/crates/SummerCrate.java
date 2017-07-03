package com.decimatepvp.functions.crate.crates;

import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.functions.crate.CrateReward;
import com.decimatepvp.utils.EnchantGlow;
import com.decimatepvp.utils.ParticleEffect;

public class SummerCrate extends TypicalCrate {

	private ItemStack summerKey;
	
	@SuppressWarnings("deprecation")
	public SummerCrate(List<CrateReward> rewards) {
		super(ChatColor.GOLD + "Summer Crate", rewards);
		setupKey();
		
		super.setGroundEffect(ParticleEffect.FLAME);
		super.addTimeStand(new Date(2017-1900, 7-1, 1, 12, 0));
	}
	
	private void setupKey(){
		summerKey = new ItemStack(Material.TRIPWIRE_HOOK);
		ItemMeta meta = summerKey.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Summer Crate Key");
		summerKey.setItemMeta(meta);
		EnchantGlow.addGlow(summerKey);
	}

	@Override
	public ItemStack getItemStack() {
		return summerKey;
	}

	@Override
	public void disable() {
		
	}

}
