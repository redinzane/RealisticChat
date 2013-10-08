package io.github.redinzane.realisticchat;

import org.bukkit.entity.Player;

public class ConversationWaiter
{
	public Player caller;
	public Player playerBeingCalled;
	
	ConversationWaiter(Player caller, Player playerBeingCalled)
	{
		this.caller = caller;
		this.playerBeingCalled = playerBeingCalled;
	}
}
