package com.decimatepvp.functions.trade;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.decimatepvp.core.DecimateCore;
import com.decimatepvp.utils.DecimateUtils;

public class TradeRequest {
	
	private TradeManager manager;

	private Player sender, sendee;
	
	private OfflinePlayer offlineSendee;
	
	private boolean accepted = false;

	public TradeRequest(TradeManager manager, Player sender, Player sendee) {
		this.sender = sender;
		this.manager = manager;
		this.sendee = sendee;
	}
	
	public void send() {
		if(sendee != null) {
			sendee.sendMessage(DecimateUtils.color("&ePlayer &a"+sender.getName()+" &e has sent you a trade request!"));
			sendee.sendMessage(DecimateUtils.color("&eUse /trade accept to accept."));
		}
		else {
			manager.decline(offlineSendee);
		}

		if(sender != null) {
			sender.sendMessage(DecimateUtils.color("&eYour request has been sent!"));
		}
		else {
			manager.decline(offlineSendee);
		}
		
		BukkitRunnable br = new BukkitRunnable() {

			@Override
			public void run() {
				if(!accepted) {
					decline();
				}
			}
			
		};
		
		br.runTaskLater(DecimateCore.getCore(), 600l);
	}
	
	public void decline() {
		if(sender != null) {
			sender.sendMessage(DecimateUtils.color("&cYour request has been declined."));
		}
		else {
			manager.decline(offlineSendee);
			if(sendee != null) {
				sendee.sendMessage(DecimateUtils.color("&cRequest has been cancelled."));
			}
		}
		
		if(sendee != null) {
			sendee.sendMessage(DecimateUtils.color("&cRequest has been declined."));
		}
		else {
			manager.decline(offlineSendee);
			
			if(sender != null) {
				sender.sendMessage(DecimateUtils.color("&cRequest has been declined."));
			}
		}
	}
	
	public void accept() {
		if(sendee != null) {
			sendee.sendMessage(DecimateUtils.color("&2Your request has been accepted!"));
			accepted = true;
		}
		else {
			sender.sendMessage(DecimateUtils.color("&2Your request has been cancelled."));
			manager.decline(offlineSendee);
		}
	}

	public Player getSender() {
		return sender;
	}

	public Player getSendee() {
		return sendee;
	}

}
