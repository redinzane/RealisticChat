package ch.k42.aftermath.radiotower;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.Material;

/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 15.09.13
 * Time: 16:51
 * To change this template use File | Settings | File Templates.
 */
public class RadioTower{


    public static final Material BASE_BLOCK = Material.OBSIDIAN;

    private static int MIN_HEIGHT = 6;
    private static int MAX_HEIGHT = 50;
    private static int MAX_RANGE = 10000;
    private static double FULL_POWER_FACTOR = 0.3;



    public final int WORLD_HEIGHT;

    protected String message;
    protected Plugin plugin;
    private final Location location;

    private int nominalRange = 0;
    private int nominalRangeSquared = 0;
    private int rolloffRangeSquared = 0;

    public static boolean setParameters(int MIN_HEIGHT,int MAX_HEIGHT,int MAX_RANGE,double CUTOFF) {
        if(MIN_HEIGHT<1) return false;
        if(MAX_HEIGHT<MIN_HEIGHT) return false;
        if(MAX_RANGE<0) return false;
        if(CUTOFF<0 || CUTOFF>1) return false;

        RadioTower.MIN_HEIGHT = MIN_HEIGHT;
        RadioTower.MAX_HEIGHT = MAX_HEIGHT;
        RadioTower.MAX_RANGE = MAX_RANGE;
        RadioTower.FULL_POWER_FACTOR = CUTOFF;
        return true;
    }


    public RadioTower(Plugin plugin, Location location) {
        this.location = location;
        this.WORLD_HEIGHT = location.getWorld().getMaxHeight();
        this.plugin = plugin;
        this.nominalRange = MAX_RANGE;
        if(update()){
            location.getWorld().playEffect(location, Effect.MOBSPAWNER_FLAMES,0);
        }
    }

    public void broadcastMessage() {
        if(!isActive()) return; // not active
        Bukkit.getPluginManager().callEvent(new RadioMessageEvent(message,this));
    }

    private boolean isActive(){
        return this.location.getBlock().getBlockPower()==0;
    }

    private void setRange(int range) {
        if(range<0) range=0;
        this.nominalRange = range;
        this.nominalRangeSquared = range*range;
        this.rolloffRangeSquared = (int)( this.nominalRange*(1-FULL_POWER_FACTOR));
        this.rolloffRangeSquared *= rolloffRangeSquared;
    }

    public double getReceptionPower(Location location) {
        if(this.location.getBlock().getBlockPower()!=0) return 0; // tower off?

        int distanceSquared = (int) this.location.distanceSquared(location);
        if(nominalRangeSquared<distanceSquared) return 0;

        if(distanceSquared<=nominalRangeSquared*FULL_POWER_FACTOR)
            return 1;
        double diff = (distanceSquared-nominalRangeSquared*FULL_POWER_FACTOR);

        return 1-diff/rolloffRangeSquared;
    }

    public boolean update() {
        if(!this.location.getBlock().getType().equals(BASE_BLOCK)) // check if base is correct
            return false;
        boolean hasRedstoneTorch = false;
        boolean hasSign = false;
        for(int x=-1;x<=1;x++){
            for(int z=-1;z<=1;z++){
                if(Math.abs(x)!=Math.abs(z)){ // check for sign & torch
                    Location l = location.clone();
                    Block b = l.add(x,0,z).getBlock();
                    if(b.getType().equals(Material.REDSTONE_TORCH_ON)||b.getType().equals(Material.REDSTONE_TORCH_OFF)){
                        hasRedstoneTorch = true;
                    }else if(b.getType().equals(Material.WALL_SIGN)){
                        hasSign = true;
                        org.bukkit.block.Sign s = (org.bukkit.block.Sign) b.getState();
                        StringBuffer sb = new StringBuffer();
                        for(String line : s.getLines()){ // read message
                            sb.append(line);
                        }
                        this.message = sb.toString();
                    }
                }
            }
        }
        if(!hasSign || !hasRedstoneTorch) return false; // sign or torch missing
        Location base = location.clone().add(0,1,0); // start of the antenna
        int height = 0;
        for(int i=0;i<MAX_HEIGHT && base.getY()<WORLD_HEIGHT;i++){
            if(base.getBlock().getType().equals(Material.IRON_FENCE)){
                height++;
            }else {
                break;
            }
            base.add(0,1,0);
        }
        if(height<MIN_HEIGHT) return false; // antenna not high enough
        while (base.getBlock().getType().equals(Material.AIR)){
            base.add(0,1,0);
        }

        if(base.getY()!=WORLD_HEIGHT) return false; // no sunlight

        setRange((int) (MAX_RANGE*((height-MIN_HEIGHT)/( (double) MAX_HEIGHT-MIN_HEIGHT))));
        return true;
    }

    /*
     * Too much duplication FIXME
     */
    public static boolean validate(Location location){
        int WORLD_HEIGHT = location.getWorld().getMaxHeight();
        if(!location.getBlock().getType().equals(BASE_BLOCK)) // check if base is correct
            return false;
//        Bukkit.getLogger().info("Base block found");
        boolean hasRedstoneTorch = false;
        boolean hasSign = false;
        String message = "";
        for(int x=-1;x<=1;x++){
            for(int z=-1;z<=1;z++){
                if(Math.abs(x)!=Math.abs(z)){ // check for sign & torch
                    Location l = location.clone();
                    Block b = l.add(x,0,z).getBlock();
                    if(b.getType().equals(Material.REDSTONE_TORCH_ON)||b.getType().equals(Material.REDSTONE_TORCH_OFF)){
                        hasRedstoneTorch = true;
                    }else if(b.getType().equals(Material.WALL_SIGN)){
                        hasSign = true;

                        org.bukkit.block.Sign s = (org.bukkit.block.Sign) b.getState();

                        StringBuffer sb = new StringBuffer();
                        for(String line : s.getLines()){ // read message
                            sb.append(line);
                        }
                        message = sb.toString();
                    }
                }
            }
        }
        if(!hasSign || !hasRedstoneTorch) return false; // sign or torch missing
        Location base = location.clone().add(0,1,0); // start of the antenna
        int height = 0;
        for(int i=0;i<MAX_HEIGHT && base.getY()<WORLD_HEIGHT;i++){
            if(base.getBlock().getType().equals(Material.IRON_FENCE)){
                height++;
            }else {
                break;
            }
            base.add(0,1,0);
        }
        if(height<MIN_HEIGHT) return false; // antenna not high enough
        while (base.getBlock().getType().equals(Material.AIR)){
            base.add(0,1,0);
        }

        if(base.getY()!=WORLD_HEIGHT) return false; // no sunlight

        int range =((int) (MAX_RANGE*((height-MIN_HEIGHT)/( (double) MAX_HEIGHT-MIN_HEIGHT))));
        Bukkit.getLogger().info("Valid tower found, range: " + range +  "  message: " + message);
        return true;
    }

    @Override
    public String toString() {
        return location + " : " + message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RadioTower tower = (RadioTower) o;

        if (!location.equals(tower.location)) return false;
        if (message != null ? !message.equals(tower.message) : tower.message != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }
}
