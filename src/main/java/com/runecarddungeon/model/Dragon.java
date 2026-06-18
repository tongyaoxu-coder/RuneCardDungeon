package com.runecarddungeon.model;

public class Dragon extends Enemy{

	private int turnCount =0;

	public Dragon() {
		super("Red dragon: Lavarax", 130);
		rollIntent();
	}
	
	@Override
	public void takeTurn(Player target) {
		switch(turnCount) {
		case 0:
			this.addBlock(10);
			break;
		case 1:
			target.takeDamage(15);
			break;
		case 2:
			break;//charging
		case 3:
			target.takeDamage(30);
			break;
		}
		turnCount= (turnCount+1)%4;
		rollIntent();	
	}
	
	@Override
	public void rollIntent() {
		switch(turnCount) {
		case 0:
			this.setCurrIntent("Dragon Aura: Shield 10");
			break;
		case 1:
			this.setCurrIntent("Rend: Damage 15");
			break;
		case 2:
			this.setCurrIntent("Warning: The dragon is charging up...");//charging
		case 3:
			this.setCurrIntent("Danger: Cataclysmic Breath 30!");
			break;
		}
	}
}
