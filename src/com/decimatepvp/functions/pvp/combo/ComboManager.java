package com.decimatepvp.functions.pvp.combo;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.decimatepvp.utils.FactionUtils;
import com.decimatepvp.utils.PlayerUtils;
import com.massivecraft.factions.Rel;

public class ComboManager implements Manager, Listener {

	private List<Combo> combos = new ArrayList<Combo>();
	private int run;
	
	public ComboManager(){
		startRunner();
	}
	
	private void startRunner(){
		final int n = 5;
		
		run = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(DecimateCore.getCore(), new Runnable(){

			@Override
			public void run() {
				for(int i = 0; i < combos.size(); i++){
					if(combos.get(i).time(n)){
//						combos.get(i).removeCombo();
						combos.remove(i--);
					}
				}
			}
			
		}, n, n);
	}
	
	private boolean isInCombo(Player attacker, Player attacked){
		for(Combo combo : this.combos){
			if(combo.getAttacker().equals(attacker.getName()) &&
					combo.getAttacked().equals(attacked.getName())){
				return true;
			}
		}
		return false;
	}
	
	private void killCombos(Player attacker){
		for(int i = 0; i < combos.size(); i++){
			if(combos.get(i).getAttacker().equals(attacker.getName())){
//				combos.get(i).removeCombo();
				combos.remove(i--);
			}
		}
	}
	
	private Combo getCombo(Player attacker, Player attacked){
		for(Combo combo : this.combos){
			if(combo.getAttacker().equals(attacker.getName()) &&
					combo.getAttacked().equals(attacked.getName())){
				return combo;
			}
		}
		return null;
	}
	
	
	private String format(int combo){
		switch(combo){
		case 1:
		case 2:
			return ChatColor.RED.toString() + combo;
		case 3:
		case 4:
			return ChatColor.GOLD.toString() + combo;
		case 5:
		case 6:
			return ChatColor.YELLOW.toString() + combo;
		case 7:
		case 8:
			return ChatColor.AQUA.toString() + combo;
		default:
			return ChatColor.AQUA.toString() + ChatColor.BOLD + combo;
		}
	}
	
//	private boolean isStand(ArmorStand stand){
//		for(Combo combo : this.combos){
//			if(combo.getStand().equals(stand)){
//				return true;
//			}
//		}
//		return false;
//	}
	
//	@EventHandler
//	public void onDamage(EntityDamageEvent event){
//		if(event.getEntity() instanceof ArmorStand){
//			ArmorStand stand = (ArmorStand) event.getEntity();
//			if(isStand(stand)){
//				event.setCancelled(true);
//			}
//		}
//	}
	
//	@EventHandler
//	public void onInteract(PlayerInteractAtEntityEvent event){
//		if(event.getRightClicked() instanceof ArmorStand){
//			ArmorStand stand = (ArmorStand) event.getRightClicked();
//			if(isStand(stand)){
//				event.setCancelled(true);
//			}
//		}
//	}
	
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent event){
		if(event.isCancelled()){
			return;
		}
		if(event.getFinalDamage() <= 0){
			return;
		}
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
			Player attacker = (Player) event.getDamager();
			Player attacked = (Player) event.getEntity();
			if(attacked.hasMetadata("NPC")){
				return;
			}
			if(PlayerUtils.isInSpawn(attacker.getLocation()) || PlayerUtils.isInSpawn(attacked.getLocation())){
				
			}
			if(!FactionUtils.getFaction(attacker).equals(FactionUtils.getWilderness())
					&& !FactionUtils.getFaction(attacked).equals(FactionUtils.getWilderness())
					&& FactionUtils.getFPlayer(attacker).getRelationTo(FactionUtils.getFPlayer(attacked)).isAtLeast(Rel.ALLY)){
				return;
			}
			if(isInCombo(attacker, attacked)){
				Combo combo = getCombo(attacker, attacked);
				if(combo.getCombo() != 1){
//					if(combo.getCombo() == 2){
//						ArmorStand stand = attacked.getWorld().spawn(attacked.getEyeLocation().clone().add(0,300,0), ArmorStand.class);
//						stand.setVisible(false);
//						stand.setGravity(false);
//						stand.setSmall(true);
//						stand.setCustomNameVisible(true);
//						combo.setStand(stand);
//					}
//					combo.getStand().setCustomName(format(combo.getCombo() - 1));
//					combo.getStand().teleport(attacked.getLocation().clone().add(Math.random(), 2, Math.random()));
					attacker.playSound(attacked.getLocation(), Sound.NOTE_PLING, 1, 1);
					PlayerUtils.sendActionbar(attacker, ChatColor.GRAY + "Combo: " + format(combo.getCombo() - 1));
				}
				event.setDamage(combo.hit(event.getDamage(),
						 attacker.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)));
			}else{
				this.combos.add(new Combo(attacker.getName(), attacked.getName()));
			}
			killCombos(attacked);
		}
	}
	
	@Override
	public void disable() {
//		for(Combo combo : this.combos){
//			combo.removeCombo();
//		}
		Bukkit.getServer().getScheduler().cancelTask(run);
		combos.clear();
	}

}
