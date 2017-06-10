package com.decimatepvp.functions.bookCommand;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.decimatepvp.core.Manager;

public class CommandBookManager implements Listener, Manager {

	private List<CommandBook> commandBooks = new ArrayList<>();
	
	public CommandBookManager(){
		
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event){
		CommandBook book = getCommandBook(event.getItem());
		if(book != null){
			book.run(event.getPlayer());
		}
	}
	
	public CommandBook getCommandBook(ItemStack item){
		if(item == null){
			return null;
		}
		ItemStack c = item.clone();
		c.setAmount(1);
		for(CommandBook book : this.commandBooks){
			if(book.getItem().equals(c)){
				return book;
			}
		}
		return null;
	}
	
	public void addCommandBook(CommandBook book){
		if(!this.commandBooks.contains(book)){
			this.commandBooks.add(book);
		}
	}
	
	public List<CommandBook> getCommandBooks(){
		return this.commandBooks;
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}
	
}
