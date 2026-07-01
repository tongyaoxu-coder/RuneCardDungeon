package com.runecarddungeon.model;

import com.runecarddungeon.battle.BattleManager;
import com.runecarddungeon.effect.CardEffect;

import java.util.Collections;
import java.util.List;

public class Card {

    private final String name;
    private final int energyCost;
    private final CardEffect effect;
    private final String description;
    private final String imagePath;

    // Use an empty image path for older cards
    public Card(
            String name,
        // Every cards need to spend certain amount of energy to play
            int energyCost,
            CardEffect effect,
            String description) {

        this(
                name,
                energyCost,
                effect,
                description,
                ""
        );
    }

    public Card(
            String name,
            int energyCost,
            CardEffect effect,
            String description,
            String imagePath) {

        this.name = name == null ? "" : name;
        this.energyCost = Math.max(0, energyCost);
        this.effect = effect;
        this.description =
                description == null ? "" : description;
        this.imagePath =
                imagePath == null ? "" : imagePath;
    }

    public boolean play(
            Player player,
            Actor target,
            BattleManager battleManager) {

        // Check if there is a player( to play the card)
        // The card have effect to play
        // Battle rules manager exists
        if (player == null || effect == null || battleManager == null) {
            return false;
        }
        // Check if the player have enough energy to play the card
        // If not, fail to play the card -> return false
        if (!player.spendEnergy(energyCost)) {
            return false;
        }

        effect.apply(
                player,
                target,
                battleManager
        );
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

    // GameManager uses a list of effects
    public List<CardEffect> getEffects() {
        if (effect == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(effect);
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public String toString() {
        return name + " [Cost: " + energyCost + ", Effect: " + description + "]";
    }
}
