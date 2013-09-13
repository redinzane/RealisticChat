package io.github.redinzane.realisticchat;

import java.util.LinkedList;
import org.bukkit.entity.Player;

public class Conversation 
{
	Player caller;
	LinkedList<Player> playersInConversation;
	int playercounter;
	boolean isConversationValid = false;
	
	public static LinkedList<Conversation> Conversations = new LinkedList<Conversation>();
	
	Conversation(Player caller, Player called)
	{
		this.caller = caller;
		playersInConversation.add(caller);
		playersInConversation.add(called);
		playercounter = 2;
		isConversationValid = true;
	}
	
	
	/**
	 * Adds a player to the conversation
	 * @return value - returns false if maximum reached, else true
	 */
	protected boolean addPlayerToConversation(Player player)
	{
		if(playercounter<10)
		{
			if(playersInConversation.contains(player))
			{
				return true;
			}
			else
			{
				playersInConversation.add(player);
				playercounter++;
				return true;
			}
		}
		else
		{
			if(playersInConversation.contains(player))
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
	 * @return value - returns false if failed reached, else true
	 */
	protected boolean removePlayerFromConversation(Player player)
	{
		
		if(playersInConversation.remove(player))
		{
			if(player.equals(caller))
			{
				isConversationValid = false;
				playercounter--;
				return true;
			}
			else
			{
				playercounter--;
				if(playercounter<2)
				{
					isConversationValid = false;
				}
				return true;
			}
		}
		else
		{
			return false;
		}
	}

}
