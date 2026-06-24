package com.runecarddungeon.model;

public class Player extends Actor {

    private int energy;
    private int maxEnergy;
    private final Deck deck;

    public Player(
            String name,
            int maxHp,
            int maxEnergy) {

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

    /**
     * Attempts to spend energy.
     *
     * @return true when enough energy is available
     */
    public boolean spendEnergy(int cost) {
        int safeCost = Math.max(0, cost);

        if (energy < safeCost) {
            System.out.println(
                    "Not enough energy. Current energy: "
                            + energy
                            + ", required energy: "
                            + safeCost
            );

            return false;
        }

        energy -= safeCost;
        return true;
    }

    public void resetEnergy() {
        energy = maxEnergy;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = Math.max(0, maxEnergy);
        this.energy = Math.min(energy, this.maxEnergy);
    }

    public void increaseMaxEnergy(int amount) {
        maxEnergy += Math.max(0, amount);
        energy = maxEnergy;
    }

    public Deck getDeck() {
        return deck;
    }
}
