package io.github.redinzane.realisticchat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class CellPhoneCallEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private String message;

	public CellPhoneCallEvent(Player player, String message) {
		this.player = player;
		this.message = message;
	}

	public Player getPlayer() {
		return player;
	}

	public String getMessage() {
		return message;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}