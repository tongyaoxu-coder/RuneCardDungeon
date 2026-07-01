package com.runecarddungeon.effect;

import com.runecarddungeon.battle.BattleManager;
import com.runecarddungeon.model.Actor;

public class DamageEffect implements CardEffect {

    private final int damage;

    public DamageEffect(int damage) {
        // Make sure damage is not negative
        this.damage = Math.max(0, damage);
    }

    @Override
    public void apply(
            Actor releaser,
            Actor target,
            BattleManager battleManager) {

        if (target != null) {
            target.takeDamage(damage);
        }
    }

    public int getDamage() {
        return damage;
    }
}
