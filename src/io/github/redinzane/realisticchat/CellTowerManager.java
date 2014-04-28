package io.github.redinzane.realisticchat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.RedstoneTorch;

public class CellTowerManager implements Listener, Runnable {
	private Map<Location, CellTower> towers = new ConcurrentHashMap<Location, CellTower>();
	RealisticChatListener RCListener;
	private int numberOfTowers = 0;
	public static final String TOWERS_FILE = "celltowers.csv";

	public CellTowerManager(RealisticChatListener RCListener,
			RealisticChatConfiguration config) {
		this.RCListener = RCListener;

		String baseBlock = config.getBaseBlock();
		if (Material.getMaterial(baseBlock) != null) {
			CellTower.BASE_BLOCK = Material.getMaterial(baseBlock);
		}
		int minHeight = config.getCTMinHeight();
		int maxHeight = config.getCTMaxHeight();
		if (maxHeight > minHeight) {
			CellTower.MIN_HEIGHT = minHeight;
			CellTower.MAX_HEIGHT = maxHeight;
		}
		CellTower.MAX_RANGE = config.getCTMaxRange();

	}

	public void registerTower(Location location) {
		if (towers.containsKey(location)) {
			return;
		}
		else if (CellTower.validate(location)) {
			towers.put(location, new CellTower(location));
		}
	}

	public Set<Location> getTowerLocations() {
		return towers.keySet();
	}

	public CellTower getTower(Location location) {
		return towers.get(location);
	}

	public final List<CellTower> getTowers() {
		return Collections.unmodifiableList(new ArrayList<CellTower>(towers
				.values()));
	}

	private static final int MAX_BLOCKCHECK = 10;

	@EventHandler
	public void blockPlaced(BlockPlaceEvent event) {
		Block block = event.getBlockPlaced();
		Location location = block.getLocation();
		if (block.getType().equals(CellTower.BASE_BLOCK)) {
			registerTower(location);
		} else if (block.getType().equals(Material.IRON_FENCE)) {
			for (int i = 0; i < MAX_BLOCKCHECK; i++) {
				location.add(0, -1, 0);
				if (!location.getBlock().getType().equals(Material.IRON_FENCE)) {
					break;
				}
			}
			if (location.getBlock().getType().equals(CellTower.BASE_BLOCK)) {
				registerTower(location);
			}

		} else if (block.getType().equals(Material.REDSTONE_TORCH_ON)
				|| block.getType().equals(Material.REDSTONE_TORCH_OFF)) {
			RedstoneTorch torch = (RedstoneTorch) block.getState().getData();
			location.add(torch.getAttachedFace().getModX(), torch
					.getAttachedFace().getModY(), torch.getAttachedFace()
					.getModZ());
			if (location.getBlock().getType().equals(CellTower.BASE_BLOCK)) {
				registerTower(location);
			}
		}
	}

	@EventHandler
	public void rightClick(PlayerInteractEvent event) {
		if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event
				.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			return; // only rightclick
		}

