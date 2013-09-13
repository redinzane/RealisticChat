package io.github.redinzane.realisticchat;

import java.io.File;

import io.github.redinzane.realisticchat.RealisticChatConfiguration;

import org.bukkit.plugin.java.JavaPlugin;

public class RealisticChat extends JavaPlugin 
{
		// Configuration
		private RealisticChatConfiguration config;
		// Minecraft packet handling
		private RealisticChatListener realisticChatListener;
		private RadioListener radioListener;
		
		@Override
	    public void onEnable()
		{
			//Creates a Config
			config = new RealisticChatConfiguration(getConfig());
			
			if (!hasConfig()) {
				getConfig().options().copyDefaults(true);
				saveConfig();
				
				// Load it again
				config = new RealisticChatConfiguration(getConfig());
				getLogger().info("Creating default configuration.");
			}
			
			realisticChatListener = new RealisticChatListener();
			radioListener = new RadioListener();

			// Register listeners
			getServer().getPluginManager().registerEvents(realisticChatListener, this);
			getServer().getPluginManager().registerEvents(radioListener, this);
			
			//Read values from Config here
			realisticChatListener.distanceForWhispering = config.getDistanceForWhispering();
			realisticChatListener.distanceForYelling = config.getDistanceForYelling();
			realisticChatListener.distanceForTalking = config.getDistanceForTalking();
			realisticChatListener.distanceForBreakingUpFactor = config.getDistanceForBreakingUpFactor();
			
			
			//To-Do: Read antennas from file here
			
	    }
	 
	    @Override
	    public void onDisable()
	    {
	    	
	    }
	    
	    //Checks if a config file exists
	    private boolean hasConfig() {
			File file = new File(getDataFolder(), "config.yml");
			return file.exists();
		}
	}