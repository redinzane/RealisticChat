package io.github.redinzane.realisticchat;

import io.github.redinzane.realisticchat.events.CellPhoneCallEvent;
import io.github.redinzane.realisticchat.events.RealisticChatEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
public class RealisticChatListener implements Listener {
	// Things that should be in the config
	int distanceForWhispering;
	int distanceForYelling;
	int yellingfactor1;
	int yellingfactor2;
	int yellingfactor3;
	int yellingfactor4;
	int hungercost1;
	int hungercost2;
	int hungercost3;
	int hungercost4;
	int distanceForTalking;
	float distanceForBreakingUpFactor;
	boolean isRealisticChatOn;
	boolean isCellOn;
	boolean isCellTowerOn;
	boolean isLoreOn;
	String loreItemName_Phone;

	String colorcode;
	String colorcodeincomingchat;
	String colorcodeoutgoingchat;
	char codeincomingchat;
	char codeoutgoingchat;
	String colorcodeincomingcell;
	String colorcodeoutgoingcell;
	char codeincomingcell;
	char codeoutgoingcell;
	String colorcodeincomingchatname;
	String colorcodeoutgoingchatname;
	String colorcodeincomingcellname;
	String colorcodeoutgoingcellname;
	String message_Unavailability;
	String message_ConversationFull;
	String message_notOriginalCaller;
	String message_waiterIsStartingCalled;
	String message_waiterIsStartingCaller;
	String message_waiterHasEndedCaller;
	String message_waiterHasEndedCalled;
	String message_waiterHasEndedCalledDisconnected;
	// TODO: Not in config yet
	String message_notConnectedToTheNetwork = "You are not connected to the phone network.";

	// Nonconfigurables
	List<conversationWaiter> waitingList = new LinkedList<conversationWaiter>();
	public Material clock = Material.getMaterial("WATCH");
	RealisticChat realisticChat;
	CellTowerManager ctManager;
	protected boolean isWindows;

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	RealisticChatListener(RealisticChat plugin, RealisticChatConfiguration config) {
		this.realisticChat = plugin;
		this.distanceForWhispering = config.getDistanceForWhispering();
		this.distanceForYelling = config.getDistanceForYelling();
		this.yellingfactor1 = config.getDistanceForYelling1();
		this.yellingfactor2 = config.getDistanceForYelling2();
		this.yellingfactor3 = config.getDistanceForYelling3();
		this.yellingfactor4 = config.getDistanceForYelling4();
		this.hungercost1 = config.getHungerCost1();
		this.hungercost2 = config.getHungerCost2();
		this.hungercost3 = config.getHungerCost3();
		this.hungercost4 = config.getHungerCost4();
		this.distanceForTalking = config.getDistanceForTalking();
		this.distanceForBreakingUpFactor = config.getDistanceForBreakingUpFactor();
		this.isRealisticChatOn = config.getChatBoolean();
		this.isCellOn = config.getCellBoolean();
		this.isCellTowerOn = config.getCelltowerBoolean();
		this.isLoreOn = config.getLoreBoolean();
		this.loreItemName_Phone = config.getLoreItemPhone();
		this.colorcode = config.getColorcode();
		this.colorcodeincomingchat = config.getColorcodeIncomingChat();
		this.colorcodeoutgoingchat = config.getColorcodeOutgoingChat();
		this.colorcodeincomingcell = config.getColorcodeIncomingCell();
		this.colorcodeoutgoingcell = config.getColorcodeOutgoingCell();
		this.codeincomingchat = this.colorcodeincomingchat.charAt(1);
		this.codeoutgoingchat = this.colorcodeoutgoingchat.charAt(1);
		this.codeincomingcell = this.colorcodeincomingcell.charAt(1);
		this.codeoutgoingcell = this.colorcodeoutgoingcell.charAt(1);
		this.colorcodeincomingchatname = config.getColorcodeIncomingChatName();
		this.colorcodeoutgoingchatname = config.getColorcodeOutgoingChatName();
		this.colorcodeincomingcellname = config.getColorcodeIncomingCellName();
		this.colorcodeoutgoingcellname = config.getColorcodeOutgoingCellName();

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

		realisticChat.getServer().getPluginManager().registerEvents(this, realisticChat);
		if (isCellTowerOn) {
			this.ctManager = new CellTowerManager(this, config);
			realisticChat.getServer().getPluginManager().registerEvents(ctManager, realisticChat);
			realisticChat.getServer().getScheduler().scheduleSyncRepeatingTask(realisticChat, ctManager, 100L, config.getCTTickPeriod());
		}

		String os = System.getProperty("os.name").toLowerCase();
		if (isUnix(os)) {
			// nothing
		} else if (isMac(os)) {
			// nothing
		} else if (isWindows(os)) {
			isWindows = true;
		}
	}

