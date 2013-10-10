package io.github.redinzane.realisticchat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class RealisticChatListener implements Listener
{
	//Things that should be in the config
	int distanceForWhispering;
	int distanceForYelling;
	int distanceForTalking;
	float distanceForBreakingUpFactor;
	String message_Unavailability = "§8This player is currently unavailable. Please try again later.";
	String message_ConversationFull = "§8Your conference call is full. You cannot add any more players.";
	String message_notOriginalCaller = "§8Only the original caller can add players to the conference call";
	
	List<ConversationWaiter> waitingList;
	public Material clock;
	RealisticChat realisticChat;
	
	
	//Always, always construct after reading the config
	RealisticChatListener(RealisticChat plugin)
	{
		this.realisticChat = plugin;
		distanceForWhispering = realisticChat.config.getDistanceForWhispering();
		distanceForYelling = realisticChat.config.getDistanceForYelling();
		distanceForTalking = realisticChat.config.getDistanceForTalking();
		distanceForBreakingUpFactor = realisticChat.config.getDistanceForBreakingUpFactor();
		clock = Material.getMaterial("WATCH");
	}
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event)
	{
		Player playerChatting = event.getPlayer();
		String message = event.getMessage();
		Player[] onlinePlayers = Bukkit.getOnlinePlayers();
		
		List<Player> playersToSendTo = new ArrayList<Player>();
		
		boolean isInConversation = false;
		boolean isAPlayerCalled = false;
		boolean isCalledPlayerInConversation = false;
		boolean isCalledPlayerWaitingForConversation = false;
		
		Conversation callersConversation = null;
		Player playerBeingCalled = null;
		
		//Check if player is making a phonecall
		if(phoneValidator(playerChatting.getItemInHand()))
		{
			for(Player player: onlinePlayers)
			{
				if(message.equals(player.getName()))
				{
					isAPlayerCalled = true;
					playerBeingCalled = player;
					for(Conversation conversation: Conversation.conversations)
					{
						//Check if called Player is already in a Conversation
						if(conversation.containsPlayer(player))
						{
							isCalledPlayerInConversation = true;
							callersConversation = conversation;
						}
					}
						
				}
			}
		}
		
		for(Conversation conversation: Conversation.conversations)
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
				if(waiter.playerBeingCalled.equals(playerBeingCalled))
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
		//Else, make more checks, then call
		else if(isAPlayerCalled)
		{
			//See if we are already in a conversation
			if(isInConversation)
			{
				//Are we the caller?
				if(callersConversation.caller.equals(playerChatting))
				{
					//Is the conversation full?
					if(callersConversation.playercounter <= Conversation.maxPlayercount)
					{
						playerChatting.sendMessage(message_ConversationFull);
					}
					else
					{
						//Is the called player holding a phone?
						if(phoneValidator(playerBeingCalled.getItemInHand()))
						{
							callersConversation.addPlayerToConversation(playerBeingCalled);
						}
						else
						{
							waitingList.add(new ConversationWaiter(playerChatting, playerBeingCalled));
						}
					}
				}
				else
				{
					playerChatting.sendMessage(message_notOriginalCaller);
				}
			}
			else
			{
				//Is the called player holding a phone?
				if(phoneValidator(playerBeingCalled.getItemInHand()))
				{
					new Conversation(playerChatting, playerBeingCalled);
				}
				else
				{
					waitingList.add(new ConversationWaiter(playerChatting, playerBeingCalled));
				}
			}
		}
		
		
		
	}
	
	@EventHandler
	public void onPlayerItemHeldChanging(PlayerItemHeldEvent event)
	{
		Player relevantPlayer = event.getPlayer();
		int slot = event.getNewSlot();
		boolean conversationExists = false;
		
		for(ConversationWaiter waiter: waitingList)
		{
			if(waiter.caller.equals(relevantPlayer))
			{
				if(phoneValidator(relevantPlayer.getInventory().getItem(slot)))
				{
					//Do nothing...
				}
				else
				{
					waitingList.remove(waiter);
				}
				break;
			}
			else if(waiter.playerBeingCalled.equals(relevantPlayer))
			{
				if(phoneValidator(relevantPlayer.getInventory().getItem(slot)))
				{
					for(Conversation conversation: Conversation.conversations)
					{
						if(waiter.caller.equals(conversation.caller))
						{
							conversation.addPlayerToConversation(relevantPlayer);
							waitingList.remove(waiter);
							conversationExists = true;
							break;
						}
					}
					if(conversationExists == true)
					{
						break;
					}
					else
					{
						new Conversation(waiter.caller, waiter.playerBeingCalled);
					}
				}
			}
		}
		for(Conversation conversation: Conversation.conversations)
		{
			if(conversation.containsPlayer(relevantPlayer))
			{
				if(phoneValidator(relevantPlayer.getInventory().getItem(slot)))
				{
					//Do nothing
				}
				else
				{
					conversation.removePlayerFromConversation(relevantPlayer);
					break;
				}
				
			}
		}
	}
	
	//This is it's own method so I can make an option for speciallly marked items later
		/**
		 * Checks if an item is a phone
		 * @return value - returns false if not a phone, else true
		 */
		public boolean phoneValidator(ItemStack item)
		{
			if(item.getType().equals(clock))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	
	
}
