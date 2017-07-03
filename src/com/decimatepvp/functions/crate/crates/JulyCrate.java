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

public class JulyCrate extends TypicalCrate {

	private ItemStack julyKey;
	
	@SuppressWarnings("deprecation")
	public JulyCrate(List<CrateReward> rewards) {
		super(ChatColor.RED + "July Crate", rewards);
		setupKey();
		super.setFourth();
		
		super.setGroundEffect(ParticleEffect.PORTAL);
		super.addTimeStand(new Date(2017-1900, 7-1, 30, 20, 0));
	}
	
	private void setupKey(){
		julyKey = new ItemStack(Material.TRIPWIRE_HOOK);
		ItemMeta meta = julyKey.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "July" + ChatColor.WHITE + " Crate " + ChatColor.BLUE + "Key");
		julyKey.setItemMeta(meta);
		EnchantGlow.addGlow(julyKey);
	}

	@Override
	public ItemStack getItemStack() {
		return julyKey;
	}

	@Override
	public void disable() {
		
	}

}
