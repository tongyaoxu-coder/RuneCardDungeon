package com.runecarddungeon.effect;

import com.runecarddungeon.model.Actor;

public class BlockEffect implements CardEffect {
    private int block;

    public BlockEffect(int block) {
        this.block = block;
    }

    @Override
    public void apply(Actor releaser, Actor target) {
        releaser.addBlock(block);
    }
}
