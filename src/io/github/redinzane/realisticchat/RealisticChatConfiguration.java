package io.github.redinzane.realisticchat;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

public class RealisticChatConfiguration
{
	private final Configuration config;
	private static final char COLORCODE = '&';
	
	private static final String SECTION_DISTANCES = "distances";
	private static final String SECTION_FEATURES = "features";
	private static final String SECTION_LOREITEMS = "loreitems";
    private static final String SECTION_CELLTOWER = "celltower";
    private static final String SECTION_MESSAGES = "messages";

    private static final String COLORCODE_KEY = "colorcode";
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
    private static final String CT_MAXRANGE_KEY  = "maximumRange";
    private static final String CT_SCRAMBLERANGE_KEY  = "scrambleRange";
	
	private static final String DISTANCEFORWHISPERING_KEY = "distanceforwhisperimg";
	private static final String DISTANCEFORTALKING_KEY = "distancefortalking";
	private static final String DISTANCEFORYELLING_KEY = "distanceforyelling";
	private static final String DISTANCEFORBREAKINGUPFACTOR_KEY = "distanceforbreakingupfactor";
	
	private static final String LOREITEMPHONE_KEY = "phone";
	
	private static final String CELLTOWER_KEY = "celltower";
	private static final String REALISTICCHAT_KEY = "realisticchat";
	private static final String CELL_KEY = "cellphones";
	private static final String LORE_KEY = "loreitems";
	
	private static final int DEFAULT_DISTANCEFORWHISPERING = 10;
	private static final int DEFAULT_DISTANCEFORTALKING = 50;
	private static final int DEFAULT_DISTANCEFORYELLING = 1000;
	private static final float DEFAULT_DISTANCEFORBREAKINGUPFACTOR = 0.7f;
	private static final int DEFAULT_MAXPLAYERCOUNT = 10;
	
    public static final int DEFAULT_CT_MIN_HEIGHT = 6;
    public static final int DEFAULT_CT_MAX_HEIGHT = 50;
    public static final int DEFAULT_CT_MAX_RANGE = 10000;
    public static final double DEFAULT_CT_SCRAMBLE_RANGE = 0.9;
	
	private static final String DEFAULT_LOREITEMPHONE = "&bPhone";
	
	public RealisticChatConfiguration(Configuration config) 
	{
		this.config = config;
	}
	
	private ConfigurationSection getSectionOrDefault(String name) 
	{
		ConfigurationSection section = config.getConfigurationSection(name);
		
		if (section != null)
			return section;
		else
			return config.createSection(name);
	}
	
	
	//Messages
	/**
	 * Retrieve the Colorcode.
	 * @returnColorcode
	 */
	public String getColorcode()
	{
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(COLORCODE_KEY);
		return ChatColor.translateAlternateColorCodes(COLORCODE, value);
	}
	/**
	 * Retrieve the Message.
	 * @return Message including Colorcode
	 */
	public String getConversationFullMessage()
	{
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(CONVERSATIONFULL_KEY);
		return ChatColor.translateAlternateColorCodes(COLORCODE, value);
	}
	/**
	 * Retrieve the Message.
	 * @return Message including Colorcode
	 */
	public String getNotOriginalCallerMessage()
	{
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(NOTORIGINALCALLER_KEY);
		return ChatColor.translateAlternateColorCodes(COLORCODE, value);
	}
	/**
	 * Retrieve the Message.
	 * @return Message including Colorcode
	 */
	public String getUnavailabilityMessage()
	{
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(UNAVAILABILITY_KEY);
		return ChatColor.translateAlternateColorCodes(COLORCODE, value);
	}
	/**
	 * Retrieve the Message.
	 * @return Message including Colorcode
	 */
	public String getWaiterHasEndedCalledMessage()
	{
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(WAITERHASENDEDCALLED_KEY);
		return ChatColor.translateAlternateColorCodes(COLORCODE, value);
	}
	/**
	 * Retrieve the Message.
	 * @return Message including Colorcode
	 */
	public String getWaiterHasEndedCallerMessage()
	{
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(WAITERHASENDEDCALLER_KEY);
		return ChatColor.translateAlternateColorCodes(COLORCODE, value);
	}
	/**
	 * Retrieve the Message.
	 * @return Message including Colorcode
	 */
	public String getWaiterHasEndedCalledDisconnectedMessage()
	{
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(WAITERHASENDEDCALLEDDISCONNECTED_KEY);
		return ChatColor.translateAlternateColorCodes(COLORCODE, value);
	}
	/**
	 * Retrieve the Message.
	 * @return Message including Colorcode
	 */
	public String getWaiterIsStartingCalledMessage()
	{
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(WAITERISSTARTINGCALLED_KEY);
		return ChatColor.translateAlternateColorCodes(COLORCODE, value);
	}
	/**
	 * Retrieve the Message.
	 * @return Message including Colorcode
	 */
	public String getWaiterIsStartingCallerMessage()
	{
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(WAITERISSTARTINGCALLER_KEY);
		return ChatColor.translateAlternateColorCodes(COLORCODE, value);
	}
	
