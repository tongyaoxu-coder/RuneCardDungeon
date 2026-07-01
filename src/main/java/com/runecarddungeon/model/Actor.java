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
        // if the actor has block, 
        // consume block first
        if (this.block >= dmg) {
            this.block -= dmg;
        } else {
            // if the attack amount exceeds block
            // the remaining attack will consume hp
            int remainingDmg = dmg - this.block;
            this.block = 0;
            // hp never be negative
            this.hp = Math.max(0, this.hp - remainingDmg);
        }
    }

    //Block effect
    public void addBlock(int amount) {
        // block can  accumulate
        this.block += amount;
    }

    public void resetBlock() {
        this.block = 0;
    }

    // Heal effect 
    public void heal(int amount) {
        // Heal effect can increase hp
        // But it cannot exceed maxHp in regular case
        this.hp = Math.min(maxHp, this.hp + amount);
    }

    // ATK logic
    // The amount of actor makes in an single attack
    public void setBaseAttack(int attack) {
        this.baseAttack = attack;
    }

    public int getAttack() {
        return Math.max(0, baseAttack - attackReduction);
    }

    // Attack can be reduce through weaken effect card
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
    // Any actor with hp = 0 dies (hp never be negative)
    public boolean isAlive() {
        return this.hp > 0;
    }

    // Getters for specific model class or card effect class
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getBlock() { return block; }
    public int getBaseAttack() { return baseAttack; }

    // setters for specific model class or card effect class
    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(maxHp, hp));
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }
}
