/**
 * Warlight AI Game Bot
 *
 * Last update: April 02, 2014
 *
 * @author Jim van Eeden
 * @version 1.0
 * @License MIT License (http://opensource.org/Licenses/MIT)
 */

package com.synisis.ai.main;

import java.util.*;
import java.util.stream.Collectors;


public class Region {
	
	private int id;
	private LinkedList<Region> neighbors;
	private SuperRegion superRegion;
	private int armies;
	private boolean isWasteLand;
	private String playerName;
	
	public Region(int id, SuperRegion superRegion)
	{
		this.id = id;
		this.superRegion = superRegion;
		this.neighbors = new LinkedList<Region>();
		this.playerName = "unknown";
		this.armies = 0;
		this.isWasteLand = false;

		superRegion.addSubRegion(this);
	}
	
	public Region(int id, SuperRegion superRegion, String playerName, int armies, boolean isWasteLand)
	{
		this.id = id;
		this.superRegion = superRegion;
		this.neighbors = new LinkedList<Region>();
		this.playerName = playerName;
		this.armies = armies;
		this.isWasteLand = isWasteLand;

		superRegion.addSubRegion(this);
	}

	public List<Region> getNeighborsWithinSameSuperRegion() {
		ArrayList out = new ArrayList();
		Iterator neighborRegionIterator = this.getNeighbors().iterator();

		while(neighborRegionIterator.hasNext()) {
			Region neighbor = (Region)neighborRegionIterator.next();
			if(neighbor.getSuperRegion().equals(this.getSuperRegion())) {
				out.add(neighbor);
			}
		}

		return out;
	}

	public void addNeighbor(Region neighbor)
	{
		if(!neighbors.contains(neighbor))
		{
			neighbors.add(neighbor);
			neighbor.addNeighbor(this);
		}
	}
	
	/**
	 * @param region a Region object
	 * @return True if this Region is a neighbor of given Region, false otherwise
	 */
	public boolean isNeighbor(Region region)
	{
		if(neighbors.contains(region))
			return true;
		return false;
	}

	/**
	 * @param playerName A string with a player's name
	 * @return True if this region is owned by given playerName, false otherwise
	 */
	public boolean ownedByPlayer(String playerName)
	{
		if(playerName.equals(this.playerName))
			return true;
		return false;
	}
	
	/**
	 * @param armies Sets the number of armies that are on this Region
	 */
	public void setArmies(int armies) {
		this.armies = armies;
	}
	
	/**
	 * @param playerName Sets the Name of the player that this Region belongs to
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	/**
	 * @return The id of this Region
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return A list of this Region's neighboring Regions
	 */
	public LinkedList<Region> getNeighbors() {
		return neighbors;
	}
	
	/**
	 * @return The SuperRegion this Region is part of
	 */
	public SuperRegion getSuperRegion() {
		return superRegion;
	}
	
	/**
	 * @return The number of armies on this region
	 */
	public int getArmies() {
		return armies;
	}
	
	/**
	 * @return A string with the name of the player that owns this region
	 */
	public String getPlayerName() {
			return playerName;
	}


	public boolean isWasteLand() {
		return isWasteLand;
	}

	public void setWasteLand(boolean wasteLand) {
		isWasteLand = wasteLand;
	}

	public boolean areRegionsNearToEachOther(Region first, Region second) {

		return false;
	}


	public static Comparator<Region> COMPARE_BY_NEIGHBORS_INSIDE_SUPER_REGION = new Comparator<Region>() {
		public int compare(Region first, Region second) {

			List<Region> firstRegionNeighbors = first.getNeighbors()
					.stream()
					.filter(region -> region.getSuperRegion().getId() == first.getSuperRegion().getId()).collect(Collectors.toList());

			List<Region> secondRegionNeighbors = second.getNeighbors()
					.stream()
					.filter(region -> region.getSuperRegion().getId() == second.getSuperRegion().getId()).collect(Collectors.toList());

			return firstRegionNeighbors.size() - secondRegionNeighbors.size();
		}
	};


	public static Comparator<Region> COMPARE_BY_SUPERREGION_ARMIES_REWARD = new Comparator<Region>() {
		public int compare(Region first, Region second) {
			return first.getSuperRegion().getArmiesReward() - second.getSuperRegion().getArmiesReward();
		}
	};


	public static Comparator<Region> COMPARE_BY_SUPERREGION_SIZE = new Comparator<Region>() {
		public int compare(Region first, Region second) {
			return first.getSuperRegion().getSubRegions().size() - second.getSuperRegion().getSubRegions().size();
		}
	};

	public static Comparator<Region> COMPARE_BY_NOT_BAD_REGIONS = new Comparator<Region>() {
		public int compare(Region first, Region second) {
			if(first.getSuperRegion().isBadRegion() && !second.getSuperRegion().isBadRegion()) {
				return 1;
			}
			if(!first.getSuperRegion().isBadRegion() && second.getSuperRegion().isBadRegion()){
				return -1;
			}
			return 0;
		}
	};

	public static Comparator<Region> COMPARE_BY_ARMIES_REWARD = new Comparator<Region>() {
		public int compare(Region first, Region second) {
			return first.getSuperRegion().getArmiesReward() - second.getSuperRegion().getArmiesReward();
		}
	};
}
