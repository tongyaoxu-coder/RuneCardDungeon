package com.runecarddungeon.effect;

import com.runecarddungeon.battle.BattleManager;
import com.runecarddungeon.model.Actor;

public class DrawCardEffect implements CardEffect {

    private final int drawAmount;

    public DrawCardEffect(int drawAmount) {
        this.drawAmount = Math.max(0, drawAmount);
    }

    @Override
    public void apply(
            Actor releaser,
            Actor target,
            BattleManager battleManager) {

        if (battleManager != null && drawAmount > 0) {
            battleManager.drawCards(drawAmount);
        }
    }

    public int getDrawAmount() {
        return drawAmount;
    }
}
