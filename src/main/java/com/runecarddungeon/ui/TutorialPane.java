package com.runecarddungeon.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class TutorialPane extends StackPane {

    private static final Font PIXEL;
    private static final Font CINZEL;
    private static final String PIXEL_FAM;
    private static final String CINZEL_FAM;
    static {
        Font cf = null, pf = null;
        java.io.File f1 = new java.io.File("assets/fonts/Cinzel-Regular.ttf");
        java.io.File f2 = new java.io.File("assets/fonts/PressStart2P-Regular.ttf");
        if (f1.exists()) cf = Font.loadFont(f1.toURI().toString(), 13);
        if (f2.exists()) pf = Font.loadFont(f2.toURI().toString(), 14);
        CINZEL = cf; CINZEL_FAM = cf != null ? cf.getFamily() : "Georgia";
        PIXEL  = pf; PIXEL_FAM  = pf != null ? pf.getFamily() : "Georgia";
    }

    public TutorialPane(Runnable onBack) {

        // ── background ──
        java.io.File bgFile = new java.io.File("assets/bg_menu.png");
        if (bgFile.exists()) {
            ImageView bg = new ImageView(new Image(bgFile.toURI().toString()));
            bg.setPreserveRatio(false);
            bg.fitWidthProperty().bind(widthProperty());
            bg.fitHeightProperty().bind(heightProperty());
            this.getChildren().add(bg);
        } else {
            this.setStyle("-fx-background-color:#3a6b45;");
        }

        Region overlay = new Region();
        overlay.setStyle("-fx-background-color:rgba(8,16,4,0.58);");
        overlay.prefWidthProperty().bind(widthProperty());
        overlay.prefHeightProperty().bind(heightProperty());
        this.getChildren().add(overlay);

        // ── ART TITLE ──
        Label title = new Label("HOW TO PLAY");
        title.setFont(Font.font(PIXEL_FAM, 26));
        title.setStyle(
            "-fx-font-family:'" + PIXEL_FAM + "';"
            + "-fx-font-size:26px;-fx-text-fill:#ffd700;"
            + "-fx-effect:"
            + "  dropshadow(gaussian,rgba(255,180,0,0.9),20,0.6,0,2),"
            + "  dropshadow(gaussian,rgba(0,0,0,0.95),6,0.4,0,2);");

        Region div = new Region();
        div.setPrefHeight(2); div.setMaxWidth(520);
        div.setStyle("-fx-background-color:linear-gradient("
                + "from 0% 0% to 100% 0%,transparent,#c8a020,transparent);");

        // ── two columns ──
        VBox left  = buildLeft();
        VBox right = buildRight();
        left.setPrefWidth(320);
        right.setPrefWidth(320);
        HBox columns = new HBox(40, left, right);
        columns.setAlignment(Pos.TOP_CENTER);

        // ── back button ──
        Button back = new Button("←   Back to Menu");
        back.setPrefWidth(220);
        String bBase = "-fx-font-family:'" + CINZEL_FAM + "';"
                + "-fx-font-size:13px;-fx-font-weight:bold;"
                + "-fx-background-color:#1e4a10dd;-fx-text-fill:#c8f088;"
                + "-fx-background-radius:8;-fx-padding:10 0;-fx-cursor:hand;";
        String bHov  = "-fx-font-family:'" + CINZEL_FAM + "';"
                + "-fx-font-size:13px;-fx-font-weight:bold;"
                + "-fx-background-color:#1e4a10;-fx-text-fill:white;"
                + "-fx-background-radius:8;-fx-padding:10 0;-fx-cursor:hand;"
                + "-fx-effect:dropshadow(gaussian,#c8f088,10,0.4,0,0);";
        back.setStyle(bBase);
        back.setOnMouseEntered(e -> back.setStyle(bHov));
        back.setOnMouseExited(e  -> back.setStyle(bBase));
        back.setOnAction(e -> onBack.run());

        // ── centred content, capped width ──
        VBox content = new VBox(20, title, div, columns, back);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(720);
        content.setMaxHeight(Region.USE_PREF_SIZE);
        content.setPadding(new Insets(30));

        this.getChildren().add(content);
        this.setAlignment(Pos.CENTER);   // ← always centred at any window size
    }

    private VBox buildLeft() {
        VBox col = new VBox(8);
        col.setAlignment(Pos.TOP_LEFT);
        col.getChildren().addAll(
            sec("🎮  Basic Rules"),
            tip("Turn-based — you act first, then the enemy attacks."),
            tip("Reduce the enemy's HP to 0 to win the level."),
            tip("Your HP carries over between levels."),
            gap(),
            sec("⚡  Energy & Cards"),
            tip("You start each turn with Energy (● dots)."),
            tip("Each card costs Energy equal to its number."),
            tip("Click a card to play it. End Turn when done."),
            gap(),
            sec("🛡  Block"),
            tip("Block absorbs damage before HP is reduced."),
            tip("Block resets to 0 at the start of your turn."),
            tip("Stack Block cards before the enemy attacks.")
        );
        return col;
    }

    private VBox buildRight() {
        VBox col = new VBox(8);
        col.setAlignment(Pos.TOP_LEFT);
        col.getChildren().addAll(
            sec("🃏  Card Types"),
            cardRow("⚔", "#cc3333", "Attack",  "Deals damage to the enemy."),
            cardRow("🛡", "#2266cc", "Defense", "Adds Block to reduce damage."),
            cardRow("💚", "#22aa55", "Heal",    "Restores your HP."),
            cardRow("☠",  "#8833cc", "Curse",   "Weakens the enemy's attack."),
            gap(),
            sec("💡  Tips"),
            tip("Check enemy Intent — it shows their next move."),
            tip("If you can't afford a card, End Turn to reset Energy."),
            tip("Prioritise Block when the enemy hits hard."),
            tip("Upgrade levels appear between battles.")
        );
        return col;
    }

    private Label sec(String text) {
        Label l = new Label(text);
        l.setFont(Font.font(CINZEL_FAM, 14));
        l.setStyle("-fx-font-family:'" + CINZEL_FAM + "';"
                + "-fx-font-size:14px;-fx-font-weight:bold;-fx-text-fill:#f0d080;"
                + "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.7),4,0.3,0,1);");
        return l;
    }

    private Label tip(String text) {
        Label l = new Label("  •  " + text);
        l.setFont(Font.font(CINZEL_FAM, 12));
        l.setStyle("-fx-font-family:'" + CINZEL_FAM + "';"
                + "-fx-font-size:12px;-fx-text-fill:#e8e8d8;");
        l.setWrapText(true);
        l.setMaxWidth(305);
        return l;
    }

    private HBox cardRow(String icon, String colour, String type, String desc) {
        Label badge = new Label(icon + "  " + type);
        badge.setFont(Font.font(CINZEL_FAM, 11));
        badge.setStyle("-fx-font-family:'" + CINZEL_FAM + "';"
                + "-fx-font-size:11px;-fx-font-weight:bold;-fx-text-fill:white;"
                + "-fx-background-color:" + colour + ";-fx-background-radius:4;"
                + "-fx-padding:2 8;-fx-min-width:70;");
        Label d = new Label(desc);
        d.setFont(Font.font(CINZEL_FAM, 12));
        d.setStyle("-fx-font-family:'" + CINZEL_FAM + "';"
                + "-fx-font-size:12px;-fx-text-fill:#e8e8d8;");
        HBox row = new HBox(10, badge, d);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(0, 0, 0, 8));
        return row;
    }

    private Region gap() {
        Region r = new Region();
        r.setPrefHeight(6);
        return r;
    }
}
