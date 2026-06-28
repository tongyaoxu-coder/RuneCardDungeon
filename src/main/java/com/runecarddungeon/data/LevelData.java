package com.runecarddungeon.data;

import com.runecarddungeon.model.*;

public enum LevelData {
    
    //Level 1: Slime House (Beginner's Tutorial)
    LEVEL_1(1, "Slime House",
            "The Slime Lair in the Border Forest—Defeat all the slimes！",
            "Slime", 8, 8, 1,
            "Teaching Attack, Health Regeneration, and Mana Consumption Mechanics",
            "None", "None", false),
    
    //Level 2: Enhancement Selection (No Combat)
    LEVEL_2(2, "Enhanced Selection",
            "Choose a power-up and continue your adventure!",
            "None", 0, 0, 0,
            "Enhancement Choice: Attack Card Enhancement or +15 Max Health",
            "None", "None", false),
    
    //Level 3: Skeleton Fortress
    LEVEL_3(3, "Skeleton Fortress",
            "Skeleton warriors guard the ancient fortress, and they gain a shield every turn!",
            "Skeleton", 15, 7, 1,
            "Break the shield first, then attack; make good use of defense cards.",
            "Gain 4 shield points per turn", "None", false),
    
    //Level 4: Enhancement Selection (No Combat)
    LEVEL_4(4, "Enhanced Selection",
            "Choose a power-up and get ready to face the boss!",
            "None", 0, 0, 0,
            "Enhanced Choice: Weaken card enhancements OR gain +1 additional mana per turn",
            "None", "None", false),
    
    //Level 5: Red Dragon Elite (Boss Level)
    LEVEL_5(5, "Red Dragon Elite",
            "Final Boss! Defeat the Red Dragon to beat the game!",
            "Dragon", 52, 13, 1,
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

    // ===== Create an Enemy =====
    public Actor createEnemy() {
        switch (this) {
            case LEVEL_1:
                return new Slime("Slime", enemyHp,enemyDamage);
            case LEVEL_3:
                return new Skeleton("Skeletin", enemyHp,enemyDamage);
            case LEVEL_5:
                return new Dragon("Dragon", enemyHp,enemyDamage);
            default:
                return null;
        }
    }
}
