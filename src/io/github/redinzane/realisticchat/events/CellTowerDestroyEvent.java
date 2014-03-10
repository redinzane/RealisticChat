package io.github.redinzane.realisticchat.events;

import org.bukkit.Location;
import org.bukkit.event.HandlerList;

public class CellTowerDestroyEvent 
{
	  private static final HandlerList handlers = new HandlerList();
	   private Location location;
	 
	   public CellTowerDestroyEvent(Location location) 
	   {
	       this.location = location;
	   }
	 
	   public Location getLocation()
	   {
		   return location;
	   }
	 
	   public HandlerList getHandlers() 
	   {
	       return handlers;
	   }
	 
	   public static HandlerList getHandlerList() 
	   {
	       return handlers;
	   }
}

