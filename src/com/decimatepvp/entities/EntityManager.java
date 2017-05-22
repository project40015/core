package com.decimatepvp.entities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.decimatepvp.core.DecimateCore;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityTypes;

public class EntityManager implements CommandExecutor {
	
	public EntityManager() {
		registerEntity(WitherBoss.class, "WitherBoss", 64);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command paramCommand, String paramString,
			String[] paramArrayOfString) {
		if(sender.hasPermission("Decimate.staff.witherboss")) {
			if(sender instanceof Player) {
				Location loc = ((Player) sender).getLocation();
				WitherBoss boss = spawnWitherBoss(loc);
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if((boss != null) && (boss.isAlive())) {
//							if(boss.locY < 0) {
//								boss.locX = loc.getX();
//								boss.locY = loc.getY();
//								boss.locZ = loc.getZ();
//							}
							Bukkit.broadcastMessage(boss.locX + " " + boss.locY + " " + boss.locZ);
						}
					}
				}.runTaskTimer(DecimateCore.getCore(), 0, 20l);
			}
			else {
				sender.sendMessage(ChatColor.RED + "Only players may use this command.");
			}
		}
		return false;
	}
	
	public WitherBoss spawnWitherBoss(Location location) {
		WitherBoss boss = new WitherBoss(location.getWorld());
		boss.spawn(location);
		
		return boss;
	}
	
	public void registerEntity(Class<? extends Entity> customClass, String name, int id) {
        try {
        	List<Map<?, ?>> dataMap = new ArrayList<Map<?, ?>>();
        	for (Field f : EntityTypes.class.getDeclaredFields()) {
        		if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
        			f.setAccessible(true);
        			dataMap.add((Map<?, ?>) f.get(null));
        		}
        	}

        	if (dataMap.get(2).containsKey(id)) {
        		dataMap.get(0).remove(name);
        		dataMap.get(2).remove(id);
        	}

        	Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
        	method.setAccessible(true);
        	method.invoke(null, customClass, name, id);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
	}

}
