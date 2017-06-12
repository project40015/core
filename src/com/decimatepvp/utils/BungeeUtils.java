package com.decimatepvp.utils;

import org.bukkit.entity.Player;

import com.decimatepvp.core.DecimateCore;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeUtils {

	public static void send(Player player, String server){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);
		player.sendPluginMessage(DecimateCore.getCore(), "BungeeCord", out.toByteArray());
	}
	
}
