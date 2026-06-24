package com.runecarddungeon.data;

import com.runecarddungeon.model.Player;

public class PlayerFactory {
	// declare player type.
	// support future enriching player types
	public enum PlayerType{
		Knight
	}

	public static Player createPlayer(PlayerType type) {
		Player player =null;
		
		switch(type) {
		case Knight:
			// Knight initial data
			player = new Player("Knight",57,5); 
			break;
		default:
			throw new IllegalArgumentException("Unkown Player + "+type);
		}
	
		return player;
	}
	
}
