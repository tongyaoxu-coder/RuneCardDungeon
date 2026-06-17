package com.runecarddungeon.model;

public abstract class Enemy extends Actor {
	private String currIntent;
	
	public Enemy(String name, int maxHp) {
		super(name, maxHp);
	}
	
	public abstract void rollIntent();
	public abstract void takeTurn(Player target);
	public String getCurrentIntent() {return currIntent;}
	public void setCurrIntent(String intent) {this.currIntent=intent;}
   
}
