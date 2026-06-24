package com.runecarddungeon.data;

import com.runecarddungeon.effect.BlockEffect;
import com.runecarddungeon.effect.DamageEffect;
import com.runecarddungeon.effect.DrawCardEffect;
import com.runecarddungeon.effect.HealEffect;
import com.runecarddungeon.effect.WeakenEffect;
import com.runecarddungeon.model.Card;
import com.runecarddungeon.model.Deck;

public final class CardFactory {

    // Card energy costs
    private static final int ATTACK_COST = 3;
    private static final int HEAL_COST = 2;
    private static final int DEFENSE_COST = 3;
    private static final int WEAKEN_COST = 2;

    // Card effect values
    private static final int ATTACK_DAMAGE = 8;
    private static final int UPGRADED_ATTACK_DAMAGE = 11;

    private static final int HEAL_AMOUNT = 8;
    private static final int BLOCK_AMOUNT = 6;

    private static final int WEAKEN_AMOUNT = 6;
    private static final int UPGRADED_WEAKEN_AMOUNT = 10;

    // Card image paths
    private static final String ATTACK_IMAGE =
            "assets/cards/attack.png";

    private static final String HEAL_IMAGE =
            "assets/cards/heal.png";

    private static final String DEFENSE_IMAGE =
            "assets/cards/shield.png";

    private static final String WEAKEN_IMAGE =
            "assets/cards/curse.png";

    private CardFactory() {
        // Utility class
    }

    // =========================================================
    // Card creation
    // =========================================================

    public static Card createAttackCard() {
        return new Card(
                "Attack",
                ATTACK_COST,
                new DamageEffect(ATTACK_DAMAGE),
                "Deal " + ATTACK_DAMAGE + " damage.",
                ATTACK_IMAGE
        );
    }

    public static Card createAttackCardUpgraded() {
        return new Card(
                "Attack+",
                ATTACK_COST,
                new DamageEffect(UPGRADED_ATTACK_DAMAGE),
                "Deal " + UPGRADED_ATTACK_DAMAGE + " damage.",
                ATTACK_IMAGE
        );
    }

    public static Card createHealCard() {
        return new Card(
                "Heal",
                HEAL_COST,
                new HealEffect(HEAL_AMOUNT),
                "Restore " + HEAL_AMOUNT + " HP.",
                HEAL_IMAGE
        );
    }

    public static Card createDefenseCard() {
        return new Card(
                "Shield",
                DEFENSE_COST,
                new BlockEffect(BLOCK_AMOUNT),
                "Gain " + BLOCK_AMOUNT + " block.",
                DEFENSE_IMAGE
        );
    }

    public static Card createWeakenCard() {
        return new Card(
                "Weaken",
                WEAKEN_COST,
                new WeakenEffect(WEAKEN_AMOUNT),
                "Reduce the enemy's attack by "
                        + WEAKEN_AMOUNT
                        + " this turn.",
                WEAKEN_IMAGE
        );
    }

    public static Card createWeakenCardUpgraded() {
        return new Card(
                "Weaken+",
                WEAKEN_COST,
                new WeakenEffect(UPGRADED_WEAKEN_AMOUNT),
                "Reduce the enemy's attack by "
                        + UPGRADED_WEAKEN_AMOUNT
                        + " this turn.",
                WEAKEN_IMAGE
        );
    }

    /**
     * This card is currently not included in the official level decks.
     * It is kept for compatibility and polymorphism demonstration.
     */
    public static Card createDrawCard() {
        return new Card(
                "Insight",
                0,
                new DrawCardEffect(2),
                "Draw 2 cards."
        );
    }

    // =========================================================
    // Starter deck
    // =========================================================

    /**
     * Level 1 deck:
     * Attack x1
     * Heal x1
     */
    public static Deck createStarterDeck() {
        Deck deck = new Deck();

        deck.addCard(createAttackCard());
        deck.addCard(createHealCard());

        deck.shuffle();
        return deck;
    }

    // =========================================================
    // Level deck creation
    // =========================================================

    public static Deck createDeckForLevel(int levelNumber) {

        UpgradeManager upgradeManager =
                UpgradeManager.getInstance();

        Deck deck = new Deck();

        switch (levelNumber) {

            case 1:
                // Attack x1, Heal x1
                deck.addCard(createAttackCard());
                deck.addCard(createHealCard());
                break;

            case 2:
                // Upgrade-only level
                return deck;

            case 3:
                // Attack x2, Heal x2, Shield x2
                for (int i = 0; i < 2; i++) {

                    if (upgradeManager.isAttackUpgraded()) {
                        deck.addCard(createAttackCardUpgraded());
                    } else {
                        deck.addCard(createAttackCard());
                    }

                    deck.addCard(createHealCard());
                    deck.addCard(createDefenseCard());
                }
                break;

            case 4:
                // Upgrade-only level
                return deck;

            case 5:
                // Attack x2, Heal x2, Shield x2, Weaken x2
                for (int i = 0; i < 2; i++) {

                    if (upgradeManager.isAttackUpgraded()) {
                        deck.addCard(createAttackCardUpgraded());
                    } else {
                        deck.addCard(createAttackCard());
                    }

                    deck.addCard(createHealCard());
                    deck.addCard(createDefenseCard());

                    if (upgradeManager.isWeakenUpgraded()) {
                        deck.addCard(createWeakenCardUpgraded());
                    } else {
                        deck.addCard(createWeakenCard());
                    }
                }
                break;

            default:
                System.out.println(
                        "Unknown level number: "
                                + levelNumber
                                + ". Using the starter deck."
                );

                return createStarterDeck();
        }

        deck.shuffle();
        return deck;
    }
}
