package com.decimatepvp.core.utils;

import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.decimatepvp.core.DecimateCore;
import com.google.common.collect.Maps;

public class DecimateConfig {
	
	public double COST_OF_SAND = 50;
	
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
		COST_OF_SAND = config.getDouble("CraftTnt.Sand-Cost");
	}
	
	public double getValueForSpawner(int id) {
		return spawnerValues.containsKey(id) ? spawnerValues.get(id) : 0.0D;
	}

}
