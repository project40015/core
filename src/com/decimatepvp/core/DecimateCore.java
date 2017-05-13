package com.decimatepvp.core;

import com.decimatepvp.core.commands.CraftTntCommand;
import com.decimatepvp.core.commands.FreezeCommand;
import com.decimatepvp.core.commands.LogoutCommand;
import com.decimatepvp.core.listener.EntityItemListener;
import com.decimatepvp.core.listener.ExplosionListener;
import com.decimatepvp.core.listener.PlayerBreakBlockListener;
import com.decimatepvp.core.listener.PlayerDamageListener;
import com.decimatepvp.core.listener.PlayerMoveListener;
import com.decimatepvp.core.listener.PlayerQuitListener;
import com.decimatepvp.core.listener.PlayerUseItemListener;
import com.decimatepvp.core.utils.DecimateConfig;

public class DecimateCore extends JavaPlugin {
	
	//Test
	
	private static DecimateCore core;
	
	public Economy eco;
	
	public DecimateConfig config;
	
	//Command Classes
	public CraftTntCommand craftTnt;
	public FreezeCommand freeze;
	public LogoutCommand logout;
	
	@Override
	public void onEnable() {
		core = this;
		config = new DecimateConfig();

		setupEco();
		loadCommands();
		loadListeners(new PlayerMoveListener(), new PlayerQuitListener(), new PlayerBreakBlockListener(),
				new EntityItemListener(), new ExplosionListener(), new PlayerUseItemListener(),
				new PlayerDamageListener());
	}

	private void loadListeners(Listener... listeners) {
		for(Listener listener : listeners) {
			getServer().getPluginManager().registerEvents(listener, this);
		}
	}

	private void loadCommands() {
		craftTnt = new CraftTntCommand();
		freeze = new FreezeCommand();
		logout = new LogoutCommand();
		getCommand("tnt").setExecutor(craftTnt);
		getCommand("freeze").setExecutor(freeze);
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

}
