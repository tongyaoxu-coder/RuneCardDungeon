package com.runecarddungeon.battle;

import com.runecarddungeon.data.CardFactory;
import com.runecarddungeon.data.LevelData;
import com.runecarddungeon.data.LevelManager;
import com.runecarddungeon.model.Card;
import com.runecarddungeon.model.Deck;
import com.runecarddungeon.model.Enemy;
import com.runecarddungeon.model.Player;

import java.util.ArrayList;
import java.util.List;

public class BattleManager {

    private static final int STARTING_HAND_SIZE = 4;

    private final Player player;
    private final List<Enemy> enemies;
    private Enemy currentEnemy;
    private int currentEnemyIndex;

    private final Deck drawPile;
    private final Deck hand;
    private final Deck discardPile;

    private BattleState state;

    public BattleManager(Player player, List<Enemy> enemies) {
        this(player, enemies, CardFactory.createDeckForLevel(
            LevelManager.getInstance().getCurrentLevelNumber()
        ));
    }

    public BattleManager(
            Player player,
            List<Enemy> enemies,
            Deck startingDeck) {

        if (player == null || enemies == null || enemies.isEmpty() || startingDeck == null) {
            throw new IllegalArgumentException(
                    "Player, enemies (non-empty) and deck cannot be null.");
        }

        this.player = player;
        this.enemies = new ArrayList<>(enemies);
        this.currentEnemyIndex = 0;
        this.currentEnemy = this.enemies.get(0);

        this.drawPile = new Deck();
        this.hand = new Deck();
        this.discardPile = new Deck();

        this.drawPile.addCards(startingDeck.getCards());
        this.drawPile.shuffle();

        this.state = BattleState.PLAYER_TURN;

        System.out.println("Current Enemy:" + currentEnemy.getName() + " (Remaining " + enemies.size() + " enemy(s))");
        startPlayerTurn();
    }

    public Enemy getCurrentEnemy() {
        return currentEnemy;
    }

    public int getRemainingEnemyCount() {
        return enemies.size();
    }

    public boolean isAllEnemiesDefeated() {
        return enemies.isEmpty();
    }

    public void startPlayerTurn() {
        if (state == BattleState.VICTORY || state == BattleState.DEFEAT) {
            return;
        }

        if (currentEnemy != null && currentEnemy.getHp() <= 0) {
            enemies.remove(currentEnemy);
            System.out.println(currentEnemy.getName() + " Defeated! Remaining " + enemies.size() + " enemy(s)");

            if (enemies.isEmpty()) {
                state = BattleState.VICTORY;
                System.out.println("All enemies have been defeated!");
                return;
            }

            currentEnemy = enemies.get(0);
            System.out.println("Next Enemy: " + currentEnemy.getName());
        }

        if (currentEnemy == null) {
            state = BattleState.VICTORY;
            return;
        }

        state = BattleState.PLAYER_TURN;

        player.resetBlock();
        player.resetEnergy();

        currentEnemy.onTurnStart();

        drawCards(STARTING_HAND_SIZE);
    }

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

    public boolean playCard(Card card) {
        return playCardWithCallback(card, null, null);
    }

    // Includes a callback for the UI layer to call
    public boolean playCardWithCallback(Card card,
                                         Runnable onBeforeDamage,
                                         Runnable onAfterDamage) {
        if (state != BattleState.PLAYER_TURN) {
            return false;
        }

        if (card == null || !hand.contains(card)) {
            return false;
        }

        if (currentEnemy == null) {
            return false;
        }

        // Adjustment Before Health Point Deduction
        if (onBeforeDamage != null) {
            onBeforeDamage.run();
        }

        boolean playedSuccessfully = card.play(player, currentEnemy, this);

        if (!playedSuccessfully) {
            return false;
        }

        hand.removeCard(card);
        discardPile.addCard(card);

        updateBattleState();

        // Debuff Reset
        if (currentEnemy != null) {
            currentEnemy.resetAttackDamage();
        }

        // Post-damage callback (play hit animation)
        if (onAfterDamage != null) {
            onAfterDamage.run();
        }

        return true;
    }

    public void endPlayerTurn() {
        if (state != BattleState.PLAYER_TURN) {
            return;
        }

        discardHand();
        state = BattleState.ENEMY_TURN;

        if (currentEnemy != null && currentEnemy.isAlive()) {
            currentEnemy.takeTurn(player);

             //Weaken only lasts for the current enemy turn.
             //Restore the enemy's original attack afterwards.
            currentEnemy.resetAttackDamage();
        }

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
        if (currentEnemy != null && currentEnemy.getHp() <= 0) {
            return;
        }

        if (enemies.isEmpty()) {
            state = BattleState.VICTORY;
            return;
        }

        if (player.getHp() <= 0) {
            state = BattleState.DEFEAT;
        }
    }

    //Getters

    public Player getPlayer() {
        return player;
    }

    public Enemy getEnemy() {
        return currentEnemy;
    }

    public List<Enemy> getAllEnemies() {
        return enemies;
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
