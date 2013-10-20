package io.github.redinzane.realisticchat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

/**
 * A class representing a conversation via phone
 * @author Moritz Schwab
 */

public class Conversation 
{
	Player caller;
	List<Player> playersInConversation = new ArrayList<Player>();
	int playercounter;
	long timeStarted;
	boolean isConversationValid = false;
	
	//Things that should be in the config
	String message_Disconnect = "The call has disconnected.";
	String message_ConversationEstablished = "Conversation established between:";
	String message_PlayerAdded = " has entered the conversation.";
	String message_PlayerRemoved = " has left the conversation.";
	static int maxPlayercount = 10;
	
	public static List<Conversation> conversations = new LinkedList<Conversation>();
	
	/**
	 * Creates a Conversation and automatically adds it to the list of conversations
	 * @param caller - the caller
	 * @param called - the player being called
	 */
	Conversation(Player caller, Player called)
	{
		this.caller = caller;
		this.playersInConversation.add(caller);
		this.playersInConversation.add(called);
		this.playercounter = 2;
		this.timeStarted = System.currentTimeMillis();
		isConversationValid = true;
		String establishingMessage = message_ConversationEstablished + " " + caller.getName() + " and " + called.getName();
		caller.sendMessage(RealisticChatListener.getChatColorCode(RealisticChatListener.gray) + establishingMessage);
		called.sendMessage(RealisticChatListener.getChatColorCode(RealisticChatListener.gray) + establishingMessage);
		conversations.add(this);
	}
	
	
	/**
	 * Adds a player to the conversation
	 * @return value - returns false if maximum reached, else true
	 */
	protected boolean addPlayerToConversation(Player player)
	{
		if(this.playercounter < maxPlayercount)
		{
			if(this.playersInConversation.contains(player))
			{
				return true;
			}
			else
			{
				this.playersInConversation.add(player);
				this.playercounter++;
				for(Player allPlayers: this.playersInConversation)
				{
					allPlayers.sendMessage(RealisticChatListener.getChatColorCode(RealisticChatListener.gray) + player.getName() + message_PlayerAdded);
				}
				return true;
			}
		}
		else
		{
			if(this.playersInConversation.contains(player))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	/**
	 * Removes a player from the conversation
	 * Check if the conversation is valid afterwards
	 * @return value - returns false if failed, else true
	 */
	protected boolean removePlayerFromConversation(Player player)
	{
		
		if(this.playersInConversation.remove(player))
		{
			player.sendMessage(RealisticChatListener.getChatColorCode(RealisticChatListener.gray) + message_Disconnect);
			if(player.equals(caller))
			{
				this.isConversationValid = false;
				playercounter--;
				removeConversation();
				return true;
			}
			else
			{
				this.playercounter--;
				if(this.playercounter<2)
				{
					this.isConversationValid = false;
					removeConversation();
				}
				else
				{
					for(Player remainingPlayer: this.playersInConversation)
					{
						remainingPlayer.sendMessage(RealisticChatListener.getChatColorCode(RealisticChatListener.gray) + player.getName() + message_PlayerRemoved);
					}
				}
				return true;
			}
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Removes this conversation from existence, hopefully 
	 * @return value - returns false if failed, else true
	 */
	protected boolean removeConversation()
	{
		if(conversations.contains(this))
		{
			for(Player player: this.playersInConversation)
			{
				player.sendMessage(RealisticChatListener.getChatColorCode(RealisticChatListener.gray) + message_Disconnect);
			}
			conversations.remove(this);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Returns an array containing all players in the conversation including the caller
	 * @return value - returns an array of players
	 */
	protected Player[] getPlayersInConversation()
	{
		Player[] players = new Player[this.playercounter];
		int i = 0;
		for(Player player: this.playersInConversation)
		{
			players[i] = player;
			i++;
		}
		return players;
	}
	
	/**
	 * Checks if player is in conversation
	 * @return value - returns false if player is not in conversation, else true
	 */
	protected boolean containsPlayer(Player player)
	{
		return this.playersInConversation.contains(player);
	}
	
	

}
