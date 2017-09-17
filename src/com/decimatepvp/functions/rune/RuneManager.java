package com.decimatepvp.functions.rune;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.core.Manager;
import com.decimatepvp.functions.rune.runes.HunterRune;

public class RuneManager implements Manager {

	private List<Rune> runes = new ArrayList<>();
	
	public RuneManager(){
		registerRunes();
	}
	
	private void registerRunes(){
		runes.add(new HunterRune());
		
		for(Rune rune : runes){
			Bukkit.getPluginManager().registerEvents(rune, DecimateCore.getCore());
		}
	}
	
	public Rune getRune(RuneID id){
		for(Rune rune : runes){
			if(rune.getRuneID().equals(id)){
				return rune;
			}
		}
		return null;
	}

	@Override
	public void disable() {
		for(Rune rune : runes){
			if(rune instanceof DisableRune){
				((DisableRune) rune).onDisable();
			}
		}
	}
	
}
