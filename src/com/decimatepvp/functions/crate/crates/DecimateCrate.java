package com.decimatepvp.functions.crate.crates;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.functions.crate.CrateReward;
import com.decimatepvp.utils.ParticleEffect;

public class DecimateCrate extends TypicalCrate {

	private ItemStack decimateKey;
	
	public DecimateCrate(List<CrateReward> rewards) {
		super(ChatColor.LIGHT_PURPLE + "Decimate Crate", rewards);
		setupKey();
		
		super.setGroundEffect(ParticleEffect.FLAME);
	}
	
	private void setupKey(){
		decimateKey = new ItemStack(Material.TRIPWIRE_HOOK);
		ItemMeta meta = decimateKey.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Decimate Crate Key");
		decimateKey.setItemMeta(meta);
	}

	@Override
	public ItemStack getItemStack() {
		return decimateKey;
	}

	@Override
	public void disable() {
	}
	
}
