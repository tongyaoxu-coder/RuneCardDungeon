package com.runecarddungeon.model;

public class Card {// base class
	private String name;
	private int energyCost;
	private Effect effect;
	private String description;
	
	public Card(String name, int energyCost, Effect effect, String description) {
		this.name=name;
		this.energyCost=energyCost;
		this.effect=effect;
		this.description=description;
	}
	
	//card releaser: player
	//card target: enemy
	public void play(Actor releaser, Actor target) {
		if(effect!=null) {
			effect.apply(releaser, target);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public int getEnergyCost() {
		return energyCost;
	}
	
	public Effect getEffect() {
		return effect;
	}
	
	public String getDescription() {
		return description;
	}
}
