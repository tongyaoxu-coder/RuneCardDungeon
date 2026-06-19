package com.runecarddungeon.model;

public class Slime extends Enemy{
	
	public Slime() {
		super("Small Slime", 30, 5);
		rollIntent();
	}
	
	@Override
	public void takeTurn(Player target) {
		this.attack(target);
		rollIntent();//update attack intent after a round
	}
	
	@Override
	public void rollIntent() {
		
		this.setCurrIntent("Attack ready: hit "+this.getAttackDamage());
	}

}
