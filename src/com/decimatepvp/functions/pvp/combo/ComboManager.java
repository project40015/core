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

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.decimatepvp.utils.PlayerUtils;

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
			return ChatColor.GREEN.toString() + combo;
		default:
			return ChatColor.GREEN.toString() + ChatColor.BOLD + combo;
		}
	}
	
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
			Player attacker = (Player) event.getDamager();
			Player attacked = (Player) event.getEntity();
			if(isInCombo(attacker, attacked)){
				Combo combo = getCombo(attacker, attacked);
				attacker.playSound(attacked.getLocation(), Sound.NOTE_PLING, 1, 1);
				PlayerUtils.sendActionbar(attacker, ChatColor.GRAY + "Combo: " + format(combo.getCombo()));
				event.setDamage(combo.hit(event.getDamage()));
			}else{
				this.combos.add(new Combo(attacker.getName(), attacked.getName()));
			}
			killCombos(attacked);
		}
	}
	
	@Override
	public void disable() {
		Bukkit.getServer().getScheduler().cancelTask(run);
		combos.clear();
	}

}
