package io.github.redinzane.realisticchat;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

public class RealisticChatConfiguration {
	private final Configuration config;
	private static final char COLORCODE = '&';

	private static final String SECTION_NUMBERS = "numbers";
	private static final String SECTION_FEATURES = "features";
	private static final String SECTION_LOREITEMS = "loreitems";
	private static final String SECTION_CELLTOWER = "celltower";
	private static final String SECTION_MESSAGES = "messages";

	private static final String COLORCODE_KEY = "colorcode";
	private static final String COLORCODEINCOMINGCHAT_KEY = "colorcodeincomingchat";
	private static final String COLORCODEOUTGOINGCHAT_KEY = "colorcodeoutgoingchat";
	private static final String COLORCODEINCOMINGCELL_KEY = "colorcodeincomingcell";
	private static final String COLORCODEOUTGOINGCELL_KEY = "colorcodeoutgoingcell";
	private static final String COLORCODEINCOMINGCHATNAME_KEY = "colorcodeincomingchatname";
	private static final String COLORCODEOUTGOINGCHATNAME_KEY = "colorcodeoutgoingchatname";
	private static final String COLORCODEINCOMINGCELLNAME_KEY = "colorcodeincomingcellname";
	private static final String COLORCODEOUTGOINGCELLNAME_KEY = "colorcodeoutgoingcellname";
	private static final String UNAVAILABILITY_KEY = "message_Unavailability";
	private static final String CONVERSATIONFULL_KEY = "message_ConversationFull";
	private static final String NOTORIGINALCALLER_KEY = "message_notOriginalCaller";
	private static final String WAITERISSTARTINGCALLED_KEY = "message_waiterIsStartingCalled";
	private static final String WAITERISSTARTINGCALLER_KEY = "message_waiterIsStartingCaller";
	private static final String WAITERHASENDEDCALLER_KEY = "message_waiterHasEndedCaller";
	private static final String WAITERHASENDEDCALLED_KEY = "message_waiterHasEndedCalled";
	private static final String WAITERHASENDEDCALLEDDISCONNECTED_KEY = "message_waiterHasEndedCalledDisconnected";

	private static final String DISCONNECT_KEY = "message_Disconnect";
	private static final String CONVERSATIONESTABLISHED_KEY = "message_ConversationEstablished";
	private static final String PLAYERADDED_KEY = "message_PlayerAdded";
	private static final String PLAYERREMOVED_KEY = "message_PlayerRemoved";

	private static final String CT_MINHEIGHT_KEY = "minimumHeight";
	private static final String CT_MAXHEIGHT_KEY = "maximumHeight";
	private static final String CT_MAXRANGE_KEY = "maximumRange";
	private static final String CT_BASEBLOCK_KEY = "baseblock";
	private static final String CT_TICKPERIOD_KEY = "towertickperiod";

	private static final String DISTANCEFORWHISPERING_KEY = "distanceforwhispering";
	private static final String DISTANCEFORTALKING_KEY = "distancefortalking";
	private static final String DISTANCEFORYELLING_KEY = "distanceforyelling";
	private static final String DISTANCEFORYELLING1_KEY = "yellingfactor1";
	private static final String DISTANCEFORYELLING2_KEY = "yellingfactor2";
	private static final String DISTANCEFORYELLING3_KEY = "yellingfactor3";
	private static final String DISTANCEFORYELLING4_KEY = "yellingfactor4";
	private static final String HUNGERCOST1_KEY = "hungercost1";
	private static final String HUNGERCOST2_KEY = "hungercost2";
	private static final String HUNGERCOST3_KEY = "hungercost3";
	private static final String HUNGERCOST4_KEY = "hungercost4";
	private static final String DISTANCEFORBREAKINGUPFACTOR_KEY = "distanceforbreakingupfactor";

	private static final String LOREITEMPHONE_KEY = "phone";

	private static final String CELLTOWER_KEY = "celltower";
	private static final String REALISTICCHAT_KEY = "realisticchat";
	private static final String CELL_KEY = "cellphones";
	private static final String LORE_KEY = "loreitems";

