package com.decimatepvp.functions.minicommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightVisionCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(!(arg0 instanceof Player)){
			arg0.sendMessage("This command is for players.");
			return false;
		}
		
		Player player = (Player) arg0;
		
		if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
			player.removePotionEffect(PotionEffectType.NIGHT_VISION);
			player.sendMessage(ChatColor.YELLOW + "Disabled night vision.");
		}else{
			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20*30*40*50, 0));
			player.sendMessage(ChatColor.GREEN + "Enabled night vision.");
		}
		
		return false;
	}

}
