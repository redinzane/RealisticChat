package io.github.redinzane.realisticchat;

import java.util.LinkedList;
import org.bukkit.entity.Player;

public class Conversation 
{
	Player caller;
	LinkedList<Player> playersInConversation;
	int playercounter;
	boolean isConversationValid = false;
	String disconnectMessage = "The call has disconnected.";
	
	public static LinkedList<Conversation> Conversations = new LinkedList<Conversation>(); //why not an ArrayList with size MAX_PLAYERCOUNT ?
	
	
	Conversation(Player caller, Player called)
	{
		this.caller = caller;
		this.playersInConversation.add(caller);
		this.playersInConversation.add(called);
		this.playercounter = 2;
		isConversationValid = true;
		Conversations.add(this);
	}
	
	
	/**
	 * Adds a player to the conversation
	 * @return value - returns false if maximum reached, else true
	 */
	protected boolean addPlayerToConversation(Player player)
	{
		if(this.playercounter<10) // Hardcoded, should be in config, playersInConversation.size() would probably be enough
		{
			if(this.playersInConversation.contains(player))
			{
				return true;
			}
			else
			{
				this.playersInConversation.add(player);
				this.playercounter++;
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
		if(Conversations.contains(this))
		{
			for(Player player: this.playersInConversation)
			{
				player.sendMessage(disconnectMessage);
			}
			Conversations.remove(this);
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
