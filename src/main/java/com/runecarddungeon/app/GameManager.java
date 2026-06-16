package app;

import battle.BattleManager;
import model.Card;

public class GameManager {

    private BattleManager battle;

    public GameManager(BattleManager battle) {
        this.battle = battle;
    }

    public void playCard(Card card) {

        battle.getEnemy().takeDamage(
                card.getDamage()
        );

        System.out.println(
                "使用卡牌：" + card.getName()
        );
    }
}
11
