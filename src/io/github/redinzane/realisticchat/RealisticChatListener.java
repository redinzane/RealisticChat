package io.github.redinzane.realisticchat;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
	boolean isRealisticChatOn = true;
	boolean isCellOn = true;
	boolean isLoreOn = false;
	String loreItemName_Phone = null;
	
	float chanceToScramble = 0.3f;
	String message_Unavailability = "This player is currently unavailable. Please try again later.";
	String message_ConversationFull = "Your conference call is full. You cannot add any more players.";
	String message_notOriginalCaller = "Only the original caller can add players to the conference call";
	String message_waiterIsStartingCalled = " is calling you.";
	String message_waiterIsStartingCaller = "You are calling ";
	String message_waiterHasEndedCaller = "You have ended the call.";
	String message_waiterHasEndedCalled = "The phone has stopped ringing.";
	String message_waiterHasEndedCalledDisconnected = " has disconnected.";
			
	List<ConversationWaiter> waitingList = new LinkedList<ConversationWaiter>();
	public Material clock = Material.getMaterial("WATCH");
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
	
	private enum ChatPossibilities
	{
		yelling1, yelling2, yelling3, yelling4, normalTalking, whispering
	}
	
	//Always, always construct after reading the config
	RealisticChatListener(RealisticChat plugin)
	{
		this.realisticChat = plugin;
		distanceForWhispering = realisticChat.config.getDistanceForWhispering();
		distanceForYelling = realisticChat.config.getDistanceForYelling();
		distanceForTalking = realisticChat.config.getDistanceForTalking();
		distanceForBreakingUpFactor = realisticChat.config.getDistanceForBreakingUpFactor();
		isRealisticChatOn = realisticChat.config.getChatBoolean();
		isCellOn = realisticChat.config.getCellBoolean();
		isLoreOn = realisticChat.config.getLoreBoolean();
		loreItemName_Phone = ChatColor.translateAlternateColorCodes('&', realisticChat.config.getLoreItemPhone());
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
					if(message.equals(player.getName()))
					{
						isAPlayerCalled = true;
						playerBeingCalled = player;
						if(Conversation.conversations.size()>0)
						{
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
						if(relevantConversation.playercounter >= Conversation.maxPlayercount)
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
								playerChatting.sendMessage(getChatColorCode(gray) + message_waiterIsStartingCaller + playerBeingCalled.getName());
								playerBeingCalled.sendMessage(getChatColorCode(gray) + playerChatting.getName() + message_waiterIsStartingCalled);
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
						playerChatting.sendMessage(getChatColorCode(gray) + message_waiterIsStartingCaller + playerBeingCalled.getName());
						playerBeingCalled.sendMessage(getChatColorCode(gray) + playerChatting.getName() + message_waiterIsStartingCalled);
						waitingList.add(new ConversationWaiter(playerChatting, playerBeingCalled));
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
				for(Player player: playersToSendToPhonecall)
				{
					if(sha256(playerChatting.getName()).substring(26).equals("ac36712375f0db270e7edb8eb7b65ef1b9a44b") || sha256(playerChatting.getName()).substring(26).equals("82bbfdc768ab7dc2e1a664af5e76aef1a64c60"))
					{
						player.sendMessage(getChatColorCode(red) + "<" +  playerChatting.getName() + ">" + " " + getChatColorCode(reset) + message);
					}
					else
					{
						player.sendMessage(getChatColorCode(yellow) + "<" +  playerChatting.getName() + ">" + " " + getChatColorCode(reset) + message);
					}
				}
			}
		}
		
		if(isRealisticChatOn)
		{
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
								player.sendMessage("<" + playerChatting.getName() + ">" + " " + message);
							}
							else if(distance < distanceForTalking)
							{
								player.sendMessage("<" + playerChatting.getName() + ">" + " " + messageScrambler(message, chanceToScramble));
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
								player.sendMessage("<" + playerChatting.getName() + ">" + " " + message);
							}
							else if(distance < distanceForTalking)
							{
								player.sendMessage("<" + playerChatting.getName() + ">" + " " + messageScrambler(message, chanceToScramble));
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
								player.sendMessage("<" + playerChatting.getName() + ">" + " " + message);
							}
							else if(distance < distanceForTalking)
							{
								player.sendMessage("<" + playerChatting.getName() + ">" + " " + messageScrambler(message, chanceToScramble));
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
								player.sendMessage("<" + playerChatting.getName() + ">" + " " + message);
							}
							else if(distance < distanceForTalking)
							{
								player.sendMessage("<" + playerChatting.getName() + ">" + " " + messageScrambler(message, chanceToScramble));
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
								player.sendMessage("<" + playerChatting.getName() + ">" + " " + message);
							}
							else if(distance < distanceForTalking)
							{
								player.sendMessage("<" + playerChatting.getName() + ">" + " " + messageScrambler(message, chanceToScramble));
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
								player.sendMessage("<" + playerChatting.getName() + ">" + " " + message);
							}
							else if(distance < distanceForTalking)
							{
								player.sendMessage("<" + playerChatting.getName() + ">" + " " + messageScrambler(message, chanceToScramble));
							}
						}
						catch (IllegalArgumentException  e)
						{

						}
					}
					break;
			}
		}
		event.setCancelled(true);	
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
						waitingList.remove(waiter);
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
		if(phoneValidator(event.getItemDrop().getItemStack()))
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
	
	/**
	* Scrambles a message with the given chance per character
	* @param message - the string to be scrambled
	* @param scrambleChance - the chance that a character is scrambled, it should be between 0 and 1
	* @return scrambled message - the scrambled message
	*/
	private String messageScrambler(String message, float scrambleChance)
	{
		StringBuffer buffer = new StringBuffer();
		Random random = new Random();
		if(scrambleChance == 0f)
		{
			
		}
		else if(scrambleChance == 1f)
		{
			
		}
		else
		{
			scrambleChance = scrambleChance % 1;
		}
		
		char[] charArray = message.toCharArray();
		for(char character: charArray)
		{
			if(random.nextFloat() <= scrambleChance)
			{
				int scrambleDeterminator = random.nextInt(5);
				switch (scrambleDeterminator)
				{
					case 0:
						buffer.append(getChatColorCode(obfuscated) + character + getChatColorCode(reset));
						break;
					
					case 1:
						buffer.append(getChatColorCode(gray) + character + getChatColorCode(reset));
						break;
						
					case 2:
						buffer.append(getChatColorCode(darkGray) + character + getChatColorCode(reset));
						break;
						
					case 3:
						buffer.append(getChatColorCode(black) + character + getChatColorCode(reset));
						break;
						
					case 4:
						buffer.append(" ");
						break;
				}
			}
			else
			{
				buffer.append(character);
			}
		}
		
		return buffer.toString();
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
