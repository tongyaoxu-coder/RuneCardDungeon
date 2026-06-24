package com.runecarddungeon.model;

import com.runecarddungeon.battle.BattleManager;
import com.runecarddungeon.effect.CardEffect;

public class Card {

    private final String name;
    private final int energyCost;
    private final CardEffect effect;
    private final String description;
    private final String imagePath;

    /**
     * Backward-compatible constructor.
     */
    public Card(
            String name,
            int energyCost,
            CardEffect effect,
            String description) {

        this(name, energyCost, effect, description, "");
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
        this.description = description == null ? "" : description;
        this.imagePath = imagePath == null ? "" : imagePath;
    }

    /**
     * Attempts to play this card.
     *
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
