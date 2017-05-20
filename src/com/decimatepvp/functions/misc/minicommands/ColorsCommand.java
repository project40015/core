package com.decimatepvp.functions.misc.minicommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ColorsCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(!(arg0 instanceof Player)){
			arg0.sendMessage("This command is for players.");
			return false;
		}
		
		Player player = (Player) arg0;
		player.sendMessage(ChatColor.GRAY + "Colors:");
		player.sendMessage("");
		player.sendMessage(ChatColor.translateAlternateColorCodes('_', "_0&0 _1&1 _2&2 _3&3 _4&4 _5&5 _6&6 _7&7" +
				" _8&8 _9&9 _a&a _b&b _c&c _d&d _e&e _f&f"));
		player.sendMessage(ChatColor.translateAlternateColorCodes('_', "_m&m_r _n&n_r _l&l_r _o&o_r &k = _kk"));
		
		return false;
	}

}
