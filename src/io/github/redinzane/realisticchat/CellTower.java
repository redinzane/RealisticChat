package io.github.redinzane.realisticchat;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class CellTower {
	// Static Stuff
	protected static int MIN_HEIGHT = 1;
	protected static int MAX_HEIGHT = 2;
	protected static int MAX_RANGE;
	protected static Material BASE_BLOCK = Material.getMaterial("NOTE_BLOCK");
	private static final int MAX_POWER = 180000; // 180kW, power of a big radio tower in Switzerland. Not an ideal reference, but eh

	private static final int ALPHA = 100; //Magic value
	
	// Non-static stuff
	private Location location;
	private int maxRange = 0;
	private double antennaGain;

	public CellTower(Location location) {
		this.location = location;
		this.maxRange = 0;
		if (update()) {
			location.getWorld().playEffect(location, Effect.MOBSPAWNER_FLAMES,
					0);
		}
	}

	public static boolean validate(Location location) {
		if (location == null) {
			return false;
		}
		int WORLD_HEIGHT = location.getWorld().getMaxHeight();
		// check if base is correct
		if (!location.getBlock().getType().equals(BASE_BLOCK)) {
			return false;
		}
		boolean hasRedstoneTorch = false;
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				if (Math.abs(x) != Math.abs(z)) {
					// check for torch
					Location loc = location.clone();
					Block block = loc.add(x, 0, z).getBlock();
					if (block.getType().equals(Material.REDSTONE_TORCH_ON) || block.getType().equals(Material.REDSTONE_TORCH_OFF)) {
						hasRedstoneTorch = true;
					}
				}
			}
		}
		if (!hasRedstoneTorch) {
			return false; // sign or torch missing
		}

		Location base = location.clone().add(0, 1, 0); // start of the antenna
		int height = calculateHeight(base, WORLD_HEIGHT);
		base.add(0, height+1, 0);
		if (height < MIN_HEIGHT) {
			return false; // antenna not high enough
		}
		while (base.getBlock().getType().equals(Material.AIR)) {
			base.add(0, 1, 0);
		}
		if (base.getY() != WORLD_HEIGHT) {
			return false; // no sunlight
		}
		return true;
	}
	
	private static int calculateHeight(Location location, int WORLD_HEIGHT) {
		Location base = location.clone().add(0, 1, 0); // start of the antenna
		int height = 0;
		for (int i = 0; i < MAX_HEIGHT && base.getY() < WORLD_HEIGHT; i++) {
			if (base.getBlock().getType().equals(Material.IRON_FENCE)) {
				height++;
			} else {
				break;
			}
			base.add(0, 1, 0);
		}
		return height;
	}
	
	public boolean update() {
		boolean result = validate(this.location);
		if (result) {
			int height = calculateHeight(this.location.clone().add(0, 1, 0), location.getWorld().getMaxHeight());
			calculateRange(height);
			return true;
		} else {
			return false;
		}
	}

	private void calculateRange(int height) {
		double linFactor = ((height) / ((double) MAX_HEIGHT));
		if (linFactor < 0) {
			linFactor = 0;
		}
		int range = (int) (MAX_RANGE * linFactor);
		this.antennaGain = linFactor * MAX_POWER;
		if (range < 0) {
			range = 0;
		}
		range += 20;
		this.maxRange = range;
	}

	private double inversePowerLaw(double radius) {
		if (radius > this.maxRange) {
			return 0;
		}
		if (radius < 1)
			radius = 1;
		{
			return ALPHA / (radius);
		}
	}

	public double getReceptionPower(Location location) {
		return this.antennaGain * getNormalizedReceptionPower(location);
	}

	public double getNormalizedReceptionPower(Location location) {
		if (!this.location.getWorld().equals(location.getWorld())) {
			return 0;
		}
		if (this.location.getBlock().getBlockPower() != 0) {
			return 0; // tower off?
		}
		double distance = this.location.distance(location);
		if (distance > MAX_RANGE) {
			return 0;
		}
		return inversePowerLaw(distance);
	}


	public Location getLocation() {
		return location;
	}

	public double getAntennaGain() {
		return antennaGain;
	}
}
