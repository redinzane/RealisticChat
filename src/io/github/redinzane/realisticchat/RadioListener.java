package io.github.redinzane.realisticchat;

import ch.k42.aftermath.radiotower.Minions;
import ch.k42.aftermath.radiotower.RadioMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.ItemMeta;

public final class RadioListener implements Listener{
    private String LOREITEMRADIO;
    private Player player;


    public RadioListener(String LOREITEMRADIO, Player player) {
        this.LOREITEMRADIO = LOREITEMRADIO;
        this.player = player;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void receiveMessage(RadioMessageEvent event){
//        Bukkit.getLogger().info("receiving message...");
        if(hasRadioInHand(player)){
//            Bukkit.getLogger().info("Sending message to player!");
            String msg = event.getMessageAt(player.getLocation());
            if(msg!=null) player.sendMessage(msg); // display message, obfuscate if needed
        }else {
//            Bukkit.getLogger().info("unregister handler...");
            RadioMessageEvent.getHandlerList().unregister(this); // we no longer have a radio in hand, no need to listen further
        }
    }

    private boolean hasRadioInHand(Player player){
        ItemMeta meta = player.getItemInHand().getItemMeta();
        if(meta==null) return false;
        String name = meta.getDisplayName();
        if(name==null) return false;
        if(!name.equals(LOREITEMRADIO)) return false;
        return true;

    }
}
