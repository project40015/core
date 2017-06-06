package com.decimatepvp.functions.crate.crates;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.functions.crate.CrateReward;
import com.decimatepvp.utils.ParticleEffect;

public class GodCrate extends TypicalCrate {

	private ItemStack godKey;
	
	public GodCrate(List<CrateReward> rewards) {
		super(ChatColor.YELLOW + "God Crate", rewards);
		setupKey();
		
		super.setGroundEffect(ParticleEffect.FIREWORKS_SPARK);
	}
	
	private void setupKey(){
		godKey = new ItemStack(Material.TRIPWIRE_HOOK);
		ItemMeta meta = godKey.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "God Crate Key");
		godKey.setItemMeta(meta);
	}

	@Override
	public ItemStack getItemStack() {
		return godKey;
	}

	@Override
	public void disable() {
	}
	
}