	private static final int DEFAULT_DISTANCEFORWHISPERING = 10;
	private static final int DEFAULT_DISTANCEFORTALKING = 50;
	private static final int DEFAULT_DISTANCEFORYELLING = 1000;
	private static final int DEFAULT_DISTANCEFORYELLING1 = 8;
	private static final int DEFAULT_DISTANCEFORYELLING2 = 4;
	private static final int DEFAULT_DISTANCEFORYELLING3 = 2;
	private static final int DEFAULT_DISTANCEFORYELLING4 = 1;
	private static final int DEFAULT_HUNGERCOST1 = 1;
	private static final int DEFAULT_HUNGERCOST2 = 5;
	private static final int DEFAULT_HUNGERCOST3 = 10;
	private static final int DEFAULT_HUNGERCOST4 = 20;
	private static final float DEFAULT_DISTANCEFORBREAKINGUPFACTOR = 0.7f;
	private static final int DEFAULT_MAXPLAYERCOUNT = 10;

	private static final int DEFAULT_CT_MIN_HEIGHT = 6;
	private static final int DEFAULT_CT_MAX_HEIGHT = 50;
	private static final int DEFAULT_CT_MAX_RANGE = 10000;
	private static final long DEFAULT_CT_TICKPERIOD = 100;

	private static final String DEFAULT_LOREITEMPHONE = "&bPhone";

	public RealisticChatConfiguration(Configuration config) {
		this.config = config;
	}

	private ConfigurationSection getSectionOrDefault(String name) {
		ConfigurationSection section = config.getConfigurationSection(name);

		if (section != null)
			return section;
		else
			return config.createSection(name);
	}

