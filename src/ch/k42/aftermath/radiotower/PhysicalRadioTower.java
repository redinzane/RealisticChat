package ch.k42.aftermath.radiotower;

import org.bukkit.Location;

/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 15.09.13
 * Time: 17:43
 * To change this template use File | Settings | File Templates.
 */
public interface PhysicalRadioTower {

    /**
     * Reads out the message of the tower
     * @return the message
     */
    public String getMessage();

    /**
     * The physical location of the tower
     * @return location of the tower
     */
    public Location getLocation();

    /**
     * Returns the power state of the tower
     * @return
     */
    public int getPower();

    /**
     * Validates if the physical tower is still standing
     * @return true if this is a valid tower or false if not
     */
    public boolean validate();

    /**
     * updates the state and power of the tower
     * @return true if the towers state has changed or false if not
     */
    public boolean update();

    /**
     * Evaluates how high the tower is in terms of his possible hight (the higher, the better the range)
     * @return height / maximum height ratio, lies in the interval [0,1]
     */
    public double getHeightRatio();
}
