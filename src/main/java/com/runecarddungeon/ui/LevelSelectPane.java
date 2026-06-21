package com.runecarddungeon.ui;

import java.util.function.IntConsumer;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/*
 * LevelSelectPane lets the player jump straight into any of the three
 * levels. It extends VBox and shows one button per level plus a back
 * button. The actions are passed in by Main, so this pane does not need
 * to know anything about the game flow.
 */
public class LevelSelectPane extends VBox {

    // Display names for the three levels, shown on the buttons.
    private static final String[] LEVEL_NAMES = {
        "Level 1 - Slime",
        "Level 2 - Skeleton",
        "Level 3 - Dragon (Boss)"
    };

    /*
     * @param onSelectLevel called with the chosen level index (0, 1, or 2)
     * @param onBack        action to run when "Back" is clicked
     */
    public LevelSelectPane(IntConsumer onSelectLevel, Runnable onBack) {

        // Configure this VBox itself.
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setStyle("-fx-padding: 48; -fx-background-color: #f4f1e8;");

        // Title.
        Label title = new Label("Select Level");
        title.setFont(new Font(28));
        this.getChildren().add(title);

        // One button per level. Clicking passes that level's index to Main.
        for (int i = 0; i < LEVEL_NAMES.length; i++) {
            final int levelIndex = i;   // capture the index for the lambda
            Button levelButton = new Button(LEVEL_NAMES[i]);
            levelButton.setPrefWidth(240);
            levelButton.setOnAction(event -> onSelectLevel.accept(levelIndex));
            this.getChildren().add(levelButton);
        }

        // Back button returns to the main menu.
        Button backButton = new Button("Back");
        backButton.setPrefWidth(240);
        backButton.setOnAction(event -> onBack.run());
        this.getChildren().add(backButton);
    }
}
