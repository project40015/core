package com.decimatepvp.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.ps.PS;

public class FactionUtils {
	
	public static Faction getWilderness() {
		return FactionColl.get().getNone();
	}

	public static Faction getSafezone() {
		return FactionColl.get().getSafezone();
	}

	public static Faction getWarzone() {
		return FactionColl.get().getWarzone();
	}

	public static boolean isInAllyTerritory(Player player) {
		Faction fplyr = getFaction(player);
		Faction area = getFactionByLoc(player.getLocation());

		Rel rel = fplyr.getRelationWish(area);
		if((rel == Rel.MEMBER) || (rel == Rel.ALLY)) {
			return true;
		}

		return false;
	}

	public static boolean isAreaSafe(Player player) {
		Faction fplyr = getFaction(player);
		Faction area = getFactionByLoc(player.getLocation());

		Rel rel = fplyr.getRelationWish(area);
		if((rel == Rel.MEMBER) || (area == getWilderness()) || (area == getWarzone())) {
			return true;
		}

		return false;
	}

	public static boolean isOwner(Player player, Location location) {
		Faction faction = getFactionByLoc(location);
		if(getFaction(player).equals(faction)) {
			if(faction.getPermitted(MPerm.ID_BUILD).contains(getFPlayer(player).getRole()) || getFPlayer(player).getRole() == Rel.LEADER || getFPlayer(player).getRole() == Rel.COLEADER) {
				return true;
			}
		} else {
			return false;
		}
		return true;
	}

	public static Faction getFactionByLoc(Location location) {
		return BoardColl.get().getFactionAt(PS.valueOf(location));
	}

	public static MPlayer getFPlayer(OfflinePlayer player) {
		return MPlayer.get(player.getUniqueId().toString());
	}

	public static Faction getFaction(OfflinePlayer player) {
		return getFPlayer(player).getFaction();
	}

}

//Old factions jar



//package com.decimatepvp.utils;
//
//import org.bukkit.Bukkit;
//import org.bukkit.Location;
//import org.bukkit.OfflinePlayer;
//import org.bukkit.entity.Player;
//
//import com.massivecraft.factions.Board;
//import com.massivecraft.factions.FLocation;
//import com.massivecraft.factions.FPlayer;
//import com.massivecraft.factions.FPlayers;
//import com.massivecraft.factions.Faction;
//import com.massivecraft.factions.Factions;
//import com.massivecraft.factions.struct.Relation;
//import com.massivecraft.factions.struct.Role;
//
//public class FactionUtils {
//
//	private static Factions factions = Factions.getInstance();
//	private static FPlayers players = FPlayers.getInstance();
//	private static Board board = Board.getInstance();
//
//	public static Faction getWilderness() {
//		return factions.getWilderness();
//	}
//
//	public static Faction getSafezone() {
//		return factions.getSafeZone();
//	}
//
//	public static Faction getWarzone() {
//		return factions.getWarZone();
//	}
//
//	public static boolean isInAllyTerritory(Player player) {
//		Faction fplyr = getFaction(player);
//		Faction area = getFactionByLoc(player.getLocation());
//
//		Relation rel = area.getRelationTo(fplyr);
//		if((rel == Relation.MEMBER) || (rel == Relation.ALLY)) {
//			return true;
//		}
//
//		return false;
//	}
//
//	public static boolean isAreaSafe(Player player) {
//		Faction fplyr = getFaction(player);
//		Faction area = getFactionByLoc(player.getLocation());
//
//		Relation rel = area.getRelationTo(fplyr);
//		if((rel == Relation.MEMBER) || (area == factions.getWilderness()) || (area == factions.getWarZone())) {
//			return true;
//		}
//
//		return false;
//	}
//
//	public static boolean isOwner(Player player, Location location) {
//		Faction faction = board.getFactionAt(new FLocation(location));
//		if(getFaction(player).equals(faction)) {
//			if(getFPlayer(player).getRole() == Role.MODERATOR || getFPlayer(player).getRole() == Role.ADMIN) {
//				return true;
//			}
//		}
//		else {
//			return false;
//		}
//		for(FLocation loc : faction.getClaimOwnership().keySet()) {
//			if(loc.isInChunk(location)) {
//				for(String str : faction.getClaimOwnership().get(loc)) {
//					if(str.equalsIgnoreCase(player.getUniqueId().toString())) {
//						return true;
//					}
//				}
//				return false;
//			}
//		}
//		return true;
//	}
//
//	public static Faction getFactionByLoc(Location location) {
//		return board.getFactionAt(new FLocation(location));
//	}
//
//	public static FPlayer getFPlayer(OfflinePlayer player) {
//		return players.getByOfflinePlayer(player);
//	}
//
//	public static Faction getFaction(OfflinePlayer player) {
//		return getFPlayer(player).getFaction();
//	}
//
//	public static Factions getFactions() {
//		return factions;
//	}
//
//}
