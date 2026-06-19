package com.runecarddungeon.model;

import com.runecarddungeon.effect.CardEffect;

public class Card {
    private String name;
    private int energyCost;
    private CardEffect effect;
    private String description;

    public Card(String name, int energyCost, CardEffect effect, String description) {
        this.name = name;
        this.energyCost = energyCost;
        this.effect = effect;
        this.description = description;
    }

    // Player plays a card on a target.
    // Return true if the card is successfully played.
    public boolean play(Player player, Actor target) {
        if (player.getEnergy() < energyCost) {
            return false;
        }

        player.useEnergy(energyCost);

        if (effect != null) {
            effect.apply(player, target);
        }

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
