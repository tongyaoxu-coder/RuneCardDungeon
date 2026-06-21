package com.runecarddungeon.app;

import com.runecarddungeon.battle.BattleManager;
import com.runecarddungeon.battle.BattleState;
import com.runecarddungeon.data.EnemyFactory;
import com.runecarddungeon.data.EnemyFactory.EnemyType;
import com.runecarddungeon.model.Enemy;
import com.runecarddungeon.model.Player;
import com.runecarddungeon.ui.BattlePane;
import com.runecarddungeon.ui.LevelSelectPane;
import com.runecarddungeon.ui.MainMenuPane;
import com.runecarddungeon.ui.ResultPane;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * Main entry point of the game. Launches the JavaFX window and controls the
 * overall flow: switching between the menu, level select, battle, and result
 * screens, and advancing the player through the levels.
 */
public class Main extends Application {

    // The levels in order.
    private final EnemyType[] levels = {
        EnemyType.SLIME,
        EnemyType.SKELETON,
        EnemyType.DRAGON
    };

    private int currentLevel = 0;   // index of the current level (0 = first)
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
                currentLevel = levelIndex;
                player = new Player("Hero", 80, 3);
                startLevel();
            },
            this::showMainMenu
        );
        showScene(selectPane, 600, 400);
    }

    // Starts a new game from the first level with a fresh player.
    private void startGame() {
        currentLevel = 0;
        player = new Player("Hero", 80, 3);   // name, max HP, max energy
        startLevel();
    }

    // Builds the enemy and battle logic for the current level,
    // then displays the battle screen.
    private void startLevel() {
        Enemy enemy = EnemyFactory.createEnemy(levels[currentLevel]);
        BattleManager battleManager = new BattleManager(player, enemy);

        BattlePane battlePane = new BattlePane(
            battleManager,
            () -> onBattleEnd(battleManager),   // when the battle ends
            this::restartLevel,                 // when "Restart" is chosen
            this::showMainMenu                  // when "Back to Menu" is chosen
        );
        showScene(battlePane, 700, 500);
    }

    // Restarts the current level with a fresh, full-HP player.
    private void restartLevel() {
        player = new Player("Hero", 80, 3);
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
            // Player lost -> back to menu.
            showScene(new ResultPane("Defeat", "Back to Menu",
                    this::showMainMenu), 600, 400);

        } else if (state == BattleState.VICTORY) {
            boolean isFinalLevel = (currentLevel == levels.length - 1);

            if (isFinalLevel) {
                // Cleared the last level -> game won.
                showScene(new ResultPane("Victory!", "Back to Menu",
                        this::showMainMenu), 600, 400);
            } else {
                // Cleared a normal level -> continue to the next one.
                showScene(new ResultPane("Level Clear!", "Next Level",
                        this::goToNextLevel), 600, 400);
            }
        }
    }

    // Advances to the next level and starts it.
    private void goToNextLevel() {
        currentLevel++;
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
