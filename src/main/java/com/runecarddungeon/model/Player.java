package com.runecarddungeon.model;

public class Player extends Actor {
	private int energy;
	private int maxEnergy;
    private Deck deck;
	
	public Player(String name, int maxHp, int maxEnergy) {
		super(name, maxHp);//inherit from base class
		this.maxEnergy=maxEnergy;
		this.energy=maxEnergy;
		this.deck=new Deck();
	}
	
	public int getEnergy() {return energy;}
	//energy cant be negative
	public boolean spendEnergy(int cost) {
		if(this.energy>=cost) {
			this.energy-=cost;
			return true;
		}
		System.out.println("Not enough energy. Present energy: "+this.energy+" need: "+cost);
		
		return false;
	}
	
	public void resetEnergy() {
		this.energy=this.maxEnergy;
	}
	
	public Deck getDeck() {
		return this.deck;
	}
    
}