	// Messages
	/**
	 * Retrieve the Colorcode.
	 * 
	 * @returnColorcode
	 */
	public String getColorcode() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				COLORCODE_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Colorcode.
	 * 
	 * @returnColorcode
	 */
	public String getColorcodeIncomingChat() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				COLORCODEINCOMINGCHAT_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Colorcode.
	 * 
	 * @returnColorcode
	 */
	public String getColorcodeOutgoingChat() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				COLORCODEOUTGOINGCHAT_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Colorcode.
	 * 
	 * @returnColorcode
	 */
	public String getColorcodeIncomingCell() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				COLORCODEINCOMINGCELL_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Colorcode.
	 * 
	 * @returnColorcode
	 */
	public String getColorcodeOutgoingCell() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				COLORCODEOUTGOINGCELL_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Colorcode.
	 * 
	 * @returnColorcode
	 */
	public String getColorcodeIncomingChatName() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				COLORCODEINCOMINGCHATNAME_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Colorcode.
	 * 
	 * @returnColorcode
	 */
	public String getColorcodeOutgoingChatName() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				COLORCODEOUTGOINGCHATNAME_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Colorcode.
	 * 
	 * @returnColorcode
	 */
	public String getColorcodeIncomingCellName() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				COLORCODEINCOMINGCELLNAME_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Colorcode.
	 * 
	 * @returnColorcode
	 */
	public String getColorcodeOutgoingCellName() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				COLORCODEOUTGOINGCELLNAME_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Message.
	 * 
	 * @return Message including Colorcode
	 */
	public String getConversationFullMessage() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				CONVERSATIONFULL_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Message.
	 * 
	 * @return Message including Colorcode
	 */
	public String getNotOriginalCallerMessage() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				NOTORIGINALCALLER_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Message.
	 * 
	 * @return Message including Colorcode
	 */
	public String getUnavailabilityMessage() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				UNAVAILABILITY_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Message.
	 * 
	 * @return Message including Colorcode
	 */
	public String getWaiterHasEndedCalledMessage() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				WAITERHASENDEDCALLED_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Message.
	 * 
	 * @return Message including Colorcode
	 */
	public String getWaiterHasEndedCallerMessage() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				WAITERHASENDEDCALLER_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Message.
	 * 
	 * @return Message including Colorcode
	 */
	public String getWaiterHasEndedCalledDisconnectedMessage() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				WAITERHASENDEDCALLEDDISCONNECTED_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Message.
	 * 
	 * @return Message including Colorcode
	 */
	public String getWaiterIsStartingCalledMessage() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				WAITERISSTARTINGCALLED_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Message.
	 * 
	 * @return Message including Colorcode
	 */
	public String getWaiterIsStartingCallerMessage() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				WAITERISSTARTINGCALLER_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Message.
	 * 
	 * @return Message including Colorcode
	 */
	public String getConversationEstablishedMessage() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				CONVERSATIONESTABLISHED_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Message.
	 * 
	 * @return Message including Colorcode
	 */
	public String getDisconnectMessage() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				DISCONNECT_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Message.
	 * 
	 * @return Message including Colorcode
	 */
	public String getPlayerAddedMessage() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				PLAYERADDED_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	/**
	 * Retrieve the Message.
	 * 
	 * @return Message including Colorcode
	 */
	public String getPlayerRemovedMessage() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				PLAYERREMOVED_KEY);
		if (value == null) {
			return "";
		} else {
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
		}
	}

	// Feature settings
	/**
	 * Get the feature on/off.
	 * 
	 * @return value - new value.
	 */
	public boolean getChatBoolean() {
		boolean value = getSectionOrDefault(SECTION_FEATURES).getBoolean(
				REALISTICCHAT_KEY);

		return value;
	}

	/**
	 * Set the feature on/off.
	 * 
	 * @param value
	 *            - new value.
	 */
	public void setChatBoolean(boolean value) {
		getSectionOrDefault(SECTION_FEATURES).set(REALISTICCHAT_KEY, value);
	}

	/**
	 * Get the feature on/off.
	 * 
	 * @return value - new value.
	 */
	public boolean getCellBoolean() {
		boolean value = getSectionOrDefault(SECTION_FEATURES).getBoolean(
				CELL_KEY);

		return value;
	}

	/**
	 * Set the feature on/off.
	 * 
	 * @param value
	 *            - new value.
	 */
	public void setCellBoolean(boolean value) {
		getSectionOrDefault(SECTION_FEATURES).set(CELL_KEY, value);
	}

	/**
	 * Get the feature on/off.
	 * 
	 * @return value - new value.
	 */
	public boolean getLoreBoolean() {
		boolean value = getSectionOrDefault(SECTION_FEATURES).getBoolean(
				LORE_KEY);

		return value;
	}

	/**
	 * Set the feature on/off.
	 * 
	 * @param value
	 *            - new value.
	 */
	public void setLoreBoolean(boolean value) {
		getSectionOrDefault(SECTION_FEATURES).set(LORE_KEY, value);
	}

	/**
	 * Get the feature on/off.
	 * 
	 * @return value - new value.
	 */
	public boolean getCelltowerBoolean() {
		boolean value = getSectionOrDefault(SECTION_FEATURES).getBoolean(
				CELLTOWER_KEY);

		return value;
	}

	/**
	 * Set the feature on/off.
	 * 
	 * @param value
	 *            - new value.
	 */
	public void setCelltowerBoolean(boolean value) {
		getSectionOrDefault(SECTION_FEATURES).set(CELLTOWER_KEY, value);
	}

	// Section with chatting settings
	/**
	 * Retrieve the distance in blocks.
	 * 
	 * @return distance in blocks.
	 */
	public int getDistanceForWhispering() {
		int value = getSectionOrDefault(SECTION_NUMBERS).getInt(
				DISTANCEFORWHISPERING_KEY);

		if (value < 0)
			return DEFAULT_DISTANCEFORWHISPERING;
		else
			return value;
	}

	/**
	 * Set the distance in blocks.
	 * 
	 * @param value
	 *            - new distance.
	 */
	public void setDistanceForWhispering(int value) {
		getSectionOrDefault(SECTION_NUMBERS).set(DISTANCEFORWHISPERING_KEY,
				value);
	}

	/**
	 * Retrieve the distance in blocks.
	 * 
	 * @return distance in blocks.
	 */
	public int getDistanceForTalking() {
		int value = getSectionOrDefault(SECTION_NUMBERS).getInt(
				DISTANCEFORTALKING_KEY);

		if (value < 0)
			return DEFAULT_DISTANCEFORTALKING;
		else
			return value;
	}

	/**
	 * Set the distance in blocks.
	 * 
	 * @param value
	 *            - new distance.
	 */
	public void setDistanceForTalking(int value) {
		getSectionOrDefault(SECTION_NUMBERS).set(DISTANCEFORTALKING_KEY, value);
	}

	/**
	 * Retrieve the distance in blocks.
	 * 
	 * @return Distance in blocks.
	 */
	public int getDistanceForYelling() {
		int value = getSectionOrDefault(SECTION_NUMBERS).getInt(
				DISTANCEFORYELLING_KEY);

		if (value < 0)
			return DEFAULT_DISTANCEFORYELLING;
		else
			return value;
	}

	/**
	 * Set the distance in blocks.
	 * 
	 * @param value
	 *            - new distance.
	 */
	public void setDistanceForYelling(int value) {
		getSectionOrDefault(SECTION_NUMBERS).set(DISTANCEFORYELLING_KEY, value);
	}

	/**
	 * Retrieve the distance in fractions.
	 * 
	 * @return Distance in blocks.
	 */
	public int getDistanceForYelling1() {
		int value = getSectionOrDefault(SECTION_NUMBERS).getInt(
				DISTANCEFORYELLING1_KEY);

		if (value < 0)
			return DEFAULT_DISTANCEFORYELLING1;
		else
			return value;
	}

	/**
	 * Set the distance in fraction.
	 * 
	 * @param value
	 *            - new distance.
	 */
	public void setDistanceForYelling1(int value) {
		getSectionOrDefault(SECTION_NUMBERS)
				.set(DISTANCEFORYELLING1_KEY, value);
	}

	/**
	 * Retrieve the distance in fractions.
	 * 
	 * @return Distance in blocks.
	 */
	public int getDistanceForYelling2() {
		int value = getSectionOrDefault(SECTION_NUMBERS).getInt(
				DISTANCEFORYELLING2_KEY);

		if (value < 0)
			return DEFAULT_DISTANCEFORYELLING2;
		else
			return value;
	}

	/**
	 * Set the distance in fraction.
	 * 
	 * @param value
	 *            - new distance.
	 */
	public void setDistanceForYelling2(int value) {
		getSectionOrDefault(SECTION_NUMBERS)
				.set(DISTANCEFORYELLING2_KEY, value);
	}

	/**
	 * Retrieve the distance in fractions.
	 * 
	 * @return Distance in blocks.
	 */
	public int getDistanceForYelling3() {
		int value = getSectionOrDefault(SECTION_NUMBERS).getInt(
				DISTANCEFORYELLING3_KEY);

		if (value < 0)
			return DEFAULT_DISTANCEFORYELLING3;
		else
			return value;
	}

	/**
	 * Set the distance in fraction.
	 * 
	 * @param value
	 *            - new distance.
	 */
	public void setDistanceForYelling3(int value) {
		getSectionOrDefault(SECTION_NUMBERS)
				.set(DISTANCEFORYELLING3_KEY, value);
	}

	/**
	 * Retrieve the distance in fractions.
	 * 
	 * @return Distance in blocks.
	 */
	public int getDistanceForYelling4() {
		int value = getSectionOrDefault(SECTION_NUMBERS).getInt(
				DISTANCEFORYELLING4_KEY);

		if (value < 0)
			return DEFAULT_DISTANCEFORYELLING4;
		else
			return value;
	}

	/**
	 * Set the distance in fraction.
	 * 
	 * @param value
	 *            - new distance.
	 */
	public void setDistanceForYelling4(int value) {
		getSectionOrDefault(SECTION_NUMBERS)
				.set(DISTANCEFORYELLING4_KEY, value);
	}

	/**
	 * Retrieve the hunger cost in points.
	 * 
	 * @return Hunger cost in points.
	 */
	public int getHungerCost1() {
		int value = getSectionOrDefault(SECTION_NUMBERS)
				.getInt(HUNGERCOST1_KEY);

		if (value < 0)
			return DEFAULT_HUNGERCOST1;
		else
			return value;
	}

	/**
	 * Set the hunger cost in points.
	 * 
	 * @param value
	 *            - new hunger cost.
	 */
	public void setHungerCost1(int value) {
		getSectionOrDefault(SECTION_NUMBERS).set(HUNGERCOST1_KEY, value);
	}

	/**
	 * Retrieve the hunger cost in points.
	 * 
	 * @return Hunger cost in points.
	 */
	public int getHungerCost2() {
		int value = getSectionOrDefault(SECTION_NUMBERS)
				.getInt(HUNGERCOST2_KEY);

		if (value < 0)
			return DEFAULT_HUNGERCOST2;
		else
			return value;
	}

	/**
	 * Set the hunger cost in points.
	 * 
	 * @param value
	 *            - new hunger cost.
	 */
	public void setHungerCost2(int value) {
		getSectionOrDefault(SECTION_NUMBERS).set(HUNGERCOST2_KEY, value);
	}

	/**
	 * Retrieve the hunger cost in points.
	 * 
	 * @return Hunger cost in points.
	 */
	public int getHungerCost3() {
		int value = getSectionOrDefault(SECTION_NUMBERS)
				.getInt(HUNGERCOST3_KEY);

		if (value < 0)
			return DEFAULT_HUNGERCOST3;
		else
			return value;
	}

	/**
	 * Set the hunger cost in points.
	 * 
	 * @param value
	 *            - new hunger cost.
	 */
	public void setHungerCost3(int value) {
		getSectionOrDefault(SECTION_NUMBERS).set(HUNGERCOST3_KEY, value);
	}

	/**
	 * Retrieve the hunger cost in points.
	 * 
	 * @return Hunger cost in points.
	 */
	public int getHungerCost4() {
		int value = getSectionOrDefault(SECTION_NUMBERS)
				.getInt(HUNGERCOST4_KEY);

		if (value < 0)
			return DEFAULT_HUNGERCOST4;
		else
			return value;
	}

	/**
	 * Set the hunger cost in points.
	 * 
	 * @param value
	 *            - new hunger cost.
	 */
	public void setHungerCost4(int value) {
		getSectionOrDefault(SECTION_NUMBERS).set(HUNGERCOST4_KEY, value);
	}

	/**
	 * Get the distance in fraction.
	 * 
	 * @return value - distance.
	 */
	public float getDistanceForBreakingUpFactor() {
		float value = (float) getSectionOrDefault(SECTION_NUMBERS).getDouble(
				DISTANCEFORBREAKINGUPFACTOR_KEY);

		if (value <= 0 || value > 1)
			return DEFAULT_DISTANCEFORBREAKINGUPFACTOR;
		else
			return value;
	}

	/**
	 * Set the distance in fraction.
	 * 
	 * @param value
	 *            - new distance.
	 */
	public void setDistanceForBreakingUpFactor(float value) {
		getSectionOrDefault(SECTION_NUMBERS).set(
				DISTANCEFORBREAKINGUPFACTOR_KEY, value);
	}

	/**
	 * Get the max Player Count for conversations.
	 * 
	 * @return value - maxPlayerCount.
	 */
	public int getMaxPlayerCount() {
		int value = getSectionOrDefault(SECTION_NUMBERS).getInt(
				DISTANCEFORBREAKINGUPFACTOR_KEY);

		if (value <= 2)
			return DEFAULT_MAXPLAYERCOUNT;
		else
			return value;
	}

	/**
	 * Set the max Player Count for Conversations.
	 * 
	 * @param value
	 *            - new distance.
	 */
	public void setMaxPlayerCount(int value) {
		getSectionOrDefault(SECTION_NUMBERS).set(
				DISTANCEFORBREAKINGUPFACTOR_KEY, value);
	}

	// Cell settings
	public String getLoreItemPhone() {
		String value = getSectionOrDefault(SECTION_LOREITEMS).getString(
				LOREITEMPHONE_KEY);

		if (value == null)
			return DEFAULT_LOREITEMPHONE;
		else
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
	}

	/**
	 * Set the lore item name.
	 * 
	 * @param value
	 *            - new distance.
	 */
	public void setLoreItemPhone(String value) {
		getSectionOrDefault(SECTION_NUMBERS).set(LOREITEMPHONE_KEY,
				ChatColor.translateAlternateColorCodes(COLORCODE, value));
	}

	// Cell Tower Settings

	/**
	 * Get the min height.
	 * 
	 * @return value - min height.
	 */
	public int getCTMinHeight() {
		int value = getSectionOrDefault(SECTION_CELLTOWER).getInt(
				CT_MINHEIGHT_KEY);

		if (value < 0)
			return DEFAULT_CT_MIN_HEIGHT;
		else
			return (value);
	}

	/**
	 * Get the max height.
	 * 
	 * @return value - max height.
	 */
	public int getCTMaxHeight() {
		int value = getSectionOrDefault(SECTION_CELLTOWER).getInt(
				CT_MAXHEIGHT_KEY);

		if (value < 0)
			return DEFAULT_CT_MAX_HEIGHT;
		else
			return (value);
	}

	/**
	 * Get the max range.
	 * 
	 * @return value - max range.
	 */
	public int getCTMaxRange() {
		int value = getSectionOrDefault(SECTION_CELLTOWER).getInt(
				CT_MAXRANGE_KEY);

		if (value < 0)
			return DEFAULT_CT_MAX_RANGE;
		else
			return (value);
	}

	/**
	 * Get the tick period.
	 * 
	 * @return value - tick period.
	 */
	public long getCTTickPeriod() {
		long value = getSectionOrDefault(SECTION_CELLTOWER).getLong(
				CT_TICKPERIOD_KEY);

		if (value < 0)
			return DEFAULT_CT_TICKPERIOD;
		else
			return (value);
	}

	/**
	 * Retrieve the base block
	 * 
	 * @return Base Block
	 */
	public String getBaseBlock() {
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(
				CT_BASEBLOCK_KEY);
		if (value == null) {
			return "";
		} else {
			return value;
		}
	}

}
