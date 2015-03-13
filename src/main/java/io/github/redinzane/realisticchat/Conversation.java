package io.github.redinzane.realisticchat;

import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.entity.Player;

public class Conversation {
	public static LinkedList<Conversation> conversations = new LinkedList<Conversation>();
	Player caller;
	LinkedList<Player> playersInConversation = new LinkedList<Player>();
	int playercounter;
	long timeStarted;
	boolean isConversationValid = false;
	String uuid;

	static String colorcode;
	static String message_Disconnect;
	static String message_ConversationEstablished;
	static String message_PlayerAdded;
	static String message_PlayerRemoved;
	static int maxPlayercount;

	/**
	 * Creates a Conversation and automatically adds it to the list of conversations.
	 * 
	 * @param caller
	 *            - the caller
	 * @param called
	 *            - the player being called
	 */
	Conversation(Player caller, Player called) {
		this.caller = caller;
		this.playersInConversation.add(caller);
		this.playersInConversation.add(called);
		this.playercounter = 2;
		this.timeStarted = System.currentTimeMillis();
		this.uuid = UUID.randomUUID().toString();
		isConversationValid = true;
		String establishingMessage = colorcode + message_ConversationEstablished + caller.getDisplayName() + colorcode + " and " + called.getDisplayName() + colorcode;
		caller.sendMessage(establishingMessage);
		called.sendMessage(establishingMessage);
		conversations.add(this);
		
	}

	/**
	 * Adds a player to the conversation.
	 * 
	 * @return value - returns false if maximum reached, else true
	 */
	protected boolean addPlayerToConversation(Player player) {
		if (this.playercounter < maxPlayercount) {
			if (this.playersInConversation.contains(player)) {
				return true;
			} else {
				this.playersInConversation.add(player);
				this.playercounter++;
				for (Player allPlayers : this.playersInConversation) {
					allPlayers.sendMessage(colorcode + player.getDisplayName() + colorcode + message_PlayerAdded);
				}
				return true;
			}
		} else {
			if (this.playersInConversation.contains(player)) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * Removes a player from the conversation Check if the conversation is valid afterwards.
	 * 
	 * @return value - returns false if failed, else true
	 */
	protected boolean removePlayerFromConversation(Player player) {

		if (this.playersInConversation.remove(player)) {
			player.sendMessage(colorcode + message_Disconnect);
			if (player.equals(caller)) {
				this.isConversationValid = false;
				playercounter--;
				removeConversation();
				return true;
			} else {
				this.playercounter--;
				if (this.playercounter < 2) {
					this.isConversationValid = false;
					removeConversation();
				} else {
					for (Player remainingPlayer : this.playersInConversation) {
						remainingPlayer.sendMessage(colorcode + player.getDisplayName() + colorcode + message_PlayerRemoved);
					}
				}
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * Removes this conversation from existence, hopefully.
	 * 
	 * @return value - returns false if failed, else true
	 */
	protected boolean removeConversation() {
		if (conversations.contains(this)) {
			for (Player player : this.playersInConversation) {
				player.sendMessage(colorcode + message_Disconnect);
			}
			conversations.remove(this);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns an array containing all players in the conversation including the caller.
	 * 
	 * @return value - returns an array of players
	 */
	protected Player[] getPlayersInConversation() {
		Player[] players = new Player[this.playercounter];
		int i = 0;
		for (Player player : this.playersInConversation) {
			players[i] = player;
			i++;
		}
		return players;
	}

	/**
	 * Checks if player is in conversation.
	 * 
	 * @return value - returns false if player is not in conversation, else true
	 */
	protected boolean containsPlayer(Player player) {
		return this.playersInConversation.contains(player);
	}
}