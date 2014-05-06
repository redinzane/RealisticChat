package io.github.redinzane.realisticchat;

/**
 * Created by Moritz Schwab
 * CellTower system largely inspired by Thomas Richner's RadioTower plugin with friendly permission and some help in understanding it
 */

import java.util.Arrays;
import java.util.List;

import io.github.redinzane.realisticchat.RealisticChatConfiguration;
import io.github.redinzane.realisticchat.minions.ChatMessage;
import io.github.redinzane.realisticchat.minions.RealisticChatDAO;

import org.bukkit.plugin.java.JavaPlugin;

public class RealisticChat extends JavaPlugin {
    
    @Override
    public List<Class<?>> getDatabaseClasses() {
        return Arrays.<Class<?>> asList(ChatMessage.class);
    }
    
	protected RealisticChatConfiguration config;
	private RealisticChatListener realisticChatListener;
	RealisticChatDAO dao = new RealisticChatDAO(this);
	private static final long ONE_DAY_IN_TICKS = 20 * 60 * 60 * 24;
	
	@SuppressWarnings("deprecation")
    @Override
	public void onEnable() {
	    
		this.saveDefaultConfig();
		config = new RealisticChatConfiguration(getConfig());
        dao = new RealisticChatDAO(this);
        dao.connect();
        //Enable the god class (bad form)
		realisticChatListener = new RealisticChatListener(this, dao, config);
		realisticChatListener.onEnable();
		// do a prune roughly every 24 hours 
		getServer().getScheduler().scheduleAsyncRepeatingTask(this,new Runnable() {
            @Override
            public void run() {pruneDatabase();}},100L,ONE_DAY_IN_TICKS);
	}

	@Override
	public void onDisable() {
		realisticChatListener.onDisable();
		getServer().getScheduler().cancelTasks(this);
	}

	// expose database setup
    @Override
    public void installDDL() {
        super.installDDL();
    }
    
    public void pruneDatabase() {
        dao.pruneDatabase();
    }
    
    
}