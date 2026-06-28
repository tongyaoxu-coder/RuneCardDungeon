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
import com.runecarddungeon.ui.TutorialPane;
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

        // Create the initial scene once — we will only swap its root,
        // never replace the Scene itself, so fullscreen is preserved.
        stage.setScene(new Scene(new javafx.scene.layout.StackPane(), 700, 500));

        // F11 or Escape toggles fullscreen
        stage.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.F11) {
                stage.setFullScreen(!stage.isFullScreen());
            }
        });

        showMainMenu();
        stage.show();
    }

    // Displays the main menu screen.
    private void showMainMenu() {
        MainMenuPane menu = new MainMenuPane(
            this::startGame,
            this::showLevelSelect,
            this::showTutorial,
            () -> stage.close()
        );
        showScene(menu, 700, 500);
    }

    // Displays the tutorial / how-to-play screen.
    private void showTutorial() {
        TutorialPane tutorial = new TutorialPane(this::showMainMenu);
        showScene(tutorial, 700, 500);
    }

    // Displays the level select screen. Picking a level starts that single
    // level with a fresh, full-HP player.
    private void showLevelSelect() {
        LevelSelectPane selectPane = new LevelSelectPane(
            levelIndex -> {
                LevelManager lm = LevelManager.getInstance();
                lm.resetToFirstLevel();
                // UI card index 0→LEVEL_1, 1→LEVEL_3, 2→LEVEL_5
                // Each battle level is separated by an upgrade level, so step = index*2
                int steps = levelIndex * 2;
                for (int i = 0; i < steps; i++) {
                    lm.nextLevel();
                }
                player = new Player("Hero", 57, 5);
                startLevel();
            },
            this::showMainMenu
        );
        showScene(selectPane, 700, 500);
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
        showScene(new ResultPane("🏆 Congratulations! All Levels Cleared!", "Back to Menu",
                this::showMainMenu), 700, 500);
        return;
    }

    // Strengthen the Selection Process
    if (levelData.isUpgradeLevel()) {
        UpgradePane upgradePane = new UpgradePane(
            player,
            levelData.getLevelNumber(),
            () -> {
                LevelManager.getInstance().nextLevel();
                startLevel();
            }
        );
        showScene(upgradePane, 700, 500);
        return;
    }

    // Create multiple enemies (based on enemyCount)
    List<Enemy> enemies = new ArrayList<>();
    for (int i = 0; i < levelData.getEnemyCount(); i++) {
        Enemy enemy = (Enemy) levelData.createEnemy();
        if (enemy != null) {
            enemies.add(enemy);
        }
    }

    if (enemies.isEmpty()) {
        // No enemies—proceed directly to the next level
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
        showScene(new ResultPane("Defeated", "Back to Menu",
                this::showMainMenu), 700, 500);

    } else if (state == BattleState.VICTORY) {
        //Use LevelManager to determine if there is a next level
        boolean hasNext = LevelManager.getInstance().nextLevel();

        if (!hasNext) {
            // All Levels Cleared
            showScene(new ResultPane("🏆 All Levels Cleared!", "Back to Menu",
                    this::showMainMenu), 700, 500);
        } else {
            // Proceed to the Next Level
            showScene(new ResultPane("Victory!", "Next Level",
                    this::goToNextLevel), 700, 500);
        }
    }
}
    // Advances to the next level and starts it.
    private void goToNextLevel() {
        startLevel();
    }

    // Switches the displayed screen by replacing the Scene's root node.
    // This preserves window size and fullscreen state — never creates a new Scene.
    private void showScene(Parent pane, double width, double height) {
        if (stage.getScene() == null) {
            stage.setScene(new Scene(pane, width, height));
        } else {
            stage.getScene().setRoot(pane);
        }
    }

    // Java entry point that launches the JavaFX application.
    public static void main(String[] args) {
        launch(args);
    }
}
