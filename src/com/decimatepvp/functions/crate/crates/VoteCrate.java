package com.decimatepvp.functions.crate.crates;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.functions.crate.CrateReward;
import com.decimatepvp.utils.EnchantGlow;

public class VoteCrate extends TypicalCrate {

	private ItemStack voteKey;
	
	public VoteCrate(List<CrateReward> rewards) {
		super(ChatColor.GRAY + "Vote Crate", rewards, new Location(Bukkit.getWorlds().get(0), 16, 76, 22));
		setupKey();
		
//		super.setGroundEffect(ParticleEffect.CRIT);
	}
	
	private void setupKey(){
		voteKey = new ItemStack(Material.TRIPWIRE_HOOK);
		ItemMeta meta = voteKey.getItemMeta();
		meta.setDisplayName(ChatColor.GRAY + "Vote Crate Key");
		voteKey.setItemMeta(meta);
		EnchantGlow.addGlow(voteKey);
	}

	@Override
	public ItemStack getItemStack() {
		return voteKey;
	}

	@Override
	public void disable() {
	}
	
}
