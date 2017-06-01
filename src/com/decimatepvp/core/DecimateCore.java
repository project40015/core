package com.decimatepvp.core;

import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.decimatepvp.core.commands.BottleExpCommand;
import com.decimatepvp.core.commands.CraftTntCommand;
import com.decimatepvp.core.commands.LogoutCommand;
import com.decimatepvp.core.listener.AnvilDamageListener;
import com.decimatepvp.core.listener.EntityItemListener;
import com.decimatepvp.core.listener.PlayerBreakBlockListener;
import com.decimatepvp.core.listener.PlayerUseItemListener;
import com.decimatepvp.core.utils.DecimateConfig;
import com.decimatepvp.enchants.EnchantCommand;
import com.decimatepvp.enchants.EnchantListener;
import com.decimatepvp.enchants.EnchantManager;
import com.decimatepvp.entities.EntityManager;
import com.decimatepvp.events.CustomEventCaller;
import com.decimatepvp.functions.animation.AnimationManager;
import com.decimatepvp.functions.announcement.AnnouncementManager;
import com.decimatepvp.functions.antitnt.AntiTntManager;
import com.decimatepvp.functions.crate.CrateKeyCommand;
import com.decimatepvp.functions.crate.CrateManager;
import com.decimatepvp.functions.misc.crophopper.CropHopperCommand;
import com.decimatepvp.functions.misc.crophopper.CropHopperManager;
import com.decimatepvp.functions.misc.harvester.HarvesterCommand;
import com.decimatepvp.functions.misc.harvester.HarvesterManager;
import com.decimatepvp.functions.misc.itemcooldown.ItemCooldownManager;
import com.decimatepvp.functions.misc.minicommands.BlacklistManager;
import com.decimatepvp.functions.misc.minicommands.ColorsCommand;
import com.decimatepvp.functions.misc.minicommands.MicroCommands;
import com.decimatepvp.functions.misc.minicommands.NightVisionCommand;
import com.decimatepvp.functions.misc.minicommands.OnlineCommand;
import com.decimatepvp.functions.patch.border.WorldBorderManager;
import com.decimatepvp.functions.patch.enderpearl.EnderDelayManager;
import com.decimatepvp.functions.patch.glitchpatch.GlitchPatchManager;
import com.decimatepvp.functions.potions.PotionAbilityManager;
import com.decimatepvp.functions.pvp.PvPManager;
import com.decimatepvp.functions.pvp.combo.ComboManager;
import com.decimatepvp.functions.staff.bans.BanManager;
import com.decimatepvp.functions.staff.factions.FactionCommandListener;
import com.decimatepvp.functions.staff.factions.FactionDamageListener;
import com.decimatepvp.functions.staff.freeze.FreezeCommand;
import com.decimatepvp.functions.staff.freeze.FreezeManager;
import com.decimatepvp.functions.staff.mcips.AccountIPManager;
import com.decimatepvp.functions.staff.spectate.SpectateCommand;
import com.decimatepvp.functions.staff.spectate.SpectateManager;
import com.decimatepvp.functions.staff.staffchat.StaffChatCommand;
import com.decimatepvp.functions.staff.staffchat.StaffChatManager;
import com.decimatepvp.functions.staff.stafflog.StaffCommandsManager;
import com.decimatepvp.functions.staff.togglechat.ToggleChatCommand;
import com.decimatepvp.functions.staff.togglechat.ToggleChatManager;
import com.decimatepvp.functions.tntfill.TntFillCommand;
import com.decimatepvp.functions.tntfill.TntFillManager;
import com.decimatepvp.minievents.MiniEvents;

import net.milkbowl.vault.economy.Economy;

public class DecimateCore extends JavaPlugin {
	
	private static DecimateCore core;
	
	public Economy eco;
	
	//Config
	private DecimateConfig config;
	
	/*
	 * Commands
	 */
	public CraftTntCommand craftTnt;
	public LogoutCommand logout;
	
	/*
	 * Managers
	 */
	private Manager[] managers = new Manager[16];
	
