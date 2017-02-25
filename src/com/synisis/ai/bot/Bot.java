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

import com.synisis.ai.main.Region;
import com.synisis.ai.move.AttackTransferMove;
import com.synisis.ai.move.PlaceArmiesMove;

import java.util.ArrayList;


public interface Bot {
	
	public Region getPreferredStartingRegions(BotState state, Long timeOut);
	
	public ArrayList<PlaceArmiesMove> getPlaceArmiesMoves(BotState state, Long timeOut);
	
	public ArrayList<AttackTransferMove> getAttackTransferMoves(BotState state, Long timeOut);

}
