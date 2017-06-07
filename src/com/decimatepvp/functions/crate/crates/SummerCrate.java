package com.decimatepvp.functions.crate.crates;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.functions.crate.CrateReward;
import com.decimatepvp.utils.ParticleEffect;

public class SummerCrate extends TypicalCrate {

	public SummerCrate() {
		super(ChatColor.GOLD + "Summer Crate", true);
		super.setGroundEffect(ParticleEffect.REDSTONE);
	}

	@Override
	protected void giveReward(Player player, CrateReward reward) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ItemStack getItemStack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean open(Player player, CrateReward reward, Location location) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}

}
