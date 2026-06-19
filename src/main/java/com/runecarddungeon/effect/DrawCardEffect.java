package com.runecarddungeon.effect;

import com.runecarddungeon.battle.BattleManager;
import com.runecarddungeon.model.Actor;

public class DrawCardEffect implements CardEffect {

    private final int drawAmount;

    public DrawCardEffect(int drawAmount) {
        this.drawAmount = drawAmount;
    }

    @Override
    public void apply(
            Actor releaser,
            Actor target,
            BattleManager battleManager) {

        if (battleManager != null) {
            battleManager.drawCards(drawAmount);
        }
    }
}
