package com.runecarddungeon.data;

import com.runecarddungeon.model.Enemy;
import com.runecarddungeon.model.Slime;
import com.runecarddungeon.model.Skeleton;
import com.runecarddungeon.model.Dragon;

// Create enemies based on the selected type
public class EnemyFactory{
	
	public enum EnemyType{
		SLIME,
		SKELETON,
		DRAGON
	}
	
	public static Enemy createEnemy(EnemyType type) {
		Enemy enemy=null;
		
		switch (type) {
		case SLIME:
			enemy = new Slime();
			break;
		case SKELETON:
			enemy= new Skeleton();
			break;
		case DRAGON:
			enemy = new Dragon();
			break;
		default:
			throw new IllegalArgumentException("Unkown Enemy + "+type);
		}
		
		// Set the default values for the enemy
		enemy.initial();
		
		return enemy;
	}
	
}
