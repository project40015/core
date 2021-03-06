package com.decimatepvp.functions.misc.economy;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.utils.ParticleEffect;

public class SpawnerUpgradeManager implements Listener, CommandExecutor {
	
	public SpawnerUpgradeManager(){
		this.setupSword();
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent event){
		if(event.getEntity().getType().equals(EntityType.VILLAGER)){
			if(Math.random() < 0.5){
				event.getDrops().add(new ItemStack(Material.EMERALD));
			}
			if(Math.random() < 0.75){
				event.getDrops().add(new ItemStack(Material.APPLE));
			}
		}
		if(event.getEntity().getType().equals(EntityType.PIG_ZOMBIE)){
			if(Math.random() < 0.5){
				event.getDrops().add(new ItemStack(Material.GOLD_INGOT));
			}
			if(Math.random() < 0.025){
				event.getDrops().add(new ItemStack(Material.GOLDEN_APPLE));
			}
		}
		DecimateSpawnerType type = DecimateSpawnerType.getSpawnerType(event.getEntityType());
		if(type != null){
			if(type.doesDropCrystal()){
				double chance = type.getBaseDropChance();
				if((event.getEntity().getKiller() == null || !(event.getEntity().getKiller() instanceof Player))){
					chance /= 40;
				}else{
					if(event.getEntity().getKiller() instanceof Player){
						Player player = (Player) event.getEntity().getKiller();
						ItemStack hand = player.getItemInHand();
						if(hand != null && hand.getItemMeta() != null && hand.getItemMeta().getLore() != null && hand.getItemMeta().getLore().contains(ChatColor.GRAY + "Soul Catcher I")){
							chance*=2.5;
						}
					}
				}
				if(Math.random() * 100 < chance){
					event.getDrops().add(type.getItemStack());
				}
			}
		}
	}
	
