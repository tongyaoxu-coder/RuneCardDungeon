package com.runecarddungeon.model;

public abstract class Actor {
    private String name;
    private int hp;
    private int maxHp;
    private int block;
    private int baseAttack;
    private int attackReduction;

    public Actor(String name, int maxHp) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.block = 0;
        this.baseAttack = 0;
        this.attackReduction = 0;
    }

    // Take demage logic
    public void takeDamage(int dmg) {
        if (this.block >= dmg) {
            this.block -= dmg;
        } else {
            int remainingDmg = dmg - this.block;
            this.block = 0;
            this.hp = Math.max(0, this.hp - remainingDmg);
        }
    }

    //Block effect
    public void addBlock(int amount) {
        this.block += amount;
    }

    public void resetBlock() {
        this.block = 0;
    }

    // Heal effect 
    public void heal(int amount) {
        this.hp = Math.min(maxHp, this.hp + amount);
    }

    // ATK logic
    public void setBaseAttack(int attack) {
        this.baseAttack = attack;
    }

    public int getAttack() {
        return Math.max(0, baseAttack - attackReduction);
    }

    public void reduceAttack(int amount) {
        this.attackReduction += amount;
    }

    public void resetAttackReduction() {
        this.attackReduction = 0;
    }

    public void addAttack(int amount) {
        this.baseAttack += amount;
    }

    // general actors attributes
    public boolean isAlive() {
        return this.hp > 0;
    }

    // ===== Getters & Setters =====
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getBlock() { return block; }
    public int getBaseAttack() { return baseAttack; }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(maxHp, hp));
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }
}
