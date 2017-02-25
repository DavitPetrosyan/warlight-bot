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
import java.util.Comparator;
import java.util.LinkedList;

public class SuperRegion {
	
	private int id;
	private int armiesReward;
	private boolean isBadRegion;
	private LinkedList<Region> subRegions;
	
	public SuperRegion(int id, int armiesReward, boolean isBadRegion)
	{
		this.id = id;
		this.armiesReward = armiesReward;
		this.isBadRegion = isBadRegion;
		subRegions = new LinkedList<Region>();
	}
	
	public void addSubRegion(Region subRegion)
	{
		if(!subRegions.contains(subRegion))
			subRegions.add(subRegion);
	}
	
	/**
	 * @return A string with the name of the player that fully owns this SuperRegion
	 */
	public String ownedByPlayer()
	{
		String playerName = subRegions.getFirst().getPlayerName();
		for(Region region : subRegions)
		{
			if (!playerName.equals(region.getPlayerName()))
				return null;
		}
		return playerName;
	}
	
	/**
	 * @return The id of this SuperRegion
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return The number of armies a Player is rewarded when he fully owns this SuperRegion
	 */
	public int getArmiesReward() {
		return armiesReward;
	}

	public boolean isBadRegion() {
		return isBadRegion;
	}

	public void setBadRegion(boolean badRegion) {
		isBadRegion = badRegion;
	}

	/**
	 * @return A list with the Regions that are part of this SuperRegion
	 */
	public LinkedList<Region> getSubRegions() {
		return subRegions;
	}
}
