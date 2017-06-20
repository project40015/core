package com.decimatepvp.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.struct.Relation;

public class FactionUtils {
	
	private static Factions factions = Factions.getInstance();
	private static FPlayers players = FPlayers.getInstance();
	private static Board board = Board.getInstance();
	
	public static Faction getWilderness() {
		return factions.getWilderness();
	}
	
	public static Faction getSafezone() {
		return factions.getSafeZone();
	}
	
	public static Faction getWarzone() {
		return factions.getWarZone();
	}

	public static boolean isInAllyTerritory(Player player) {
		Faction fplyr = getFaction(player);
		Faction area = getFactionByLoc(player.getLocation());
		
		Relation rel = area.getRelationTo(fplyr);
		if((rel == Relation.MEMBER) || (rel == Relation.ALLY)) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isAreaSafe(Player player) {
		Faction fplyr = getFaction(player);
		Faction area = getFactionByLoc(player.getLocation());
		
		Relation rel = area.getRelationTo(fplyr);
		if((rel == Relation.MEMBER) || (area == factions.getWilderness()) || (area == factions.getWarZone())) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isOwner(Player player, Location location){
		Faction faction = board.getFactionAt(new FLocation(location));
		for(FLocation loc : faction.getClaimOwnership().keySet()){
			if(loc.isInChunk(location)){
				Bukkit.broadcastMessage(faction.getClaimOwnership().get(loc).size() + "");
				if(faction.getClaimOwnership().get(loc).size() == 0){
					return true;
				}
				for(String str : faction.getClaimOwnership().get(loc)){
					if(str.equalsIgnoreCase(player.getUniqueId().toString())){
						Bukkit.broadcastMessage(str);
						return true;
					}
				}
				break;
			}
		}
		return false;
	}
	
	public static Faction getFactionByLoc(Location location) {
		return board.getFactionAt(new FLocation(location));
	}
	
	public static FPlayer getFPlayer(OfflinePlayer player) {
		return players.getByOfflinePlayer(player);
	}
	
	public static Faction getFaction(OfflinePlayer player) {
		return getFPlayer(player).getFaction();
	}
	
	public static Factions getFactions() {
		return factions;
	}

}
