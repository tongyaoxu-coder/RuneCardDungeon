package com.runecarddungeon.model;

public class Slime extends Enemy{
	private boolean isAttackNext= true;
	
	public Slime() {
		super("Small Slime", 30);
		rollIntent();
	}
	
	@Override
	public void takeTurn(Player target) {
		if(isAttackNext) {
			target.takeDamage(6);//Slime attack
		}else {
			this.addBlock(3);
		}
		rollIntent();//update attack intent after a round
	}
	
	@Override
	public void rollIntent() {
		isAttackNext= !isAttackNext;
		this.setCurrIntent(isAttackNext ? "Attack ready: hit 6":"Block ready: block 3");
	}

}
