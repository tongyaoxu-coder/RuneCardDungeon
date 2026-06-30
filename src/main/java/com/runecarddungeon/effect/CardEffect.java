package com.runecarddungeon.effect;

import com.runecarddungeon.battle.BattleManager;
import com.runecarddungeon.model.Actor;

public interface CardEffect {

    // Apply the card effect to the target
    void apply(
            Actor releaser,
            Actor target,
            BattleManager battleManager
    );
}
