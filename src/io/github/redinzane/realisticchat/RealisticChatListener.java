package io.github.redinzane.realisticchat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
<<<<<<< HEAD
import org.bukkit.event.player.AsyncPlayerChatEvent;
=======
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
>>>>>>> Fleshing out Call beginnings

@SuppressWarnings("deprecation")
public class RealisticChatListener implements Listener
{
	int distanceForWhispering;
	int distanceForYelling;
	int distanceForTalking;
	float distanceForBreakingUpFactor;
	List<ConversationWaiter> waitingList;
	String message_Unavailability = "§8This player is currently unavailable. Please try again later.";
	String message_ConversationFull = "§8Your conference call is full. You cannot add any more players.";
	
	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
	{
		Player playerChatting = event.getPlayer();
		String message = event.getMessage();
		Player[] onlinePlayers = Bukkit.getOnlinePlayers();
		
		List<Player> playersToSendTo = new ArrayList<Player>();
		
		boolean isInConversation = false;
		boolean isPlayerCaller = false;
		boolean isAPlayerCalled = false;
		boolean isCalledPlayerInConversation = false;
		boolean isCalledPlayerWaitingForConversation = false;
		Player playerBeingCalled = null;
		Conversation relevantConversation;
		
		//Check if player is making a phonecall
		if(playerChatting.getItemInHand().getType().equals(Material.getMaterial("WATCH")))
		{
			for(Player player: onlinePlayers)
			{
				if(message.equals(player.getName()))
				{
					isAPlayerCalled = true;
					playerBeingCalled = player;
					for(Conversation conversation: Conversation.Conversations)
					{
						//Check if called Player is already in a Conversation
						if(conversation.containsPlayer(player))
						{
							isCalledPlayerInConversation = true;
						}
					}
						
				}
			}
		}
		
		for(Conversation conversation: Conversation.Conversations)
		{
			if(conversation.containsPlayer(playerChatting))
			{
				isInConversation = true;
			}
		}
		
		if(isAPlayerCalled == true)
		{
			for(ConversationWaiter waiter: waitingList)
			{
				if(waiter.playerBeingCalled == playerBeingCalled)
				{
					isCalledPlayerWaitingForConversation = true;
				}
			}
		}
		
		//If the called player is unavailable, inform the caller
		if(isAPlayerCalled && (isCalledPlayerInConversation || isCalledPlayerWaitingForConversation))
		{
			playerChatting.sendMessage(message_Unavailability);
		}
		
		
		
		
		
	}
	
	@EventHandler
	public void onPlayerItemHeldChanging(PlayerItemHeldEvent event)
	{
		
	}
	
	
}
