package io.github.redinzane.realisticchat;

import io.github.redinzane.realisticchat.events.CellTowerDestroyEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CellTowerListener implements Listener
{
	RealisticChat plugin;
	
	public CellTowerListener(RealisticChat plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void CellTowerDestroyEvent(CellTowerDestroyEvent event)
	{
		
	}
}
