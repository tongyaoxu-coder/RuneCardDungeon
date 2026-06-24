package com.runecarddungeon.data;

import com.runecarddungeon.model.Player;

public class UpgradeManager {

    private static UpgradeManager instance;

    private boolean attackUpgraded;
    private boolean maxHpUpgraded;
    private boolean weakenUpgraded;
    private boolean energyUpgraded;

    private UpgradeManager() {
        attackUpgraded = false;
        maxHpUpgraded = false;
        weakenUpgraded = false;
        energyUpgraded = false;
    }

    public static UpgradeManager getInstance() {
        if (instance == null) {
            instance = new UpgradeManager();
        }

        return instance;
    }

    // Level 2 upgrade

    public void applyUpgradeLevel2(
            Player player,
            boolean chooseAttack) {

        if (player == null) {
            return;
        }

        if (chooseAttack) {
            attackUpgraded = true;

            System.out.println(
                    "Attack card upgraded: damage 8 -> 11."
            );
        } else {
            maxHpUpgraded = true;

            player.setMaxHp(
                    player.getMaxHp() + 15
            );

            player.setHp(
                    player.getHp() + 15
            );

            System.out.println(
                    "Maximum HP increased by 15. Current HP: "
                            + player.getHp()
            );
        }
    }

    // Level 4 upgrade

    public void applyUpgradeLevel4(
            Player player,
            boolean chooseWeaken) {

        if (player == null) {
            return;
        }

        if (chooseWeaken) {
            weakenUpgraded = true;

            System.out.println(
                    "Weaken card upgraded: attack reduction 6 -> 10."
            );
        } else {
            energyUpgraded = true;

            player.setMaxEnergy(
                    player.getMaxEnergy() + 1
            );

            player.resetEnergy();

            System.out.println(
                    "Maximum energy increased by 1. Current maximum energy: "
                            + player.getMaxEnergy()
            );
        }
    }

    public boolean isAttackUpgraded() {
        return attackUpgraded;
    }

    public boolean isMaxHpUpgraded() {
        return maxHpUpgraded;
    }

    public boolean isWeakenUpgraded() {
        return weakenUpgraded;
    }

    public boolean isEnergyUpgraded() {
        return energyUpgraded;
    }

    public void reset() {
        attackUpgraded = false;
        maxHpUpgraded = false;
        weakenUpgraded = false;
        energyUpgraded = false;
    }
}
