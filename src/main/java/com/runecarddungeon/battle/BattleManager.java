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

        System.out.println("⚔️ 当前敌人：" + currentEnemy.getName() + " (剩余 " + enemies.size() + " 个敌人)");
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
            // 移除已击败的敌人
            enemies.remove(currentEnemy);
            System.out.println("💀 " + currentEnemy.getName() + " 被击败！剩余 " + enemies.size() + " 个敌人");

            if (enemies.isEmpty()) {
                // 所有敌人都击败了
                state = BattleState.VICTORY;
                System.out.println("🏆 所有敌人被击败！");
                return;
            }

            // 切换到下一个敌人
            currentEnemy = enemies.get(0);
            System.out.println("⚔️ 下一个敌人：" + currentEnemy.getName());
        }

        if (currentEnemy == null) {
            state = BattleState.VICTORY;
            return;
        }

        state = BattleState.PLAYER_TURN;

        player.resetBlock();
        player.resetEnergy();

        // 敌人回合开始触发被动
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
        if (state != BattleState.PLAYER_TURN) {
            return false;
        }

        if (card == null || !hand.contains(card)) {
            return false;
        }

        if (currentEnemy == null) {
            return false;
        }

        boolean playedSuccessfully =
                card.play(player, currentEnemy, this);

        if (!playedSuccessfully) {
            return false;
        }

        hand.removeCard(card);
        discardPile.addCard(card);

        updateBattleState();
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

    // ===== Getters =====

    public Player getPlayer() {
        return player;
    }

    public Enemy getEnemy() {
        return currentEnemy;  // 返回当前敌人
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
