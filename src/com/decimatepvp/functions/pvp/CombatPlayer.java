package com.decimatepvp.functions.pvp;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.decimatepvp.core.DecimateCore;

public class CombatPlayer {
	
	private DecimateCore core = DecimateCore.getCore();
	
	private Zombie zombie;
	
	private String uuid;
	
	private ItemStack[] inv;
	
	public CombatPlayer(Player player) {
		zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
		uuid = player.getUniqueId().toString();
		EntityEquipment equip = zombie.getEquipment();
		equip.setArmorContents(player.getEquipment().getArmorContents());
		equip.setItemInHand(player.getItemInHand());
//
		equip.setBootsDropChance(0);
		equip.setLeggingsDropChance(0);
		equip.setChestplateDropChance(0);
		equip.setHelmetDropChance(0);
		equip.setItemInHandDropChance(0);

		inv = player.getInventory().getContents();
		
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

	public void onDeath() {
		Location loc = zombie.getLocation();
		for(ItemStack item : inv) {
			if(item != null) {
				loc.getWorld().dropItemNaturally(loc, item);
			}
		}
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
