package com.runecarddungeon.effect;

import com.runecarddungeon.battle.BattleManager;
import com.runecarddungeon.model.Actor;

public class WeakenEffect implements CardEffect {

    private final int reductionAmount;

    public WeakenEffect(int reductionAmount) {
        this.reductionAmount = Math.max(0, reductionAmount);
    }

    @Override
    public void apply(
            Actor releaser,
            Actor target,
            BattleManager battleManager) {

        if (target != null) {
            target.reduceAttack(reductionAmount);
        }
    }

    public int getReductionAmount() {
        return reductionAmount;
    }
}
