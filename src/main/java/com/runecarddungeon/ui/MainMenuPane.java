package com.runecarddungeon.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/*
 * MainMenuPane is the main menu screen. It extends VBox, so the object
 * itself is a vertical panel holding the title and the menu buttons.
 * It does not handle navigation itself; the actions to run are passed in
 * by Main, keeping the UI separated from the flow control.
 */
public class MainMenuPane extends VBox {

    /*
     * @param onStart       action to run when "Start Game" is clicked
     * @param onSelectLevel action to run when "Select Level" is clicked
     * @param onExit        action to run when "Exit" is clicked
     */
    public MainMenuPane(Runnable onStart, Runnable onSelectLevel, Runnable onExit) {

        // Configure this VBox itself.
        this.setAlignment(Pos.CENTER);
        this.setSpacing(24);
        this.setStyle("-fx-padding: 48; -fx-background-color: #f4f1e8;");

        // Game title.
        Label title = new Label("Rune Card Dungeon");
        title.setFont(new Font(34));

        // "Start Game" button.
        Button startButton = new Button("Start Game");
        startButton.setPrefWidth(200);
        startButton.setOnAction(event -> onStart.run());

        // "Select Level" button.
        Button selectLevelButton = new Button("Select Level");
        selectLevelButton.setPrefWidth(200);
        selectLevelButton.setOnAction(event -> onSelectLevel.run());

        // "Exit" button.
        Button exitButton = new Button("Exit");
        exitButton.setPrefWidth(200);
        exitButton.setOnAction(event -> onExit.run());

        // Add the title and buttons in display order (top to bottom).
        this.getChildren().addAll(title, startButton, selectLevelButton, exitButton);
    }
}
