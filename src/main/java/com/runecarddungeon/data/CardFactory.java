package com.runecarddungeon.data;

import java.util.ArrayList;
import java.util.List;

import com.runecarddungeon.effect.BlockEffect;
import com.runecarddungeon.effect.DamageEffect;
import com.runecarddungeon.effect.HealEffect;
import com.runecarddungeon.model.Card;

public class CardFactory {

    public static List<Card> createStarterDeck() {
        List<Card> cards = new ArrayList<>();

        cards.add(new Card("Strike", 1, new DamageEffect(6), "Deal 6 damage."));
        cards.add(new Card("Strike", 1, new DamageEffect(6), "Deal 6 damage."));
        cards.add(new Card("Defend", 1, new BlockEffect(5), "Gain 5 block."));
        cards.add(new Card("Defend", 1, new BlockEffect(5), "Gain 5 block."));
        cards.add(new Card("Heal", 1, new HealEffect(5), "Restore 5 HP."));
        cards.add(new Card("Heavy Strike", 2, new DamageEffect(12), "Deal 12 damage."));
        cards.add(new Card("Iron Wall", 2, new BlockEffect(12), "Gain 12 block."));
        cards.add(new Card("Greater Heal", 2, new HealEffect(10), "Restore 10 HP."));
        cards.add(new Card("Power Hit", 3, new DamageEffect(20), "Deal 20 damage."));
        cards.add(new Card("Guard", 0, new BlockEffect(3), "Gain 3 block."));

        return cards;
    }
}
