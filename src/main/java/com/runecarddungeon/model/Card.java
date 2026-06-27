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

    /**
     * Backward-compatible constructor for cards without an image path.
     */
    public Card(
            String name,
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

    /**
     * Creates a card with an image path.
     */
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

    /**
     * Attempts to play this card.
     *
     * @param player        the player using the card
     * @param target        the card target
     * @param battleManager the current battle manager
     * @return true if the card was successfully played
     */
    public boolean play(
            Player player,
            Actor target,
            BattleManager battleManager) {

        if (player == null
                || effect == null
                || battleManager == null) {

            return false;
        }

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

    /**
     * Used by BattleManager and the current single-effect card system.
     */
    public CardEffect getEffect() {
        return effect;
    }

    /**
     * Compatibility method for GameManager.
     *
     * Each current card has exactly one effect, so this method returns
     * a read-only list containing that effect.
     */
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
        return name
                + " [Cost: "
                + energyCost
                + ", Effect: "
                + description
                + "]";
    }
}
