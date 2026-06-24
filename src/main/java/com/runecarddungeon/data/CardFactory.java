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
    private static final int SHIELD_COST = 3;
    private static final int WEAKEN_COST = 2;

    // Base card values
    private static final int ATTACK_DAMAGE = 8;
    private static final int HEAL_AMOUNT = 8;
    private static final int SHIELD_AMOUNT = 6;
    private static final int WEAKEN_AMOUNT = 6;

    // Upgraded card values
    private static final int UPGRADED_ATTACK_DAMAGE = 11;
    private static final int UPGRADED_WEAKEN_AMOUNT = 10;

    // Card image paths
    private static final String ATTACK_IMAGE_PATH =
            "assets/cards/attack.png";

    private static final String HEAL_IMAGE_PATH =
            "assets/cards/heal.png";

    private static final String SHIELD_IMAGE_PATH =
            "assets/cards/shield.png";

    private static final String WEAKEN_IMAGE_PATH =
            "assets/cards/curse.png";

    private CardFactory() {
        // Utility class
    }

    // =========================================================
    // Individual card creation
    // =========================================================

    public static Card createAttackCard() {
        return new Card(
                "Attack",
                ATTACK_COST,
                new DamageEffect(ATTACK_DAMAGE),
                "Deal " + ATTACK_DAMAGE + " damage.",
                ATTACK_IMAGE_PATH
        );
    }

    public static Card createAttackCardUpgraded() {
        return new Card(
                "Attack+",
                ATTACK_COST,
                new DamageEffect(UPGRADED_ATTACK_DAMAGE),
                "Deal " + UPGRADED_ATTACK_DAMAGE + " damage.",
                ATTACK_IMAGE_PATH
        );
    }

    public static Card createHealCard() {
        return new Card(
                "Heal",
                HEAL_COST,
                new HealEffect(HEAL_AMOUNT),
                "Restore " + HEAL_AMOUNT + " HP.",
                HEAL_IMAGE_PATH
        );
    }

    public static Card createDefenseCard() {
        return new Card(
                "Shield",
                SHIELD_COST,
                new BlockEffect(SHIELD_AMOUNT),
                "Gain " + SHIELD_AMOUNT + " block.",
                SHIELD_IMAGE_PATH
        );
    }

    /**
     * Alias for code that refers to the card as a shield card.
     */
    public static Card createShieldCard() {
        return createDefenseCard();
    }

    public static Card createWeakenCard() {
        return new Card(
                "Weaken",
                WEAKEN_COST,
                new WeakenEffect(WEAKEN_AMOUNT),
                "Reduce the enemy's attack by "
                        + WEAKEN_AMOUNT
                        + " this turn.",
                WEAKEN_IMAGE_PATH
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
                WEAKEN_IMAGE_PATH
        );
    }

    /**
     * This card is not part of the current official level decks.
     * It remains available for compatibility and polymorphism testing.
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
     * Creates the Level 1 starter deck.
     *
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

    /**
     * Creates the correct card deck for the requested level.
     *
     * Level 1:
     * Attack x1, Heal x1
     *
     * Level 2:
     * Upgrade selection only, no battle deck
     *
     * Level 3:
     * Attack x2, Heal x2, Shield x2
     *
     * Level 4:
     * Upgrade selection only, no battle deck
     *
     * Level 5:
     * Attack x2, Heal x2, Shield x2, Weaken x2
     */
    public static Deck createDeckForLevel(int levelNumber) {

        UpgradeManager upgradeManager =
                UpgradeManager.getInstance();

        Deck deck = new Deck();

        switch (levelNumber) {
            case 1:
                addCards(deck, createAttackCard(), 1);
                addCards(deck, createHealCard(), 1);
                break;

            case 2:
                // Upgrade selection level: no battle deck.
                return deck;

            case 3:
                addAttackCards(deck, upgradeManager, 2);
                addCards(deck, createHealCard(), 2);
                addCards(deck, createDefenseCard(), 2);
                break;

            case 4:
                // Upgrade selection level: no battle deck.
                return deck;

            case 5:
                addAttackCards(deck, upgradeManager, 2);
                addCards(deck, createHealCard(), 2);
                addCards(deck, createDefenseCard(), 2);
                addWeakenCards(deck, upgradeManager, 2);
                break;

            default:
                System.out.println(
                        "Unknown level number: "
                                + levelNumber
                                + ". Using the Level 1 starter deck."
                );

                return createStarterDeck();
        }

        deck.shuffle();
        return deck;
    }

    /**
     * Adds the requested number of attack cards while respecting
     * the Level 2 attack upgrade.
     */
    private static void addAttackCards(
            Deck deck,
            UpgradeManager upgradeManager,
            int amount) {

        for (int i = 0; i < amount; i++) {
            if (upgradeManager.isAttackUpgraded()) {
                deck.addCard(createAttackCardUpgraded());
            } else {
                deck.addCard(createAttackCard());
            }
        }
    }

    /**
     * Adds the requested number of weaken cards while respecting
     * the Level 4 weaken upgrade.
     */
    private static void addWeakenCards(
            Deck deck,
            UpgradeManager upgradeManager,
            int amount) {

        for (int i = 0; i < amount; i++) {
            if (upgradeManager.isWeakenUpgraded()) {
                deck.addCard(createWeakenCardUpgraded());
            } else {
                deck.addCard(createWeakenCard());
            }
        }
    }

    /**
     * Adds separate card instances to the deck.
     *
     * A new card is created from the supplied card's data each time
     * so future stateful card behavior will not share one object.
     */
    private static void addCards(
            Deck deck,
            Card template,
            int amount) {

        for (int i = 0; i < amount; i++) {
            deck.addCard(copyCard(template));
        }
    }

    private static Card copyCard(Card card) {
        return new Card(
                card.getName(),
                card.getEnergyCost(),
                card.getEffect(),
                card.getDescription(),
                card.getImagePath()
        );
    }
}
