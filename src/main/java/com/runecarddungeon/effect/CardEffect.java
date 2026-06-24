package com.runecarddungeon.effect;

import com.runecarddungeon.battle.BattleManager;
import com.runecarddungeon.model.Actor;

public interface CardEffect {

    /**
     * Applies this card effect.
     *
     * @param releaser      the actor who plays the card
     * @param target        the target affected by the card
     * @param battleManager the current battle manager
     */
    void apply(
            Actor releaser,
            Actor target,
            BattleManager battleManager
    );
}
