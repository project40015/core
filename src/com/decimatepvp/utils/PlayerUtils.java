package com.decimatepvp.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerUtils {

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
