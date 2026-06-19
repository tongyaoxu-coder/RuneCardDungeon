package com.runecarddungeon.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        if (card != null) {
            this.cards.add(card);
        }
    }

    public void addCards(List<Card> cards) {
        if (cards != null) {
            this.cards.addAll(cards);
        }
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }

    public List<Card> drawCards(int amount) {
        List<Card> drawnCards = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            Card card = drawCard();
            if (card != null) {
                drawnCards.add(card);
            }
        }

        return drawnCards;
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    public int getRemainingCount() {
        return this.cards.size();
    }

    public void clear() {
        this.cards.clear();
    }

    public List<Card> getCards() {
        return cards;
    }
}
