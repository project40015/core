package com.decimatepvp.functions.crate.rewards;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.functions.crate.CrateReward;
import com.decimatepvp.functions.crate.Rarity;
import com.decimatepvp.utils.Skull;

public class CashReward extends CrateReward {

	private int cash;
	
	public CashReward(String name, ItemStack icon, Rarity rarity, int chance, int cash) {
		super(rarity.getColor() + name, icon, Skull.EXCLAMATION.getSkull(), rarity, chance);
		this.cash = cash;
	}

	@Override
	public void reward(Player player) {
		DecimateCore.getCore().eco.depositPlayer(player, cash);
	}

}