	/**
	 * Retrieve the Message.
	 * @return Message including Colorcode
	 */
	public String getConversationEstablishedMessage()
	{
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(CONVERSATIONESTABLISHED_KEY);
		return ChatColor.translateAlternateColorCodes(COLORCODE, value);
	}
	/**
	 * Retrieve the Message.
	 * @return Message including Colorcode
	 */
	public String getDisconnectMessage()
	{
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(DISCONNECT_KEY);
		return ChatColor.translateAlternateColorCodes(COLORCODE, value);
	}
	/**
	 * Retrieve the Message.
	 * @return Message including Colorcode
	 */
	public String getPlayerAddedMessage()
	{
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(PLAYERADDED_KEY);
		return ChatColor.translateAlternateColorCodes(COLORCODE, value);
	}
	/**
	 * Retrieve the Message.
	 * @return Message including Colorcode
	 */
	public String getPlayerRemovedMessage()
	{
		String value = getSectionOrDefault(SECTION_MESSAGES).getString(PLAYERREMOVED_KEY);
		return ChatColor.translateAlternateColorCodes(COLORCODE, value);
	}
	
	
	
	//Feature settings
	/**
	 * Get the feature on/off.
	 * @return value - new value.
	 */
	public boolean getChatBoolean() 
	{
		boolean value = getSectionOrDefault(SECTION_FEATURES).getBoolean(REALISTICCHAT_KEY);
		
		return value;
	}
	/**
	 * Set the feature on/off.
	 * @param value - new value.
	 */
	public void setChatBoolean(boolean value) 
	{
		getSectionOrDefault(SECTION_FEATURES).set(REALISTICCHAT_KEY, value);
	}
	/**
	 * Get the feature on/off.
	 * @return value - new value.
	 */
	public boolean getCellBoolean() 
	{
		boolean value = getSectionOrDefault(SECTION_FEATURES).getBoolean(CELL_KEY);
		
		return value;
	}
	/**
	 * Set the feature on/off.
	 * @param value - new value.
	 */
	public void setCellBoolean(boolean value) 
	{
		getSectionOrDefault(SECTION_FEATURES).set(CELL_KEY, value);
	}
	/**
	 * Get the feature on/off.
	 * @return value - new value.
	 */
	public boolean getLoreBoolean() 
	{
		boolean value = getSectionOrDefault(SECTION_FEATURES).getBoolean(LORE_KEY);
		
		return value;
	}
	/**
	 * Set the feature on/off.
	 * @param value - new value.
	 */
	public void setLoreBoolean(boolean value) 
	{
		getSectionOrDefault(SECTION_FEATURES).set(LORE_KEY, value);
	}
	/**
	 * Get the feature on/off.
	 * @return value - new value.
	 */
	public boolean getCelltowerBoolean() 
	{
		boolean value = getSectionOrDefault(SECTION_FEATURES).getBoolean(CELLTOWER_KEY);
		
		return value;
	}
	/**
	 * Set the feature on/off.
	 * @param value - new value.
	 */
	public void setCelltowerBoolean(boolean value) 
	{
		getSectionOrDefault(SECTION_FEATURES).set(CELLTOWER_KEY, value);
	}
	
