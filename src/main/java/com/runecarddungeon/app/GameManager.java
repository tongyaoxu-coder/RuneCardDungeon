package com.runecarddungeon.app;

import com.runecarddungeon.model.Actor;
import com.runecarddungeon.model.Card;
import com.runecarddungeon.effect.CardEffect;
import com.runecarddungeon.effect.DamageEffect;
import com.runecarddungeon.effect.HealEffect;
import com.runecarddungeon.effect.BlockEffect;
import com.runecarddungeon.battle.BattleManager;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    
    private static GameManager instance;
    private BattleManager battleManager;
    private Actor player;
    private Actor enemy;
    private List<Card> playerHand;
    private int currentRound;
    private boolean isGameOver;
    
    private GameManager() {
        playerHand = new ArrayList<>();
        currentRound = 0;
        isGameOver = false;
    }
    
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
    
    public int getCardDamage(Card card) {
        int total = 0;
        if (card.getEffects() != null) {
            for (CardEffect effect : card.getEffects()) {
                if (effect instanceof DamageEffect) {
                    total += ((DamageEffect) effect).getDamage();
                }
            }
        }
        return total;
    }
    

    public int getCardHeal(Card card) {
        int total = 0;
        if (card.getEffects() != null) {
            for (CardEffect effect : card.getEffects()) {
                if (effect instanceof HealEffect) {
                    total += ((HealEffect) effect).getHealAmount();
                }
            }
        }
        return total;
    }
    

    public int getCardBlock(Card card) {
        int total = 0;
        if (card.getEffects() != null) {
            for (CardEffect effect : card.getEffects()) {
                if (effect instanceof BlockEffect) {
                    total += ((BlockEffect) effect).getBlock();
                }
            }
        }
        return total;
    }
    

    public void playCard(Card card, Actor releaser, Actor target) {
        if (card.getEffects() != null) {
            for (CardEffect effect : card.getEffects()) {
                effect.apply(releaser, target, battleManager);
            }
        }
        currentRound++;
        checkGameOver();
    }
    
    private void checkGameOver() {
        if (player != null && player.getHp() <= 0) {
            isGameOver = true;
            System.out.println("Game Over：You have died");
        }
        if (enemy != null && enemy.getHp() <= 0) {
            isGameOver = true;
            System.out.println("Game Over：Enemies have died");
        }
    }
    
    public void startBattle(Actor player, Actor enemy) {
        this.player = player;
        this.enemy = enemy;
        this.currentRound = 0;
        this.isGameOver = false;
        System.out.println("The battle begins.");
    }
    
    public void drawCard(Card card) {
        if (playerHand.size() < 5) {
            playerHand.add(card);
        }
    }
    
    public BattleManager getBattleManager() { return battleManager; }
    public void setBattleManager(BattleManager battleManager) { this.battleManager = battleManager; }
    public Actor getPlayer() { return player; }
    public void setPlayer(Actor player) { this.player = player; }
    public Actor getEnemy() { return enemy; }
    public void setEnemy(Actor enemy) { this.enemy = enemy; }
    public List<Card> getPlayerHand() { return playerHand; }
    public void setPlayerHand(List<Card> playerHand) { this.playerHand = playerHand; }
    public int getCurrentRound() { return currentRound; }
    public boolean isGameOver() { return isGameOver; }
}
