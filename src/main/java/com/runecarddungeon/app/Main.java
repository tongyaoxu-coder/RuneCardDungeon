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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

// Main class of the game
public class Main extends Application {

    private Player player; 
    private Stage stage;
    private MediaPlayer bgMusic;

    //Start the game window
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("Rune Card Dungeon");
        stage.setScene(new Scene(new javafx.scene.layout.StackPane(), 700, 500));

        // Press F11 to switch fullscreen mode
        stage.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.F11) {
                stage.setFullScreen(!stage.isFullScreen());
            }
        });

        showMainMenu();
        stage.show();
        startMusic();
    }

    // Play the background music
    private void startMusic() {
        if (bgMusic != null) return; 
        java.io.File f = new java.io.File("assets/forest_battle_loop.mp3");
        if (!f.exists()) return;
        try {
            Media media = new Media(f.toURI().toString());
            bgMusic = new MediaPlayer(media);
            bgMusic.setCycleCount(MediaPlayer.INDEFINITE);
            bgMusic.setVolume(0.45);
            bgMusic.play();
        } catch (Exception e) {
            System.out.println("BGM load failed: " + e.getMessage());
        }
    }

    // Show the main menu
    private void showMainMenu() {
        MainMenuPane menu = new MainMenuPane(
            this::startGame,
            this::showLevelSelect,
            this::showTutorial,
            () -> stage.close()
        );
        showScene(menu, 700, 500);
    }

    // Show the tutorial page
    private void showTutorial() {
        TutorialPane tutorial = new TutorialPane(this::showMainMenu);
        showScene(tutorial, 700, 500);
    }

    //Show level selection
    private void showLevelSelect() {
        LevelSelectPane selectPane = new LevelSelectPane(
            levelIndex -> {
                LevelManager lm = LevelManager.getInstance();
                lm.resetToFirstLevel();
                // Reset upgrades
                com.runecarddungeon.data.UpgradeManager.getInstance().reset();
                //Move to the selected level
                int steps = levelIndex * 2;
                for (int i = 0; i < steps; i++) lm.nextLevel();
                player = new Player("Hero", 57, 5);
                startLevel();
            },
            this::showMainMenu
        );
        showScene(selectPane, 700, 500);
    }

    //Start a new game
    private void startGame() {
        LevelManager.getInstance().resetToFirstLevel();
        com.runecarddungeon.data.UpgradeManager.getInstance().reset();
        player = new Player("Hero", 57, 5);
        startLevel();
    }

// Start the current level
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

    //Restart current level
    private void restartLevel() {
        player = new Player("Hero", 57, 5);
        startLevel();
    }

 // Check battle result
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
    //Start next level
    private void goToNextLevel() {
        startLevel();
    }
    
    // Change to another screen
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
