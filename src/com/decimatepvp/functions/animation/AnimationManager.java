package com.decimatepvp.functions.animation;

import java.util.Map;

import org.bukkit.Bukkit;

import com.decimatepvp.core.Manager;
import com.google.common.collect.Maps;

public class AnimationManager implements Manager {
	
	private Map<Integer, Animation> animations = Maps.newHashMap();
	
	public void registerAnimation(int task, Animation animation) {
		animations.put(task, animation);
	}
	
	public void removeAnimation(int task) {
		animations.remove(task);
	}

	@Override
	public void disable() {
		for(Integer integer : animations.keySet()) {
			Bukkit.getScheduler().cancelTask(integer);
		}
	}

}
