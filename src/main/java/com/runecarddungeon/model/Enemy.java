package com.runecarddungeon.model;

public abstract class Enemy extends Actor {
    private String currIntent;
    private int attackDamage;
    private int baseAttackDamage; 

    public Enemy(String name, int maxHp, int attackDamage) {
        super(name, maxHp);
        this.attackDamage = attackDamage;
        this.baseAttackDamage = attackDamage;
        this.currIntent = "准备攻击";
    }

    // 初始化 
    public void initial() {
        super.setHp(super.getMaxHp());
        System.out.println(this.getName() + " is coming... Hp:" + super.getHp());
    }

    // 攻击力系统 
    public int getAttackDamage() {
        return this.attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
        this.baseAttackDamage = attackDamage;
    }

     //获取当前实际攻击力（考虑削弱效果）
    public int getCurrentAttackDamage() {
        return Math.max(0, attackDamage);
    }

     //降低攻击力（削弱卡效果）
    public void reduceAttack(int amount) {
        this.attackDamage = Math.max(0, this.attackDamage - amount);
        System.out.println(this.getName() + " 攻击力降低 " + amount + " 点，当前攻击力：" + this.attackDamage);
    }

     //重置攻击力（回合结束时调用）
    public void resetAttackDamage() {
        this.attackDamage = this.baseAttackDamage;
    }

    // 行动系统
    public void attack(Player player) {
        if (player != null) {
            int damage = getCurrentAttackDamage();
            System.out.println(this.getName() + " 发动攻击，造成 " + damage + " 点伤害！");
            player.takeDamage(damage);
        }
    }


     //敌人回合行动
    public abstract void takeTurn(Player target);

     //随机生成意图
    public abstract void rollIntent();

    //每回合开始时的被动效果
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
