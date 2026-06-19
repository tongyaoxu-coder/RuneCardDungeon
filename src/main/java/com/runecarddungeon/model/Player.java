package com.runecarddungeon.model;

public class Player extends Actor {

    private int energy;
    private final int maxEnergy;

    public Player(String name, int maxHp, int maxEnergy) {
        super(name, maxHp);
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
    }

    public void resetEnergy() {
        this.energy = maxEnergy;
    }

    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public boolean useEnergy(int amount) {
        if (amount < 0 || energy < amount) {
            return false;
        }

        energy -= amount;
        return true;
    }
}
