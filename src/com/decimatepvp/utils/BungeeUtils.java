package com.decimatepvp.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.decimatepvp.core.DecimateCore;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import decimatenetworkcore.core.DataSyncEvent;
import decimatenetworkcore.core.DecimateNetworkCore;

public class BungeeUtils implements Listener {

	public static void send(Player player, String server){
		DecimateNetworkCore.getInstance().getDataUserManager().syncUser(player.getUniqueId().toString(), server);
	}
	
	@EventHandler
	public void onDataSync(DataSyncEvent event){
		Player player = Bukkit.getPlayer(UUID.fromString(event.getUuid()));
		if(player == null){
			return;
		}
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(event.getServer());
		player.sendPluginMessage(DecimateCore.getCore(), "BungeeCord", out.toByteArray());
	}
	
}
