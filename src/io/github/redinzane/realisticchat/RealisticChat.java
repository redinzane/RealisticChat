package io.github.redinzane.realisticchat;

import io.github.redinzane.realisticchat.RealisticChatConfiguration;

import org.bukkit.plugin.java.JavaPlugin;

public class RealisticChat extends JavaPlugin 
{
		// Configuration
		protected RealisticChatConfiguration config;
		// Minecraft packet handling
		private RealisticChatListener realisticChatListener;

		@Override
	    public void onEnable()
		{
			//Creates a Config
			this.saveDefaultConfig();
			config = new RealisticChatConfiguration(getConfig());
			
			realisticChatListener = new RealisticChatListener(this, config);
			
			// Register listeners
			getServer().getPluginManager().registerEvents(realisticChatListener, this);

			//To-Do: Read antennas from file here
			
	    }

    @Override
	    public void onDisable()
	    {
	    	
	    }
	    
	}