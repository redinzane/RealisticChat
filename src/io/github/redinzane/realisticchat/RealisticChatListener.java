package io.github.redinzane.realisticchat;

import io.github.redinzane.realisticchat.events.CellPhoneCallEvent;
import io.github.redinzane.realisticchat.events.RealisticChatEvent;
import io.github.redinzane.realisticchat.minions.ChatMessage;
import io.github.redinzane.realisticchat.minions.Minions;
import io.github.redinzane.realisticchat.minions.RealisticChatDAO;

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
	String loreItemNamePhone;

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
	String messageUnavailability;
	String messageConversationFull;
	String messageNotOriginalCaller;
	String messageWaiterIsStartingCalled;
	String messageWaiterIsStartingCaller;
	String messageWaiterHasEndedCaller;
	String messageWaiterHasEndedCalled;
	String messageWaiterHasEndedCalledDisconnected;
	// TODO: Not in config yet
	String messageNotConnectedToTheNetwork = "You are not connected to the phone network.";

	// Nonconfigurables
	List<ConversationWaiter> waitingList = new LinkedList<ConversationWaiter>();
	public Material clock = Material.getMaterial("WATCH");
	RealisticChat realisticChat;
	RealisticChatDAO dao;
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

	RealisticChatListener(RealisticChat plugin, RealisticChatDAO rcDAO, RealisticChatConfiguration config) {
		this.realisticChat = plugin;
		this.dao = rcDAO;
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
		this.loreItemNamePhone = config.getLoreItemPhone();
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

		this.messageConversationFull = config.getConversationFullMessage();
		this.messageNotOriginalCaller = config.getNotOriginalCallerMessage();
		this.messageUnavailability = config.getUnavailabilityMessage();
		this.messageWaiterIsStartingCalled = config.getWaiterIsStartingCalledMessage();
		this.messageWaiterIsStartingCaller = config.getWaiterIsStartingCallerMessage();
		this.messageWaiterHasEndedCaller = config.getWaiterHasEndedCallerMessage();
		this.messageWaiterHasEndedCalled = config.getWaiterHasEndedCalledMessage();
		this.messageWaiterHasEndedCalledDisconnected = config.getWaiterHasEndedCalledDisconnectedMessage();

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

	private static boolean isMac(final String os) {
		return (os.indexOf("mac") >= 0);
	}

	private static boolean isWindows(final String os) {
		return (os.indexOf("win") >= 0);
	}

	private static boolean isUnix(final String os) {
		return (os.indexOf("nux") >= 0);
	}

	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		Player playerChatting = event.getPlayer();
		String message = event.getMessage();

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
		final String message = event.getMessage();
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
					playerChatting.sendMessage(colorcode + messageNotConnectedToTheNetwork);
					return;
				} else if (ctManager.getTower(towerLocation).getNormalizedReceptionPower(place) <= 0) {
					if (isInConversation) {
						relevantConversation.removePlayerFromConversation(playerChatting);
					}
					playerChatting.sendMessage(colorcode + messageNotConnectedToTheNetwork);
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

		if (isAPlayerCalled) {
			for (ConversationWaiter waiter : waitingList) {
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
			playerChatting.sendMessage(colorcode + messageUnavailability);
			}
		// Else, make more checks, then call
		else if (isAPlayerCalled) {
			// See if we are already in a conversations
			if (isInConversation) {
				// Are we the caller?
				if (relevantConversation.caller.equals(playerChatting)) {
					// Is the conversation full?
					if (relevantConversation.playercounter >= Conversation.maxPlayercount) {
						playerChatting.sendMessage(colorcode + messageConversationFull);
					} else {
						// Is the called player holding a phone?
						if (phoneValidator(playerBeingCalled.getItemInHand())) {
							relevantConversation.addPlayerToConversation(playerBeingCalled);
						} else {
							playerChatting.sendMessage(colorcode + messageWaiterIsStartingCaller + playerBeingCalled.getDisplayName() + colorcode);
							playerBeingCalled.sendMessage(colorcode + playerChatting.getDisplayName() + colorcode + messageWaiterIsStartingCalled);
							waitingList.add(new ConversationWaiter(playerChatting, playerBeingCalled));
						}
					}
				} else {
					playerChatting.sendMessage(colorcode + messageNotOriginalCaller);
				}
			} else {
				// Is the called player holding a phone?
				if (phoneValidator(playerBeingCalled.getItemInHand())) {
					new Conversation(playerChatting, playerBeingCalled);
				} else {
					playerChatting.sendMessage(colorcode + messageWaiterIsStartingCaller + playerBeingCalled.getDisplayName() + colorcode);
					playerBeingCalled.sendMessage(colorcode + playerChatting.getDisplayName() + colorcode + messageWaiterIsStartingCalled);
					waitingList.add(new ConversationWaiter(playerChatting, playerBeingCalled));
				}
			}
		}

		if (isInConversation) {
			for (Player player : relevantConversation.getPlayersInConversation()) {
				if (!player.equals(playerChatting)) {
				    if (isCellTowerOn) {
                        Location place = player.getLocation();
                        Location towerLocation = ctManager.findClosestTower(place);
                        if (towerLocation == null) {
                            player.sendMessage(colorcode + messageNotConnectedToTheNetwork);
                            relevantConversation.removePlayerFromConversation(player);
                        } else if (ctManager.getTower(towerLocation).getNormalizedReceptionPower(place) <= 0) {
                            player.sendMessage(colorcode + messageNotConnectedToTheNetwork);
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
			ChatMessage msg = new ChatMessage(relevantConversation.uuid, playerChatting.getUniqueId().toString(), playerChatting.getName(), "cell", playerList.toString(), message, System.currentTimeMillis());
            dao.log(msg);

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
		String receivers = null;
		ChatMessage msg;
		
		switch (interpretedMessage) {
			case normalTalking:
				receivers = chatMessageHandler(playerChatting, message, distanceForTalking);
				if (isWindows) {
                    realisticChat.getLogger().info("Talking: <" + playerChatting.getName() + "> " + message + " #");
                } else {
                    realisticChat.getLogger().info(ANSI_YELLOW + "Talking: <" + playerChatting.getName() + "> " + ANSI_CYAN + message + " #" + ANSI_RESET);
                }
                msg = new ChatMessage("", playerChatting.getUniqueId().toString(), playerChatting.getName(), "talking", receivers, message, System.currentTimeMillis());
                dao.log(msg);				
                break;

			case whispering:
			    receivers = chatMessageHandler(playerChatting, message, distanceForWhispering);
				if (isWindows) {
                    realisticChat.getLogger().info("Whispering: <" + playerChatting.getName() + "> " + message + " #");
                } else {
                    realisticChat.getLogger().info(ANSI_YELLOW + "Whispering: <" + playerChatting.getName() + "> " + ANSI_CYAN + message + " #" + ANSI_RESET);
                }
                msg = new ChatMessage("", playerChatting.getUniqueId().toString(), playerChatting.getName(), "whispering", receivers, message, System.currentTimeMillis());
                dao.log(msg);
                break;

			case yelling4:
				playerChatting.setFoodLevel(playerChatting.getFoodLevel() - hungercost4);
				applicableDistance = (distanceForYelling / yellingfactor4);
				receivers = chatMessageHandler(playerChatting, message, applicableDistance);
				if(isWindows) {
                    realisticChat.getLogger().info("Yelling4: <" + playerChatting.getName() + "> " + message + " #");
                } else {
                    realisticChat.getLogger().info(ANSI_YELLOW + "Yelling4: <" + playerChatting.getName() + "> " + ANSI_CYAN + message + " #" + ANSI_RESET);
                }
                msg = new ChatMessage("", playerChatting.getUniqueId().toString(), playerChatting.getName(), "yelling4", receivers, message, System.currentTimeMillis());
                dao.log(msg);
				break;

			case yelling3:
				playerChatting.setFoodLevel(playerChatting.getFoodLevel() - hungercost3);
				applicableDistance = (distanceForYelling / yellingfactor3);
				receivers = chatMessageHandler(playerChatting, message, applicableDistance);
				if (isWindows) {
                    realisticChat.getLogger().info("Yelling3: <" + playerChatting.getName() + "> " + message + " #");
                } else {
                    realisticChat.getLogger().info(ANSI_YELLOW + "Yelling3: <" + playerChatting.getName() + "> " + ANSI_CYAN + message + " #" + ANSI_RESET);
                }
                msg = new ChatMessage("", playerChatting.getUniqueId().toString(), playerChatting.getName(), "yelling3", receivers, message, System.currentTimeMillis());
                dao.log(msg);
				break;

			case yelling2:
				playerChatting.setFoodLevel(playerChatting.getFoodLevel() - hungercost2);
				applicableDistance = (distanceForYelling / yellingfactor2);
				receivers = chatMessageHandler(playerChatting, message, applicableDistance);
				if (isWindows) {
                    realisticChat.getLogger().info("Yelling2: <" + playerChatting.getName() + "> " + message + " #");
                } else {
                    realisticChat.getLogger().info(ANSI_YELLOW + "Yelling2: <" + playerChatting.getName() + "> " + ANSI_CYAN + message + " #" + ANSI_RESET);
                }
                msg = new ChatMessage("", playerChatting.getUniqueId().toString(), playerChatting.getName(), "yelling2", receivers, message, System.currentTimeMillis());
                dao.log(msg);
				break;

			case yelling1:
				playerChatting.setFoodLevel(playerChatting.getFoodLevel() - hungercost1);
				applicableDistance = (distanceForYelling / yellingfactor1);
				receivers = chatMessageHandler(playerChatting, message, applicableDistance);
				if (isWindows) {
                    realisticChat.getLogger().info("Yelling1: <" + playerChatting.getName() + "> " + message + " #");
                } else {
                    realisticChat.getLogger().info(ANSI_YELLOW + "Yelling1: <" + playerChatting.getName() + "> " + ANSI_CYAN + message + " #" + ANSI_RESET);
                }
                msg = new ChatMessage("", playerChatting.getUniqueId().toString(), playerChatting.getName(), "yelling1", receivers, message, System.currentTimeMillis());
                dao.log(msg);
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
					for (ConversationWaiter waiter : waitingList) {
						if (waiter.playerBeingCalled.equals(relevantPlayer)) {
							waitingList.remove(waiter);
							break;
						}
					}
					relevantPlayer.sendMessage(colorcode + messageNotConnectedToTheNetwork);
					return;
				} else if (ctManager.getTower(towerLocation).getNormalizedReceptionPower(place) <= 0) {
					for (ConversationWaiter waiter : waitingList) {
						if (waiter.playerBeingCalled.equals(relevantPlayer)) {
							waitingList.remove(waiter);
							break;
						}
					}
					relevantPlayer.sendMessage(colorcode + messageNotConnectedToTheNetwork);
					return;
				}
			}
			for (final ConversationWaiter waiter : waitingList) {
				if (waiter.playerBeingCalled.equals(relevantPlayer)) {
					for (Conversation conversations : Conversation.conversations) {
						if (waiter.caller.equals(conversations.caller)) {
							conversations.addPlayerToConversation(relevantPlayer);
							waitingList.remove(waiter);
							conversationsExists = true;
							break;
						}
					}
					if (conversationsExists) {
						break;
					} else {
						new Conversation(waiter.caller, waiter.playerBeingCalled);
						waitingList.remove(waiter);
					}
				}
			}
		} else {
			for (ConversationWaiter waiter : waitingList) {
				if (waiter.caller.equals(relevantPlayer)) {
					waiter.caller.sendMessage(colorcode + messageWaiterHasEndedCaller);
					waiter.playerBeingCalled.sendMessage(colorcode + messageWaiterHasEndedCalled);
					waitingList.remove(waiter);
				}
			}
			for (Conversation conversations : Conversation.conversations) {
				if (conversations.containsPlayer(relevantPlayer)) {
					if (!phoneValidator(relevantPlayer.getInventory().getItem(slot))) {
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
			for (ConversationWaiter waiter : waitingList) {
				if (waiter.caller.equals(relevantPlayer)) {
					waiter.caller.sendMessage(colorcode + messageWaiterHasEndedCaller);
					waiter.playerBeingCalled.sendMessage(colorcode + messageWaiterHasEndedCalled);
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
		for (ConversationWaiter waiter : waitingList) {
			if (waiter.caller.equals(relevantPlayer)) {
				waiter.playerBeingCalled.sendMessage(colorcode + messageWaiterHasEndedCalled);
				waitingList.remove(waiter);
			} else if (waiter.playerBeingCalled.equals(relevantPlayer)) {
				waiter.caller.sendMessage(colorcode + waiter.playerBeingCalled.getDisplayName() + colorcode + messageWaiterHasEndedCalledDisconnected);
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
	 * Checks if an item is a phone.
	 * 
	 * @return value - returns false if not a phone, else true
	 */
	public boolean phoneValidator(final ItemStack item) {
		try {
			if (!isLoreOn) {
				if (item.getType().equals(clock)) {
					return true;
				} else {
					return false;
				}
			} else {
				if (item.getType().equals(clock)) {
					if (item.getItemMeta().hasDisplayName()) {
						if (item.getItemMeta().getDisplayName().equals(loreItemNamePhone)) {
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

	private ChatPossibilities realisticMessageInterpreter(final String message) {
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

	/**
     * Handles the sending of messages for ranged chat and returns a list of receivers as a string seperated by whitespace.
     * 
     * @param playerChatting - The player trying to chat.
     * @param message - The message.
     * @param applicableDistance - The Distance this chat should be heard.
     * 
     * @return receivers - everyone who heard.
     */
	private String chatMessageHandler(Player playerChatting, String message, double applicableDistance)
	{
	    StringBuilder receivers = new StringBuilder();
	    for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                if (!player.equals(playerChatting)) {
                    double distance = playerChatting.getLocation().distance(player.getLocation());
                    if (distance < (applicableDistance * distanceForBreakingUpFactor)) {
                        player.sendMessage(colorcodeincomingchatname + playerChatting.getDisplayName() + ": " + colorcodeincomingchat + message);
                        receivers.append(player.getDisplayName() + " ");
                    } else if (distance < applicableDistance) {
                        player.sendMessage(colorcodeincomingchatname
                                + playerChatting.getDisplayName()
                                + ": "
                                + colorcodeincomingchat
                                + Minions.obfuscateMessage(message, (distance - (applicableDistance * distanceForBreakingUpFactor))
                                        / (applicableDistance - (applicableDistance * distanceForBreakingUpFactor)), codeincomingchat));
                        receivers.append(player.getDisplayName() + " ");
                    }
                } else {
                    player.sendMessage(colorcodeoutgoingchatname + playerChatting.getDisplayName() + ": " + colorcodeoutgoingchat + message);
                }
            } catch (IllegalArgumentException e) {

            }
        }
        return receivers.toString();	    
	}
	private class ConversationWaiter {
		public Player caller;
		public Player playerBeingCalled;

		ConversationWaiter(Player caller, Player playerBeingCalled) {
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
