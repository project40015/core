package com.decimatepvp.functions.staff.bans;

import java.util.UUID;

public class PlayerBan {
	
	private UUID uuid;
	private long timeBanned, timeUnbanned;
	
	public PlayerBan(String uuid, long timeBanned, long timeUnbanned) {
		this.uuid = UUID.fromString(uuid);
		this.timeBanned = timeBanned;
		this.timeUnbanned = timeUnbanned;
	}

	public UUID getUuid() {
		return uuid;
	}

	public long getTimeBanned() {
		return timeBanned;
	}

	public long getTimeUnbanned() {
		return timeUnbanned;
	}

}
