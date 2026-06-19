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

            if (card == null) {
                break;
            }

            drawnCards.add(card);
        }

        return drawnCards;
    }

    public boolean removeCard(Card card) {
        return cards.remove(card);
    }

    public boolean contains(Card card) {
        return cards.contains(card);
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

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }
}
