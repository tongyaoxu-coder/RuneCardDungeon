package com.runecarddungeon.effect;

import com.runecarddungeon.model.Actor;

public interface CardEffect {
    void apply(Actor releaser, Actor target);
}
