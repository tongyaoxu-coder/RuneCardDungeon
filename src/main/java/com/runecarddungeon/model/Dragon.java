package com.runecarddungeon.model;

public class Dragon extends Enemy{
	 // Gain 7 shield points per turn
    private static final int SHIELD_PER_TURN = 7;
	private int turnCount =0;
	
	public Dragon() {
		// Initialize the boss (MaxHp: 52, AttackDamage: 13)
		super("Dragon", 52, 13);
		rollIntent();
	}
	
	public Dragon(String name, int maxHp, int attackDamage) {
		super(name, maxHp, attackDamage);
		rollIntent();
	}
	
	// dragon strike atk
	private int getStrikeDamage() {
        return this.getCurrentAttackDamage() + 10;
    }
	
	// Dragon AI behaviors design
	// 5 turns build a cycle.
	@Override
	public void takeTurn(Player target) {
		switch(turnCount) {
		case 0:
			// first turn add block
			this.addBlock(SHIELD_PER_TURN);
			break;
		case 1:
			// second turn attack player
			this.attack(target);
			break;
		case 2:
			// third turn charging
			break;
		case 3:
			// forth turn strike
			target.takeDamage(this.getStrikeDamage());
			break;
		case 4:
			// fifth turn rest after strike
			break;
		}
		turnCount= (turnCount+1)%5;
		rollIntent();	
	}
	
	@Override
	public void rollIntent() {
		switch(turnCount) {
		case 0:
			this.setCurrIntent("Dragon Aura: Shield "+SHIELD_PER_TURN);
			break;
		case 1:
			this.setCurrIntent("Fire ball: Damage "+this.getAttackDamage());
			break;
		case 2:
			this.setCurrIntent("Warning: The dragon is charging up...");//charging
			break;
		case 3:
			this.setCurrIntent("Danger: Cataclysmic Explosion " +this.getStrikeDamage()+ "!");
			break;
		case 4:
			this.setCurrIntent("Weak: The dragon is out of strength");
			break;
		}
	}
}