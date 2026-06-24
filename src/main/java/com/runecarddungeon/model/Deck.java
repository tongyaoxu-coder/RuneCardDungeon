package com.runecarddungeon.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private final List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        if (card != null) {
            cards.add(card);
        }
    }

    public void addCards(List<Card> newCards) {
        if (newCards == null) {
            return;
        }

        for (Card card : newCards) {
            addCard(card);
        }
    }

    /**
     * Draws and removes the first card in this deck.
     *
     * @return the drawn card, or null if the deck is empty
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            return null;
        }

        return cards.remove(0);
    }

    /**
     * Draws up to the requested number of cards.
     */
    public List<Card> drawCards(int amount) {
        List<Card> drawnCards = new ArrayList<>();
        int safeAmount = Math.max(0, amount);

        for (int i = 0; i < safeAmount; i++) {
            Card card = drawCard();

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

    /**
     * Returns a read-only view of the cards.
     */
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }
}
