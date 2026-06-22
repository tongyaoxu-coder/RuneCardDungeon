package com.runecarddungeon.model;

import com.runecarddungeon.battle.BattleManager;
import com.runecarddungeon.effect.CardEffect;

public class Card {

    private final String name;
    private final int energyCost;
    private final CardEffect effect;
    private final String description;

    public Card(
            String name,
            int energyCost,
            CardEffect effect,
            String description) {

        this.name = name;
        this.energyCost = Math.max(0, energyCost);
        this.effect = effect;
        this.description = description;
    }

    /*Attempts to play the card.
     * @return true when the player has enough energy and the card is successfully played
     */
    public boolean play(
            Player player,
            Actor target,
            BattleManager battleManager) {

        if (player == null || effect == null || battleManager == null) {
            return false;
        }

        if (!player.spendEnergy(energyCost)) {
            return false;
        }

        effect.apply(player, target, battleManager);
        return true;
    }

    public String getName() {
        return name;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public CardEffect getEffect() {
        return effect;
    }

    public String getDescription() {
        return description;
    }
}
