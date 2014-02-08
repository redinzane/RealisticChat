package io.github.redinzane.realisticchat;

import java.io.File;
import java.util.logging.Level;

import ch.k42.aftermath.radiotower.RadioTower;
import ch.k42.aftermath.radiotower.RadioTowerManager;
import io.github.redinzane.realisticchat.RealisticChatConfiguration;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class RealisticChat extends JavaPlugin 
{
		// Configuration
		protected RealisticChatConfiguration config;
		// Minecraft packet handling
		private RealisticChatListener realisticChatListener;
		private RadioTuningListener radioListener;
		
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
			
			//Always, always construct after reading the config
			realisticChatListener = new RealisticChatListener(this);
			radioListener = new RadioTuningListener(config.getLoreItemRadio(),this);

			// Register listeners
			getServer().getPluginManager().registerEvents(realisticChatListener, this);


            // All radio stuff
            if(config.getRadioBoolean()){
                RadioTower.setParameters(config.getRTMinHeight(),config.getRTMaxHeight(),config.getRTMaxRange(),config.getRTCutoffRange());
                getLogger().info("enabling RadioTowers");
                RadioTowerManager rtm = new RadioTowerManager(this);
                getServer().getPluginManager().registerEvents(radioListener, this);
                getServer().getPluginManager().registerEvents(rtm, this);
                long time = 20L*config.getRadioCooldown();
                getServer().getScheduler().scheduleSyncRepeatingTask(this,rtm,100L ,time);
            }

			
			//Read values from Config here
			
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