package com.runecarddungeon.model;

public abstract class Enemy extends Actor {
    private String currIntent;
    private int attackDamage;
    private int baseAttackDamage; 

    public Enemy(String name, int maxHp, int attackDamage) {
        super(name, maxHp);
        this.attackDamage = attackDamage;
        this.baseAttackDamage = attackDamage;
        this.currIntent = "Attack ready!";
    }

    // Initialise 
    public void initial() {
        super.setHp(super.getMaxHp());
        System.out.println(this.getName() + " is coming... Hp:" + super.getHp());
    }

    // ATK
    public int getAttackDamage() {
        return this.attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
        this.baseAttackDamage = attackDamage;
    }

     //getter of ATK
    public int getCurrentAttackDamage() {
        return Math.max(0, attackDamage);
    }

     // Reduce ATK
    public void reduceAttack(int amount) {
        this.attackDamage = Math.max(0, this.attackDamage - amount);
        System.out.println(this.getName() + " ATK reduce by " + amount + " Cureent ATK：" + this.attackDamage);
    }

     // ATK reset
    public void resetAttackDamage() {
        this.attackDamage = this.baseAttackDamage;
    }

    // Movement systemd
    public void attack(Player player) {
        if (player != null) {
            int damage = getCurrentAttackDamage();
            System.out.println(this.getName() + " Attack! " + damage + " hurt！");
            player.takeDamage(damage);
        }
    }

     // Enemy roll
    public abstract void takeTurn(Player target);

     //generate intent randomly
    public abstract void rollIntent();

    //passive effect at the beginning of each round
    public void onTurnStart() {
    }

    //Getters & Setters
    public String getCurrentIntent() {
        return currIntent;
    }

    public void setCurrIntent(String intent) {
        this.currIntent = intent;
    }

    public int getBaseAttackDamage() {
        return baseAttackDamage;
    }
}
