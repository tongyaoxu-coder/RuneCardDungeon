package com.runecarddungeon.effect;

import com.runecarddungeon.battle.BattleManager;
import com.runecarddungeon.model.Actor;

public class HealEffect implements CardEffect {

    private final int healAmount;

    public HealEffect(int healAmount) {
        this.healAmount = healAmount;
    }

    @Override
    public void apply(
            Actor releaser,
            Actor target,
            BattleManager battleManager) {

        if (releaser != null) {
            releaser.heal(healAmount);
        }
    }
}

public int getHealAmount() {
        return healAmount;
    }
}
