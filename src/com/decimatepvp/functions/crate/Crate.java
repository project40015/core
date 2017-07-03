package com.decimatepvp.functions.crate;

import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.utils.ParticleEffect;

public abstract class Crate {

	private String name;
	private List<CrateReward> rewards;
	private int totalChance;
	protected boolean opening = false;
	private Inventory rewardPage;
	private ArmorStand nameStand, timeStand;
	private Location location;
	private ParticleEffect groundEffect;
	
	private boolean comingSoon = false;
	private boolean seasonal = false;
	private String time = "N/A";
	
	private Crate next;
	
	private Date over;
	
	public Crate(String name, List<CrateReward> rewards){
		this.name = name;
		this.rewards = rewards;
		for(CrateReward reward : rewards){
			this.totalChance += reward.getChance();
		}
		setupRewards();
	}
	
	public Crate(String name, boolean comingSoon){
		this.name = name;
		this.comingSoon = comingSoon;
	}
	
	protected void addTimeStand(Date over){
		this.seasonal = true;
		this.over = over;
	}
	
	protected void setGroundEffect(ParticleEffect effect){
		this.groundEffect = effect;
	}
	
	public boolean hasGroundEffect(){
		return groundEffect != null;
	}
	
	public ParticleEffect getGroundEffect(){
		return this.groundEffect;
	}
	
	public void setNext(Crate crate){
		this.next = crate;
	}
	
	public boolean hasNext(){
		return next != null;
	}
	
	public Crate getNext(){
		return next;
	}
	
	public void spawn(Location location){
		this.location = location;
		nameStand = location.getWorld().spawn(location.clone().add(.5,-.9,.5), ArmorStand.class);
		nameStand.setVisible(false);
		nameStand.setCustomNameVisible(true);
		nameStand.setCustomName(name);
		nameStand.setGravity(false);
		
		if(this.seasonal){
			this.timeStand = nameStand.getWorld().spawn(nameStand.getLocation().clone().add(0,-.3,0), ArmorStand.class);
			timeStand.setVisible(false);
			timeStand.setCustomNameVisible(true);
			timeStand.setCustomName(time);
			timeStand.setGravity(false);
		}
	}
	
	private void orderRewards(){
		boolean again = false;
		for(int i = 0; i < rewards.size() - 1; i++){
			if(rewards.get(i).getChance() < rewards.get(i+1).getChance()){
				CrateReward r = rewards.get(i);
				rewards.set(i, rewards.get(i+1));
				rewards.set(i+1, r);
				again = true;
			}
		}
		if(again){
			orderRewards();
		}
	}
	
	private int pageSize(){
		int n = this.rewards.size();
		if(n <= 9){
			return 9;
		}
		if(n <= 18){
			return 18;
		}
		if(n <= 27){
			return 27;
		}
		return 54;
	}
	
	private void setupRewards(){
		orderRewards();
		rewardPage = Bukkit.getServer().createInventory(null, pageSize(), ChatColor.GRAY.toString() + ChatColor.UNDERLINE + "Crate Rewards");
		for(CrateReward reward : rewards){
			rewardPage.addItem(reward.getIcon(totalChance));
		}
	}
	
	public boolean isComingSoon(){
		return this.comingSoon;
	}
	
	public String getName(){
		return name;
	}
	
	public List<CrateReward> getRewards(){
		return rewards;
	}
	
	public boolean isCrateKey(ItemStack item){
		if(item == null || item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null){
			return false;
		}
		return item.getItemMeta().getDisplayName().equals(getItemStack().getItemMeta().getDisplayName())
				&& item.getType() == getItemStack().getType();
	}
	
	public void giveKey(Player player, int amount){
		ItemStack t = this.getItemStack().clone();
		t.setAmount(amount);
		player.getInventory().addItem(t);
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
	
	public int getTotalChance(){
		return this.totalChance;
	}
	
	public ArmorStand getNameStand(){
		return this.nameStand;
	}
	
	public Location getLocation(){
		return location;
	}
	
	public boolean isSeasonal(){
		return this.seasonal;
	}
	
	public void setTimeString(String st){
		this.time = st;
		if(this.timeStand != null){
			this.timeStand.setCustomName(time);
		}
	}
	
	public boolean isOver(){
		return (new Date()).after(over);
	}
	
	public Date getOver(){
		return this.over;
	}
	
	public void clearStands(){
		if(this.nameStand != null){
//			if(!this.nameStand.isDead()){
				this.nameStand.remove();
//			}
		}
		if(this.timeStand != null){
//			if(!this.timeStand.isDead()){
				this.timeStand.remove();
//			}
		}
	}
	
	protected abstract void giveReward(Player player, CrateReward reward, Location location);
	public abstract ItemStack getItemStack();
	public abstract boolean open(Player player, CrateReward reward, Location location);
	public abstract void disable();
	
}
