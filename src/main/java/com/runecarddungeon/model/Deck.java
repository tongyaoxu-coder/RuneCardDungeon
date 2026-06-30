package com.runecarddungeon.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private final List<Card> cards;

    public Deck() {
        // Start with an empty deck
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        // Ignore null cards
        if (card != null) {
            cards.add(card);
        }
    }

    public void addCards(List<Card> newCards) {
        if (newCards == null) {
            return;
        }

        // Use addCard so null cards are skipped
        for (Card card : newCards) {
            addCard(card);
        }
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            return null;
        }

        // The first card is treated as the top of the deck
        return cards.remove(0);
    }

    public List<Card> drawCards(int amount) {
        List<Card> drawnCards = new ArrayList<>();

        // Prevent a negative draw amount
        int safeAmount = Math.max(0, amount);

        for (int i = 0; i < safeAmount; i++) {
            Card card = drawCard();

            // Stop early if there are no cards left
            if (card == null) {
                break;
            }

            drawnCards.add(card);
        }

        return drawnCards;
    }

    public boolean removeCard(Card card) {
        return card != null && cards.remove(card);
    }

    public boolean contains(Card card) {
        return card != null && cards.contains(card);
    }

    public void shuffle() {
        // Randomize the current card order
        Collections.shuffle(cards);
    }

    public int getRemainingCount() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void clear() {
        cards.clear();
    }

    public List<Card> getCards() {
        // Do not allow other classes to modify the list directly
        return Collections.unmodifiableList(cards);
    }
}
