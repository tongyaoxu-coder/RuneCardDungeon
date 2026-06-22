package com.runecarddungeon.model;

public class Slime extends Enemy {

    public Slime(String name, int maxHp) {
        super(name, maxHp, 8);
    }

    @Override
    public void rollIntent() {
        setCurrIntent("Prepare to Attack");
    }

    @Override
    public void takeTurn(Player target) {
        attack(target);
    }
}
