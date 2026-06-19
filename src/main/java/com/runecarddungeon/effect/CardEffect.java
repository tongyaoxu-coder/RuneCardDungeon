package com.runecarddungeon.effect;

import com.runecarddungeon.battle.BattleManager;
import com.runecarddungeon.model.Actor;

public interface CardEffect {

    /**
     * Applies the card effect.
     *
     * @param releaser     actor who plays the card
     * @param target       target of the card
     * @param battleManager current battle manager
     */
    void apply(Actor releaser, Actor target, BattleManager battleManager);
}
