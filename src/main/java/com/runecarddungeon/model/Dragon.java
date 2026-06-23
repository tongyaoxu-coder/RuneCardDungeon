package com.runecarddungeon.model;

public class Dragon extends Enemy {
    private static final int SHIELD_PER_TURN = 5;

    public Dragon() {
        super("Dragon", 72, 26);
        rollIntent();
    }

    public Dragon(String name, int maxHp, int attackDamage) {
        super(name, maxHp, attackDamage);
        rollIntent();
    }

    @Override
    public void onTurnStart() {
        // 龙鳞护甲：每回合获得5点护盾
        addBlock(SHIELD_PER_TURN);
        System.out.println(getName() + " Dragon Scale Armor activated！gain " + SHIELD_PER_TURN + " shield points！");
    }

    @Override
    public void takeTurn(Player target) {
        System.out.println(getName() + " Unleash a fiery breath! Deals  " + getAttackDamage() + " damage！");
        attack(target);
        rollIntent();
    }

    @Override
    public void rollIntent() {
        this.setCurrIntent("Fiery Breath: Deals " + getAttackDamage() + " damage, gain " + SHIELD_PER_TURN + " hield points");
    }
}
