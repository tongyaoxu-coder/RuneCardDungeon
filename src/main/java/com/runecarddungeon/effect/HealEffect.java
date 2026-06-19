package com.runecarddungeon.effect;

import com.runecarddungeon.model.Actor;

public class HealEffect implements CardEffect {
    private int healAmount;

    public HealEffect(int healAmount) {
        this.healAmount = healAmount;
    }

    @Override
    public void apply(Actor releaser, Actor target) {
        releaser.heal(healAmount);
    }
}
