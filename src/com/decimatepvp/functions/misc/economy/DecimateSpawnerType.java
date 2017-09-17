package com.decimatepvp.functions.misc.economy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.utils.EnchantGlow;

public enum DecimateSpawnerType {

	VILLAGER(EntityType.VILLAGER),
	IRON_GOLEM(EntityType.IRON_GOLEM, "Villager Upgrade Artifact &c(Tier VIII)", VILLAGER, 0), // Does not drop from iron golems, just found in crates
	CREEPER(EntityType.CREEPER, "Iron Golem Upgrade Artifact &d(Tier VII)", IRON_GOLEM, 0.01),
	ENDERMAN(EntityType.ENDERMAN, "Creeper Upgrade Artifact &6(Tier VI)", CREEPER, 0.02),
	ZOMBIE_PIGMAN(EntityType.PIG_ZOMBIE, "Enderman Upgrade Artifact &3(Tier V)", ENDERMAN, 0.05),
	SKELETON(EntityType.SKELETON, "Pig Zombie Upgrade Artifact &e(Tier IV)", ZOMBIE_PIGMAN, 0.1),
	ZOMBIE(EntityType.ZOMBIE, "Skeleton Upgrade Artifact &f(Tier III)", SKELETON, 0.25),
	COW(EntityType.COW, "Zombie Upgrade Artifact &7(Tier II)", ZOMBIE, 0.5),
	PIG(EntityType.PIG, "Cow Upgrade Artifact &8(Tier I)", COW, 0.8);
	
	private EntityType entityType;
	private String itemName;
	private ItemStack itemStack;
	private DecimateSpawnerType typeAfter;
	private double baseDropChance;
	
	DecimateSpawnerType(EntityType entityType, String itemName, DecimateSpawnerType typeAfter, double baseDropChance){
		this.entityType = entityType;
		this.itemName = ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', itemName);
		
		itemStack = new ItemStack(Material.PRISMARINE_SHARD);
		ItemMeta im = itemStack.getItemMeta();
		im.setDisplayName(this.itemName);
		List<String> lore = new ArrayList<>();
		lore.add("");
		lore.add(ChatColor.WHITE + "Shift " + ChatColor.GRAY + "+ " + ChatColor.WHITE + "Right Click " + ChatColor.GRAY + "a ");
		lore.add(ChatColor.GOLD + entityType.toString().toLowerCase().replaceAll("_", " ") + ChatColor.GRAY + " spawner to turn it");
		lore.add(ChatColor.GRAY + "into a " + ChatColor.GOLD + typeAfter.getEntityType().toString().toLowerCase().replaceAll("_", " ") + ChatColor.GRAY + " spawner!");
		im.setLore(lore);
		itemStack.setItemMeta(im);
		EnchantGlow.addGlow(itemStack);
		
		this.typeAfter = typeAfter;
		this.baseDropChance = baseDropChance;
	}
	
	DecimateSpawnerType(EntityType entityType){
		this.entityType = entityType;
		this.itemName = "";
		itemStack = null;
	}
	
	public double getBaseDropChance(){
		return this.baseDropChance;
	}
	
	public EntityType getEntityType(){
		return entityType;
	}
	
	public String getItemName(){
		return itemName;
	}
	
	public boolean doesDropCrystal(){
		return this.itemStack != null;
	}
	
	public ItemStack getItemStack(){
		return itemStack;
	}
	
	public DecimateSpawnerType getTypeAfter(){
		return this.typeAfter;
	}
	
	public static DecimateSpawnerType getSpawnerType(EntityType type){
		for(DecimateSpawnerType t : DecimateSpawnerType.values()){
			if(t.getEntityType().equals(type)){
				return t;
			}
		}
		return null;
	}
	
}
