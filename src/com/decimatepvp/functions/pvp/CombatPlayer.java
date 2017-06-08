package com.decimatepvp.functions.pvp;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.decimatepvp.core.DecimateCore;

public class CombatPlayer {
	
	private DecimateCore core = DecimateCore.getCore();
	
	private Zombie zombie;
	
	private String uuid;
	
	private Inventory inventory;
	
	public CombatPlayer(Player player) {
		zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
		uuid = player.getUniqueId().toString();
		EntityEquipment equip = zombie.getEquipment();
		equip.setArmorContents(player.getEquipment().getArmorContents());
		equip.setItemInHand(player.getItemInHand());
		equip.setBootsDropChance(100);
		equip.setLeggingsDropChance(100);
		equip.setChestplateDropChance(100);
		equip.setHelmetDropChance(100);
		equip.setItemInHandDropChance(100);

		inventory = new CraftInventory(((CraftInventory) player.getInventory()).getInventory());
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
			if(item != null) {
				loc.getWorld().dropItemNaturally(loc, item);
			}
		}
		
		event.setDroppedExp(0);
		event.getDrops().clear();
	}

	public void remove() {
		if(!zombie.isDead()) {
			zombie.remove();
		}
	}
	
	public int getId() {
		return zombie.getEntityId();
	}

	public String getUUID() {
		return uuid;
	}

}
