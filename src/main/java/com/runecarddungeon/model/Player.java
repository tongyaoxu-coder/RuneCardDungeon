package com.runecarddungeon.model;

public class Player extends Actor {
	private int energy;
	private int maxEnergy;
	
	public Player(String name, int maxHp, int maxEnergy) {
		super(name, maxHp);//inherit from base class
		this.maxEnergy=maxEnergy;
		this.energy=maxEnergy;
	}
	
	public void resetEnergy() {
		this.energy=maxEnergy;
	}
	
	public int getEnergy() {return energy;}
	//energy cant be negative
	public void useEnergy(int amount) {this.energy=Math.max(0, this.energy-amount);}
    
}
