package com.runecarddungeon.data;

import com.runecarddungeon.model.Player;

public class UpgradeManager {
    private static UpgradeManager instance;
    private boolean attackUpgraded = false;
    private boolean maxHpUpgraded = false;
    private boolean weakenUpgraded = false;
    private boolean energyUpgraded = false;

    private UpgradeManager() {}

    public static UpgradeManager getInstance() {
        if (instance == null) {
            instance = new UpgradeManager();
        }
        return instance;
    }

    // ===== 第2关强化 =====
    public void applyUpgradeLevel2(Player player, boolean chooseAttack) {
        if (chooseAttack) {
            attackUpgraded = true;
            System.out.println("Attack Card Enhancement: Damage 8 → 12");
        } else {
            maxHpUpgraded = true;
            player.setMaxHp(player.getMaxHp() + 15);
            player.setHp(player.getHp() + 15);
            System.out.println("Max HP +15, Current HP: " + player.getHp());
        }
    }

    // ===== 第4关强化 =====
    public void applyUpgradeLevel4(Player player, boolean chooseWeaken) {
        if (chooseWeaken) {
            weakenUpgraded = true;
            System.out.println("Weakened Card Buff: Effect 6 → 10");
        } else {
            energyUpgraded = true;
            player.setMaxEnergy(player.getMaxEnergy() + 1);
            player.resetEnergy();
            System.out.println("+1 MP per turn; current maximum MP： " + player.getMaxEnergy());
        }
    }

    // ===== Getters =====
    public boolean isAttackUpgraded() { return attackUpgraded; }
    public boolean isMaxHpUpgraded() { return maxHpUpgraded; }
    public boolean isWeakenUpgraded() { return weakenUpgraded; }
    public boolean isEnergyUpgraded() { return energyUpgraded; }

    public void reset() {
        attackUpgraded = false;
        maxHpUpgraded = false;
        weakenUpgraded = false;
        energyUpgraded = false;
    }
}
