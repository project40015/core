package com.decimatepvp.functions.spectate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.decimatepvp.core.Manager;

public class SpectateManager implements Listener, Manager {

	private List<Spectator> spectators = new ArrayList<>();
	
	
	public SpectateManager(){
		
	}
	
	public void toggleSpectator(Player player){
		if(!spectators.contains(player.getUniqueId().toString())){
			player.sendMessage(ChatColor.GREEN + "Enabled spectator mode!");
			spectators.add(new Spectator(player));
			player.setGameMode(GameMode.SPECTATOR);
			player.getInventory().clear();
			player.setHealth(20);
			player.setFoodLevel(20);
			player.getActivePotionEffects().clear();
			for(Player p : Bukkit.getServer().getOnlinePlayers()){
				if(!p.equals(player)){
					p.hidePlayer(player);
				}
			}
		}else{
			player.sendMessage(ChatColor.YELLOW + "Disabled spectator mode!");
			getSpectator(player).getInventory().paste(player);
			spectators.remove(getSpectator(player));
			for(Player p : Bukkit.getServer().getOnlinePlayers()){
				if(!p.canSee(player)){
					p.showPlayer(player);
				}
			}
		}
	}
	
	public boolean isSpectating(Player player){
		for(Spectator sp : spectators){
			if(sp.getPlayer().equals(player)){
				return true;
			}
		}
		return false;
	}
	
	public Spectator getSpectator(Player player){
		for(Spectator sp : spectators){
			if(sp.getPlayer().equals(player)){
				return sp;
			}
		}
		return null;
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event){
		if(isSpectating(event.getPlayer())){
			String str = event.getMessage().toLowerCase().replaceAll("/", "").split(" ")[0];
			List<String> allowed = Arrays.asList("warp", "spawn", "spectate", "tp", "ban", "tempban", "mute", "pardon", "r", "msg");
			if(!allowed.contains(str)){
				event.getPlayer().sendMessage(ChatColor.RED + "You cannot use this command while spectating. Exit spectator mode using "
						+ ChatColor.YELLOW + "/spectate toggle" + ChatColor.RED + "!");
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		for(Spectator sp : spectators){
			event.getPlayer().hidePlayer(sp.getPlayer());
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		quit(event.getPlayer());
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event){
		quit(event.getPlayer());
	}
	
	private void quit(Player player){
		for(Spectator sp : spectators){
			if(sp.getPlayer().equals(player)){
				sp.getInventory().paste(sp.getPlayer());
			}else{
				player.showPlayer(sp.getPlayer());
			}
		}
	}

	@Override
	public void disable() {
		for(Spectator sp : this.spectators){
			this.toggleSpectator(sp.getPlayer());
		}
	}
	
}
