package com.decimatepvp.functions.crate;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class Crate {

	private String name;
	private List<CrateReward> rewards;
	private int totalChance;
	protected boolean opening = false;
	private Inventory rewardPage;
	
	public Crate(String name, List<CrateReward> rewards){
		this.name = name;
		this.rewards = rewards;
		for(CrateReward reward : rewards){
			this.totalChance += reward.getChance();
		}
		setupRewards();
	}
	
	private void setupRewards(){
		rewardPage = Bukkit.getServer().createInventory(null, 27, ChatColor.GRAY.toString() + ChatColor.UNDERLINE + "Crate Rewards");
		for(CrateReward reward : rewards){
			rewardPage.addItem(reward.getIcon(totalChance));
		}
	}
	
	public String getName(){
		return name;
	}
	
	public List<CrateReward> getRewards(){
		return rewards;
	}
	
	public boolean isCrateKey(ItemStack item){
		if(item == null || item.getItemMeta() == null){
			return false;
		}
		return item.getItemMeta().getDisplayName().equals(getItemStack().getItemMeta().getDisplayName())
				&& item.getType() == getItemStack().getType();
	}
	
	public void giveKey(Player player){
		player.getInventory().addItem(this.getItemStack());
	}
	
	public CrateReward reward(){
		int i = (int)((Math.random()*totalChance) + 1);
		int p = 0;
		for(CrateReward cr : rewards){
			if(i <= p + cr.getChance()){
				return cr;
			}else{
				p += cr.getChance();
			}
		}
		return null;
	}
	
	public CrateReward reward(CrateReward last){
		int i = (int)((Math.random()*(totalChance-last.getChance())) + 1);
		int p = 0;
		for(CrateReward cr : rewards){
			if(cr.equals(last)){
				continue;
			}
			if(i <= p + cr.getChance()){
				return cr;
			}else{
				p += cr.getChance();
			}
		}
		return null;
	}
	
	public Inventory getRewardPage(){
		return this.rewardPage;
	}
	
	protected abstract void giveReward(Player player, CrateReward reward);
	public abstract ItemStack getItemStack();
	public abstract boolean open(Player player, CrateReward reward, Location location);
	public abstract void disable();
	
}
