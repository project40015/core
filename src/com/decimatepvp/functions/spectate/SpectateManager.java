package com.decimatepvp.functions.spectate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.decimatepvp.core.Manager;

public class SpectateManager implements Listener, Manager {
	
	private final List<String> allowed =
			Arrays.asList("warp", "spawn", "spectate", "tp", "ban", "tempban", "mute", "pardon", "r", "msg", "whois");

	private List<Spectator> spectators = new ArrayList<>();
	
	
	public SpectateManager(){
		
	}
	
	public void toggleSpectator(Player player){
		if(!this.isSpectating(player)){
			player.sendMessage(ChatColor.GREEN + "Enabled spectator mode!");
			spectators.add(new Spectator(player));
			player.setGameMode(GameMode.SPECTATOR);
			player.getInventory().clear();
			player.getInventory().setHelmet(new ItemStack(Material.AIR));
			player.getInventory().setChestplate(new ItemStack(Material.AIR));
			player.getInventory().setLeggings(new ItemStack(Material.AIR));
			player.getInventory().setBoots(new ItemStack(Material.AIR));
			player.setHealth(20);
			player.setFoodLevel(20);
			for(PotionEffect pe : player.getActivePotionEffects()){
				player.removePotionEffect(pe.getType());
			}
			for(Player p : Bukkit.getServer().getOnlinePlayers()){
				if(!p.equals(player)){
					p.hidePlayer(player);
					if(p.hasPermission("Decimate.spectate")){
						p.sendMessage(ChatColor.GRAY + "Player " + ChatColor.WHITE + player.getName() + ChatColor.GRAY + " has started spectating.");
					}
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
				if(p.hasPermission("Decimate.spectate")){
					p.sendMessage(ChatColor.GRAY + "Player " + ChatColor.WHITE + player.getName() + ChatColor.GRAY + " has stopped spectating.");
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
		if(!event.getPlayer().hasPermission("Decimate.factions.commandbypass")) {
			if(isSpectating(event.getPlayer())){
				String str = event.getMessage().toLowerCase().replaceAll("/", "").split(" ")[0];
				
				if(!this.allowed.contains(str)){
					event.getPlayer().sendMessage(ChatColor.RED + "You cannot use this command while spectating. Exit spectator mode using "
							+ ChatColor.YELLOW + "/spectate toggle" + ChatColor.RED + "!");
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		for(Spectator sp : spectators){
			if(!sp.getPlayer().equals(event.getPlayer())){
				event.getPlayer().hidePlayer(sp.getPlayer());
			}
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
				this.toggleSpectator(sp.getPlayer());
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
