package com.decimatepvp.functions.announcement;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.bossbar.BossBarAPI;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.utils.Configuration;
import com.decimatepvp.utils.DecimateUtils;
import com.decimatepvp.utils.PlayerUtils;
import com.google.common.collect.Lists;

public class AnnouncementManager {
	
	private final Sound sound = Sound.NOTE_PIANO;
	private final float pitch = 1;
	
	private List<String> announcements = Lists.newArrayList();
	private int delay = 3600;
	
	private int currentAnnouncement = 0;
	
	public AnnouncementManager() {
		loadAnnouncements();
		
		getAnnouncementTimer().runTaskTimer(DecimateCore.getCore(), delay, delay);
	}
	
	private void loadAnnouncements() {
		Configuration cfg = new Configuration(DecimateCore.getCore(), "announcements.yml");
		FileConfiguration config = cfg.getData();
		
		announcements.addAll(config.getStringList("Announcements"));

//		Bukkit.broadcastMessage("Announcements: ");
//		for(String string : config.getStringList("Announcements")) {
//			Bukkit.broadcastMessage(string);
//		}
		
		delay = config.getInt("Time-per-announcement") * 20;
	}

	public BukkitRunnable getAnnouncementTimer() {
		return new BukkitRunnable() {
			
			@Override
			public void run() {
				String message = DecimateUtils.color(getNextAnnouncement());
				for(Player player : Bukkit.getOnlinePlayers()) {
					PlayerUtils.sendBossbar(player, message, BossBarAPI.Color.PINK, BossBarAPI.Style.PROGRESS, 20, 2);
					player.playSound(player.getEyeLocation(), sound, pitch, 1f);
				}
			}
		};
	}
	
	public String getNextAnnouncement() {
		String message = this.announcements.get(currentAnnouncement);
		currentAnnouncement++;
		if(currentAnnouncement == this.announcements.size()) {
			currentAnnouncement = 0;
		}
		
		return message;
	}

}
