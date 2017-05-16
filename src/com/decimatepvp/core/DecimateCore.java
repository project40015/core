package com.decimatepvp.core;

import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.decimatepvp.core.commands.BottleExpCommand;
import com.decimatepvp.core.commands.CraftTntCommand;
import com.decimatepvp.core.commands.LogoutCommand;
import com.decimatepvp.core.listener.AnvilDamageListener;
import com.decimatepvp.core.listener.EntityItemListener;
import com.decimatepvp.core.listener.ExplosionListener;
import com.decimatepvp.core.listener.PlayerBreakBlockListener;
import com.decimatepvp.core.listener.PlayerUseItemListener;
import com.decimatepvp.core.utils.DecimateConfig;
import com.decimatepvp.functions.antitnt.AntiTntManager;
import com.decimatepvp.functions.enderpearl.EnderDelayManager;
import com.decimatepvp.functions.factions.FactionCommandListener;
import com.decimatepvp.functions.factions.FactionDamageListener;
import com.decimatepvp.functions.freeze.FreezeCommand;
import com.decimatepvp.functions.freeze.FreezeManager;
import com.decimatepvp.functions.glitchpatch.GlitchPatchManager;
import com.decimatepvp.functions.harvester.HarvesterCommand;
import com.decimatepvp.functions.harvester.HarvesterManager;
import com.decimatepvp.functions.itemcooldown.ItemCooldownManager;
import com.decimatepvp.functions.minicommands.BlacklistManager;
import com.decimatepvp.functions.minicommands.ColorsCommand;
import com.decimatepvp.functions.minicommands.MicroCommands;
import com.decimatepvp.functions.minicommands.NightVisionCommand;
import com.decimatepvp.functions.minicommands.OnlineCommand;
import com.decimatepvp.functions.spectate.SpectateCommand;
import com.decimatepvp.functions.spectate.SpectateManager;
import com.decimatepvp.functions.staffchat.StaffChatCommand;
import com.decimatepvp.functions.staffchat.StaffChatManager;
import com.decimatepvp.functions.stafflog.StaffCommandsManager;
import com.decimatepvp.functions.tntfill.TntFillCommand;
import com.decimatepvp.functions.tntfill.TntFillManager;
import com.decimatepvp.functions.togglechat.ToggleChatCommand;
import com.decimatepvp.functions.togglechat.ToggleChatManager;
import com.decimatepvp.functions.xpboost.ExpBoostCommand;
import com.decimatepvp.functions.xpboost.ExpBoostManager;
import com.decimatepvp.minievents.MiniEvents;

import net.milkbowl.vault.economy.Economy;

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
	private StaffChatManager staffChatManager;
	private HarvesterManager harvesterManager;
	private ItemCooldownManager itemCooldownManager;
	private StaffCommandsManager staffCommandsManager;
	private ExpBoostManager expBoostManager;
//	private AnvilManager anvilManager;
	private EnderDelayManager enderDelayManager;
	private BlacklistManager blacklistManager;
	private AntiTntManager antiTntManager;
	
	private Manager[] managers = new Manager[12];
	
	@Override
	public void onEnable() {
		core = this;
		config = new DecimateConfig();

		int n = 0;
		managers[n++] = freezeManager = new FreezeManager(this);
		managers[n++] = tntFillManager = new TntFillManager();
		managers[n++] = spectateManager = new SpectateManager();
		managers[n++] = toggleChatManager = new ToggleChatManager();
		managers[n++] = staffChatManager = new StaffChatManager();
		managers[n++] = harvesterManager = new HarvesterManager();
		managers[n++] = itemCooldownManager = new ItemCooldownManager();
		managers[n++] = staffCommandsManager = new StaffCommandsManager();
		managers[n++] = expBoostManager = new ExpBoostManager();
//		managers[n++] = anvilManager = new AnvilManager();
		managers[n++] = enderDelayManager = new EnderDelayManager();
		managers[n++] = antiTntManager = new AntiTntManager();
				
		setupEco();
		loadCommands();
		loadListeners(harvesterManager, staffChatManager, freezeManager, new PlayerBreakBlockListener(),
				new EntityItemListener(), new ExplosionListener(), new PlayerUseItemListener(),
				toggleChatManager, spectateManager, itemCooldownManager, new GlitchPatchManager(), new BottleExpCommand(),
				new AnvilDamageListener(), new BottleExpCommand(), new MiniEvents(), new FactionCommandListener(),
				new FactionDamageListener(), new ExpBoostManager(), staffCommandsManager, expBoostManager,
				enderDelayManager, antiTntManager);
	}
	
	@Override
	public void onDisable(){
		for(Manager manager : managers){
			manager.disable();
		}
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
		getCommand("staffchat").setExecutor(new StaffChatCommand());
		getCommand("harvester").setExecutor(new HarvesterCommand());
		getCommand("colors").setExecutor(new ColorsCommand());
		getCommand("nv").setExecutor(new NightVisionCommand());
		getCommand("bottleexp").setExecutor(new BottleExpCommand());
		getCommand("who").setExecutor(new OnlineCommand());
		MicroCommands mc = new MicroCommands();
		getCommand("discord").setExecutor(mc);
		getCommand("website").setExecutor(mc);
		getCommand("ping").setExecutor(mc);
		getCommand("antitnt").setExecutor(mc);
		getCommand("expboost").setExecutor(new ExpBoostCommand());
		getCommand("blacklist").setExecutor(blacklistManager);
		getCommand("blacklistpardon").setExecutor(blacklistManager);
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

	public StaffChatManager getStaffChatManager() {
		return staffChatManager;
	}

	public HarvesterManager getHarvesterManager() {
		return harvesterManager;
	}
	
	public ItemCooldownManager getItemCooldownManager() {
		return itemCooldownManager;
	}
	
	public StaffCommandsManager getStaffCommandsManager() {
		return this.staffCommandsManager;
	}

	public ExpBoostManager getExpBoostManager() {
		return expBoostManager;
	}

	public AntiTntManager getAntiTntManager() {
		return antiTntManager;
	}

}
