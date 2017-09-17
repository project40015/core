package com.decimatepvp.functions.joinWar;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.decimatepvp.utils.BungeeUtils;
import com.decimatepvp.utils.FactionUtils;
import com.massivecraft.factions.entity.Faction;

import decimate.WarSocket.ServerInformationPacketRequestEvent;
import decimate.WarSocket.WarSocket;

public class JoinWar implements Listener, CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(arg0 instanceof Player){
			Player player = (Player) arg0;
			if(player.hasPermission("Decimate.war.join")){
				Faction faction = FactionUtils.getFaction(player);
				String fString = "";
				if(!faction.equals(FactionUtils.getWilderness())){
					fString = faction.getName();
				}
				String rankString = toRankString(player);
				WarSocket.getInstance().emitServerInformationPacketResult(player.getUniqueId().toString(), rankString, fString);
				
				player.sendMessage(ChatColor.YELLOW + "Requested to join a war...");
			}else{
				player.sendMessage(ChatColor.RED + "You do not have permission to run this command.");
			}
		}
		return false;
	}

	private String toRankString(OfflinePlayer offp){
		if(offp.isOnline()){
			Player player = offp.getPlayer();
			
			if(player.hasPermission(new Permission("Decimate.rank.owner", PermissionDefault.FALSE))){
				return "OWNER";
			}else if(player.hasPermission(new Permission("Decimate.rank.developer", PermissionDefault.FALSE))){
				return "DEVELOPER";
			}else if(player.hasPermission(new Permission("Decimate.rank.moderator", PermissionDefault.FALSE))){
				return "MODERATOR";
			}else if(player.hasPermission(new Permission("Decimate.rank.helper", PermissionDefault.FALSE))){
				return "HELPER";
			}else if(player.hasPermission(new Permission("Decimate.rank.youtuber", PermissionDefault.FALSE))){
				return "YOUTUBER";
			}else if(player.hasPermission(new Permission("Decimate.rank.donor", PermissionDefault.FALSE))){
				return "DONOR";
			}
		}
		return "DEFAULT";
	}
	
	@EventHandler
	public void onRequest(ServerInformationPacketRequestEvent event){
		BungeeUtils.send(Bukkit.getPlayer(UUID.fromString(event.getUUID())), "test");
	}
	
}
