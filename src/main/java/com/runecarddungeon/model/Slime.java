package com.runecarddungeon.model;

public class Slime extends Enemy {

    public Slime() {
        // name: Slime, maxHp: 8, attack power: 8
        super("Slime", 8, 8);
        rollIntent();
    }

    public Slime(String name, int maxHp, int attackDamage) {
        super(name, maxHp, attackDamage);
        rollIntent();
    }

    // Slime behavior
    // Slime attack the player every turns, dont have complex intent
    @Override
    public void takeTurn(Player target) {
        System.out.println(getName() + " Attack: Deals " + getAttackDamage() + " damage！");
        attack(target);
        rollIntent();
    }

    @Override
    public void rollIntent() {
        this.setCurrIntent("Attack: Deals " + getAttackDamage() + " damage");
    }
}
