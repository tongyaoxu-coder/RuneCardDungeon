package com.runecarddungeon.model;

public class Skeleton extends Enemy {
    // Gain 3 shield points when addBlock() is called
    private static final int SHIELD_PER_TURN = 3;
    // Track turns for alternating behavior (Attack -> Defend)
    private int turnCount = 0;

    public Skeleton() {
        super("Skeleton", 15, 7);
        rollIntent();
    }

    public Skeleton(String name, int maxHp, int attackDamage) {
        super(name, maxHp, attackDamage);
        rollIntent();
    }

    // Skeleton behaviors
    @Override
    public void takeTurn(Player target) {
    	// Skeleton behaviors logic
        // 2 turns build a cycle
        switch(turnCount % 2) {
            case 0:
                // Attack turn
                System.out.println(getName() + " Launch an attack, dealing " + getCurrentAttackDamage() + " damage!");
                attack(target);
                break;
            case 1:
                // Defend turn
                System.out.println(getName() + " Gain " + SHIELD_PER_TURN + " shield points!");
                addBlock(SHIELD_PER_TURN);
                break;
        }
        
        // Proceed to next turn
        turnCount++;
        rollIntent();
    }

    @Override
    public void rollIntent() {
        // Generate intent based on the upcoming turn
        // For ui referring
        switch(turnCount % 2) {
            case 0:
                this.setCurrIntent("Attack: Deals " + getCurrentAttackDamage() + " damage");
                break;
            case 1:
                this.setCurrIntent("Defend: Grants " + SHIELD_PER_TURN + " shield points");
                break;
        }
    }
}
