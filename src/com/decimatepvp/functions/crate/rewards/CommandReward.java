package com.decimatepvp.functions.crate.rewards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.functions.bookCommand.CommandBook;
import com.decimatepvp.functions.crate.CrateReward;
import com.decimatepvp.functions.crate.Rarity;

public class CommandReward extends CrateReward {

	private String command;
	private CommandBook commandBook;
	
	public CommandReward(String name, ItemStack icon, ItemStack anim, Rarity rarity, int chance, String command, boolean book) {
		super(name, icon, anim, rarity, chance);
		this.command = command;
		if(book){
			ItemStack commandBookIcon = new ItemStack(Material.BOOK);
			ItemMeta im = commandBookIcon.getItemMeta();
			im.setDisplayName(rarity.getColor() + name + ChatColor.GRAY + " (Right Click)");
			commandBookIcon.setItemMeta(im);
			this.commandBook = new CommandBook(commandBookIcon, command);
			DecimateCore.getCore().getCommandBookManager().addCommandBook(commandBook);
		}
	}

	@Override
	public void reward(Player player) {
		if(commandBook == null){
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", player.getName()));
		}else{
			player.getInventory().addItem(this.commandBook.getItem());
		}
	}

}