	public boolean isSimilar(ItemStack a, ItemStack b){
		if(a.equals(b)){
			return true;
		}
		if(!a.getType().equals(b.getType())){
			return false;
		}
		if(a.getItemMeta() != null && b.getItemMeta() != null){
			if(a.getItemMeta().getDisplayName() != null & b.getItemMeta().getDisplayName() != null){
				if(a.getItemMeta().getDisplayName().equals(b.getItemMeta().getDisplayName())){
					return true;
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getPlayer().isSneaking()){
			if(event.getClickedBlock().getState() instanceof CreatureSpawner){
				CreatureSpawner spawner = (CreatureSpawner) event.getClickedBlock().getState();
				DecimateSpawnerType type = DecimateSpawnerType.getSpawnerType(spawner.getSpawnedType());
				if(type != null && type.doesDropCrystal()){
					if(isSimilar(event.getPlayer().getItemInHand(), type.getItemStack())){
						ItemStack clone = event.getPlayer().getItemInHand().clone();
						clone.setAmount(clone.getAmount()-1);
						event.getPlayer().setItemInHand(event.getPlayer().getItemInHand().getAmount() == 1 ? new ItemStack(Material.AIR) : clone);
						event.getPlayer().updateInventory();
						spawner.setSpawnedType(type.getTypeAfter().getEntityType());
						event.getPlayer().sendMessage(ChatColor.YELLOW + "You upgraded a " + 
								ChatColor.GOLD + type.getEntityType().toString().toLowerCase().replaceAll("_", " ") + ChatColor.YELLOW +
								" spawner to a " + ChatColor.GOLD + type.getTypeAfter().getEntityType().toString().toLowerCase().replaceAll("_", " ") +
								ChatColor.YELLOW + " spawner!");
						event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ANVIL_USE, 1,1);
						spawner.update();
						playUpgradeEffect(event.getClickedBlock().getLocation());
					}
				}
			}
		}
	}
	
	private void playUpgradeEffect(Location location){
		for(int i = 1; i <= 10; i++){
			ParticleEffect.VILLAGER_HAPPY.display(0, 0, 0, 0, 1, location.clone().add(0, i/10.0, 0), 20);
			ParticleEffect.VILLAGER_HAPPY.display(0, 0, 0, 0, 1, location.clone().add(1, i/10.0, 0), 20);
			ParticleEffect.VILLAGER_HAPPY.display(0, 0, 0, 0, 1, location.clone().add(1, i/10.0, 1), 20);
			ParticleEffect.VILLAGER_HAPPY.display(0, 0, 0, 0, 1, location.clone().add(0, i/10.0, 1), 20);
			
			ParticleEffect.VILLAGER_HAPPY.display(0, 0, 0, 0, 1, location.clone().add(1, 0, i/10.0), 20);
			ParticleEffect.VILLAGER_HAPPY.display(0, 0, 0, 0, 1, location.clone().add(0, 0, i/10.0), 20);
			ParticleEffect.VILLAGER_HAPPY.display(0, 0, 0, 0, 1, location.clone().add(i/10.0, 0, 0), 20);
			ParticleEffect.VILLAGER_HAPPY.display(0, 0, 0, 0, 1, location.clone().add(i/10.0, 0, 1), 20);
			
			ParticleEffect.VILLAGER_HAPPY.display(0, 0, 0, 0, 1, location.clone().add(1, 1, i/10.0), 20);
			ParticleEffect.VILLAGER_HAPPY.display(0, 0, 0, 0, 1, location.clone().add(0, 1, i/10.0), 20);
			ParticleEffect.VILLAGER_HAPPY.display(0, 0, 0, 0, 1, location.clone().add(i/10.0, 1, 0), 20);
			ParticleEffect.VILLAGER_HAPPY.display(0, 0, 0, 0, 1, location.clone().add(i/10.0, 1, 1), 20);
		}
	}

	public DecimateSpawnerType getArtifact(String name){
		for(DecimateSpawnerType type : DecimateSpawnerType.values()){
			if(type.toString().equalsIgnoreCase(name)){
				return type;
			}
		}
		return null;
	}
	
	private ItemStack sword;
	
	private void setupSword(){
		sword = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta sm = sword.getItemMeta();
		sm.setDisplayName(ChatColor.YELLOW + "Artifact Sword");
		sm.setLore(Arrays.asList(ChatColor.GRAY + "Soul Catcher I"));
		sword.setItemMeta(sm);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		sword.addUnsafeEnchantment(Enchantment.DURABILITY, 8);
		sword.addEnchantment(Enchantment.DAMAGE_UNDEAD, 5);
		sword.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 4);
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(arg0.isOp()){
			if(arg3.length >= 2){
				Player player = null;
				if((player = Bukkit.getServer().getPlayer(arg3[0])) != null){
					DecimateSpawnerType type = getArtifact(arg3[1]);
					if(type != null){
						if(type.getItemStack() != null){
							int amount = 1;
							if(arg3.length >= 3){
								try{
									amount = Integer.parseInt(arg3[2]);
								}catch(Exception ex){ }
							}
							for(int i = 0; i < amount; i++){
								player.getInventory().addItem(type.getItemStack().clone());
							}
							player.sendMessage(ChatColor.YELLOW + "You received " + amount + " " + type.getTypeAfter().toString().toLowerCase().replaceAll("_", " ") + " artifact(s)!");
						}else{
							arg0.sendMessage(ChatColor.RED + "This spawner type does not have an upgrade artifact.");
						}
					}else if(arg3[1].equalsIgnoreCase("sword")){
						if(player.getInventory().firstEmpty() != -1){
							player.getInventory().addItem(sword);
						}else{
							player.getWorld().dropItem(player.getLocation(), sword);
							player.sendMessage("");
							player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "ALERT! " + ChatColor.GRAY + "Your artifact sword was placed on the ground as your inventory is full!");
							player.sendMessage("");
						}
					}else{
						arg0.sendMessage(ChatColor.RED + "Invalid artifact name, list of artifacts:");
						arg0.sendMessage(ChatColor.GRAY + "  - " + ChatColor.YELLOW + "SWORD " + ChatColor.GRAY + "(gives artifact sword)");
						for(DecimateSpawnerType t : DecimateSpawnerType.values()){
							arg0.sendMessage(ChatColor.GRAY + "  - " + ChatColor.YELLOW + t.toString());
						}
					}
				}else{
					arg0.sendMessage(ChatColor.RED + "Player not found.");
				}
			}else{
				arg0.sendMessage(ChatColor.RED + "Invalid syntax, try: " + ChatColor.YELLOW + "/artifact (player) (type) [amount]");
			}
		}else{
			arg0.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
		}
		return false;
	}
	
}
