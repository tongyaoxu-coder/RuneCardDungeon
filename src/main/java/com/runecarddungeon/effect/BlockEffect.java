package com.runecarddungeon.effect;

import com.runecarddungeon.battle.BattleManager;
import com.runecarddungeon.model.Actor;

public class BlockEffect implements CardEffect {

    private final int block;

    public BlockEffect(int block) {
        this.block = block;
    }

    @Override
    public void apply(
            Actor releaser,
            Actor target,
            BattleManager battleManager) {

        if (releaser != null) {
            releaser.addBlock(block);
        }
    }
}

public int getBlock() {
        return block;
    }
}
