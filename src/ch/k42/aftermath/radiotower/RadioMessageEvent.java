package ch.k42.aftermath.radiotower;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Thomas on 07.02.14.
 */
public class RadioMessageEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private String message;
    private RadioTower tower;

    public RadioMessageEvent(String message, RadioTower tower) {
        this.message = message;
        this.tower = tower;
    }

    /**
     *
     * @param location the location of the receiver
     * @return the broadcasted message or null if none reveived
     */
    public String getMessageAt(Location location) {

        double reception = tower.getReceptionPower(location);
        if(reception>0)
            return Minions.obfuscateMessage(message, 1 - reception);
        return null;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
