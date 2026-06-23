package com.runecarddungeon.app;

import com.runecarddungeon.battle.BattleManager;
import com.runecarddungeon.battle.BattleState;
import com.runecarddungeon.data.LevelData;
import com.runecarddungeon.data.LevelManager;
import com.runecarddungeon.model.Enemy;
import com.runecarddungeon.model.Player;
import com.runecarddungeon.ui.BattlePane;
import com.runecarddungeon.ui.LevelSelectPane;
import com.runecarddungeon.ui.MainMenuPane;
import com.runecarddungeon.ui.ResultPane;
import com.runecarddungeon.ui.UpgradePane;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

/*
 * Main entry point of the game. Launches the JavaFX window and controls the
 * overall flow: switching between the menu, level select, battle, and result
 * screens, and advancing the player through the levels.
 */
public class Main extends Application {

    private Player player;          // kept across levels so HP carries over
    private Stage stage;            // the main application window

    // JavaFX entry point, called automatically when the application starts.
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("Rune Card Dungeon");
        showMainMenu();
        stage.show();
    }

    // Displays the main menu screen.
    private void showMainMenu() {
        MainMenuPane menu = new MainMenuPane(
            this::startGame,
            this::showLevelSelect,
            () -> stage.close()
        );
        showScene(menu, 600, 400);
    }

    // Displays the level select screen. Picking a level starts that single
    // level with a fresh, full-HP player.
    private void showLevelSelect() {
        LevelSelectPane selectPane = new LevelSelectPane(
            levelIndex -> {
                LevelManager lm = LevelManager.getInstance();
                for (int i = 0; i < levelIndex; i++) {
                    lm.nextLevel();
                }
                player = new Player("Hero", 57, 5);
                startLevel();
            },
            this::showMainMenu
        );
        showScene(selectPane, 600, 400);
    }

    // Starts a new game from the first level with a fresh player.
    private void startGame() {
        LevelManager.getInstance().resetToFirstLevel();
        player = new Player("Hero", 57, 5);
        startLevel();
    }

    // Builds the enemy and battle logic for the current level,
    // then displays the battle screen.
private void startLevel() {
    LevelData levelData = LevelManager.getInstance().getCurrentLevel();
    if (levelData == null) {
        showScene(new ResultPane("🏆 恭喜通关全部关卡！", "Back to Menu",
                this::showMainMenu), 600, 400);
        return;
    }

    // 强化选择关
    if (levelData.isUpgradeLevel()) {
        UpgradePane upgradePane = new UpgradePane(
            player,
            levelData.getLevelNumber(),
            () -> {
                LevelManager.getInstance().nextLevel();
                startLevel();
            }
        );
        showScene(upgradePane, 600, 400);
        return;
    }

    // 创建多个敌人（根据 enemyCount）
    List<Enemy> enemies = new ArrayList<>();
    for (int i = 0; i < levelData.getEnemyCount(); i++) {
        Enemy enemy = (Enemy) levelData.createEnemy();
        if (enemy != null) {
            enemies.add(enemy);
        }
    }

    if (enemies.isEmpty()) {
        // 没有敌人，直接进入下一关
        LevelManager.getInstance().nextLevel();
        startLevel();
        return;
    }

    BattleManager battleManager = new BattleManager(player, enemies);

    BattlePane battlePane = new BattlePane(
        battleManager,
        () -> onBattleEnd(battleManager),
        this::restartLevel,
        this::showMainMenu
    );
    showScene(battlePane, 700, 500);
}

    // Restarts the current level with a fresh, full-HP player.
    private void restartLevel() {
        player = new Player("Hero", 57, 5);
        startLevel();
    }

    /*
     * Handles the end of a battle: shows a defeat screen if the player lost,
     * a victory screen if the final level was cleared, or a next-level prompt
     * otherwise.
     */
private void onBattleEnd(BattleManager battleManager) {
    BattleState state = battleManager.getState();

    if (state == BattleState.DEFEAT) {
        showScene(new ResultPane("Defeat", "Back to Menu",
                this::showMainMenu), 600, 400);

    } else if (state == BattleState.VICTORY) {
        // 用 LevelManager 判断是否还有下一关
        boolean hasNext = LevelManager.getInstance().nextLevel();

        if (!hasNext) {
            // 所有关卡通关
            showScene(new ResultPane("Congratulations on completing all the levels！", "Back to Menu",
                    this::showMainMenu), 600, 400);
        } else {
            // 进入下一关
            showScene(new ResultPane("Level Cleared！", "Next Level",
                    this::goToNextLevel), 600, 400);
        }
    }
}
    // Advances to the next level and starts it.
    private void goToNextLevel() {
        startLevel();
    }

    // Wraps a pane in a Scene and shows it on the stage.
    // This is the single place where screen switching happens.
    private void showScene(Parent pane, double width, double height) {
        stage.setScene(new Scene(pane, width, height));
    }

    // Java entry point that launches the JavaFX application.
    public static void main(String[] args) {
        launch(args);
    }
}
