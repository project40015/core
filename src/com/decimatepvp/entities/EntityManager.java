package com.decimatepvp.entities;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.google.common.collect.Lists;

import net.minecraft.server.v1_8_R3.Entity;

public class EntityManager implements Listener, CommandExecutor {
	
	private List<Entity> entities = Lists.newArrayList();
	
	public EntityManager() {
		registerEntity(WitherBoss.class, "WitherBoss", 64);
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		if(entities.contains(event.getEntity())) {
			entities.remove(event.getEntity());
		}
	}
	
	public void onPlayerDeath(EntityDamageByEntityEvent event) {
		WitherBoss.spawnMinion(event);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command paramCommand, String paramString,
			String[] paramArrayOfString) {
		if(sender.hasPermission("Decimate.staff.witherboss")) {
			if(sender instanceof Player) {
				Location loc = ((Player) sender).getLocation();
				entities.add(spawnWitherBoss(loc));
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
	
	@SuppressWarnings("unchecked")
	public void registerEntity(Class<? extends Entity> clazz, String name, int id) {
        try {
        	((Map<String, Class<?>>) getPrivateField("c", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).
        	put(name, clazz);

        	((Map<Class<?>, String>) getPrivateField("d", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).
        	put(clazz, name);

//        	((Map<Integer, Class<?>>) getPrivateField("e", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).
//        	put(id, clazz);

        	((Map<Class<?>, Integer>) getPrivateField("f", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).
        	put(clazz, id);

        	((Map<String, Integer>) getPrivateField("g", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).
        	put(name, id);
        	
//        	Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
//        	method.setAccessible(true);
//        	method.invoke(null, clazz, name, id);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
    public static Object getPrivateField(String fieldName, Class<?> clazz, Object object) {
        Field field;
        Object o = null;

        try {
            field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            o = field.get(object);
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }

        return o;
    }

	public List<Entity> getEntities() {
		return entities;
	}

}
