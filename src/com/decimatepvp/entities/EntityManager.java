package com.decimatepvp.entities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.server.v1_8_R3.EntityTypes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.decimatepvp.core.Manager;
import com.decimatepvp.utils.DecimateUtils;
import com.decimatepvp.utils.PlayerUtils;
import com.google.common.collect.Maps;

public class EntityManager implements Manager, Listener, CommandExecutor {

	private static Random random = new Random();

	private final Map<Entity, WitherBoss> withers = Maps.newHashMap();

	public EntityManager() {
		this.registerEntity(WitherBoss.class, "WitherBoss", 64);
	}

	@EventHandler
	public void entityDamageWither(EntityDamageByEntityEvent event) {
		org.bukkit.entity.Entity damager = event.getDamager();
		org.bukkit.entity.Entity damagee = event.getEntity();

		if(damager instanceof Player) {
			if(((CraftEntity) damagee).getHandle() instanceof WitherBoss) {
				double damage = event.getFinalDamage();

				Player player = (Player) damager;
				WitherBoss boss = withers.get(damagee);
				
				if(0 > boss.getHealth() - damage) {
					damage = boss.getHealth();
				}
				
				boss.playerDamage.put(player.getName(), (float) (boss.playerDamage.containsKey(player.getName())
						? boss.playerDamage.get(player.getName()) + damage : damage));
			}
		}
	}

	private Location getLocationNearSpawn(World world) {
		int x = random.nextInt(400) - 200;
		if(x > 0 && x < 100) {
			x += 100;
		}
		int z = random.nextInt(400) - 200;
		if(z > 0 && z < 100) {
			z += 100;
		}
		int y = world.getHighestBlockYAt(x, z) + 1;

		return new Location(world, x, y, z);
	}

	public Collection<WitherBoss> getWithers() {
		return withers.values();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command paramCommand, String paramString, String[] args) {
		if(sender.hasPermission("Decimate.staff.witherboss")) {
			World world = Bukkit.getServer().getWorlds().get(0);
			Location location = this.getLocationNearSpawn(world);
			if(args.length == 3) {
				double x = 0, y = 0, z = 0;
				try {
					if(!args[0].equals("~")){
						x = Double.parseDouble(args[0]);
					}else if(sender instanceof Player){
						x = ((Player)sender).getLocation().getX();
					}
					if(!args[1].equals("~")){
						y = Double.parseDouble(args[1]);
					}else if(sender instanceof Player){
						y = ((Player)sender).getLocation().getY();
					}
					if(!args[2].equals("~")){
						z = Double.parseDouble(args[2]);
					}else if(sender instanceof Player){
						z = ((Player)sender).getLocation().getZ();
					}
					location = new Location(world, x, y, z);
				}catch (Exception e) {
					sender.sendMessage(ChatColor.BLUE + "Proper Usage: /boss [x] [y] [z]");
					return false;
				}
			}
			else if(args.length == 4) {
				World paramWorld = null;
				double x = 0, y = 0, z = 0;
				try {
					paramWorld = Bukkit.getWorld(args[0]);
					x = Double.parseDouble(args[1]);
					y = Double.parseDouble(args[2]);
					z = Double.parseDouble(args[3]);
				} catch (Exception e) {
					sender.sendMessage(ChatColor.BLUE + "Proper Usage: /boss [world] [x] [y] [z]");
					return false;
				}
				location = new Location(paramWorld, x, y, z);
			}
			WitherBoss boss = this.spawnWitherBoss(location);
			withers.put(boss.getBukkitEntity(), boss);
			
			for(Player player : Bukkit.getOnlinePlayers()) {
				PlayerUtils.sendTitle(player, "The Wither has spawned!",
						20, 40, 40, ChatColor.DARK_GRAY);
			}

			Bukkit.broadcastMessage(ChatColor.GOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			Bukkit.broadcastMessage(DecimateUtils.color("&7The&c Wither &7has been spotted nearing &c"
					+ location.getBlockX() + " " + location.getBlockZ() + "&7."));
			Bukkit.broadcastMessage(DecimateUtils.color("&7If you slay it you will be rewarded with amazing treasure. Be warned, it has killed many other wanderers."));
			Bukkit.broadcastMessage(ChatColor.GOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		}
		else {
			sender.sendMessage(ChatColor.RED + "You do not have the proper permissions.");
		}

		return false;
	}

	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		if(withers.containsKey(event.getEntity())) {
			withers.remove(event.getEntity());
		}
	}

	public void onPlayerDeath(EntityDamageByEntityEvent event) {
		WitherBoss.spawnMinion(event);
	}

	public void registerEntity(Class<? extends WitherBoss> clazz, String name, int id) {
		try {
			List<Map<?, ?>> dataMap = new ArrayList<>();
			for(Field f : EntityTypes.class.getDeclaredFields()) {
				if(f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
					f.setAccessible(true);
					dataMap.add((Map<?, ?>) f.get(null));
				}
			}

			if(dataMap.get(2).containsKey(id)) {
				dataMap.get(0).remove(name);
				dataMap.get(2).remove(id);
			}

			Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
			method.setAccessible(true);
			method.invoke(null, clazz, name, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public WitherBoss spawnWitherBoss(Location location) {
		WitherBoss boss = new WitherBoss(location.getWorld());
		boss.spawn(location);

		return boss;
	}

	@Override
	public void disable() {
		for(WitherBoss boss : getWithers()) {
			boss.world.removeEntity(boss);
		}
	}

}
