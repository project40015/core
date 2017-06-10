package com.decimatepvp.functions.crate.crates;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.functions.crate.CrateReward;
import com.decimatepvp.utils.ParticleEffect;

public class SummerCrate extends TypicalCrate {

	private ItemStack summerKey;
	
	public SummerCrate(List<CrateReward> rewards) {
		super(ChatColor.GOLD + "Summer Crate", rewards);
		setupKey();
		
		super.setGroundEffect(ParticleEffect.FLAME);
	}
	
	private void setupKey(){
		summerKey = new ItemStack(Material.TRIPWIRE_HOOK);
		ItemMeta meta = summerKey.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Summer Crate Key");
		summerKey.setItemMeta(meta);
	}

	@Override
	public ItemStack getItemStack() {
		return summerKey;
	}

	@Override
	public void disable() {
		
	}

}
