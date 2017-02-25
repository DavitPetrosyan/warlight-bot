/**
 * Warlight AI Game Bot
 *
 * Last update: April 02, 2014
 *
 * @author Jim van Eeden
 * @version 1.0
 * @License MIT License (http://opensource.org/Licenses/MIT)
 */

package com.synisis.ai.bot;

/**
 * This is a simple bot that does random (but correct) moves.
 * This class implements the Bot interface and overrides its Move methods.
 *  You can implements these methods yourself very easily now,
 * since you can retrieve all information about the match from variable “state”.
 * When the bot decided on the move to make, it returns an ArrayList of Moves. 
 * The bot is started by creating a Parser to which you add
 * a new instance of your bot, and then the parser is started.
 */

import com.synisis.ai.main.Region;
import com.synisis.ai.move.AttackTransferMove;
import com.synisis.ai.move.PlaceArmiesMove;

import java.util.*;


public class BotStarter implements Bot
{
	@Override
	/**
	 * A method used at the start of the game to decide which player start with what Regions. 6 Regions are required to be returned.
	 * This example randomly picks 6 regions from the pickable starting Regions given by the engine.
	 * @return : a list of m (m=6) Regions starting with the most preferred Region and ending with the least preferred Region to start with 
	 */
	public Region getPreferredStartingRegions(BotState state, Long timeOut)
	{
		ArrayList<Region> pickableStartingRegions = state.getPickableStartingRegions();
		pickableStartingRegions.sort(Region.COMPARE_BY_SUPERREGION_SIZE);
		if(pickableStartingRegions.size() > 1) {
			int firstRegionSize = pickableStartingRegions.get(0).getSuperRegion().getSubRegions().size();
			int secondRegionSize = pickableStartingRegions.get(1).getSuperRegion().getSubRegions().size();
			if (firstRegionSize == secondRegionSize) {
				ArrayList<Region> pickableStartingRegionsForNewCriteria = new ArrayList<>();

				pickableStartingRegionsForNewCriteria.add(pickableStartingRegions.get(0));
				pickableStartingRegionsForNewCriteria.add(pickableStartingRegions.get(1));
				if(pickableStartingRegions.size() > 2 &&
						secondRegionSize == pickableStartingRegions.get(2).getSuperRegion().getSubRegions().size()){
					pickableStartingRegionsForNewCriteria.add(pickableStartingRegions.get(2));
				}

				pickableStartingRegionsForNewCriteria.sort(Region.COMPARE_BY_NOT_BAD_REGIONS);

				if(pickableStartingRegionsForNewCriteria.get(0).getSuperRegion().isBadRegion() == pickableStartingRegionsForNewCriteria.get(1).getSuperRegion().isBadRegion()) {
					pickableStartingRegionsForNewCriteria.sort(Region.COMPARE_BY_NEIGHBORS_INSIDE_SUPER_REGION);
					return pickableStartingRegionsForNewCriteria.get(0);
				} else {
					return pickableStartingRegionsForNewCriteria.get(0);
				}
			} else {
				return pickableStartingRegions.get(0);
			}
		}
		return pickableStartingRegions.get(0);
	}

	@Override
	/**
	 * This method is called for at first part of each round. This example puts two armies on random regions
	 * until he has no more armies left to place.
	 * @return The list of PlaceArmiesMoves for one round
	 */
	public ArrayList<PlaceArmiesMove> getPlaceArmiesMoves(BotState state, Long timeOut)
	{
		//TODO
		//implement deploys

		ArrayList<PlaceArmiesMove> placeArmiesMoves = new ArrayList<PlaceArmiesMove>();
		String myName = state.getMyPlayerName();
		int armies = 2;
		int armiesLeft = state.getStartingArmies();
		LinkedList<Region> visibleRegions = state.getVisibleMap().getRegions();
		
		while(armiesLeft > 0)
		{
			double rand = Math.random();
			int r = (int) (rand*visibleRegions.size());
			Region region = visibleRegions.get(r);
			
			if(region.ownedByPlayer(myName))
			{
				placeArmiesMoves.add(new PlaceArmiesMove(myName, region, armies));
				armiesLeft -= armies;
			}
		}
		
		return placeArmiesMoves;
	}

	@Override
	/**
	 * This method is called for at the second part of each round. This example attacks if a region has
	 * more than 6 armies on it, and transfers if it has less than 6 and a neighboring owned region.
	 * @return The list of PlaceArmiesMoves for one round
	 */
	public ArrayList<AttackTransferMove> getAttackTransferMoves(BotState state, Long timeOut)
	{

		//TODO
		//implement attack/Transfere

		ArrayList<AttackTransferMove> attackTransferMoves = new ArrayList<AttackTransferMove>();
		String myName = state.getMyPlayerName();
		int armies = 5;
//		state.getStartingArmies();
		for(Region fromRegion : state.getVisibleMap().getRegions())
		{
			if(fromRegion.ownedByPlayer(myName)) //do an attack
			{
				ArrayList<Region> possibleToRegions = new ArrayList<Region>();
				possibleToRegions.addAll(fromRegion.getNeighbors());
				
				while(!possibleToRegions.isEmpty())
				{
					double rand = Math.random();
					int r = (int) (rand*possibleToRegions.size());
					Region toRegion = possibleToRegions.get(r);
					
					if(!toRegion.getPlayerName().equals(myName) && fromRegion.getArmies() > 6) //do an attack
					{
						attackTransferMoves.add(new AttackTransferMove(myName, fromRegion, toRegion, armies));
						break;
					}
					else if(toRegion.getPlayerName().equals(myName) && fromRegion.getArmies() > 1) //do a transfer
					{
						attackTransferMoves.add(new AttackTransferMove(myName, fromRegion, toRegion, armies));
						break;
					}
					else
						possibleToRegions.remove(toRegion);
				}
			}
		}
		
		return attackTransferMoves;
	}

	public void start(String[] args)
	{
		BotParser parser = new BotParser(new BotStarter());
		parser.run();
	}

}
