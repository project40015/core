package com.decimatepvp.functions.misc.decimatestop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.utils.BungeeUtils;

@SuppressWarnings("deprecation")
public class DecimateStop implements Listener, CommandExecutor {

	private boolean shutdown = false;
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(!arg0.isOp()){
			arg0.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return false;
		}
		
		shutdown = true;
		
//		Bukkit.getServer().setWhitelist(true);

		
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			BungeeUtils.send(player, "lobby");
		}
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			player.kickPlayer(ChatColor.RED + "Server restarting...");
		}
		
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DecimateCore.getCore(), new Runnable(){

			@Override
			public void run() {
//				Bukkit.getServer().shutdown();
//				Bukkit.getServer().setWhitelist(false);
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop");
			}
			
		}, 20*5);
		
		
		return false;
	}
	
	@EventHandler
	public void onJoin(PlayerPreLoginEvent event){
		if(shutdown){
			event.setResult(Result.KICK_WHITELIST);
			event.setKickMessage(ChatColor.RED + "Server is currently restarting...");
		}
	}

	
	
}
