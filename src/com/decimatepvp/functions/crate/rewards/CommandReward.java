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
	
	public CommandReward(String name, ItemStack icon, Rarity rarity, int chance, String command, boolean book) {
		this(name, icon, rarity, chance, command, book, "", "");
	}
	
	public CommandReward(String name, ItemStack icon, Rarity rarity, int chance, String command, boolean book, String description) {
		this(name, icon, rarity, chance, command, book, description, "");
	}
	
	public CommandReward(String name, ItemStack icon, Rarity rarity, int chance, String command, boolean book, String description, String itemDesc) {
		super(name, icon, rarity, chance, description);
		this.command = command;
		if(book){
			ItemStack commandBookIcon = new ItemStack(Material.BOOK);
			ItemMeta im = commandBookIcon.getItemMeta();
			im.setDisplayName(rarity.getColor() + name + ChatColor.GRAY + " (Right Click)");
			commandBookIcon.setItemMeta(im);
			this.commandBook = itemDesc.equals("") ? new CommandBook(commandBookIcon, command) : new CommandBook(commandBookIcon, command, itemDesc);
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
