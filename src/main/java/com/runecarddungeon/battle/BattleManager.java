package com.runecarddungeon.battle;

import com.runecarddungeon.data.CardFactory;
import com.runecarddungeon.model.Card;
import com.runecarddungeon.model.Deck;
import com.runecarddungeon.model.Enemy;
import com.runecarddungeon.model.Player;

public class BattleManager {

    private static final int STARTING_HAND_SIZE = 4;

    private final Player player;
    private final Enemy enemy;

    private final Deck drawPile;
    private final Deck hand;
    private final Deck discardPile;

    private BattleState state;

    public BattleManager(Player player, Enemy enemy) {
        this(player, enemy, CardFactory.createStarterDeck());
    }

    public BattleManager(
            Player player,
            Enemy enemy,
            Deck startingDeck) {

        if (player == null || enemy == null || startingDeck == null) {
            throw new IllegalArgumentException(
                    "Player, enemy and deck cannot be null.");
        }

        this.player = player;
        this.enemy = enemy;

        this.drawPile = new Deck();
        this.hand = new Deck();
        this.discardPile = new Deck();

        this.drawPile.addCards(startingDeck.getCards());
        this.drawPile.shuffle();

        this.state = BattleState.PLAYER_TURN;

        startPlayerTurn();
    }

    /**
     * Starts a new player turn.
     */
    public void startPlayerTurn() {
        if (state == BattleState.VICTORY
                || state == BattleState.DEFEAT) {
            return;
        }

        state = BattleState.PLAYER_TURN;

        player.resetBlock();
        player.resetEnergy();

        drawCards(STARTING_HAND_SIZE);
    }

    /**
     * Draws cards from the draw pile.
     * When the draw pile is empty, discarded cards are shuffled
     * back into the draw pile.
     */
    public void drawCards(int amount) {
        for (int i = 0; i < amount; i++) {

            if (drawPile.isEmpty()) {
                recycleDiscardPile();
            }

            Card drawnCard = drawPile.drawCard();

            if (drawnCard == null) {
                break;
            }

            hand.addCard(drawnCard);
        }
    }

    /**
     * Attempts to play a card from the player's hand.
     */
    public boolean playCard(Card card) {
        if (state != BattleState.PLAYER_TURN) {
            return false;
        }

        if (card == null || !hand.contains(card)) {
            return false;
        }

        boolean playedSuccessfully =
                card.play(player, enemy, this);

        if (!playedSuccessfully) {
            return false;
        }

        hand.removeCard(card);
        discardPile.addCard(card);

        updateBattleState();
        return true;
    }

    /**
     * Ends the player's turn and allows the enemy to act.
     */
    public void endPlayerTurn() {
        if (state != BattleState.PLAYER_TURN) {
            return;
        }

        discardHand();
        state = BattleState.ENEMY_TURN;

        enemy.takeTurn(player);
        updateBattleState();

        if (state == BattleState.ENEMY_TURN) {
            startPlayerTurn();
        }
    }

    private void discardHand() {
        discardPile.addCards(hand.getCards());
        hand.clear();
    }

    private void recycleDiscardPile() {
        if (discardPile.isEmpty()) {
            return;
        }

        drawPile.addCards(discardPile.getCards());
        discardPile.clear();
        drawPile.shuffle();
    }

 private void updateBattleState() {
    if (enemy.getHp() <= 0) {
        state = BattleState.VICTORY;
    } else if (player.getHp() <= 0) {
        state = BattleState.DEFEAT;
    }
}

    public Player getPlayer() {
        return player;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public BattleState getState() {
        return state;
    }

    public Deck getDrawPile() {
        return drawPile;
    }

    public Deck getHand() {
        return hand;
    }

    public Deck getDiscardPile() {
        return discardPile;
    }

    public void setState(BattleState state) {
        if (state != null) {
            this.state = state;
        }
    }
}
