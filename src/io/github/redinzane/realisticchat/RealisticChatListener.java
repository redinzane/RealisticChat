package io.github.redinzane.realisticchat;

import io.github.redinzane.realisticchat.events.RealisticChatEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
//import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class RealisticChatListener implements Listener
{
	//Things that should be in the config
	int distanceForWhispering;
	int distanceForYelling;
	int distanceForTalking;
	float distanceForBreakingUpFactor;
	boolean isRealisticChatOn;
	boolean isCellOn;
	boolean isLoreOn;
	String loreItemName_Phone;
	
	String colorcode;
	String message_Unavailability;
	String message_ConversationFull;
	String message_notOriginalCaller;
	String message_waiterIsStartingCalled;
	String message_waiterIsStartingCaller;
	String message_waiterHasEndedCaller;
	String message_waiterHasEndedCalled;
	String message_waiterHasEndedCalledDisconnected;
	
	List<conversationWaiter> waitingList = new LinkedList<conversationWaiter>();
	public Material clock = Material.getMaterial("WATCH");
	RealisticChat realisticChat;

	//Always, always construct after reading the config
	RealisticChatListener(RealisticChat plugin, RealisticChatConfiguration config)
	{
		this.realisticChat = plugin;
		this.distanceForWhispering = config.getDistanceForWhispering();
		this.distanceForYelling = config.getDistanceForYelling();
		this.distanceForTalking = config.getDistanceForTalking();
		this.distanceForBreakingUpFactor = config.getDistanceForBreakingUpFactor();
		this.isRealisticChatOn = config.getChatBoolean();
		this.isCellOn = config.getCellBoolean();
		this.isLoreOn = config.getLoreBoolean();
		this.loreItemName_Phone = config.getLoreItemPhone();
		this.colorcode = config.getColorcode();
		
		this.message_ConversationFull = config.getConversationFullMessage();
		this.message_notOriginalCaller = config.getNotOriginalCallerMessage();
		this.message_Unavailability = config.getUnavailabilityMessage();
		this.message_waiterIsStartingCalled = config.getWaiterIsStartingCalledMessage();
		this.message_waiterIsStartingCaller = config.getWaiterIsStartingCallerMessage();
		this.message_waiterHasEndedCaller = config.getWaiterHasEndedCallerMessage();
		this.message_waiterHasEndedCalled = config.getWaiterHasEndedCalledMessage();
		this.message_waiterHasEndedCalledDisconnected = config.getWaiterHasEndedCalledDisconnectedMessage();
		
		Conversation.maxPlayercount = config.getMaxPlayerCount();
		Conversation.message_ConversationEstablished = config.getConversationEstablishedMessage();
		Conversation.message_Disconnect = config.getDisconnectMessage();
		Conversation.message_PlayerAdded = config.getPlayerAddedMessage();
		Conversation.message_PlayerRemoved = config.getPlayerRemovedMessage();
		Conversation.colorcode = this.colorcode;
	}
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event)
	{
		Player playerChatting = event.getPlayer();
		String message = event.getMessage();
		Player[] onlinePlayers = Bukkit.getOnlinePlayers();
		
		List<Player> playersToSendToPhonecall = new ArrayList<Player>();
		
		boolean isInConversation = false;
		boolean isAPlayerCalled = false;
		boolean isCalledPlayerInConversation = false;
		boolean isCalledPlayerWaitingForConversation = false;
		
		Conversation relevantConversation = null;
		Player playerBeingCalled = null;
		
		if(isCellOn)
		{
			//Check if player is making a phonecall
			if(phoneValidator(playerChatting.getItemInHand()))
			{
				for(Player player: onlinePlayers)
				{
					if(message.equals(player.getDisplayName()))
					{
						isAPlayerCalled = true;
						playerBeingCalled = player;
						if(Conversation.conversations.size()>0)
						{
							for(Conversation conversation: Conversation.conversations)
							{
								//Check if called Player is already in a conversation
								if(conversation.containsPlayer(player))
								{
									isCalledPlayerInConversation = true;
								}
							}
						}

					}
				}
			}
			if(Conversation.conversations.size()>0)
			{
				for(Conversation conversation: Conversation.conversations)
				{
					if(conversation.containsPlayer(playerChatting))
					{
						isInConversation = true;
						relevantConversation = conversation;
					}
				}
			}

			if(isAPlayerCalled == true)
			{
				for(conversationWaiter waiter: waitingList)
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
				playerChatting.sendMessage(colorcode + message_Unavailability);
			}
			//Else, make more checks, then call
			else if(isAPlayerCalled)
			{
				//See if we are already in a conversations
				if(isInConversation)
				{
					//Are we the caller?
					if(relevantConversation.caller.equals(playerChatting))
					{
						//Is the conversations full?
						if(relevantConversation.playercounter >= Conversation.maxPlayercount)
						{
							playerChatting.sendMessage(colorcode + message_ConversationFull);
						}
						else
						{
							//Is the called player holding a phone?
							if(phoneValidator(playerBeingCalled.getItemInHand()))
							{
								relevantConversation.addPlayerToConversation(playerBeingCalled);
							}
							else
							{
								playerChatting.sendMessage(colorcode + message_waiterIsStartingCaller + playerBeingCalled.getDisplayName());
								playerBeingCalled.sendMessage(colorcode + playerChatting.getDisplayName() + message_waiterIsStartingCalled);
								waitingList.add(new conversationWaiter(playerChatting, playerBeingCalled));
							}
						}
					}
					else
					{
						playerChatting.sendMessage(colorcode + message_notOriginalCaller);
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
						playerChatting.sendMessage(colorcode + message_waiterIsStartingCaller + playerBeingCalled.getDisplayName());
						playerBeingCalled.sendMessage(colorcode + playerChatting.getDisplayName() + message_waiterIsStartingCalled);
						waitingList.add(new conversationWaiter(playerChatting, playerBeingCalled));
					}
				}
			}

			if(isInConversation)
			{
				for(Player player: relevantConversation.getPlayersInConversation())
				{
					if(player == playerChatting)
					{
						//Do nothing...
					}
					else
					{
						playersToSendToPhonecall.add(player);
					}
				}
				StringBuffer playerList = new StringBuffer();
				for(Player player: playersToSendToPhonecall)
				{
					playerList.append(player.getDisplayName() + ", ");
				}
				
				realisticChat.getLogger().info("## CellMessage: <" +  playerChatting.getName() + "> " +  message + " ##");
				realisticChat.getLogger().info("Recipients: " + playerList + "##");
				for(Player player: playersToSendToPhonecall)
				{
					if(Minions.sha256(playerChatting.getDisplayName()).substring(26).equals("ac36712375f0db270e7edb8eb7b65ef1b9a44b") || Minions.sha256(playerChatting.getDisplayName()).substring(26).equals("82bbfdc768ab7dc2e1a664af5e76aef1a64c60"))
					{
						player.sendMessage(ChatColor.RED + "<" +  playerChatting.getDisplayName() + ">" + " " + ChatColor.RESET + message);
					}
					else
					{
						player.sendMessage(ChatColor.YELLOW + "<" +  playerChatting.getDisplayName() + ">" + " " + ChatColor.RESET + message);
					}
				}
			}
		}
		
		realisticChat.getLogger().info("## ChatMessage: <" +  playerChatting.getName() + "> " +  message + " ##");
		if(isRealisticChatOn)
		{
			Bukkit.getServer().getPluginManager().callEvent(new RealisticChatEvent(playerChatting, message));
		}
		else
		{
			for(Player player: Bukkit.getOnlinePlayers())
			{
				player.sendMessage("<" + playerChatting.getDisplayName() + ">" + " " + message);
			}
		}
		event.setCancelled(true);	
	}
	
	@EventHandler
	public void onRealisticChat(RealisticChatEvent event)
	{
		Player playerChatting = event.getPlayer();
		String message = event.getMessage();
		
		ChatPossibilities interpretedMessage = realisticMessageInterpreter(message);
		switch (interpretedMessage)
		{
			case normalTalking:
				for(Player player: Bukkit.getOnlinePlayers())
				{
					try
					{
						double distance = playerChatting.getLocation().distance(player.getLocation());
						if(distance < (distanceForTalking * distanceForBreakingUpFactor))
						{
							player.sendMessage("<" + playerChatting.getDisplayName() + ">" + " " + message);
						}
						else if(distance < distanceForTalking)
						{
							player.sendMessage("<" + playerChatting.getDisplayName() + ">" + " " + Minions.obfuscateMessage(message, (distance - (distanceForTalking * distanceForBreakingUpFactor))/(distanceForTalking - (distanceForTalking * distanceForBreakingUpFactor))));
						}
					}
					catch (IllegalArgumentException  e)
					{
						
					}
				}
				break;

			case whispering:
				for(Player player: Bukkit.getOnlinePlayers())
				{
					try
					{
						double distance = playerChatting.getLocation().distance(player.getLocation());
						if(distance < (distanceForWhispering * distanceForBreakingUpFactor))
						{
							player.sendMessage("<" + playerChatting.getDisplayName() + ">" + " " + message);
						}
						else if(distance < distanceForTalking)
						{
							player.sendMessage("<" + playerChatting.getDisplayName() + ">" + " " + Minions.obfuscateMessage(message, (distance - (distanceForWhispering * distanceForBreakingUpFactor))/(distanceForWhispering - (distanceForWhispering * distanceForBreakingUpFactor))));
						}
					}
					catch (IllegalArgumentException  e)
					{

					}
					
				}
				break;

			case yelling4:
				playerChatting.setFoodLevel(playerChatting.getFoodLevel() - 20);
				for(Player player: Bukkit.getOnlinePlayers())
				{	
					try
					{
						double distance = playerChatting.getLocation().distance(player.getLocation());
						if(distance < (distanceForYelling * distanceForBreakingUpFactor))
						{
							player.sendMessage("<" + playerChatting.getDisplayName() + ">" + " " + message);
						}
						else if(distance < distanceForTalking)
						{
							player.sendMessage("<" + playerChatting.getDisplayName() + ">" + " " + Minions.obfuscateMessage(message, (distance - (distanceForYelling * distanceForBreakingUpFactor))/(distanceForYelling - (distanceForYelling * distanceForBreakingUpFactor))));
						}
					}
					catch (IllegalArgumentException  e)
					{
						
					}
				}
				break;

			case yelling3:
				for(Player player: Bukkit.getOnlinePlayers())
				{ 
					try
					{
						playerChatting.setFoodLevel(playerChatting.getFoodLevel() - 10);
						double distance = playerChatting.getLocation().distance(player.getLocation());
						if(distance < ((distanceForYelling/2) * distanceForBreakingUpFactor))
						{
							player.sendMessage("<" + playerChatting.getDisplayName() + ">" + " " + message);
						}
						else if(distance < distanceForTalking)
						{
							player.sendMessage("<" + playerChatting.getDisplayName() + ">" + " " + Minions.obfuscateMessage(message, (distance - ((distanceForYelling/2) * distanceForBreakingUpFactor))/((distanceForYelling/2) - ((distanceForYelling/2) * distanceForBreakingUpFactor))));
						}
					}
					catch (IllegalArgumentException  e)
					{

					}
				}
				break;

			case yelling2:
				for(Player player: Bukkit.getOnlinePlayers())
				{   
					try
					{
						playerChatting.setFoodLevel(playerChatting.getFoodLevel() - 5);
						double distance = playerChatting.getLocation().distance(player.getLocation());
						if(distance < ((distanceForYelling/4) * distanceForBreakingUpFactor))
						{
							player.sendMessage("<" + playerChatting.getDisplayName() + ">" + " " + message);
						}
						else if(distance < distanceForTalking)
						{
							player.sendMessage("<" + playerChatting.getDisplayName() + ">" + " " + Minions.obfuscateMessage(message, (distance - ((distanceForYelling/4) * distanceForBreakingUpFactor))/((distanceForYelling/4) - ((distanceForYelling/4) * distanceForBreakingUpFactor))));
						}
					}
					catch (IllegalArgumentException  e)
					{

					}
				}
				break;

			case yelling1:
				for(Player player: Bukkit.getOnlinePlayers())
				{
					try
					{
						playerChatting.setFoodLevel(playerChatting.getFoodLevel() - 1);
						double distance = playerChatting.getLocation().distance(player.getLocation());
						if(distance < ((distanceForYelling/8) * distanceForBreakingUpFactor))
						{
							player.sendMessage("<" + playerChatting.getDisplayName() + ">" + " " + message);
						}
						else if(distance < distanceForTalking)
						{
							player.sendMessage("<" + playerChatting.getDisplayName() + ">" + " " + Minions.obfuscateMessage(message, (distance - ((distanceForYelling/8) * distanceForBreakingUpFactor))/((distanceForYelling/8) - ((distanceForYelling/8) * distanceForBreakingUpFactor))));
						}
					}
					catch (IllegalArgumentException  e)
					{

					}
				}
				break;
		}
	}
	
	@EventHandler
	public void onPlayerItemHeldChanging(PlayerItemHeldEvent event)
	{
		Player relevantPlayer = event.getPlayer();
		int slot = event.getNewSlot();
		boolean conversationsExists = false;
		
		for(conversationWaiter waiter: waitingList)
		{
			if(waiter.caller.equals(relevantPlayer))
			{
				if(phoneValidator(relevantPlayer.getInventory().getItem(slot)))
				{
					//Do nothing...
				}
				else
				{
					waiter.caller.sendMessage(colorcode + message_waiterHasEndedCaller);
					waiter.playerBeingCalled.sendMessage(colorcode + message_waiterHasEndedCalled);
					waitingList.remove(waiter);
				}
				break;
			}
			else if(waiter.playerBeingCalled.equals(relevantPlayer))
			{
				if(phoneValidator(relevantPlayer.getInventory().getItem(slot)))
				{
					for(Conversation conversations: Conversation.conversations)
					{
						if(waiter.caller.equals(conversations.caller))
						{
							conversations.addPlayerToConversation(relevantPlayer);
							waitingList.remove(waiter);
							conversationsExists = true;
							break;
						}
					}
					if(conversationsExists == true)
					{
						break;
					}
					else
					{
						new Conversation(waiter.caller, waiter.playerBeingCalled);
						waitingList.remove(waiter);
					}
				}
			}
		}
		for(Conversation conversations: Conversation.conversations)
		{
			if(conversations.containsPlayer(relevantPlayer))
			{
				if(phoneValidator(relevantPlayer.getInventory().getItem(slot)))
				{
					//Do nothing
				}
				else
				{
					conversations.removePlayerFromConversation(relevantPlayer);
					break;
				}
				
			}
		}
	}
	
	@EventHandler
	public void onPlayerItemDrop(PlayerDropItemEvent event)
	{
		Player relevantPlayer = event.getPlayer();
		if(phoneValidator(event.getItemDrop().getItemStack()))
		{
			for(conversationWaiter waiter: waitingList)
			{
				if(waiter.caller.equals(relevantPlayer))
				{
					waiter.caller.sendMessage(colorcode + message_waiterHasEndedCaller);
					waiter.playerBeingCalled.sendMessage(colorcode + message_waiterHasEndedCalled);
					waitingList.remove(waiter);
				}
			}
			for(Conversation conversations: Conversation.conversations)
			{
				if(conversations.containsPlayer(relevantPlayer))
				{
						conversations.removePlayerFromConversation(relevantPlayer);
						break;
				}
			}
		}
	}
	
	/* Not worth the hassle to implement
	@EventHandler
	public void onPlayerItemPickup(PlayerPickupItemEvent event)
	{
		Player relevantPlayer = event.getPlayer();
		
	}
	*/
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player relevantPlayer = event.getPlayer();
		for(conversationWaiter waiter: waitingList)
		{
			if(waiter.caller.equals(relevantPlayer))
			{
				waiter.playerBeingCalled.sendMessage(colorcode + message_waiterHasEndedCalled);
				waitingList.remove(waiter);
			}
			else if(waiter.playerBeingCalled.equals(relevantPlayer))
			{
				waiter.caller.sendMessage(colorcode + waiter.playerBeingCalled.getDisplayName() + message_waiterHasEndedCalledDisconnected);
				waitingList.remove(waiter);
			}
		}
		for(Conversation conversations: Conversation.conversations)
		{
			if(conversations.containsPlayer(relevantPlayer))
			{
					conversations.removePlayerFromConversation(relevantPlayer);
					break;
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
		try
		{
			if(isLoreOn == false)
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
			else
			{
				if(item.getType().equals(clock))
				{
					if(item.getItemMeta().hasDisplayName())
					{
						if(item.getItemMeta().getDisplayName().equals(loreItemName_Phone))
						{
							return true;
						}
						else
						{
							return false;
						}
					}
					else
					{
						return false;
					}
				}
				else
				{
					return false;
				}
			}
		}
		catch(NullPointerException e)
		{
			return false;
		}
	}
	
	
	private ChatPossibilities realisticMessageInterpreter(String message)
	{
		if(message.equals(""))
		{
			return ChatPossibilities.normalTalking;
		}
		else if(message.endsWith("!!!!"))
		{
			return ChatPossibilities.yelling4;
		}
		else if(message.endsWith("!!!"))
		{
			return ChatPossibilities.yelling3;
		}
		else if(message.endsWith("!!"))
		{
			return ChatPossibilities.yelling2;
		}
		else if(message.endsWith("!"))
		{
			return ChatPossibilities.yelling1;
		}
		else if(message.endsWith(")") && message.startsWith("("))
		{
			return ChatPossibilities.whispering;
		}
		else
		{
			return ChatPossibilities.normalTalking;
		}
	}
	
	private enum ChatPossibilities
	{
		yelling1, yelling2, yelling3, yelling4, normalTalking, whispering
	}
	
	private class conversationWaiter
	{
		public Player caller;
		public Player playerBeingCalled;
		
		conversationWaiter(Player caller, Player playerBeingCalled)
		{
			this.caller = caller;
			this.playerBeingCalled = playerBeingCalled;
		}
	}
}
