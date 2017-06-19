package com.decimatepvp.functions.pvp;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.decimatepvp.core.DecimateCore;

public class CombatPlayer {
	
	private DecimateCore core = DecimateCore.getCore();
	
	private Zombie zombie;
	
	private String uuid;
	
	private ItemStack[] inventory;
	
	private ItemStack[] armor;
	
	public CombatPlayer(Player player) {
		zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
		zombie.setHealth(player.getHealth());
		zombie.setFallDistance(player.getFallDistance());
//		zombie.setVelocity(player.getVelocity());
		uuid = player.getUniqueId().toString();
		EntityEquipment equip = zombie.getEquipment();
		equip.setArmorContents(player.getEquipment().getArmorContents());
		equip.setItemInHand(player.getItemInHand());
		equip.setBootsDropChance(0);
		equip.setLeggingsDropChance(0);
		equip.setChestplateDropChance(0);
		equip.setHelmetDropChance(0);
		equip.setItemInHandDropChance(0);
		zombie.setBaby(false);
		zombie.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 255));
		zombie.setCustomName(ChatColor.RED + "(Logged Player) " + ChatColor.YELLOW + player.getName());
		zombie.setCustomNameVisible(true);
		
		armor = player.getEquipment().getArmorContents();

		inventory = player.getInventory().getContents();
//		inventory = new CraftInventory(((CraftInventory) player.getInventory()).getInventory());
//		inventory.removeItem(equip.getArmorContents());
//		inventory.remove(equip.getItemInHand());
		
		startDelay();
	}

	private void startDelay() {
		CombatPlayer cp = this;
		BukkitRunnable br = new BukkitRunnable() {
			@Override
			public void run() {
				core.getPvpManager().remove(cp);
			}
		};
		br.runTaskLaterAsynchronously(core, 1200l);
	}

	public void onDeath(EntityDeathEvent event) {
		Location loc = zombie.getLocation();
		for(ItemStack item : inventory) {
			if(item != null && !item.getType().equals(Material.AIR)) {
				loc.getWorld().dropItemNaturally(loc, item);
			}
		}
		for(ItemStack item : armor) {
			if(item != null && !item.getType().equals(Material.AIR)) {
				loc.getWorld().dropItemNaturally(loc, item);
			}
		}
		
		event.setDroppedExp(0);
		event.getDrops().clear();
	}

	public void remove() {
//		if(!zombie.isDead()) {
			zombie.remove();
//		}
	}
	
	public int getId() {
		return zombie.getEntityId();
	}

	public String getUUID() {
		return uuid;
	}

}
