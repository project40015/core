package com.decimatepvp.functions.crate.crates;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;

import com.decimatepvp.functions.crate.Crate;
import com.decimatepvp.functions.crate.CrateReward;

public class VoteCrate extends Crate {

	public VoteCrate(List<CrateReward> rewards) {
		super(ChatColor.GRAY + "Vote Crate", rewards);
	}

	@Override
	public void open(Location location) {
		location.getWorld().playEffect(location.clone().add(0,1,0), Effect.CLOUD, 0);
	}
	
}
