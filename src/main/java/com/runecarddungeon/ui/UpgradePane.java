package com.runecarddungeon.ui;

import com.runecarddungeon.data.UpgradeManager;
import com.runecarddungeon.model.Player;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class UpgradePane extends StackPane {

    private static final Font CINZEL;
    private static final Font PIXEL;
    private static final String CINZEL_FAM;
    private static final String PIXEL_FAM;
    static {
        Font cf = null, pf = null;
        java.io.File f1 = new java.io.File("assets/fonts/Cinzel-Regular.ttf");
        java.io.File f2 = new java.io.File("assets/fonts/PressStart2P-Regular.ttf");
        if (f1.exists()) cf = Font.loadFont(f1.toURI().toString(), 14);
        if (f2.exists()) pf = Font.loadFont(f2.toURI().toString(), 14);
        CINZEL = cf; CINZEL_FAM = cf != null ? cf.getFamily() : "Georgia";
        PIXEL  = pf; PIXEL_FAM  = pf != null ? pf.getFamily() : "Georgia";
    }

    public UpgradePane(Player player, int level, Runnable onUpgradeComplete) {

        // Background
        java.io.File bgFile = new java.io.File("assets/bg_menu.png");
        if (bgFile.exists()) {
            ImageView bg = new ImageView(new Image(bgFile.toURI().toString()));
            bg.setPreserveRatio(false);
            bg.fitWidthProperty().bind(widthProperty());
            bg.fitHeightProperty().bind(heightProperty());
            this.getChildren().add(bg);
        } else {
            this.setStyle("-fx-background-color:#1a1a2e;");
        }

        Region overlay = new Region();
        overlay.setStyle("-fx-background-color:rgba(0,0,10,0.60);");
        overlay.prefWidthProperty().bind(widthProperty());
        overlay.prefHeightProperty().bind(heightProperty());
        this.getChildren().add(overlay);

        UpgradeManager um = UpgradeManager.getInstance();

        // Title
        Label title = new Label("⚔  Choose Your Upgrade");
        title.setStyle(
            "-fx-font-family:'" + PIXEL_FAM + "';"
            + "-fx-font-size:16px;-fx-text-fill:#ffd700;-fx-font-weight:bold;"
            + "-fx-effect:dropshadow(gaussian,rgba(255,180,0,0.9),14,0.5,0,2),"
            +            "dropshadow(gaussian,rgba(0,0,0,0.9),6,0.3,0,2);");

        Label desc = new Label("Select one upgrade to continue your adventure!");
        desc.setStyle(
            "-fx-font-family:'" + CINZEL_FAM + "';"
            + "-fx-font-size:13px;-fx-text-fill:#ccddcc;-fx-font-style:italic;");

        Region div = new Region();
        div.setPrefHeight(2); div.setPrefWidth(380);
        div.setStyle("-fx-background-color:linear-gradient("
                + "from 0% 0% to 100% 0%,transparent,#c8a020,transparent);");

        VBox optionsBox = new VBox(16);
        optionsBox.setAlignment(Pos.CENTER);

        if (level == 2) {
            Button optA = upgradeBtn(
                "⚔   Attack Upgrade: Damage 8 → 11", "#8b1a1a", "#ffaaaa");
            optA.setOnAction(e -> { um.applyUpgradeLevel2(player, true); onUpgradeComplete.run(); });

            Button optB = upgradeBtn(
                "❤   Max HP + 15", "#1a5c1a", "#aaffaa");
            optB.setOnAction(e -> { um.applyUpgradeLevel2(player, false); onUpgradeComplete.run(); });

            Label hintA = hint("Your Attack card will deal 11 damage instead of 8.");
            Label hintB = hint("Increase your maximum health by 15 points.");

            optionsBox.getChildren().addAll(optA, hintA, optB, hintB);

        } else if (level == 4) {
            Button optA = upgradeBtn(
                "☠   Curse Upgrade: Weaken 6 → 10", "#5c1a8b", "#ddaaff");
            optA.setOnAction(e -> { um.applyUpgradeLevel4(player, true); onUpgradeComplete.run(); });

            Button optB = upgradeBtn(
                "⚡   Energy + 1 per turn", "#1a3a6a", "#aaccff");
            optB.setOnAction(e -> { um.applyUpgradeLevel4(player, false); onUpgradeComplete.run(); });

            Label hintA = hint("Your Curse card will weaken the enemy by 10 instead of 6.");
            Label hintB = hint("Gain 1 additional Energy at the start of each turn.");

            optionsBox.getChildren().addAll(optA, hintA, optB, hintB);
        }

        VBox content = new VBox(20, title, desc, div, optionsBox);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(520);

        this.getChildren().add(content);
        this.setAlignment(Pos.CENTER);
    }

    private Button upgradeBtn(String text, String bg, String fg) {
        Button b = new Button(text);
        b.setPrefWidth(420);
        String base = "-fx-font-family:'" + CINZEL_FAM + "';"
                + "-fx-font-size:14px;-fx-font-weight:bold;"
                + "-fx-background-color:" + bg + "cc;"
                + "-fx-text-fill:" + fg + ";"
                + "-fx-background-radius:10;-fx-padding:14 20;-fx-cursor:hand;"
                + "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.6),6,0,0,2);";
        String hover = "-fx-font-family:'" + CINZEL_FAM + "';"
                + "-fx-font-size:14px;-fx-font-weight:bold;"
                + "-fx-background-color:" + bg + ";"
                + "-fx-text-fill:white;"
                + "-fx-background-radius:10;-fx-padding:14 20;-fx-cursor:hand;"
                + "-fx-effect:dropshadow(gaussian," + fg + ",16,0.5,0,0);";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e  -> b.setStyle(base));
        return b;
    }

    private Label hint(String text) {
        Label l = new Label("  " + text);
        l.setStyle("-fx-font-family:'" + CINZEL_FAM + "';"
                + "-fx-font-size:11px;-fx-text-fill:#888899;-fx-font-style:italic;");
        return l;
    }
}