		if (RCListener.phoneValidator(event.getItem())) {
			Player player = event.getPlayer();
			Location place = player.getLocation();
			Location location = findClosestTower(place);
			if (location == null) {
				player.sendMessage(RCListener.colorcode + RCListener.message_notConnectedToTheNetwork);
				return;
			}
			CellTower closestTower = getTower(location);
			double reception = closestTower.getNormalizedReceptionPower(place);
			if (reception <= 0) {
				player.sendMessage(RCListener.colorcode + RCListener.message_notConnectedToTheNetwork);
				return;
			}
			double power = closestTower.getAntennaGain()*reception;
			player.sendMessage(RCListener.colorcode + "["
					+ Minions.powerToString(power) + "] "
					+ "Connected to the phone network");

		}
	}

	// Returns the closest Tower in this world or null if none are found
	public Location findClosestTower(Location locationPlayer) {
		Set<Location> towersRead = getTowerLocations();
		if (towersRead.isEmpty()) {
			return null;
		}
		double distance;
		double minimal = -1;
		Location closestTower = null;
		for (Location location : towersRead) {
			try {
				distance = locationPlayer.distance(location);
				if ((distance < minimal) || (minimal < 0)) {
					minimal = distance;
					closestTower = location;
				}
			} catch (IllegalArgumentException e) {

			}
		}
		return closestTower;
	}

	protected void saveTowers() {
		RCListener.realisticChat.getLogger().info("Saving cell towers to disk in file: " + TOWERS_FILE);
		Set<Location> towersToBeSaved = getTowerLocations();
		writeCellTowersToFile(TOWERS_FILE, towersToBeSaved);
	}

	protected void readTowers() {
		RCListener.realisticChat.getLogger().info("Reading cell towers from disk in file: " + TOWERS_FILE);
		List<Location> towers = readCellTowersFromFile(TOWERS_FILE);
		for (Location location : towers) {
			registerTower(location);
		}
	}

	@Override
	public void run() {
		for (CellTower tower : towers.values()) {
			if (!tower.update()) {
				// tower still valid?
				towers.remove(tower.getLocation());
			}
		}
		if (towers.size() != numberOfTowers) {
			numberOfTowers = towers.size();
			if (RCListener.isWindows) {
				RCListener.realisticChat.getLogger().info(
						"Number of active Cell Towers: " + numberOfTowers);
			} else {
				RCListener.realisticChat.getLogger().info(
						RealisticChatListener.ANSI_YELLOW
								+ "Number of active Cell Towers: "
								+ numberOfTowers
								+ RealisticChatListener.ANSI_RESET);
			}
		}
	}

	private File fromFilename(String filename) {
		return new File(RCListener.realisticChat.getDataFolder().getAbsolutePath() + File.separator + filename);
	}

	private List<Location> readCellTowersFromFile(String filename) {
		File file = fromFilename(filename);

		List<Location> towers = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;

			Location location;
			int x, y, z;

			while ((line = br.readLine()) != null) {
				if (line.charAt(0) == '#') {
					continue;
				}
				String[] split = line.split(",");
				if (split.length < 4) {
					continue;
				}
				World world = Bukkit.getServer().getWorld(split[0]);
				x = Integer.parseInt(split[1]);
				y = Integer.parseInt(split[2]);
				z = Integer.parseInt(split[3]);
				try {
					location = new Location(world, x, y, z);
					towers.add(location);
				} catch (Exception e){
					RCListener.realisticChat.getLogger().warning("couldn't read line <" + line + "> in towers file");
				}

			}
		} catch (FileNotFoundException e) {
			RCListener.realisticChat.getLogger().warning("no input file found!");
		} catch (IOException e) {
			RCListener.realisticChat.getLogger().warning("can't read input file");
		}
		return towers;
	}

	private void writeCellTowersToFile(final String filename,
			final Set<Location> towers) {
		new Thread() {
			@Override
			public void run() {

				File file = fromFilename(filename);
				try {

					// if file doesn't exist, create it
					if (!file.exists()) {
						file.createNewFile();
					}

					FileWriter fw = new FileWriter(file.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					StringBuffer sb;

					bw.write("# contains the location of all radiotowers\n");
					bw.write("# world,X,Y,Z\n");

					for (Location location : towers) {
						sb = new StringBuffer();
						sb.append(location.getWorld().getName());
						sb.append(',');
						sb.append(location.getBlockX());
						sb.append(',');
						sb.append(location.getBlockY());
						sb.append(',');
						sb.append(location.getBlockZ());
						sb.append('\n');
						bw.write(sb.toString());
					}
					bw.close();
				} catch (IOException e) {
					RCListener.realisticChat.getLogger().warning("Can't write towers file! " + e.getMessage());
				}
			}
		}.start();
	}
}
