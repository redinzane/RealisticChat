package ch.k42.aftermath.radiotower;

import org.bukkit.Location;

/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 15.09.13
 * Time: 16:26
 * To change this template use File | Settings | File Templates.
 */
public interface RadioTower {
    public enum RadioTowerState{
        OFF,REDUCED,FULL
    }

    /**
     * Sends the specified message to all Players
     * which are in range of the RadioTower
     */
    public void broadcastMessage();

    /**
     * Getter for the Location of the tower
     * @param location Location of the tower base block
     */
    public void getLocation(Location location);

    /**
     * Changes the state of the RadioTower, range depends on the
     * state
     * @return The state of the RadioTower
     */
    public RadioTowerState getState();

    /**
     * Sets the state of the radio tower, effective range depends on state
     * @param state
     */
    public void setState(RadioTowerState state);

    /**
     * Sets the nominal range of the tower, this will not be the effective range, since this
     * depends on the state
     * @param range
     */
    public void setRange(int range);

    /**
     * Getter for the nominal range
     * @return the nominal range of the RadioTower
     */
    public int getRange();

    /**
     * Returns the calculated effective range
     * @return the effective range of the tower
     */
    public int getEffectiveRange();

    /**
     * determines if a given location is in range of the tower
     * @param location location to check
     * @return true if it is in range or false otherwise
     */
    public boolean isInRange(Location location);

    /**
     * Checks if the Tower is still valid, if not, the tower should
     * set it's state to OFF
     * @return true if still valid or false
     */
    public boolean checkValidity();
}
