package com.runecarddungeon.data;

import com.runecarddungeon.model.*;

public enum LevelData {
    
    // ===== 第1关：史莱姆之家（新手教学） =====
    LEVEL_1(1, "Slime House",
            "The Slime Lair in the Border Forest—Defeat all the slimes！",
            "Slime", 8, 8, 4,
            "Teaching Attack, Health Regeneration, and Mana Consumption Mechanics",
            "None", "None", false),
    
    // ===== 第2关：强化选择（无战斗） =====
    LEVEL_2(2, "Enhanced Selection",
            "Choose a power-up and continue your adventure!",
            "None", 0, 0, 0,
            "Enhancement Choice: Attack Card Enhancement or +15 Max Health",
            "None", "None", false),
    
    // ===== 第3关：骷髅堡垒 =====
    LEVEL_3(3, "Skeleton Fortress",
            "Skeleton warriors guard the ancient fortress, and they gain a shield every turn!",
            "Skeleton", 20, 12, 3,
            "Break the shield first, then attack; make good use of defense cards.",
            "Gain 4 shield points per turn", "None", false),
    
    // ===== 第4关：强化选择（无战斗） =====
    LEVEL_4(4, "Enhanced Selection",
            "Choose a power-up and get ready to face the boss!",
            "None", 0, 0, 0,
            "Enhanced Choice: Weaken card enhancements OR gain +1 additional mana per turn",
            "None", "None", false),
    
    // ===== 第5关：红龙精英（Boss关） =====
    LEVEL_5(5, "Red Dragon Elite",
            "Final Boss! Defeat the Red Dragon to beat the game!",
            "Dragon", 72, 26, 1,
            "Use a combination of the four types of cards to defeat the boss",
            "Dragon Scale Armor: Gains 5 shield points per turn", "None", true);

    private final int levelNumber;
    private final String levelName;
    private final String lore;
    private final String enemyType;
    private final int enemyHp;
    private final int enemyDamage;
    private final int enemyCount;
    private final String teachingPoint;
    private final String passiveAbility;
    private final String fieldEffect;
    private final boolean isBoss;

    LevelData(int levelNumber, String levelName, String lore,
              String enemyType, int enemyHp, int enemyDamage, int enemyCount,
              String teachingPoint, String passiveAbility, String fieldEffect,
              boolean isBoss) {
        this.levelNumber = levelNumber;
        this.levelName = levelName;
        this.lore = lore;
        this.enemyType = enemyType;
        this.enemyHp = enemyHp;
        this.enemyDamage = enemyDamage;
        this.enemyCount = enemyCount;
        this.teachingPoint = teachingPoint;
        this.passiveAbility = passiveAbility;
        this.fieldEffect = fieldEffect;
        this.isBoss = isBoss;
    }

    // ===== Getters =====
    public int getLevelNumber() { return levelNumber; }
    public String getLevelName() { return levelName; }
    public String getLore() { return lore; }
    public String getEnemyType() { return enemyType; }
    public int getEnemyHp() { return enemyHp; }
    public int getEnemyDamage() { return enemyDamage; }
    public int getEnemyCount() { return enemyCount; }
    public String getTeachingPoint() { return teachingPoint; }
    public String getPassiveAbility() { return passiveAbility; }
    public String getFieldEffect() { return fieldEffect; }
    public boolean isBoss() { return isBoss; }
    public boolean isUpgradeLevel() { return this == LEVEL_2 || this == LEVEL_4; }

    // ===== 创建敌人 =====
    public Actor createEnemy() {
        switch (this) {
            case LEVEL_1:
                return new Slime("史莱姆", enemyHp);
            case LEVEL_3:
                return new Skeleton("骷髅战士", enemyHp);
            case LEVEL_5:
                return new Dragon("红龙精英", enemyHp);
            default:
                return null;
        }
    }
}
