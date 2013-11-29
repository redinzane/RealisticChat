package io.github.redinzane.realisticchat;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

public class RealisticChatConfiguration
{
	private final Configuration config;
	
	private static final String SECTION_COOLDOWNS = "cooldowns";
	private static final String SECTION_DISTANCES = "distances";
	private static final String SECTION_FEATURES = "features";
	private static final String SECTION_LOREITEMS = "loreitems";
	
	
	private static final String RADIOCOOLDOWN_KEY = "radiomessagecooldown";
	
	private static final String DISTANCEFROMCELLTOWER_KEY = "distancefromcelltower";
	private static final String DISTANCEFROMRADIOTOWER_KEY = "distancefromradiotower";
	private static final String DISTANCEFORWHISPERING_KEY = "distanceforwhisperimg";
	private static final String DISTANCEFORTALKING_KEY = "distancefortalking";
	private static final String DISTANCEFORYELLING_KEY = "distanceforyelling";
	private static final String DISTANCEFORBREAKINGUPFACTOR_KEY = "distanceforbreakingupfactor";
	
	private static final String LOREITEMPHONE_KEY = "phone";
	
	private static final String RADIO_KEY = "radio";
	private static final String REALISTICCHAT_KEY = "realisticchat";
	private static final String CELL_KEY = "cellphones";
	private static final String LORE_KEY = "loreitems";
	
	
	private static final int DEFAULT_RADIOCOOLDOWN = 2000;
	
	private static final int DEFAULT_DISTANCEFROMCELLTOWER = 150;
	private static final int DEFAULT_DISTANCEFROMRADIOTOWER = 150;
	private static final int DEFAULT_DISTANCEFORWHISPERING = 10;
	private static final int DEFAULT_DISTANCEFORTALKING = 50;
	private static final int DEFAULT_DISTANCEFORYELLING = 1000;
	private static final float DEFAULT_DISTANCEFORBREAKINGUPFACTOR = 0.7f;
	
	private static final String DEFAULT_LOREITEMPHONE = "§bPhone";
	
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
	
	
	//Feature settings
	/**
	 * Get the feature on/off.
	 * @return value - new value.
	 */
	public boolean getRadioBoolean() 
	{
		boolean value = getSectionOrDefault(SECTION_FEATURES).getBoolean(RADIO_KEY);
		
		return value;
	}
	/**
	 * Set the feature on/off.
	 * @param value - new value.
	 */
	public void setRadioBoolean(boolean value) 
	{
		getSectionOrDefault(SECTION_FEATURES).set(RADIO_KEY, value);
	}
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
	
	
	//Section with Radio settings
	/**
	 * Retrieve the cooldown in milliseconds.
	 * @return Cooldown in milliseconds.
	 */
	public int getRadioCooldown() 
	{
		Object value = getSectionOrDefault(SECTION_COOLDOWNS).get(RADIOCOOLDOWN_KEY);
		
		if (value == null)
			return DEFAULT_RADIOCOOLDOWN;
		else
			return ((Number) value).intValue();
	}
	/**
	 * Set the cooldown in milliseconds.
	 * @param value - new cooldown.
	 */
	public void setRadioCooldown(int value) 
	{
		getSectionOrDefault(SECTION_COOLDOWNS).set(RADIOCOOLDOWN_KEY, value);
	}
	/**
	 * Retrieve the distance in blocks.
	 * @return Distance in milliseconds.
	 */
	public int getDistanceFromRadioTower() 
	{
		Object value = getSectionOrDefault(SECTION_DISTANCES).get(DISTANCEFROMRADIOTOWER_KEY);
		
		if (value == null)
			return DEFAULT_DISTANCEFROMRADIOTOWER;
		else
			return ((Number) value).intValue();
	}
	/**
	 * Set the distance in milliseconds.
	 * @param value - new distance.
	 */
	public void setDistanceFromRadioTower(int value) 
	{
		getSectionOrDefault(SECTION_DISTANCES).set(DISTANCEFROMRADIOTOWER_KEY, value);
	}
	
	
	//Section with chatting settings
	/**
	 * Retrieve the distance in blocks.
	 * @return Distance in milliseconds.
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
	 * Set the distance in milliseconds.
	 * @param value - new distance.
	 */
	public void setDistanceForWhispering(int value) 
	{
		getSectionOrDefault(SECTION_DISTANCES).set(DISTANCEFORWHISPERING_KEY, value);
	}
	/**
	 * Retrieve the distance in blocks.
	 * @return Distance in milliseconds.
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
	 * Set the distance in milliseconds.
	 * @param value - new distance.
	 */
	public void setDistanceForTalking(int value) 
	{
		getSectionOrDefault(SECTION_DISTANCES).set(DISTANCEFORTALKING_KEY, value);
	}
	/**
	 * Retrieve the distance in blocks.
	 * @return Distance in milliseconds.
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
	 * Set the distance in milliseconds.
	 * @param value - new distance.
	 */
	public void setDistanceForYelling(int value) 
	{
		getSectionOrDefault(SECTION_DISTANCES).set(DISTANCEFORYELLING_KEY, value);
	}
	public float getDistanceForBreakingUpFactor() 
	{
		Object value = getSectionOrDefault(SECTION_DISTANCES).get(DISTANCEFORBREAKINGUPFACTOR_KEY);
		
		if (value == null)
			return DEFAULT_DISTANCEFORBREAKINGUPFACTOR;
		else
			return ((Number) value).floatValue();
	}
	/**
	 * Set the distance in milliseconds.
	 * @param value - new distance.
	 */
	public void setDistanceForBreakingUpFactor(float value) 
	{
		getSectionOrDefault(SECTION_DISTANCES).set(DISTANCEFORBREAKINGUPFACTOR_KEY, value);
	}
	
	//Cell settings
	/**
	 * Retrieve the distance in blocks.
	 * @return Distance in milliseconds.
	 */
	public int getDistanceFromCellTower() 
	{
		Object value = getSectionOrDefault(SECTION_DISTANCES).get(DISTANCEFROMCELLTOWER_KEY);
		
		if (value == null)
			return DEFAULT_DISTANCEFROMCELLTOWER;
		else
			return ((Number) value).intValue();
	}
	/**
	 * Set the distance in milliseconds.
	 * @param value - new distance.
	 */
	public void setDistanceFromCellTower(int value) 
	{
		getSectionOrDefault(SECTION_DISTANCES).set(DISTANCEFROMCELLTOWER_KEY, value);
	}
	
	//Lore settings
	/**
	 * Retrieve the lore item name.
	 * @return Distance in milliseconds.
	 */
	public String getLoreItemPhone() 
	{
		String value = getSectionOrDefault(SECTION_LOREITEMS).getString(LOREITEMPHONE_KEY);

		if (value == null)
			return DEFAULT_LOREITEMPHONE;
		else
			return (value);
	}
	/**
	 * Set the lore item name.
	 * @param value - new distance.
	 */
	public void setLoreItemPhone(String value) 
	{
		getSectionOrDefault(SECTION_DISTANCES).set(DISTANCEFROMCELLTOWER_KEY, value);
	}
	
}
