package com.decimatepvp.core;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.decimatepvp.core.commands.BottleExpCommand;
import com.decimatepvp.core.commands.CraftTntCommand;
import com.decimatepvp.core.commands.LogoutCommand;
import com.decimatepvp.core.listener.AnvilDamageListener;
import com.decimatepvp.core.listener.BedrockFix;
import com.decimatepvp.core.listener.EntityItemListener;
import com.decimatepvp.core.listener.ExplosionListener;
import com.decimatepvp.core.listener.KillRewardListener;
import com.decimatepvp.core.listener.PlayerBreakBlockListener;
import com.decimatepvp.core.listener.PlayerUseItemListener;
import com.decimatepvp.core.listener.PreCommandCancel;
import com.decimatepvp.core.utils.DecimateConfig;
import com.decimatepvp.enchants.EnchantCommand;
import com.decimatepvp.enchants.EnchantListener;
import com.decimatepvp.enchants.EnchantManager;
import com.decimatepvp.entities.EntityManager;
import com.decimatepvp.events.CustomEventCaller;
import com.decimatepvp.functions.animation.AnimationManager;
import com.decimatepvp.functions.announcement.AnnouncementManager;
import com.decimatepvp.functions.antitnt.AntiTntManager;
import com.decimatepvp.functions.bookCommand.CommandBookManager;
import com.decimatepvp.functions.crate.CrateKeyCommand;
import com.decimatepvp.functions.crate.CrateManager;
import com.decimatepvp.functions.crate.rewards.RewardListener;
import com.decimatepvp.functions.disabletnt.TntDisableManager;
import com.decimatepvp.functions.extraexplodables.ExplodableManager;
import com.decimatepvp.functions.misc.crophopper.CropHopperCommand;
import com.decimatepvp.functions.misc.crophopper.CropHopperManager;
import com.decimatepvp.functions.misc.decimatestop.DecimateStop;
import com.decimatepvp.functions.misc.delhome.DeleteHome;
import com.decimatepvp.functions.misc.harvester.HarvesterCommand;
import com.decimatepvp.functions.misc.harvester.HarvesterManager;
import com.decimatepvp.functions.misc.itemcooldown.ItemCooldownManager;
import com.decimatepvp.functions.misc.minicommands.BlacklistManager;
import com.decimatepvp.functions.misc.minicommands.ColorsCommand;
import com.decimatepvp.functions.misc.minicommands.HubCommand;
import com.decimatepvp.functions.misc.minicommands.MicroCommands;
import com.decimatepvp.functions.misc.minicommands.NightVisionCommand;
import com.decimatepvp.functions.misc.minicommands.OnlineCommand;
import com.decimatepvp.functions.misc.sellwand.SellWandCommand;
import com.decimatepvp.functions.misc.sellwand.SellWandManager;
import com.decimatepvp.functions.misc.trench.TrenchPick;
import com.decimatepvp.functions.patch.border.WorldBorderManager;
import com.decimatepvp.functions.patch.enderpearl.EnderDelayManager;
import com.decimatepvp.functions.patch.glitchpatch.GlitchPatchManager;
import com.decimatepvp.functions.potions.PotionAbilityManager;
import com.decimatepvp.functions.pvp.PvPManager;
import com.decimatepvp.functions.pvp.combo.ComboManager;
import com.decimatepvp.functions.pvp.enchantment.EnchantmentLimitManager;
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
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class DecimateCore extends JavaPlugin implements PluginMessageListener {
	
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
	private Manager[] managers = new Manager[22];
	
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
	private CommandBookManager commandBookManager;
	private SellWandManager sellWandManager;
	private AccountIPManager accountIpManager;
	private EntityManager entityManager;
	
	/*
	 * Other
	 */
	private WorldBorderManager[] worldBorders = new WorldBorderManager[2];
	private EnchantManager enchantManager;
	private PotionAbilityManager potionManager;
	private AnnouncementManager announcementManager;
	private TrenchPick trenchPick;
	private ExplodableManager explodableManager;
	
	@Override
	public void onEnable() {
		core = this;
		config = new DecimateConfig();
		
		
	    this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
//	    this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
		
	    for(int i = 0; i < Bukkit.getWorlds().size(); i++) {
	    	World world = Bukkit.getWorlds().get(i);
	    	WorldBorder border = world.getWorldBorder();
	    	double px = (border.getSize() / 2) + 1,
	    			pz = (border.getSize() / 2) + 1;
	    	
	    	double nx = -(border.getSize() / 2) - 1,
	    			nz = -(border.getSize() / 2) - 1;
	    	
	    	worldBorders[i] = new WorldBorderManager(world, px, nx, pz, nz);
	    }
		
		enchantManager = new EnchantManager(); // Not a Real Man-ager
		potionManager = new PotionAbilityManager();
		announcementManager = new AnnouncementManager();
		entityManager = new EntityManager();
		trenchPick = new TrenchPick();

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
		managers[n++] = commandBookManager = new CommandBookManager(); //Must register before crateManager.
		managers[n++] = crateManager = new CrateManager();
		managers[n++] = pvpManager = new PvPManager();
		managers[n++] = comboManager = new ComboManager();
		managers[n++] = animationManager = new AnimationManager();
		managers[n++] = sellWandManager = new SellWandManager();
		managers[n++] = blacklistManager = new BlacklistManager();
		managers[n++] = accountIpManager = new AccountIPManager();
		managers[n++] = explodableManager = new ExplodableManager();
		managers[n++] = entityManager = new EntityManager();
				
		setupEco();
		
		loadCommands();
		loadListeners(harvesterManager, staffChatManager, freezeManager, new PlayerBreakBlockListener(),
				new EntityItemListener(), new PlayerUseItemListener(), entityManager,
				toggleChatManager, spectateManager, itemCooldownManager, new GlitchPatchManager(),
				new BottleExpCommand(), new ExplosionListener(), new DeleteHome(),
				new AnvilDamageListener(), new BottleExpCommand(), new MiniEvents(), new FactionCommandListener(),
				new FactionDamageListener(), staffCommandsManager, enderDelayManager, antiTntManager,
				accountIpManager,  new EnchantListener(), new CustomEventCaller(), cropHopperManager, potionManager,
				crateManager, comboManager, pvpManager, new EnchantmentLimitManager(), commandBookManager,
				new RewardListener(), trenchPick, sellWandManager, new ExplosionListener(), new DecimateStop(),
				new TntDisableManager(), new KillRewardListener(), new PreCommandCancel(), new BedrockFix());
		
		loadListeners(worldBorders);
	}
	
	@Override
	public void onDisable(){
		for(Manager manager : managers){
			if(manager != null){
				manager.disable();
			}
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
		getCommand("giveenchantment").setExecutor(new EnchantCommand());
		CropHopperCommand chc = new CropHopperCommand();
		getCommand("crophopper").setExecutor(chc);
		getCommand("mobhopper").setExecutor(chc);
		getCommand("potionability").setExecutor(potionManager);
		getCommand("cratekey").setExecutor(new CrateKeyCommand());
		getCommand("boss").setExecutor(entityManager);
		getCommand("human").setExecutor(pvpManager);
		getCommand("dsellwand").setExecutor(new SellWandCommand());
		getCommand("dstop").setExecutor(new DecimateStop());
		
		MicroCommands mc = new MicroCommands();
		getCommand("discord").setExecutor(mc);
		getCommand("website").setExecutor(mc);
		getCommand("ping").setExecutor(mc);
		getCommand("map").setExecutor(mc);
		getCommand("store").setExecutor(mc);
		getCommand("dbroadcast").setExecutor(mc);
		getCommand("trenchpickaxe").setExecutor(trenchPick);
		getCommand("hub").setExecutor(new HubCommand());
		getCommand("announcements").setExecutor(this.announcementManager);

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

	public WorldBorderManager getWorldBorder(int i) {
		return worldBorders[i];
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
	
	public CommandBookManager getCommandBookManager(){
		return this.commandBookManager;
	}
	
	public String getColoredDecimate(){
		return ChatColor.translateAlternateColorCodes('&', "&cD&6E&eC&aI&bM&9A&5T&cE");
	}

	public SellWandManager getSellWandManager(){
		return this.sellWandManager;
	}
	
	public ExplodableManager getExplodableManager(){
		return this.explodableManager;
	}
	
	  @Override
	  public void onPluginMessageReceived(String channel, Player player, byte[] message) {
	    if (!channel.equals("BungeeCord")) {
	      return;
	    }
	    ByteArrayDataInput in = ByteStreams.newDataInput(message);
	    String subchannel = in.readUTF();
	    if (subchannel.equals("SomeSubChannel")) {
	    	
	      // Execute here
	    	
	    }
	  }

}
