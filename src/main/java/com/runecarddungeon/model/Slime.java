package com.runecarddungeon.model;

public class Slime extends Enemy {

    public Slime(String name, int maxHp, int attackDamage) {
        super(name, maxHp, attackDamage);
        rollIntent();
    }

    @Override
    public void takeTurn(Player target) {
        System.out.println("👾 " + getName() + " Attack: Deals " + getAttackDamage() + " damage！");
        attack(target);
        rollIntent();
    }

    @Override
    public void rollIntent() {
        this.setCurrIntent("👾 Attack: Deals " + getAttackDamage() + " damage");
    }
}
