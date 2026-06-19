package com.runecarddungeon.data;

import com.runecarddungeon.effect.BlockEffect;
import com.runecarddungeon.effect.DamageEffect;
import com.runecarddungeon.effect.DrawCardEffect;
import com.runecarddungeon.effect.HealEffect;
import com.runecarddungeon.model.Card;
import com.runecarddungeon.model.Deck;

public final class CardFactory {

    private CardFactory() {
        // Prevent this utility class from being instantiated.
    }

    public static Deck createStarterDeck() {
        Deck deck = new Deck();

        deck.addCard(new Card(
                "Strike",
                1,
                new DamageEffect(6),
                "Deal 6 damage to the enemy."));

        deck.addCard(new Card(
                "Defend",
                1,
                new BlockEffect(5),
                "Gain 5 block."));

        deck.addCard(new Card(
                "Healing Rune",
                2,
                new HealEffect(6),
                "Restore 6 HP."));

        deck.addCard(new Card(
                "Insight",
                0,
                new DrawCardEffect(2),
                "Draw 2 cards."));

        deck.shuffle();
        return deck;
    }
}
