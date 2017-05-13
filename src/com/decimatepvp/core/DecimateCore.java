package com.decimatepvp.core;

import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.decimatepvp.core.commands.CraftTntCommand;
import com.decimatepvp.core.commands.LogoutCommand;
import com.decimatepvp.core.listener.EntityItemListener;
import com.decimatepvp.core.listener.ExplosionListener;
import com.decimatepvp.core.listener.PlayerBreakBlockListener;
import com.decimatepvp.core.listener.PlayerUseItemListener;
import com.decimatepvp.core.utils.DecimateConfig;
import com.decimatepvp.functions.freeze.FreezeCommand;
import com.decimatepvp.functions.freeze.FreezeManager;

public class DecimateCore extends JavaPlugin {
	
	private static DecimateCore core;
	
	public Economy eco;
	
	public DecimateConfig config;
	
	//Command Classes
	public CraftTntCommand craftTnt;
	public LogoutCommand logout;
	
	private FreezeManager freezeManager;
	
	private List<Manager> managers = new ArrayList<>();
	
	@Override
	public void onEnable() {
		core = this;
		config = new DecimateConfig();

		freezeManager = new FreezeManager(this);
		
		setupEco();
		loadCommands();
		loadListeners(freezeManager, new PlayerBreakBlockListener(),
				new EntityItemListener(), new ExplosionListener(), new PlayerUseItemListener());
	}

	private void loadListeners(Listener... listeners) {
		for(Listener listener : listeners) {
			getServer().getPluginManager().registerEvents(listener, this);
		}
	}

	private void loadCommands() {
		craftTnt = new CraftTntCommand();
		logout = new LogoutCommand();
		getCommand("tnt").setExecutor(craftTnt);
		getCommand("freeze").setExecutor(new FreezeCommand(this));
		getCommand("logout").setExecutor(logout);
	}

	public static DecimateCore getCore() {
		return core;
	}
	
	////////////////////
	
	private void setupEco() {
        RegisteredServiceProvider<Economy> economyProvider = 
        		getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if(economyProvider != null) {
            eco = economyProvider.getProvider();
        }
	}
	
	public FreezeManager getFreezeManager(){
		return this.freezeManager;
	}

}
