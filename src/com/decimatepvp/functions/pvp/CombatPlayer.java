package com.decimatepvp.functions.pvp;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;

public class CombatPlayer extends EntityPlayer {

	public CombatPlayer(Player player) {
		super(((CraftWorld) player.getWorld()).getHandle().getServer().getServer(),
				((CraftWorld) player.getWorld()).getHandle().getServer().getServer().getWorldServer(0),
				new GameProfile(player.getUniqueId(), player.getName()),
				new PlayerInteractManager(((CraftWorld) player.getWorld()).getHandle()));
		
		EntityPlayer ep = ((CraftPlayer) player).getHandle();
		
		this.inventory = ep.inventory;
		
        WorldServer worldServer = ((CraftWorld) player.getWorld()).getHandle();
        
        Location location = player.getLocation();
        setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        
        worldServer.addEntity(this);
        worldServer.players.remove(this);
	}

	public void remove() {
		world.removeEntity(this);
	}

}
