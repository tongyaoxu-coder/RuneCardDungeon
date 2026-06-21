package com.runecarddungeon.ui;

import com.runecarddungeon.battle.BattleManager;
import com.runecarddungeon.battle.BattleState;
import com.runecarddungeon.model.Card;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/*
 * BattlePane is the main battle screen. It extends StackPane so a pause
 * menu can be layered on top of the battle content.
 *
 * Layers:
 *   bottom = battleLayer (a BorderPane): enemy info, player info, hand
 *   top    = pauseLayer: a semi-transparent overlay with Resume / Restart /
 *            Back to Menu, hidden until the Pause button is pressed
 *
 * Core idea: the pane reads the current state from BattleManager and draws
 * it. After every action we call refresh() to redraw from the latest data.
 */
public class BattlePane extends StackPane {

    private final BattleManager battleManager;  // the game logic
    private final Runnable onBattleEnd;         // run when the battle ends

    private final Label enemyLabel;   // enemy name + HP
    private final Label playerLabel;  // player HP / block / energy
    private final HBox handBox;       // holds the hand cards
    private final VBox pauseLayer;    // the pause overlay (top layer)

    /*
     * @param battleManager the battle logic object (created by Main)
     * @param onBattleEnd   run when the battle ends in victory or defeat
     * @param onRestart     run when "Restart" is chosen in the pause menu
     * @param onBackToMenu  run when "Back to Menu" is chosen in the pause menu
     */
    public BattlePane(BattleManager battleManager,
                      Runnable onBattleEnd,
                      Runnable onRestart,
                      Runnable onBackToMenu) {

        this.battleManager = battleManager;
        this.onBattleEnd = onBattleEnd;

        // Build the pause overlay first so it exists before it is referenced.
        pauseLayer = createPauseLayer(onRestart, onBackToMenu);
        pauseLayer.setVisible(false);   // hidden until Pause is pressed

        // ===== Bottom layer: the battle content (a BorderPane) =====
        BorderPane battleLayer = new BorderPane();
        battleLayer.setStyle("-fx-background-color: #e8e2d0;");
        battleLayer.setPadding(new Insets(20));

        // TOP: enemy info, with a Pause button on the right.
        enemyLabel = new Label();
        enemyLabel.setFont(new Font(20));

        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(event -> pauseLayer.setVisible(true));

        BorderPane topBar = new BorderPane();
        topBar.setCenter(enemyLabel);
        topBar.setRight(pauseButton);
        topBar.setPadding(new Insets(10));
        battleLayer.setTop(topBar);

        // CENTER: player info.
        playerLabel = new Label();
        playerLabel.setFont(new Font(18));
        VBox centerBox = new VBox(playerLabel);
        centerBox.setAlignment(Pos.CENTER);
        battleLayer.setCenter(centerBox);

        // BOTTOM: hand + End Turn button.
        handBox = new HBox(10);
        handBox.setAlignment(Pos.CENTER);
        handBox.setPadding(new Insets(10));

        Button endTurnButton = new Button("End Turn");
        endTurnButton.setPrefWidth(120);
        endTurnButton.setOnAction(event -> {
            battleManager.endPlayerTurn();
            refresh();
        });

        VBox bottomBox = new VBox(10, handBox, endTurnButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10));
        battleLayer.setBottom(bottomBox);

        // Stack the two layers; later children sit on top.
        this.getChildren().addAll(battleLayer, pauseLayer);

        refresh();
    }

    // Reads the latest state from BattleManager and redraws the screen.
    // Called once at start, and after every player action.
    private void refresh() {

        // Update enemy info.
        enemyLabel.setText(
            battleManager.getEnemy().getName()
            + "   HP: " + battleManager.getEnemy().getHp()
            + " / " + battleManager.getEnemy().getMaxHp());

        // Update player info.
        playerLabel.setText(
            "Player   HP: " + battleManager.getPlayer().getHp()
            + " / " + battleManager.getPlayer().getMaxHp()
            + "    Block: " + battleManager.getPlayer().getBlock()
            + "    Energy: " + battleManager.getPlayer().getEnergy());

        // Rebuild the hand with card views.
        handBox.getChildren().clear();
        for (Card card : battleManager.getHand().getCards()) {
            handBox.getChildren().add(createCardView(card));
        }

        // If the battle is over, hand control back to Main.
        BattleState state = battleManager.getState();
        if (state == BattleState.VICTORY || state == BattleState.DEFEAT) {
            onBattleEnd.run();
        }
    }

    /*
     * Builds a clickable card view (a bordered box showing the card's name,
     * energy cost, and description). Clicking it plays the card.
     */
    private VBox createCardView(Card card) {

        Label nameLabel = new Label(card.getName());
        nameLabel.setFont(new Font(13));

        Label costLabel = new Label("Cost " + card.getEnergyCost());
        costLabel.setFont(new Font(11));

        Label descLabel = new Label(card.getDescription());
        descLabel.setFont(new Font(10));
        descLabel.setWrapText(true);

        VBox cardView = new VBox(4, nameLabel, costLabel, descLabel);
        cardView.setAlignment(Pos.CENTER);
        cardView.setPrefSize(80, 110);
        cardView.setStyle(
            "-fx-border-color: #534AB7;"
            + "-fx-border-width: 1.5;"
            + "-fx-border-radius: 8;"
            + "-fx-background-color: #eeedfe;"
            + "-fx-background-radius: 8;"
            + "-fx-padding: 8;");

        cardView.setOnMouseClicked(event -> {
            battleManager.playCard(card);
            refresh();
        });

        return cardView;
    }

    /*
     * Builds the pause overlay: a semi-transparent layer with a title and
     * Resume / Restart / Back to Menu buttons.
     *
     * @param onRestart    run when "Restart" is clicked
     * @param onBackToMenu run when "Back to Menu" is clicked
     */
    private VBox createPauseLayer(Runnable onRestart, Runnable onBackToMenu) {

        Label pausedLabel = new Label("Paused");
        pausedLabel.setFont(new Font(28));
        pausedLabel.setStyle("-fx-text-fill: white;");

        Button resumeButton = new Button("Resume");
        resumeButton.setPrefWidth(200);
        resumeButton.setOnAction(event -> pauseLayer.setVisible(false));

        Button restartButton = new Button("Restart");
        restartButton.setPrefWidth(200);
        restartButton.setOnAction(event -> onRestart.run());

        Button menuButton = new Button("Back to Menu");
        menuButton.setPrefWidth(200);
        menuButton.setOnAction(event -> onBackToMenu.run());

        VBox layer = new VBox(16, pausedLabel, resumeButton,
                restartButton, menuButton);
        layer.setAlignment(Pos.CENTER);
        // Semi-transparent black background covering the battle content.
        layer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.55);");

        return layer;
    }
}
