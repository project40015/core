package com.decimatepvp.functions.animation;

import org.bukkit.scheduler.BukkitRunnable;

import com.decimatepvp.core.DecimateCore;

public abstract class Animation extends BukkitRunnable {
	
	public final DecimateCore core = DecimateCore.getCore();
	
	public final double[][] positions;

	public Animation(double[][] positions) {
		this.positions = positions;
	}
	
	public void start(long delay, long rate) {
		core.getAnimationManager().registerAnimation(runTaskTimer(core, delay, rate).getTaskId(), this);
	}
	
	public void stop() {
		core.getAnimationManager().removeAnimation(getTaskId());
		core.getServer().getScheduler().cancelTask(getTaskId());
	}

}
