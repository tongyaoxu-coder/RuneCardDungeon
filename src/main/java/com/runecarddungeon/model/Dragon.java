package com.runecarddungeon.model;

public class Dragon extends Enemy{

	private int turnCount =0;

	public Dragon() {
		super("Red dragon: Lavarax", 130, 20);
		rollIntent();
	}
	
	@Override
	public void takeTurn(Player target) {
		switch(turnCount) {
		case 0:
			this.addBlock(10);
			break;
		case 1:
			this.attack(target);
			break;
		case 2:
			break;//charging
		case 3:
			target.takeDamage(this.getAttackDamage()*2);
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
			this.setCurrIntent("Rend: Damage "+this.getAttackDamage());
			break;
		case 2:
			this.setCurrIntent("Warning: The dragon is charging up...");//charging
			break;
		case 3:
			this.setCurrIntent("Danger: Cataclysmic Breath " +(this.getAttackDamage()*2)+ "!");
			break;
		}
	}
}
