package com.decimatepvp.functions.staff.bans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.OfflinePlayer;

import com.decimatepvp.core.Manager;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huskehhh.mysql.mysql.MySQL;

public class BanManager implements Manager {
	
	private final String host = "198.100.26.68", username = "mc_11737", password = "950094c839",
			database = "mc_11737", table = "PlayerBanTableTest";
	
	private List<String> allPlayers = Lists.newArrayList();
	private Map<String, PlayerBan> playerBans = Maps.newHashMap();
	
	private MySQL db;
	
	public BanManager() {
//		loadDatabase();
//		loadBans();
	}
	
	public void unbanPlayer(OfflinePlayer player) {
		playerBans.remove(player.getUniqueId().toString());
	}
	
	public void banPlayer(OfflinePlayer player, long time) {
		String uuid = player.getUniqueId().toString();
		PlayerBan ban = new PlayerBan(uuid, System.currentTimeMillis(), System.currentTimeMillis()+time);
		playerBans.put(uuid, ban);
	}

	public void saveBans() {
		try {
			
			for(String uuid : allPlayers) {
				if(!playerBans.containsKey(uuid)) { //If player isn't in list then remove them from database
					removePlayer(uuid);
				}
				else {
					playerBans.remove(uuid); //Removes them from the list leaving only new entries
				}
			}
			
			//Adds new entries to database
			for(Entry<String, PlayerBan> set : playerBans.entrySet()) {
				String uuid = set.getKey();
				long timeBanned = set.getValue().getTimeBanned();
				long timeUnbanned = set.getValue().getTimeUnbanned();
				
				addToDatabase(uuid, timeBanned, timeUnbanned);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadBans() {
		try {
		    Connection connection = this.db.getConnection();
		    Statement statement;
		    statement = connection.createStatement();
		    String sql = "Select * From " + table;
		    ResultSet set;
		    set = statement.executeQuery(sql);
		    
		    while (set.next()) {
		    	String uuid = set.getString("UUID");
		    	PlayerBan ban = new PlayerBan(uuid, set.getLong("TimeBanned"), set.getLong("TimeUnbanned"));
		        
		    	playerBans.put(uuid, ban);
		    	allPlayers.add(uuid);
		    }
		    
		    statement.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	///////////////////////////////////////////////////////////////////////////

	public void loadDatabase() {
		this.db = new MySQL(host, "3306", database, username, password);
		
		try {
			Connection connection = db.getConnection();
			Statement statement = connection.createStatement();
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS "
					+ "PlayerBanTableTest (UUID varchar(36), TimeBanned long, TimeUnbanned long)");
			
			statement.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addToDatabase(String uuid, long currentTime, long timeUnbanned) {
		String query = " INSERT INTO "+table+" (UUID, TimeBanned, TimeUnbanned)"
				+ " values (?, ?, ?, ?)";
		try {
			if(!this.db.checkConnection()) {
				this.db.openConnection();
			}

			Connection connection = this.db.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, uuid);
			statement.setInt(2, (int) currentTime);
			statement.setInt(3, (int) timeUnbanned);
			
			statement.execute();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void closeDatebase() {
		try {
			this.db.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void disable() {
//		saveBans();
//		closeDatebase();
	}

	public void removePlayer(String uuid) {
		try {
		    Connection connection = this.db.getConnection();
		    String query = "DELETE FROM " + table + " WHERE UUID = '" + uuid + "'";
		    PreparedStatement statement = connection.prepareStatement(query);
		    statement.setInt(1, 3);
		    
		    statement.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	
		
	}

}