	//Section with chatting settings
	/**
	 * Retrieve the distance in blocks.
	 * @return distance in blocks.
	 */
	public int getDistanceForWhispering() 
	{
		Object value = getSectionOrDefault(SECTION_DISTANCES).get(DISTANCEFORWHISPERING_KEY);
		
		if (value == null)
			return DEFAULT_DISTANCEFORWHISPERING;
		else
			return ((Number) value).intValue();
	}
	/**
	 * Set the distance in blocks.
	 * @param value - new distance.
	 */
	public void setDistanceForWhispering(int value) 
	{
		getSectionOrDefault(SECTION_DISTANCES).set(DISTANCEFORWHISPERING_KEY, value);
	}
	/**
	 * Retrieve the distance in blocks.
	 * @return distance in blocks.
	 */
	public int getDistanceForTalking() 
	{
		Object value = getSectionOrDefault(SECTION_DISTANCES).get(DISTANCEFORTALKING_KEY);
		
		if (value == null)
			return DEFAULT_DISTANCEFORTALKING;
		else
			return ((Number) value).intValue();
	}
	/**
	 * Set the distance in blocks.
	 * @param value - new distance.
	 */
	public void setDistanceForTalking(int value) 
	{
		getSectionOrDefault(SECTION_DISTANCES).set(DISTANCEFORTALKING_KEY, value);
	}
	/**
	 * Retrieve the distance in blocks.
	 * @return Distance in blocks.
	 */
	public int getDistanceForYelling() 
	{
		Object value = getSectionOrDefault(SECTION_DISTANCES).get(DISTANCEFORYELLING_KEY);
		
		if (value == null)
			return DEFAULT_DISTANCEFORYELLING;
		else
			return ((Number) value).intValue();
	}
	/**
	 * Set the distance in fraction.
	 * @param value - new distance.
	 */
	public void setDistanceForYelling(int value) 
	{
		getSectionOrDefault(SECTION_DISTANCES).set(DISTANCEFORYELLING_KEY, value);
	}
	
	/**
	 * Get the distance in fraction.
	 * @return value - distance.
	 */
	public float getDistanceForBreakingUpFactor() 
	{
		float value = (float) getSectionOrDefault(SECTION_DISTANCES).getDouble(DISTANCEFORBREAKINGUPFACTOR_KEY);
		
		if (value <=0 || value > 1)
			return DEFAULT_DISTANCEFORBREAKINGUPFACTOR;
		else
			return value;
	}
	/**
	 * Set the distance in fraction.
	 * @param value - new distance.
	 */
	public void setDistanceForBreakingUpFactor(float value) 
	{
		getSectionOrDefault(SECTION_DISTANCES).set(DISTANCEFORBREAKINGUPFACTOR_KEY, value);
	}
	
	/**
	 * Get the maxPlayerCount.
	 * @return value - maxPlayerCount.
	 */
	public int getMaxPlayerCount() 
	{
		int value = getSectionOrDefault(SECTION_DISTANCES).getInt(DISTANCEFORBREAKINGUPFACTOR_KEY);
		
		if (value <= 2)
			return DEFAULT_MAXPLAYERCOUNT;
		else
			return value;
	}
	/**
	 * Set the distance in fraction.
	 * @param value - new distance.
	 */
	public void setMaxPlayerCount(int value) 
	{
		getSectionOrDefault(SECTION_DISTANCES).set(DISTANCEFORBREAKINGUPFACTOR_KEY, value);
	}
	
	
	
	
	//Cell settings
	public String getLoreItemPhone() 
	{
		String value = getSectionOrDefault(SECTION_LOREITEMS).getString(LOREITEMPHONE_KEY);

		if (value == null)
			return DEFAULT_LOREITEMPHONE;
		else
			return ChatColor.translateAlternateColorCodes(COLORCODE, value);
	}
	
	/**
	 * Set the lore item name.
	 * @param value - new distance.
	 */
	public void setLoreItemPhone(String value) 
	{
		getSectionOrDefault(SECTION_DISTANCES).set(LOREITEMPHONE_KEY, ChatColor.translateAlternateColorCodes(COLORCODE, value));
	}

	//Cell Tower Settings
    public int getCTMinHeight(){
        int value = getSectionOrDefault(SECTION_CELLTOWER).getInt(CT_MINHEIGHT_KEY);

        if (value < 0)
            return DEFAULT_CT_MIN_HEIGHT;
        else
            return (value);
    }
    public int getCTMaxHeight(){
        int value = getSectionOrDefault(SECTION_CELLTOWER).getInt(CT_MAXHEIGHT_KEY);

        if (value < 0)
            return DEFAULT_CT_MAX_HEIGHT;
        else
            return (value);
    }
    public int getCTMaxRange(){
        int value = getSectionOrDefault(SECTION_CELLTOWER).getInt(CT_MAXRANGE_KEY);

        if (value < 0)
            return DEFAULT_CT_MAX_RANGE;
        else
            return (value);
    }
    public double getCTScrambleRange(){
        double value = getSectionOrDefault(SECTION_CELLTOWER).getDouble(CT_SCRAMBLERANGE_KEY);

        if (value < 0)
            return DEFAULT_CT_SCRAMBLE_RANGE;
        else
            return (value);
    }
}