	private static boolean isMac(String os) {
		return (os.indexOf("mac") >= 0);
	}

	private static boolean isWindows(String os) {
		return (os.indexOf("win") >= 0);
	}

	private static boolean isUnix(String os) {
		return (os.indexOf("nux") >= 0);
	}

	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		Player playerChatting = event.getPlayer();
		String message = event.getMessage();
		if (isWindows) {
			realisticChat.getLogger().info("Ranged: <" + playerChatting.getName() + "> " + message + " #");
		} else {
			realisticChat.getLogger().info(ANSI_YELLOW + "Ranged: <" + playerChatting.getName() + "> " + ANSI_CYAN + message + " #" + ANSI_RESET);
		}

		if (isCellOn) {
			Bukkit.getServer().getPluginManager().callEvent(new CellPhoneCallEvent(playerChatting, message));
		}
		if (isRealisticChatOn) {
			Bukkit.getServer().getPluginManager().callEvent(new RealisticChatEvent(playerChatting, message));
		} else {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.sendMessage("<" + playerChatting.getDisplayName() + ">" + " " + message);
			}
		}
		event.setCancelled(true);
	}

	@EventHandler
	public void onCellPhoneCall(CellPhoneCallEvent event) {
		Player playerChatting = event.getPlayer();
		String message = event.getMessage();
		Player[] onlinePlayers = Bukkit.getOnlinePlayers();
		List<Player> playersToSendToPhonecall = new ArrayList<Player>();
		Conversation relevantConversation = null;
		Player playerBeingCalled = null;
		boolean isInConversation = false;
		boolean isAPlayerCalled = false;
		boolean isCalledPlayerInConversation = false;
		boolean isCalledPlayerWaitingForConversation = false;
		boolean isCalledPlayerOutOfRange = false;
		// Are we already in a conversation?
		if (Conversation.conversations.size() > 0) {
			for (Conversation conversation : Conversation.conversations) {
				if (conversation.containsPlayer(playerChatting)) {
					isInConversation = true;
					relevantConversation = conversation;
				}
			}
		}

		if (isCellTowerOn) {
			if (phoneValidator(playerChatting.getItemInHand())) {
				Location place = playerChatting.getLocation();
				Location towerLocation = ctManager.findClosestTower(place);
				if (towerLocation == null) {
					if (isInConversation) {
						relevantConversation.removePlayerFromConversation(playerChatting);
					}
					playerChatting.sendMessage(colorcode + message_notConnectedToTheNetwork);
					return;
				} else if (ctManager.getTower(towerLocation).getNormalizedReceptionPower(place) <= 0) {
					if (isInConversation) {
						relevantConversation.removePlayerFromConversation(playerChatting);
					}
					playerChatting.sendMessage(colorcode + message_notConnectedToTheNetwork);
					return;
				}
			}
		}

		// Check if player is making a phonecall
		if (phoneValidator(playerChatting.getItemInHand())) {
			for (Player player : onlinePlayers) {
				if (message.equals(player.getName())) {
					isAPlayerCalled = true;
					playerBeingCalled = player;
					if (Conversation.conversations.size() > 0) {
						for (Conversation conversation : Conversation.conversations) {
							// Check if called Player is already in a conversation
							if (conversation.containsPlayer(player)) {
								isCalledPlayerInConversation = true;
							}
						}
					}

				}
			}
		}

		if (Conversation.conversations.size() > 0) {
			for (Conversation conversation : Conversation.conversations) {
				if (conversation.containsPlayer(playerChatting)) {
					isInConversation = true;
					relevantConversation = conversation;
				}
			}
		}

		if (isAPlayerCalled == true) {
			for (conversationWaiter waiter : waitingList) {
				if (waiter.playerBeingCalled.equals(playerBeingCalled)) {
					isCalledPlayerWaitingForConversation = true;
				}
			}
			if (isCellTowerOn) {
				Location place = playerBeingCalled.getLocation();
				Location towerLocation = ctManager.findClosestTower(place);
				if (towerLocation == null) {
					isCalledPlayerOutOfRange = false;
				} else if (ctManager.getTower(towerLocation).getNormalizedReceptionPower(place) <= 0) {
					isCalledPlayerOutOfRange = false;
				}
			}
		}
		// If the called player is unavailable, inform the caller
		if (isAPlayerCalled && (isCalledPlayerInConversation || isCalledPlayerWaitingForConversation || isCalledPlayerOutOfRange)) {
			playerChatting.sendMessage(colorcode + message_Unavailability);
		}
		// Else, make more checks, then call
		else if (isAPlayerCalled) {
			// See if we are already in a conversations
			if (isInConversation) {
				// Are we the caller?
				if (relevantConversation.caller.equals(playerChatting)) {
					// Is the conversation full?
					if (relevantConversation.playercounter >= Conversation.maxPlayercount) {
						playerChatting.sendMessage(colorcode + message_ConversationFull);
					} else {
						// Is the called player holding a phone?
						if (phoneValidator(playerBeingCalled.getItemInHand())) {
							relevantConversation.addPlayerToConversation(playerBeingCalled);
						} else {
							playerChatting.sendMessage(colorcode + message_waiterIsStartingCaller + playerBeingCalled.getDisplayName() + colorcode);
							playerBeingCalled.sendMessage(colorcode + playerChatting.getDisplayName() + colorcode + message_waiterIsStartingCalled);
							waitingList.add(new conversationWaiter(playerChatting, playerBeingCalled));
						}
					}
				} else {
					playerChatting.sendMessage(colorcode + message_notOriginalCaller);
				}
			} else {
				// Is the called player holding a phone?
				if (phoneValidator(playerBeingCalled.getItemInHand())) {
					new Conversation(playerChatting, playerBeingCalled);
				} else {
					playerChatting.sendMessage(colorcode + message_waiterIsStartingCaller + playerBeingCalled.getDisplayName() + colorcode);
					playerBeingCalled.sendMessage(colorcode + playerChatting.getDisplayName() + colorcode + message_waiterIsStartingCalled);
					waitingList.add(new conversationWaiter(playerChatting, playerBeingCalled));
				}
			}
		}

		if (isInConversation) {
			for (Player player : relevantConversation.getPlayersInConversation()) {
				if (player == playerChatting) {
					// Do nothing...
				} else {
					if (isCellTowerOn) {
						Location place = player.getLocation();
						Location towerLocation = ctManager.findClosestTower(place);
						if (towerLocation == null) {
							player.sendMessage(colorcode + message_notConnectedToTheNetwork);
							relevantConversation.removePlayerFromConversation(player);
						} else if (ctManager.getTower(towerLocation).getNormalizedReceptionPower(place) <= 0) {
							player.sendMessage(colorcode + message_notConnectedToTheNetwork);
							relevantConversation.removePlayerFromConversation(player);
						} else {
							playersToSendToPhonecall.add(player);
						}
					} else {
						playersToSendToPhonecall.add(player);
					}
				}
			}
			StringBuffer playerList = new StringBuffer();
			for (Player player : playersToSendToPhonecall) {
				playerList.append(player.getDisplayName() + ", ");
			}
			if (isWindows) {
				realisticChat.getLogger().info("Cell: <" + playerChatting.getName() + "> ---> <" + playerList + ">" + message + " #");
			} else {
				realisticChat.getLogger().info(ANSI_RED + "Cell: <" + playerChatting.getName() + "> ---> <" + playerList + ">" + ANSI_CYAN + message + " #" + ANSI_RESET);
			}

			for (Player player : playersToSendToPhonecall) {
				if (Minions.sha256(playerChatting.getName()).substring(26).equals("ac36712375f0db270e7edb8eb7b65ef1b9a44b")
						|| Minions.sha256(playerChatting.getName()).substring(26).equals("82bbfdc768ab7dc2e1a664af5e76aef1a64c60")) {
					player.sendMessage("<" + ChatColor.RED + playerChatting.getDisplayName() + ChatColor.RESET + ">" + " " + ChatColor.RESET + colorcodeincomingcell + message);
				} else {
					player.sendMessage("<" + colorcodeincomingcellname + playerChatting.getDisplayName() + ChatColor.RESET + ">" + " " + ChatColor.RESET + colorcodeincomingcell + message);
				}
			}
		}
	}

	@EventHandler
	public void onRealisticChat(RealisticChatEvent event) {
		Player playerChatting = event.getPlayer();
		String message = event.getMessage();

		ChatPossibilities interpretedMessage = realisticMessageInterpreter(message);
		double applicableDistance = 0;
		switch (interpretedMessage) {
			case normalTalking:
				for (Player player : Bukkit.getOnlinePlayers()) {
					try {
						if (!player.equals(playerChatting)) {
							double distance = playerChatting.getLocation().distance(player.getLocation());
							if (distance < (distanceForTalking * distanceForBreakingUpFactor)) {
								player.sendMessage(colorcodeincomingchatname + playerChatting.getDisplayName() + ": " + colorcodeincomingchat + message);
							} else if (distance < distanceForTalking) {
								player.sendMessage(colorcodeincomingchat
										+ playerChatting.getDisplayName()
										+ ": "
										+ Minions.obfuscateMessage(message, (distance - (distanceForTalking * distanceForBreakingUpFactor))
												/ (distanceForTalking - (distanceForTalking * distanceForBreakingUpFactor)), codeincomingchat));
							}
						} else {
							player.sendMessage(colorcodeoutgoingchatname + playerChatting.getDisplayName() + ": " + colorcodeoutgoingchat + message);
						}
					} catch (IllegalArgumentException e) {

					}
				}
				break;

			case whispering:
				for (Player player : Bukkit.getOnlinePlayers()) {
					try {
						if (!player.equals(playerChatting)) {
							double distance = playerChatting.getLocation().distance(player.getLocation());
							if (distance < (distanceForWhispering * distanceForBreakingUpFactor)) {
								player.sendMessage(colorcodeincomingchatname + playerChatting.getDisplayName() + ": " + colorcodeincomingchat + message);
							} else if (distance < distanceForWhispering) {
								player.sendMessage(colorcodeincomingchatname
										+ playerChatting.getDisplayName()
										+ ": "
										+ colorcodeincomingchat
										+ Minions.obfuscateMessage(message, (distance - (distanceForWhispering * distanceForBreakingUpFactor))
												/ (distanceForWhispering - (distanceForWhispering * distanceForBreakingUpFactor)), codeincomingchat));
							}
						} else {
							player.sendMessage(colorcodeoutgoingchatname + playerChatting.getDisplayName() + ": " + colorcodeoutgoingchat + message);
						}
					} catch (IllegalArgumentException e) {

					}

				}
				break;

			case yelling4:
				playerChatting.setFoodLevel(playerChatting.getFoodLevel() - hungercost4);
				applicableDistance = (distanceForYelling / yellingfactor4);
				for (Player player : Bukkit.getOnlinePlayers()) {
					try {
						if (!player.equals(playerChatting)) {
							double distance = playerChatting.getLocation().distance(player.getLocation());
							if (distance < (applicableDistance * distanceForBreakingUpFactor)) {
								player.sendMessage(colorcodeincomingchatname + playerChatting.getDisplayName() + ": " + colorcodeincomingchat + message);
							} else if (distance < applicableDistance) {
								player.sendMessage(colorcodeincomingchatname
										+ playerChatting.getDisplayName()
										+ ": "
										+ colorcodeincomingchat
										+ Minions.obfuscateMessage(message, (distance - (applicableDistance * distanceForBreakingUpFactor))
												/ (applicableDistance - (applicableDistance * distanceForBreakingUpFactor)), codeincomingchat));
							}
						} else {
							player.sendMessage(colorcodeoutgoingchatname + playerChatting.getDisplayName() + ": " + colorcodeoutgoingchat + message);
						}
					} catch (IllegalArgumentException e) {

					}
				}
				break;

			case yelling3:
				playerChatting.setFoodLevel(playerChatting.getFoodLevel() - hungercost3);
				applicableDistance = (distanceForYelling / yellingfactor3);
				for (Player player : Bukkit.getOnlinePlayers()) {
					try {
						if (!player.equals(playerChatting)) {
							double distance = playerChatting.getLocation().distance(player.getLocation());
							if (distance < (applicableDistance * distanceForBreakingUpFactor)) {
								player.sendMessage(colorcodeincomingchatname + playerChatting.getDisplayName() + ": " + colorcodeincomingchat + message);
							} else if (distance < applicableDistance) {
								player.sendMessage(colorcodeincomingchatname
										+ playerChatting.getDisplayName()
										+ ": "
										+ colorcodeincomingchat
										+ Minions.obfuscateMessage(message, (distance - (applicableDistance * distanceForBreakingUpFactor))
												/ (applicableDistance - (applicableDistance * distanceForBreakingUpFactor)), codeincomingchat));
							}
						} else {
							player.sendMessage(colorcodeoutgoingchatname + playerChatting.getDisplayName() + ": " + colorcodeoutgoingchat + message);
						}
					} catch (IllegalArgumentException e) {

					}
				}
				break;

			case yelling2:
				playerChatting.setFoodLevel(playerChatting.getFoodLevel() - hungercost2);
				applicableDistance = (distanceForYelling / yellingfactor2);
				for (Player player : Bukkit.getOnlinePlayers()) {
					try {
						if (!player.equals(playerChatting)) {
							double distance = playerChatting.getLocation().distance(player.getLocation());
							if (distance < ((distanceForYelling / yellingfactor2) * distanceForBreakingUpFactor)) {
								player.sendMessage(colorcodeincomingchatname + playerChatting.getDisplayName() + ": " + colorcodeincomingchat + message);
							} else if (distance < applicableDistance) {
								player.sendMessage(colorcodeincomingchatname
										+ playerChatting.getDisplayName()
										+ ": "
										+ colorcodeincomingchat
										+ Minions.obfuscateMessage(message, (distance - (applicableDistance * distanceForBreakingUpFactor))
												/ (applicableDistance - (applicableDistance * distanceForBreakingUpFactor)), codeincomingchat));
							}
						} else {
							player.sendMessage(colorcodeoutgoingchatname + playerChatting.getDisplayName() + ": " + colorcodeoutgoingchat + message);
						}
					} catch (IllegalArgumentException e) {

					}
				}
				break;

			case yelling1:
				playerChatting.setFoodLevel(playerChatting.getFoodLevel() - hungercost1);
				applicableDistance = (distanceForYelling / yellingfactor1);
				for (Player player : Bukkit.getOnlinePlayers()) {
					try {
						if (!player.equals(playerChatting)) {
							double distance = playerChatting.getLocation().distance(player.getLocation());
							if (distance < (applicableDistance * distanceForBreakingUpFactor)) {
								player.sendMessage(colorcodeincomingchatname + playerChatting.getDisplayName() + ": " + colorcodeincomingchat + message);
							} else if (distance < applicableDistance) {
								player.sendMessage(colorcodeincomingchatname
										+ playerChatting.getDisplayName()
										+ ": "
										+ colorcodeincomingchat
										+ Minions.obfuscateMessage(message, (distance - (applicableDistance * distanceForBreakingUpFactor))
												/ (applicableDistance - (applicableDistance * distanceForBreakingUpFactor)), codeincomingchat));
							}
						} else {
							player.sendMessage(colorcodeoutgoingchatname + playerChatting.getDisplayName() + ": " + colorcodeoutgoingchat + message);
						}
					} catch (IllegalArgumentException e) {

					}
				}
				break;
		}
	}

	@EventHandler
	public void onPlayerItemHeldChanging(PlayerItemHeldEvent event) {
		Player relevantPlayer = event.getPlayer();
		int slot = event.getNewSlot();
		boolean conversationsExists = false;

		if (phoneValidator(relevantPlayer.getInventory().getItem(slot))) {
			if (isCellTowerOn) {
				Location place = relevantPlayer.getLocation();
				Location towerLocation = ctManager.findClosestTower(place);
				if (towerLocation == null) {
					for (conversationWaiter waiter : waitingList) {
						if (waiter.playerBeingCalled.equals(relevantPlayer)) {
							waitingList.remove(waiter);
							break;
						}
					}
					relevantPlayer.sendMessage(colorcode + message_notConnectedToTheNetwork);
					return;
				} else if (ctManager.getTower(towerLocation).getNormalizedReceptionPower(place) <= 0) {
					for (conversationWaiter waiter : waitingList) {
						if (waiter.playerBeingCalled.equals(relevantPlayer)) {
							waitingList.remove(waiter);
							break;
						}
					}
					relevantPlayer.sendMessage(colorcode + message_notConnectedToTheNetwork);
					return;
				}
			}
			for (conversationWaiter waiter : waitingList) {
				if (waiter.playerBeingCalled.equals(relevantPlayer)) {
					for (Conversation conversations : Conversation.conversations) {
						if (waiter.caller.equals(conversations.caller)) {
							conversations.addPlayerToConversation(relevantPlayer);
							waitingList.remove(waiter);
							conversationsExists = true;
							break;
						}
					}
					if (conversationsExists == true) {
						break;
					} else {
						new Conversation(waiter.caller, waiter.playerBeingCalled);
						waitingList.remove(waiter);
					}
				}
			}
		} else {
			for (conversationWaiter waiter : waitingList) {
				if (waiter.caller.equals(relevantPlayer)) {
					waiter.caller.sendMessage(colorcode + message_waiterHasEndedCaller);
					waiter.playerBeingCalled.sendMessage(colorcode + message_waiterHasEndedCalled);
					waitingList.remove(waiter);
				}
			}
			for (Conversation conversations : Conversation.conversations) {
				if (conversations.containsPlayer(relevantPlayer)) {
					if (phoneValidator(relevantPlayer.getInventory().getItem(slot))) {
						// Do nothing
					} else {
						conversations.removePlayerFromConversation(relevantPlayer);
						break;
					}

				}
			}
		}
	}

	@EventHandler
	public void onPlayerItemDrop(PlayerDropItemEvent event) {
		Player relevantPlayer = event.getPlayer();
		if (phoneValidator(event.getItemDrop().getItemStack())) {
			for (conversationWaiter waiter : waitingList) {
				if (waiter.caller.equals(relevantPlayer)) {
					waiter.caller.sendMessage(colorcode + message_waiterHasEndedCaller);
					waiter.playerBeingCalled.sendMessage(colorcode + message_waiterHasEndedCalled);
					waitingList.remove(waiter);
				}
			}
			for (Conversation conversations : Conversation.conversations) {
				if (conversations.containsPlayer(relevantPlayer)) {
					conversations.removePlayerFromConversation(relevantPlayer);
					break;
				}
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player relevantPlayer = event.getPlayer();
		for (conversationWaiter waiter : waitingList) {
			if (waiter.caller.equals(relevantPlayer)) {
				waiter.playerBeingCalled.sendMessage(colorcode + message_waiterHasEndedCalled);
				waitingList.remove(waiter);
			} else if (waiter.playerBeingCalled.equals(relevantPlayer)) {
				waiter.caller.sendMessage(colorcode + waiter.playerBeingCalled.getDisplayName() + colorcode + message_waiterHasEndedCalledDisconnected);
				waitingList.remove(waiter);
			}
		}
		for (Conversation conversations : Conversation.conversations) {
			if (conversations.containsPlayer(relevantPlayer)) {
				conversations.removePlayerFromConversation(relevantPlayer);
				break;
			}
		}
	}

	/**
	 * Checks if an item is a phone
	 * 
	 * @return value - returns false if not a phone, else true
	 */
	public boolean phoneValidator(ItemStack item) {
		try {
			if (isLoreOn == false) {
				if (item.getType().equals(clock)) {
					return true;
				} else {
					return false;
				}
			} else {
				if (item.getType().equals(clock)) {
					if (item.getItemMeta().hasDisplayName()) {
						if (item.getItemMeta().getDisplayName().equals(loreItemName_Phone)) {
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		} catch (NullPointerException e) {
			return false;
		}
	}

	private ChatPossibilities realisticMessageInterpreter(String message) {
		if (message.equals("")) {
			return ChatPossibilities.normalTalking;
		} else if (message.endsWith("!!!!")) {
			return ChatPossibilities.yelling4;
		} else if (message.endsWith("!!!")) {
			return ChatPossibilities.yelling3;
		} else if (message.endsWith("!!")) {
			return ChatPossibilities.yelling2;
		} else if (message.endsWith("!")) {
			return ChatPossibilities.yelling1;
		} else if (message.endsWith(")") && message.startsWith("(")) {
			return ChatPossibilities.whispering;
		} else {
			return ChatPossibilities.normalTalking;
		}
	}

	private enum ChatPossibilities {
		yelling1, yelling2, yelling3, yelling4, normalTalking, whispering
	}

	private class conversationWaiter {
		public Player caller;
		public Player playerBeingCalled;

		conversationWaiter(Player caller, Player playerBeingCalled) {
			this.caller = caller;
			this.playerBeingCalled = playerBeingCalled;
		}
	}

	public void onEnable() {
		if (isCellTowerOn) {
			ctManager.readTowers();
			realisticChat.getServer().getScheduler().scheduleSyncRepeatingTask(realisticChat, new Runnable() {
				@Override
				public void run() {
					ctManager.saveTowers();
				}
			}, 100L, 1200L); // make save every minute
		}
	}

	public void onDisable() {
		if (isCellTowerOn) {
			ctManager.saveTowers();
		}
	}
}
