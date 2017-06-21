package com.decimatepvp.functions.pvp;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.decimatepvp.core.DecimateCore;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class CombatPlayer {
	
	private DecimateCore core = DecimateCore.getCore();
	
	private NPC npc;
	private int id;
	
	private String uuid;
	
	private ItemStack[] inventory;
	
	private ItemStack[] armor;
	
	public CombatPlayer(Player player) {
		npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, player.getName());
		npc.spawn(player.getLocation());
		npc.setName(ChatColor.DARK_RED + player.getName());
		((Damageable) npc.getEntity()).setHealth(player.getHealth());
		npc.getEntity().setFallDistance(player.getFallDistance());
		npc.setProtected(false);
		uuid = player.getUniqueId().toString();
		EntityEquipment equip = ((LivingEntity) npc.getEntity()).getEquipment();
		equip.setArmorContents(player.getEquipment().getArmorContents());
		equip.setItemInHand(player.getItemInHand());
		
		id = npc.getEntity().getEntityId();
		
		armor = player.getEquipment().getArmorContents();

		inventory = player.getInventory().getContents();
		
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
		br.runTaskLater(core, 600);
	}

	public void onDeath(EntityDeathEvent event) {
		Location loc = event.getEntity().getLocation();
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
			npc.despawn();
//		}
	}
	
	public int getId() {
		return id;
	}

	public String getUUID() {
		return uuid;
	}

}
