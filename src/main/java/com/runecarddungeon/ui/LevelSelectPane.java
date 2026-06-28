package com.runecarddungeon.ui;

import java.util.function.IntConsumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/*
 * LevelSelectPane — pick a level. Forest background, no parchment box.
 *
 * The game has 3 battle levels (matching LevelData):
 *   Level 1 — Slime House     (4× Slime,    beginner)
 *   Level 2 — Skeleton Fortress (3× Skeleton, gains shield each turn)
 *   Level 3 — Fire Worm Elite    (1× FireWorm,  final boss)
 */
public class LevelSelectPane extends StackPane {
    private static final javafx.scene.text.Font _CINZEL_F;
    private static final String _CINZEL_FAM;
    private static final javafx.scene.text.Font _PIXEL_F;
    private static final String _PIXEL_FAM;
    static {
        javafx.scene.text.Font cf = null, pf = null;
        java.io.File f1 = new java.io.File("assets/fonts/Cinzel-Regular.ttf");
        java.io.File f2 = new java.io.File("assets/fonts/PressStart2P-Regular.ttf");
        if (f1.exists()) cf = javafx.scene.text.Font.loadFont(f1.toURI().toString(), 12);
        if (f2.exists()) pf = javafx.scene.text.Font.loadFont(f2.toURI().toString(), 14);
        _CINZEL_F = cf; _CINZEL_FAM = cf != null ? cf.getFamily() : "Georgia";
        _PIXEL_F = pf;  _PIXEL_FAM  = pf != null ? pf.getFamily() : "Georgia";
    }

    static {
        try {
            java.io.File f1 = new java.io.File("assets/fonts/Cinzel-Regular.ttf");
            java.io.File f2 = new java.io.File("assets/fonts/PressStart2P-Regular.ttf");
            if (f1.exists()) javafx.scene.text.Font.loadFont(f1.toURI().toString(), 1);
            if (f2.exists()) javafx.scene.text.Font.loadFont(f2.toURI().toString(), 1);
        } catch (Exception ignored) {}
    }


    // index, title, subtitle, accent colour, icon
    // These match LevelData: card 0→LEVEL_1 Slime, 1→LEVEL_3 Skeleton, 2→LEVEL_5 FireWorm
    private static final String[][] LEVELS = {
        {"0", "Slime House",       "The slime lair in the border forest. Defeat all the slimes!",        "#3aaa50", "🟢"},
        {"1", "Skeleton Fortress", "Skeleton warriors guard the fortress — they gain shield each turn!", "#aaaaaa", "💀"},
        {"2", "Fire Worm Elite",   "Final boss! Survive the flames and slay the ancient Fire Worm!",     "#ff5500", "🔥"},
    };

    public LevelSelectPane(IntConsumer onSelectLevel, Runnable onBack) {

        this.setPrefSize(700, 500);

        // ── Background ──
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

        // Dark overlay for readability
        Region overlay = new Region();
        overlay.setStyle("-fx-background-color:rgba(8,16,4,0.55);");
        overlay.prefWidthProperty().bind(widthProperty());
        overlay.prefHeightProperty().bind(heightProperty());
        this.getChildren().add(overlay);

        // ── Content ──
        Label title = new Label("⚔   Select Level   ⚔");
        title.setStyle("-fx-font-size:28px;-fx-font-weight:bold;-fx-text-fill:#f0e0a0;"
                + "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.8),8,0.4,0,2);");

        VBox cards = new VBox(14);
        cards.setAlignment(Pos.CENTER);
        for (String[] lv : LEVELS) {
            cards.getChildren().add(levelCard(
                Integer.parseInt(lv[0]), lv[1], lv[2], lv[3], lv[4], onSelectLevel));
        }

        Button back = new Button("←   Back to Menu");
        back.setPrefWidth(220);
        styleBack(back);
        back.setOnAction(e -> onBack.run());

        VBox content = new VBox(22, title, cards, back);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        this.getChildren().add(content);
        this.setAlignment(Pos.CENTER);
    }

    // ── one level entry (clickable wide card) ────────────────────────────────

    private HBox levelCard(int index, String name, String desc,
                           String accent, String icon, IntConsumer onSelect) {

        Label iconLbl = new Label(icon);
        iconLbl.setStyle("-fx-font-size:30px;");

        Label nameLbl = new Label("Level " + (index + 1) + " — " + name);
        nameLbl.setStyle("-fx-font-family:'" + _CINZEL_FAM + "';-fx-text-fill:" + accent + ";-fx-font-size:15px;-fx-font-weight:bold;");

        Label descLbl = new Label(desc);
        descLbl.setStyle("-fx-font-family:'" + _CINZEL_FAM + "';-fx-text-fill:#e0e0d0;-fx-font-size:11px;");
        descLbl.setWrapText(true);
        descLbl.setMaxWidth(360);

        VBox text = new VBox(3, nameLbl, descLbl);
        text.setAlignment(Pos.CENTER_LEFT);

        HBox card = new HBox(16, iconLbl, text);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(12, 18, 12, 18));
        card.setMaxWidth(460);
        card.setPrefWidth(460);

        String base = "-fx-background-color:rgba(0,0,12,0.6);"
                + "-fx-border-color:" + accent + ";-fx-border-width:2;"
                + "-fx-background-radius:10;-fx-border-radius:10;-fx-cursor:hand;";
        String hover = "-fx-background-color:rgba(20,20,40,0.85);"
                + "-fx-border-color:white;-fx-border-width:2.5;"
                + "-fx-background-radius:10;-fx-border-radius:10;-fx-cursor:hand;"
                + "-fx-effect:dropshadow(gaussian," + accent + ",16,0.4,0,0);";
        card.setStyle(base);
        card.setOnMouseEntered(e -> card.setStyle(hover));
        card.setOnMouseExited(e  -> card.setStyle(base));
        card.setOnMouseClicked(e -> onSelect.accept(index));
        return card;
    }

    private void styleBack(Button b) {
        String base = "-fx-background-color:#1e4a10cc;-fx-text-fill:#c8f088;"
                + "-fx-font-size:13px;-fx-font-weight:bold;"
                + "-fx-background-radius:8;-fx-padding:8 0;-fx-cursor:hand;";
        String hover = "-fx-background-color:#1e4a10ff;-fx-text-fill:white;"
                + "-fx-font-size:13px;-fx-font-weight:bold;"
                + "-fx-background-radius:8;-fx-padding:8 0;-fx-cursor:hand;"
                + "-fx-effect:dropshadow(gaussian,#c8f088,10,0.4,0,0);";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e  -> b.setStyle(base));
    }
}
