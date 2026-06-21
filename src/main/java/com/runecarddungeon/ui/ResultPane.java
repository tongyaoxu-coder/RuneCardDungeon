package com.runecarddungeon.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * ResultPane - the result screen shown after a battle ends.
 */
public class ResultPane extends VBox {

    public ResultPane(String message, String buttonLabel, Runnable onButton) {

        // ===== 1. Configure this VBox itself =====
        this.setAlignment(Pos.CENTER);   // center all children
        this.setSpacing(30);             // vertical gap between children
        this.setStyle("-fx-padding: 48; -fx-background-color: #f4f1e8;");
        // padding and background colour 
        // ===== 2. Result message label =====
        Label messageLabel = new Label(message);
        messageLabel.setFont(new Font(36));  

        // ===== 3. The action button =====
        Button actionButton = new Button(buttonLabel);
        actionButton.setPrefWidth(200);
        // On click, run the action passed in from Main
        actionButton.setOnAction(event -> onButton.run());

        // ===== 4. Add both into this VBox =====
        this.getChildren().addAll(messageLabel, actionButton);
    }
}