package com.decimatepvp.core.utils;

import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.utils.DecimateUtils;
import com.google.common.collect.Maps;

public class DecimateConfig {
	
	private double costOfSand = 50;
	private int tntFillRange = 50;
	
	private String staffchatFormat = "&8&l[&c&lStaffChat&8&l]&r {PLAYER} &8>> &c{MESSAGE}";
	
	// Map<EntityID, Value>
	public Map<Integer, Double> spawnerValues = Maps.newHashMap();
	
	public DecimateConfig() {
		setupConfig();
	}
	
	public void setupConfig() {
		Configuration cfg = new Configuration(DecimateCore.getCore(), "config.yml");
		FileConfiguration config = cfg.getData();
		
		craftTnt(config);
		spawnerValues(config);
		
	}

	private void spawnerValues(FileConfiguration config) {
		for(String line : config.getStringList("SpawnerValues")) {
			int id = Integer.parseInt(line.split(":")[0]);
			double cost = Double.parseDouble(line.split(":")[1]);
			
			spawnerValues.put(id, cost);
		}
	}

	private void craftTnt(FileConfiguration config) {
		costOfSand = config.getDouble("CraftTnt.Sand-Cost");
	}
	
	public void getTntFillRange(FileConfiguration config){
		tntFillRange = config.getInt("TntFill.Range");
	}
	
	public double getValueForSpawner(int id) {
		return spawnerValues.containsKey(id) ? spawnerValues.get(id) : 0.0D;
	}
	
	public double getCostOfSand(){
		return this.costOfSand;
	}
	
	public int getTntFillRange(){
		return this.tntFillRange;
	}
	
	public String formatStaffChatMessage(Player player, String message) {
		return DecimateUtils.color(staffchatFormat.replace("{PLAYER}", player.getDisplayName()).replace("{MESSAGE}", message));
	}

}
