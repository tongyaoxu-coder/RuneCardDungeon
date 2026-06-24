package com.runecarddungeon.data;

import com.runecarddungeon.effect.BlockEffect;
import com.runecarddungeon.effect.DamageEffect;
import com.runecarddungeon.effect.DrawCardEffect;
import com.runecarddungeon.effect.HealEffect;
import com.runecarddungeon.effect.WeakenEffect;
import com.runecarddungeon.model.Card;
import com.runecarddungeon.model.Deck;

public final class CardFactory {

    private static final int ATTACK_COST = 3;
    private static final int HEAL_COST = 2;
    private static final int SHIELD_COST = 3;
    private static final int WEAKEN_COST = 2;

    private static final int ATTACK_DAMAGE = 8;
    private static final int UPGRADED_ATTACK_DAMAGE = 11;

    private static final int HEAL_AMOUNT = 8;
    private static final int SHIELD_AMOUNT = 6;

    private static final int WEAKEN_AMOUNT = 6;
    private static final int UPGRADED_WEAKEN_AMOUNT = 10;

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

    public static Card createDrawCard() {
        return new Card(
                "Insight",
                0,
                new DrawCardEffect(2),
                "Draw 2 cards."
        );
    }

    public static Deck createStarterDeck() {
        Deck deck = new Deck();

        deck.addCard(createAttackCard());
        deck.addCard(createHealCard());

        deck.shuffle();
        return deck;
    }

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
                return deck;

            case 3:
                for (int i = 0; i < 2; i++) {
                    deck.addCard(
                            upgradeManager.isAttackUpgraded()
                                    ? createAttackCardUpgraded()
                                    : createAttackCard()
                    );

                    deck.addCard(createHealCard());
                    deck.addCard(createDefenseCard());
                }
                break;

            case 4:
                return deck;

            case 5:
                for (int i = 0; i < 2; i++) {
                    deck.addCard(
                            upgradeManager.isAttackUpgraded()
                                    ? createAttackCardUpgraded()
                                    : createAttackCard()
                    );

                    deck.addCard(createHealCard());
                    deck.addCard(createDefenseCard());

                    deck.addCard(
                            upgradeManager.isWeakenUpgraded()
                                    ? createWeakenCardUpgraded()
                                    : createWeakenCard()
                    );
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
