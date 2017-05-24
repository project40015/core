package com.decimatepvp.utils;

import java.util.HashSet;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.inventivetalent.bossbar.BossBar;
import org.inventivetalent.bossbar.BossBarAPI;

public class PlayerUtils {
	
	public static void broadcastBossbar(String message, BossBarAPI.Color color, BossBarAPI.Style style,
			int timeout, int interval) {
		message = DecimateUtils.color(message);
		for(Player player : Bukkit.getOnlinePlayers()) {
			sendBossbar(player, message, color, style, timeout, interval);
		}
	}
	
	public static BossBar sendBossbar(Player player, String message, BossBarAPI.Color color, BossBarAPI.Style style,
			int timeout, int interval) {
		BossBar bossBar = BossBarAPI.addBar(player,
				new TextComponent(message),
				color, style,
				1.0f, timeout, interval);
		
		return bossBar;
	}
	
	@SuppressWarnings("deprecation")
	public static BlockFace getBlockFace(Player player) {
		BlockFace face = null;
		Block b = player.getTargetBlock((HashSet<Byte>) null, 10);
		//mat will placed and removed briefly to check a blockface
		Material mat = Material.DIRT;
		//mat needs to be different from the target block, so if the target block is dirt, mat is set to stone instead
		if(b.getType().equals(Material.DIRT)){
			mat = Material.STONE;
		}
		BlockFace[] bfArray = { BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.DOWN };
		//checks each BlockFace by placing block breifly. If target block changes, block face is found
		for(BlockFace bf : bfArray){
			Block block = b.getRelative(bf);
			Material type;
			Byte data;
			//check if adjacent block is not solid (prevents messing up blocks like chests)
			if(!block.getType().isSolid()){
				type = block.getType();
				data = block.getData();
				//block face check
				block.setType(mat);
				//if true, block face found
				if(player.getTargetBlock((HashSet<Byte>) null, 10).getType().equals(mat)){
		 
					//do whatever with blockface
					face = bf;
				}
				//return block to original state
				block.setType(type);
				block.setData(data);
			}
		}
		
		return face;
	}
	
	public static boolean hasPermission(Player player, String permission){
        Permission p = new Permission(permission, PermissionDefault.FALSE);
        return player.hasPermission(p);
    }
	
	public static void sendPacket(Player player, Packet<?> packet) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public static int getTotal(Inventory inv, Material material) {
		int total = 0;
		for(ItemStack item : inv) {
			if(item != null) {
				if(item.getType() == material) {
					total += item.getAmount();
				}
			}
		}
		return total;
	}
	
	public static void removeItems(Player player, Material material, int amount){
		for(int i = 0; i < player.getInventory().getSize(); i++){
			if(player.getInventory().getItem(i) == null){
				continue;
			}
			if(player.getInventory().getItem(i).getType().equals(material)){
				if(amount - player.getInventory().getItem(i).getAmount() >= 0){
					amount -= player.getInventory().getItem(i).getAmount();
					player.getInventory().setItem(i, new ItemStack(Material.AIR));
				}else{
					player.getInventory().setItem(i, new ItemStack(material, player.getInventory().getItem(i).getAmount() - amount));
					break;
				}
			}
		}
	}
	
	public static void sendActionbar(Player p, String message) {
        IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" +
                ChatColor.translateAlternateColorCodes('&', message) + "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte)2);
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(bar);
    }
	
	
	
	//////////////////
	
	    public static void sendTitle(Player player, String text, int fadeInTime, int showTime, int fadeOutTime, ChatColor color)
	    {
	    	IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + text + "\",color:" + color.name().toLowerCase() + "}");

	    	PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
	    	PacketPlayOutTitle length = new PacketPlayOutTitle(fadeInTime, showTime, fadeOutTime);


	    	((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
	    	((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
	    }

	
}
