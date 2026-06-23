package com.runecarddungeon.data;

import com.runecarddungeon.effect.BlockEffect;
import com.runecarddungeon.effect.DamageEffect;
import com.runecarddungeon.effect.DrawCardEffect;
import com.runecarddungeon.effect.HealEffect;
import com.runecarddungeon.effect.WeakenEffect;
import com.runecarddungeon.model.Card;
import com.runecarddungeon.model.Deck;

import java.util.ArrayList;
import java.util.List;

public final class CardFactory {

    private CardFactory() {
        // Prevent this utility class from being instantiated.
    }

    // ===== 基础卡牌创建方法 =====

    public static Card createAttackCard() {
        return new Card("攻击卡", 3, new DamageEffect(8), "造成8点伤害");
    }

    public static Card createAttackCardUpgraded() {
        return new Card("攻击卡+", 3, new DamageEffect(12), "造成12点伤害");
    }

    public static Card createHealCard() {
        return new Card("回血卡", 2, new HealEffect(8), "回复8点生命");
    }

    public static Card createDefenseCard() {
        return new Card("防御卡", 3, new BlockEffect(6), "获得6点护盾");
    }

    public static Card createWeakenCard() {
        return new Card("削弱卡", 2, new WeakenEffect(6), "降低敌方6点攻击");
    }

    public static Card createWeakenCardUpgraded() {
        return new Card("削弱卡+", 2, new WeakenEffect(10), "降低敌方10点攻击");
    }

    public static Card createDrawCard() {
        return new Card("洞察", 0, new DrawCardEffect(2), "抽2张牌");
    }

    // ===== 旧版兼容（保留给其他地方用） =====

    public static Deck createStarterDeck() {
        Deck deck = new Deck();
        deck.addCard(new Card("Strike", 1, new DamageEffect(6), "Deal 6 damage to the enemy."));
        deck.addCard(new Card("Defend", 1, new BlockEffect(5), "Gain 5 block."));
        deck.addCard(new Card("Healing Rune", 2, new HealEffect(6), "Restore 6 HP."));
        deck.addCard(new Card("Insight", 0, new DrawCardEffect(2), "Draw 2 cards."));
        deck.shuffle();
        return deck;
    }

    // ===== 根据关卡生成卡组 =====

    /**
     * 根据关卡编号和强化状态生成对应的卡组
     */
    public static Deck createDeckForLevel(int levelNumber) {
        UpgradeManager um = UpgradeManager.getInstance();
        List<Card> cards = new ArrayList<>();

        switch (levelNumber) {
            case 1:
                // 第1关：攻击卡x1，回血卡x1
                cards.add(createAttackCard());
                cards.add(createHealCard());
                break;

            case 2:
                // 第2关是强化关，不会战斗，但为了安全返回空卡组
                return new Deck();

            case 3:
                // 第3关：攻击卡x2，回血卡x2，防御卡x2
                for (int i = 0; i < 2; i++) {
                    cards.add(um.isAttackUpgraded() ? createAttackCardUpgraded() : createAttackCard());
                    cards.add(createHealCard());
                    cards.add(createDefenseCard());
                }
                break;

            case 4:
                // 第4关是强化关，不会战斗，但为了安全返回空卡组
                return new Deck();

            case 5:
                // 第5关：攻击卡x2，回血卡x2，防御卡x2，削弱卡x2
                for (int i = 0; i < 2; i++) {
                    cards.add(um.isAttackUpgraded() ? createAttackCardUpgraded() : createAttackCard());
                    cards.add(createHealCard());
                    cards.add(createDefenseCard());
                    cards.add(um.isWeakenUpgraded() ? createWeakenCardUpgraded() : createWeakenCard());
                }
                break;

            default:
                // 默认返回初始卡组
                return createStarterDeck();
        }

        Deck deck = new Deck();
        deck.addCards(cards);
        deck.shuffle();
        return deck;
    }
}
