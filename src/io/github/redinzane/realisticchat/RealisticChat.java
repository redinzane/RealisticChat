package io.github.redinzane.realisticchat;
/**
 * Created by Moritz Schwab
 * CellTower system largely inspired by Thomas Richner's RadioTower plugin with friendly permission and some help in understanding it
 */


import io.github.redinzane.realisticchat.RealisticChatConfiguration;

import org.bukkit.plugin.java.JavaPlugin;

public class RealisticChat extends JavaPlugin {
	// Configuration
	protected RealisticChatConfiguration config;

	private RealisticChatListener realisticChatListener;

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		config = new RealisticChatConfiguration(getConfig());
		realisticChatListener = new RealisticChatListener(this, config);
		realisticChatListener.onEnable();
	}

	@Override
	public void onDisable() {
		realisticChatListener.onDisable();
	}

}