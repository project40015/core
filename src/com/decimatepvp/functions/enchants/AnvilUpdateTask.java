package com.decimatepvp.functions.enchants;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryAnvil;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.utils.ItemUtils;

import net.minecraft.server.v1_8_R3.ContainerAnvil;
import net.minecraft.server.v1_8_R3.EntityPlayer;

public class AnvilUpdateTask extends BukkitRunnable {
	
	private DecimateCore core = DecimateCore.getCore();
	
	public Player p;
	public Inventory anvilinventory;
	public CraftInventoryAnvil inv;
	public ContainerAnvil anvil;
	public int repaircost;
	
	public int task;
  
	public AnvilUpdateTask(Player p, Inventory anvilinventory)
	{
		this.p = p;
		this.anvilinventory = anvilinventory;
		this.inv = ((CraftInventoryAnvil)this.anvilinventory);
		CraftPlayer cp = (CraftPlayer)p;
		final EntityPlayer ep = cp.getHandle();
		
		Bukkit.getServer().getScheduler().runTaskLater(core, new Runnable() {
			public void run()
			{
				AnvilUpdateTask.this.anvil = ((ContainerAnvil) ep.activeContainer);
			}
		}, 5L);
		runTaskTimer(core, 0L, 20L);
	}
  
	public void run() {
		net.minecraft.server.v1_8_R3.ItemStack booknms = this.inv.getIngredientsInventory().getItem(1);
		net.minecraft.server.v1_8_R3.ItemStack itemnms = this.inv.getIngredientsInventory().getItem(0);
		if(booknms == null) {
			return;
		}
		if(itemnms == null) {
			return;
		}
		
		org.bukkit.inventory.ItemStack book = CraftItemStack.asBukkitCopy(booknms);
		org.bukkit.inventory.ItemStack item = CraftItemStack.asBukkitCopy(itemnms);
		
		if((!item.getType().toString().contains("SWORD")) ||
				(!ItemUtils.isItemCloned(book, core.getExpBoostManager().getXpBook()))) {
			return;
		}
		
		ItemStack result = item.clone();
		if(core.getExpBoostManager().addToWeapon(result)) {
			setResultSlot(result);
		}
		
	    
		this.p.updateInventory();
	}
  
	public void setResultSlot(org.bukkit.inventory.ItemStack is) {
		this.anvil.setItem(2, CraftItemStack.asNMSCopy(is));
//		this.repaircost = (int) (EnchantUtils.getEnchantmentLevelFromItem(is) * 1.25);
		setExp(this.p);
	}
  
	public void setExp(final Player player) {
		task = Bukkit.getServer().getScheduler().runTask(core, new Runnable()
		{
			public void run()
			{
				((CraftPlayer) player).getHandle().setContainerData(AnvilUpdateTask.this.anvil, 0, 15);
			}
		}).getTaskId();
	}
	
	@Override
	public synchronized void cancel() throws IllegalStateException {
		super.cancel();
		Bukkit.getServer().getScheduler().cancelTask(task);
	}
}