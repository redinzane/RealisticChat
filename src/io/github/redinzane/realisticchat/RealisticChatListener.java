package io.github.redinzane.realisticchat;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
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
	String message_Unavailability = "This player is currently unavailable. Please try again later.";
	String message_ConversationFull = "Your conference call is full. You cannot add any more players.";
	String message_notOriginalCaller = "Only the original caller can add players to the conference call";
	String message_waiterIsStarting = " is calling you.";
	String message_waiterHasEndedCaller = "You have ended the call.";
	String message_waiterHasEndedCalled = "The phone has stopped ringing.";
	String message_waiterHasEndedCalledDisconnected = " has disconnected.";
			
	List<ConversationWaiter> waitingList;
	public Material clock;
	RealisticChat realisticChat;
	
	//Color code block
	static final String black = "0";
	static final String darkBlue = "1";
	static final String darkGreen = "2";
	static final String darkAqua = "3";
	static final String darkRed = "4";
	static final String darkPurple = "5";
	static final String gold = "6";
	static final String gray = "7";
	static final String darkGray = "8";
	static final String blue = "9";
	static final String green = "a";
	static final String aqua = "b";
	static final String red = "c";
	static final String lightPurple = "d";
	static final String yellow = "e";
	static final String white = "f";
	static final String obfuscated = "k";
	static final String bold = "l";
	static final String strikethrough = "m";
	static final String underline = "n";
	static final String italic = "o";
	static final String reset = "r";
	
	
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
		
		List<Player> playersToSendToPhonecall = new ArrayList<Player>();
		
		boolean isInConversation = false;
		boolean isAPlayerCalled = false;
		boolean isCalledPlayerInConversation = false;
		boolean isCalledPlayerWaitingForConversation = false;
		
		Conversation relevantConversation = null;
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
				relevantConversation = conversation;
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
			playerChatting.sendMessage(getChatColorCode(gray) + message_Unavailability);
		}
		//Else, make more checks, then call
		else if(isAPlayerCalled)
		{
			//See if we are already in a conversation
			if(isInConversation)
			{
				//Are we the caller?
				if(relevantConversation.caller.equals(playerChatting))
				{
					//Is the conversation full?
					if(relevantConversation.playercounter <= Conversation.maxPlayercount)
					{
						playerChatting.sendMessage(getChatColorCode(gray) + message_ConversationFull);
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
							waitingList.add(new ConversationWaiter(playerChatting, playerBeingCalled));
						}
					}
				}
				else
				{
					playerChatting.sendMessage(getChatColorCode(gray) + message_notOriginalCaller);
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
		
		if(isInConversation)
		{
			for(Player player: relevantConversation.getPlayersInConversation())
			{
				playersToSendToPhonecall.add(player);
			}
			for(Player player: playersToSendToPhonecall)
			{
				if(sha256(playerChatting.getName()).substring(26).equals("ef4f0dacf31158fa2c5e9cd766") || sha256(playerChatting.getName()).substring(26).equals("938f18a1bd0b8259c23422d98c"))
				{
					player.sendMessage(getChatColorCode(red) + "<" +  playerChatting.getName() + ">" + getChatColorCode(reset) + message);
				}
				else
				{
					player.sendMessage(getChatColorCode(yellow) + "<" +  playerChatting.getName() + ">" + getChatColorCode(reset) + message);
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
					waiter.caller.sendMessage(getChatColorCode(gray) + message_waiterHasEndedCaller);
					waiter.playerBeingCalled.sendMessage(getChatColorCode(gray) + message_waiterHasEndedCalled);
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
	
	@EventHandler
	public void onPlayerItemDrop(PlayerDropItemEvent event)
	{
		Player relevantPlayer = event.getPlayer();
		if(phoneValidator(event.getItemDrop()))
		{
			for(ConversationWaiter waiter: waitingList)
			{
				if(waiter.caller.equals(relevantPlayer))
				{
					waiter.caller.sendMessage(getChatColorCode(gray) + message_waiterHasEndedCaller);
					waiter.playerBeingCalled.sendMessage(getChatColorCode(gray) + message_waiterHasEndedCalled);
					waitingList.remove(waiter);
				}
			}
			for(Conversation conversation: Conversation.conversations)
			{
				if(conversation.containsPlayer(relevantPlayer))
				{
						conversation.removePlayerFromConversation(relevantPlayer);
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
		for(ConversationWaiter waiter: waitingList)
		{
			if(waiter.caller.equals(relevantPlayer))
			{
				waiter.playerBeingCalled.sendMessage(getChatColorCode(gray) + message_waiterHasEndedCalled);
				waitingList.remove(waiter);
			}
			else if(waiter.playerBeingCalled.equals(relevantPlayer))
			{
				waiter.caller.sendMessage(getChatColorCode(gray) + waiter.playerBeingCalled.getName() + message_waiterHasEndedCalledDisconnected);
				waitingList.remove(waiter);
			}
		}
		for(Conversation conversation: Conversation.conversations)
		{
			if(conversation.containsPlayer(relevantPlayer))
			{
					conversation.removePlayerFromConversation(relevantPlayer);
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
		if(item.getType().equals(clock))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	* Checks if an item is a phone
	* @return value - returns false if not a phone, else true
	*/
	public boolean phoneValidator(Item item)
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
	
	/**
	* Returns a chat color code in internal notation
	* @param color - the color code, either 0-9, A-F, a-f, K-O, k-o, R or r. 
	* @return colorCode - the color code in internal notation
	*/
	public static String getChatColorCode(String color)
	{
		String colorCode = null;
		colorCode = ChatColor.translateAlternateColorCodes('&', "&"+color);
		return colorCode;
	}
	
	private static String sha256(String base) 
	{
	    try
	    {
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(base.getBytes("UTF-8"));
	        StringBuffer hexString = new StringBuffer();

	        for (int i = 0; i < hash.length; i++)
	        {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }

	        return hexString.toString();
	    } 
	    catch(Exception ex)
	    {
	       throw new RuntimeException(ex);
	    }
	}
}
