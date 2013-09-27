package ch.k42.aftermath.radiotower;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 15.09.13
 * Time: 16:51
 * To change this template use File | Settings | File Templates.
 */
public class TowerMkI implements RadioTower {

    public static final Material RADIO_RECEIVER_ITEM = Material.COMPASS;
    public static final int MIN_HEIGHT = 6;
    public static final int MAX_HEIGHT = 50;
    public static final int MAX_RANGE = 10000;

    protected String message;
    protected RadioTowerState state;
    protected Plugin plugin;
    protected Location location;

    protected int nominalRange = 0;
    protected int effectiveRange = 0;
    protected int effectiveRangeSquared = 0;

    protected PhysicalRadioTower tower;

    public TowerMkI(Plugin plugin) {
        this.state = RadioTowerState.OFF;
        this.plugin = plugin;
        this.nominalRange = MAX_RANGE;
    }

    @Override
    public void broadcastMessage() {
        if(effectiveRange==0) return; // tower not even sending
        Player[] players = plugin.getServer().getOnlinePlayers(); // check all players if they are in range
        for(Player p : players){
            if(!p.getItemInHand().getType().equals(RADIO_RECEIVER_ITEM))   continue; // is he even holding the correct item?
            if(!p.getLocation().getWorld().equals(location))               continue; // is he even in the same world?
            if(!isInRange(p.getLocation())) continue;   // is he even in range?

            // ok, player should receive message
            p.sendMessage(message);
        }
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public RadioTowerState getState() {
        return this.state;
    }

    @Override
    public void setState(RadioTowerState state) {
        this.state = state;
        update();
    }

    @Override
    public void setRange(int range) {
        this.nominalRange = range;
    }

    @Override
    public int getRange() {
        return nominalRange;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getEffectiveRange() {
        return effectiveRange;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isInRange(Location location) {
        return this.location.distanceSquared(location)<effectiveRangeSquared;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean update() {
        if(!tower.update()) return tower.validate(); //has it even changed?

        double height = tower.getHeightRatio();

        if(height==0){
            effectiveRange=0;
        }else {
            effectiveRange = (int) ( nominalRange*tower.getPower()/16.0*((double)height)/(MAX_HEIGHT) );
        }
        // calculate the effective range

        //TODO


        return tower.validate();  //To change body of implemented methods use File | Settings | File Templates.
    }

}
