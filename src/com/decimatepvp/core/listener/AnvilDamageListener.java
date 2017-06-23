package com.decimatepvp.core.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.decimatepvp.utils.FactionUtils;

public class AnvilDamageListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onAnvilDamage(PlayerInteractEvent event) {
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.ANVIL) {
			if((event.getClickedBlock().getType() == Material.ANVIL) &&
					(FactionUtils.getWarzone().equals(FactionUtils.getFactionByLoc(event.getClickedBlock().getLocation())))) {
			 
			      
		            Block b = (Block) event.getClickedBlock();
		            if(b.getData() >= 4 && b.getData() <= 11) {
		                
		                if(b.getData() == 4) {b.setData((byte) 0);}
		                if(b.getData() == 5) {b.setData((byte) 1);}
		                if(b.getData() == 6) {b.setData((byte) 2);}
		                if(b.getData() == 7) {b.setData((byte) 3);}
		                
		                if(b.getData() == 8) {b.setData((byte) 0);}
		                if(b.getData() == 9) {b.setData((byte) 1);}
		                if(b.getData() == 10) {b.setData((byte) 2);}
		                if(b.getData() == 11) {b.setData((byte) 3);}
		                
		          
		            }
		          
		        }
		}
	}

}
