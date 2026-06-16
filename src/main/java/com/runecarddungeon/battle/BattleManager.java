package battle;

import model.Player;
import model.Enemy;

public class BattleManager {

    private Player player;
    private Enemy enemy;

    private BattleState state;

    public BattleManager(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
        this.state = BattleState.PLAYER_TURN;
    }

    public Player getPlayer() {
        return player;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public BattleState getState() {
        return state;
    }

    public void setState(BattleState state) {
        this.state = state;
    }
}
