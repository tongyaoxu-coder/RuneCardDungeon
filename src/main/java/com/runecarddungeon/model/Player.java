package com.runecarddungeon.model;

// inherit from Actor, base calss for specific player's role
// In the demo, we only design one role for the player ( the Knight)
public class Player extends Actor {
    // player have special attributes of energy (to play card), max energy and deck (card set)
    private int energy;
    private int maxEnergy;
    private final Deck deck;

    public Player(String name,int maxHp,int maxEnergy) {

        super(name, maxHp);

        this.maxEnergy = Math.max(0, maxEnergy);
        this.energy = this.maxEnergy;
        this.deck = new Deck();
    }

    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    // intend to spend the energy
    // return true only when the energy is enough
    public boolean spendEnergy(int cost) {
        int safeCost = Math.max(0, cost);
        if (energy < safeCost) {
            System.out.println("Not enough energy. Current energy: " + energy+ ", required energy: "+ safeCost);
            return false;
        }
        energy -= safeCost;
        return true;
    }

    // provide reset method. The energy gain back to max energy every roll
    public void resetEnergy() {
        energy = maxEnergy;
    }

    // setter for modify max energy
    // max energy cannot be negative
    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = Math.max(0, maxEnergy);
        this.energy = Math.min(energy, this.maxEnergy);
    }

    public void increaseMaxEnergy(int amount) {
        this.setMaxEnergy(amount+this.getMaxEnergy());
    }
    
    public void increaseMaxHp(int amount) {
    	this.setMaxHp(amount+this.getMaxHp());
    }

    public Deck getDeck() {
        return deck;
    }
}
