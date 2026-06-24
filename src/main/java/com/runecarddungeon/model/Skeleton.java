package com.runecarddungeon.model;

public class Skeleton extends Enemy {
    private static final int SHIELD_PER_TURN = 4;

    public Skeleton() {
    super("Skeleton", 20, 12);
    rollIntent();
}

    public Skeleton(String name, int maxHp, int attackDamage) {
        super(name, maxHp, attackDamage);
        rollIntent();
    }

    @Override
    public void onTurnStart() {
        // Gain 4 shield points per turn
        addBlock(SHIELD_PER_TURN);
        System.out.println(getName() + " Gain  " + SHIELD_PER_TURN + " shield points！");
    }

    @Override
    public void takeTurn(Player target) {
        //Fixed Attack per Turn
        System.out.println(getName() + " Launch an attack, dealing " + getAttackDamage() + " damage！");
        attack(target);
        rollIntent();
    }

    @Override
    public void rollIntent() {
        this.setCurrIntent("Attack: Deals " + getAttackDamage() + " damage and grants  " + SHIELD_PER_TURN + " shield points");
    }
}
