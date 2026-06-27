package com.runecarddungeon.data;

import com.runecarddungeon.effect.BlockEffect;
import com.runecarddungeon.effect.DamageEffect;
import com.runecarddungeon.effect.DrawCardEffect;
import com.runecarddungeon.effect.HealEffect;
import com.runecarddungeon.effect.WeakenEffect;
import com.runecarddungeon.model.Card;
import com.runecarddungeon.model.Deck;

public final class CardFactory {

    //Card energy costs

    private static final int ATTACK_COST = 3;
    private static final int HEAL_COST = 2;
    private static final int SHIELD_COST = 3;
    private static final int WEAKEN_COST = 2;

    //Base card values

    private static final int ATTACK_DAMAGE = 8;
    private static final int HEAL_AMOUNT = 8;
    private static final int SHIELD_AMOUNT = 6;
    private static final int WEAKEN_AMOUNT = 6;

    //Upgraded card values

    private static final int UPGRADED_ATTACK_DAMAGE = 11;
    private static final int UPGRADED_WEAKEN_AMOUNT = 10;

    //Card image paths

    private static final String ATTACK_IMAGE_PATH =
            "assets/cards/attack.png";

    private static final String UPGRADED_ATTACK_IMAGE_PATH =
            "assets/cards/attack_upgraded.png";

    private static final String HEAL_IMAGE_PATH =
            "assets/cards/heal.png";

    private static final String SHIELD_IMAGE_PATH =
            "assets/cards/shield.png";

    private static final String WEAKEN_IMAGE_PATH =
            "assets/cards/curse.png";

    private static final String UPGRADED_WEAKEN_IMAGE_PATH =
            "assets/cards/curse_upgraded.png";

    private CardFactory() {
        // Prevent this utility class from being instantiated.
    }


    // Individual card creation


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
                UPGRADED_ATTACK_IMAGE_PATH
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
     * Alias for code that refers to this card as a shield card.
     */
    public static Card createShieldCard() {
        return createDefenseCard();
    }

    public static Card createWeakenCard() {
        return new Card(
                "Weaken",
                WEAKEN_COST,
                new WeakenEffect(WEAKEN_AMOUNT),
                "Reduce enemy attack by "
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
                "Reduce enemy attack by "
                        + UPGRADED_WEAKEN_AMOUNT
                        + " this turn.",
                UPGRADED_WEAKEN_IMAGE_PATH
        );
    }

    /**
     * This card is currently not used in the official level decks.
     */
    public static Card createDrawCard() {
        return new Card(
                "Insight",
                0,
                new DrawCardEffect(2),
                "Draw 2 cards."
        );
    }


    // Starter deck


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


    // Level deck creation


    /**
     * Creates the deck for the requested level.
     *
     * Level 1:
     * Attack x1, Heal x1
     *
     * Level 2:
     * Upgrade selection only
     *
     * Level 3:
     * Attack x2, Heal x2, Shield x2
     *
     * Level 4:
     * Upgrade selection only
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
                deck.addCard(createAttackCard());
                deck.addCard(createHealCard());
                break;

            case 2:
                // Upgrade selection level. No battle deck is required.
                return deck;

            case 3:
                for (int i = 0; i < 2; i++) {

                    Card attackCard =
                            upgradeManager.isAttackUpgraded()
                                    ? createAttackCardUpgraded()
                                    : createAttackCard();

                    deck.addCard(attackCard);
                    deck.addCard(createHealCard());
                    deck.addCard(createDefenseCard());
                }
                break;

            case 4:
                // Upgrade selection level. No battle deck is required.
                return deck;

            case 5:
                for (int i = 0; i < 2; i++) {

                    Card attackCard =
                            upgradeManager.isAttackUpgraded()
                                    ? createAttackCardUpgraded()
                                    : createAttackCard();

                    Card weakenCard =
                            upgradeManager.isWeakenUpgraded()
                                    ? createWeakenCardUpgraded()
                                    : createWeakenCard();

                    deck.addCard(attackCard);
                    deck.addCard(createHealCard());
                    deck.addCard(createDefenseCard());
                    deck.addCard(weakenCard);
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