	private FreezeManager freezeManager;
	private TntFillManager tntFillManager;
	private SpectateManager spectateManager;
	private ToggleChatManager toggleChatManager;
	private StaffChatManager staffChatManager;
	private HarvesterManager harvesterManager;
	private ItemCooldownManager itemCooldownManager;
	private StaffCommandsManager staffCommandsManager;
//	private AnvilManager anvilManager;
	private EnderDelayManager enderDelayManager;
	private BlacklistManager blacklistManager;
	private AntiTntManager antiTntManager;
	private CropHopperManager cropHopperManager;
	private BanManager banManager;
	private CrateManager crateManager;
	private PvPManager pvpManager;
	private ComboManager comboManager;
	private AnimationManager animationManager;
	
	/*
	 * Other
	 */
	private WorldBorderManager worldBorder;
	private AccountIPManager accountIpManager;
	private EnchantManager enchantManager;
	private PotionAbilityManager potionManager;
	private AnnouncementManager announcementManager;
	private EntityManager entityManager;
	
	@Override
	public void onEnable() {
		core = this;
		config = new DecimateConfig();
		
		worldBorder = new WorldBorderManager(); // Not a Real Man-ager
		accountIpManager = new AccountIPManager();
		enchantManager = new EnchantManager();
		potionManager = new PotionAbilityManager();
		announcementManager = new AnnouncementManager();
		entityManager = new EntityManager();

		int n = 0;
		managers[n++] = freezeManager = new FreezeManager(this);
		managers[n++] = tntFillManager = new TntFillManager();
		managers[n++] = spectateManager = new SpectateManager();
		managers[n++] = toggleChatManager = new ToggleChatManager();
		managers[n++] = staffChatManager = new StaffChatManager();
		managers[n++] = harvesterManager = new HarvesterManager();
		managers[n++] = itemCooldownManager = new ItemCooldownManager();
		managers[n++] = staffCommandsManager = new StaffCommandsManager();
		managers[n++] = enderDelayManager = new EnderDelayManager();
		managers[n++] = antiTntManager = new AntiTntManager();
		managers[n++] = cropHopperManager = new CropHopperManager();
		managers[n++] = banManager = new BanManager();
		managers[n++] = crateManager = new CrateManager();
		managers[n++] = pvpManager = new PvPManager();
		managers[n++] = comboManager = new ComboManager();
		managers[n++] = animationManager = new AnimationManager();
				
		setupEco();
		loadCommands();
		loadListeners(harvesterManager, staffChatManager, freezeManager, new PlayerBreakBlockListener(),
				new EntityItemListener(), new PlayerUseItemListener(),
				toggleChatManager, spectateManager, itemCooldownManager, new GlitchPatchManager(), new BottleExpCommand(),
				new AnvilDamageListener(), new BottleExpCommand(), new MiniEvents(), new FactionCommandListener(),
				new FactionDamageListener(), staffCommandsManager, enderDelayManager, antiTntManager, worldBorder,
				accountIpManager,  new EnchantListener(), new CustomEventCaller(), cropHopperManager, potionManager,
				crateManager, pvpManager, comboManager);
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
		getCommand("blacklist").setExecutor(blacklistManager);
		getCommand("blacklistpardon").setExecutor(blacklistManager);
		getCommand("iplist").setExecutor(accountIpManager);
		getCommand("applyenchant").setExecutor(new EnchantCommand());
		getCommand("crophopper").setExecutor(new CropHopperCommand());
		getCommand("potionability").setExecutor(potionManager);
		getCommand("cratekey").setExecutor(new CrateKeyCommand());
		getCommand("boss").setExecutor(entityManager);
		getCommand("human").setExecutor(pvpManager);
		
		MicroCommands mc = new MicroCommands();
		getCommand("discord").setExecutor(mc);
		getCommand("website").setExecutor(mc);
		getCommand("ping").setExecutor(mc);
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

	public AntiTntManager getAntiTntManager() {
		return antiTntManager;
	}

	public WorldBorderManager getWorldBorder() {
		return worldBorder;
	}

	public EnchantManager getEnchantManager() {
		return enchantManager;
	}
	
	public CropHopperManager getCropHopperManager(){
		return cropHopperManager;
	}

	public BanManager getBanManager() {
		return banManager;
	}

	public AnnouncementManager getAnnouncementManager() {
		return announcementManager;
	}
	
	public CrateManager getCrateManager(){
		return crateManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public PvPManager getPvpManager() {
		return pvpManager;
	}
	
	public ComboManager getComboManager(){
		return comboManager;
	}

	public AnimationManager getAnimationManager() {
		return animationManager;
	}

}
