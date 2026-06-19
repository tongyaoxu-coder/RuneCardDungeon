
package com.runecarddungeon.effect;

import com.runecarddungeon.model.Actor;

public class DamageEffect implements CardEffect {
    private int damage;

    public DamageEffect(int damage) {
        this.damage = damage;
    }

    @Override
    public void apply(Actor releaser, Actor target) {
        target.takeDamage(damage);
    }
}
