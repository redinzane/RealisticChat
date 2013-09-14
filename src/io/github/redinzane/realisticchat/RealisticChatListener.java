package io.github.redinzane.realisticchat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.material.MaterialData;

@SuppressWarnings("deprecation")
public class RealisticChatListener implements Listener
{
	int distanceForWhispering;
	int distanceForYelling;
	int distanceForTalking;
	float distanceForBreakingUpFactor;
	
	final int phoneTypeID = 317;
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event)
	{
		Player playerChatting = event.getPlayer();
		String message = event.getMessage();
		Player[] onlinePlayers = Bukkit.getOnlinePlayers();
		List<Player> playersToSendTo = new ArrayList<Player>();
		for(Player player: onlinePlayers)
		{
			double distance;
			int length = message.length();
			
			if(event.getPlayer().getItemInHand().getType().equals(Material.getMaterial("WATCH")))
			{
				
			}
			if(length >=4 )
			{
				
			}
			try
			{
				distance = player.getLocation().distance(playerChatting.getLocation());
				
			}
			catch (IllegalArgumentException e)
			{
				
			}
		}
		
	}
	
	
}
