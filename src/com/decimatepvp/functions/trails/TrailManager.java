package com.decimatepvp.functions.trails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;

import decimatenetworkcore.core.DataUser;
import decimatenetworkcore.core.DecimateNetworkCore;

public class TrailManager implements Manager, CommandExecutor, Listener {

	private final String particleInventoryName = ChatColor.DARK_GRAY + "Particle Trails";
	private List<Trail> trails = new ArrayList<>();
	private HashMap<Player, Trail> activeTrails = new HashMap<>();
	
	public TrailManager(){
		loadTrails();
	}
	
	public void openTrailsPage(Player player){
		Inventory inventory = Bukkit.getServer().createInventory(player, 18, particleInventoryName);
		Trail active = null;
		if(activeTrails.containsKey(player)){
			active = activeTrails.get(player);
		}
		for(Trail trail : trails){
			inventory.addItem(trail.getIcon(active != null && active.equals(trail)));
		}
		player.openInventory(inventory);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DecimateCore.getCore(), new Runnable(){

			@Override
			public void run() {
				DataUser du = DecimateNetworkCore.getInstance().getDataUserManager().getDataUser(event.getPlayer().getUniqueId().toString());
				if(du != null && du.getFac1ActiveTrail() != null && !du.getFac1ActiveTrail().equals("")){
					Trail trail = getTrail(du.getFac1ActiveTrail());
					if(trail != null){
						activeTrails.put(event.getPlayer(), trail);
					}
				}				
			}
			
		}, 20);

	}	
	
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		if(this.activeTrails.containsKey(event.getPlayer())){
			if(event.getPlayer().getGameMode() != GameMode.SPECTATOR){
				this.activeTrails.get(event.getPlayer()).display(event.getPlayer().getLocation());
			}
		}
	}
	
	private Trail getTrail(String name){
		for(Trail trail : this.trails){
			if(trail.getName().equalsIgnoreCase(name)){
				return trail;
			}
		}
		return null;
	}
	
	private Trail getTrail(ItemStack item){
		if(item == null){
			return null;
		}
		for(Trail trail : trails){
			if(trail.getIcon(true).equals(item) ||
					trail.getIcon(false).equals(item)){
				return trail;
			}
		}
		return null;
	}
	
	public boolean select(Player player, ItemStack icon){
		if(icon != null){
			Trail trail = getTrail(icon);
			if(trail != null){
				if(trail.getName().equalsIgnoreCase("coming soon") && !player.hasPermission(trail.getPermission())){
					player.sendMessage(ChatColor.GRAY + "This trail is coming soon...");
					return false;
				}
				if(!player.hasPermission(trail.getPermission())){
					player.sendMessage(ChatColor.GRAY + "You do not have permission to use the " + trail.getName() + ChatColor.GRAY + "!");
					return false;
				}
				if(this.activeTrails.containsKey(player)){
					if(this.activeTrails.get(player).equals(trail)){
						this.activeTrails.remove(player);
						player.sendMessage(ChatColor.GRAY + "Disabled your " + trail.getName() + ChatColor.GRAY + "!");
						DecimateNetworkCore.getInstance().getDataUserManager().getDataUser(player.getUniqueId().toString()).setFac1ActiveTrail("");
						return true;
					}
					this.activeTrails.remove(player);
				}
				this.activeTrails.put(player, trail);
				player.sendMessage(ChatColor.GRAY + "Activated your " + trail.getName() + ChatColor.GRAY + "!");
				DecimateNetworkCore.getInstance().getDataUserManager().getDataUser(player.getUniqueId().toString()).setFac1ActiveTrail(trail.getName());
				return true;
			}
		}
		return false;
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event){
		if(event.getInventory().getTitle().equals(this.particleInventoryName)){
			if(!(event.getWhoClicked() instanceof Player)){
				return;
			}
			Player player = (Player) event.getWhoClicked();
			if(select(player, event.getCurrentItem())){
				player.closeInventory();
			}
			event.setCancelled(true);
		}
	}
	
	private void loadTrails(){
		this.trails.add(new JulyTrail());
		this.trails.add(new DecimateTrail());
		this.trails.add(new WarVictorTrail());
		this.trails.add(new HalloweenTrail());
	}
	
	public List<Trail> getTrails(){
		return trails;
	}

	@Override
	public void disable() {
		
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(arg0 instanceof Player){
			openTrailsPage((Player) arg0);
		}
		return false;
	}
	
}
