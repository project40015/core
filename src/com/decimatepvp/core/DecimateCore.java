package com.decimatepvp.core;

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
import com.decimatepvp.functions.spectate.SpectateCommand;
import com.decimatepvp.functions.spectate.SpectateManager;
import com.decimatepvp.functions.tntfill.TntFillCommand;
import com.decimatepvp.functions.tntfill.TntFillManager;
import com.decimatepvp.functions.togglechat.ToggleChatCommand;
import com.decimatepvp.functions.togglechat.ToggleChatManager;

public class DecimateCore extends JavaPlugin {
	
	private static DecimateCore core;
	
	public Economy eco;
	
	private DecimateConfig config;
	
	//Command Classes
	public CraftTntCommand craftTnt;
	public LogoutCommand logout;
	
	private FreezeManager freezeManager;
	private TntFillManager tntFillManager;
	private SpectateManager spectateManager;
	private ToggleChatManager toggleChatManager;

	
	@Override
	public void onEnable() {
		core = this;
		config = new DecimateConfig();

		freezeManager = new FreezeManager(this);
		tntFillManager = new TntFillManager();
		spectateManager = new SpectateManager();
		toggleChatManager = new ToggleChatManager();
		
		setupEco();
		loadCommands();
		loadListeners(freezeManager, new PlayerBreakBlockListener(),
				new EntityItemListener(), new ExplosionListener(), new PlayerUseItemListener(),
				toggleChatManager, spectateManager);
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
		getCommand("tntfill").setExecutor(new TntFillCommand(this));
		getCommand("spectate").setExecutor(new SpectateCommand(this));
		getCommand("togglechat").setExecutor(new ToggleChatCommand(this));
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
	
	public DecimateConfig getDecimateConfig(){
		return this.config;
	}
	
	public FreezeManager getFreezeManager(){
		return this.freezeManager;
	}
	
	public TntFillManager getTntFillManager(){
		return this.tntFillManager;
	}
	
	public SpectateManager getSpectateManager(){
		return this.spectateManager;
	}
	
	public ToggleChatManager getToggleChatManager(){
		return this.toggleChatManager;
	}

}
