package com.decimatepvp.functions.announcement;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.bossbar.BossBarAPI;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.utils.Configuration;
import com.decimatepvp.utils.DecimateUtils;
import com.decimatepvp.utils.PlayerUtils;
import com.google.common.collect.Lists;

public class AnnouncementManager implements CommandExecutor {
	
//	private final Sound sound = Sound.NOTE_PIANO;
//	private final float pitch = 1;
	
	private List<String> announcements = Lists.newArrayList();
	private int delay = 3600;
	
	private List<String> disabledAnnouncements = new ArrayList<String>();
	
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
				try {
					String message = DecimateUtils.color(getNextAnnouncement());
					for(Player player : Bukkit.getOnlinePlayers()) {
						if(!disabledAnnouncements.contains(player.getUniqueId().toString())){
							PlayerUtils.sendBossbar(player, message, BossBarAPI.Color.PINK, BossBarAPI.Style.PROGRESS, 20, 2);
						}
//						player.playSound(player.getEyeLocation(), sound, pitch, 1f);
					}
				}
				catch(Exception e) {
//					e.printStackTrace();
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

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(arg0 instanceof Player){
			Player player = (Player) arg0;
			player.sendMessage(ChatColor.YELLOW + "Toggled announcements");
			if(this.disabledAnnouncements.contains(player.getUniqueId().toString())){
				this.disabledAnnouncements.remove(player.getUniqueId().toString());
			}else{
				this.disabledAnnouncements.add(player.getUniqueId().toString());
			}
		}
		return false;
	}

}
