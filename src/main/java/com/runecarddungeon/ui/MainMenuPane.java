package com.runecarddungeon.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class MainMenuPane extends StackPane {

    // 在类加载时注册字体，这样 CSS 的 -fx-font-family 才能引用它们
    private static final boolean FONTS_LOADED;
    // Loaded font objects — use .getFamily() in CSS so the name always matches
    private static final Font PIXEL_FONT;
    private static final Font CINZEL_FONT;
    private static final String PIXEL_FAMILY;
    private static final String CINZEL_FAMILY;
    static {
        Font pf = null, cf = null;
        java.io.File p1 = new java.io.File("assets/fonts/PressStart2P-Regular.ttf");
        java.io.File p2 = new java.io.File("assets/fonts/Cinzel-Regular.ttf");
        if (p1.exists()) pf = Font.loadFont(p1.toURI().toString(), 20);
        if (p2.exists()) cf = Font.loadFont(p2.toURI().toString(), 13);
        PIXEL_FONT   = pf;
        CINZEL_FONT  = cf;
        PIXEL_FAMILY = pf != null ? pf.getFamily() : "Georgia";
        CINZEL_FAMILY= cf != null ? cf.getFamily() : "Georgia";
        FONTS_LOADED = true;
    }

    public MainMenuPane(Runnable onStart, Runnable onSelectLevel,
                        Runnable onTutorial, Runnable onExit) {

        this.setPrefSize(700, 500);

        // ── background ──
        java.io.File bgFile = new java.io.File("assets/bg_menu.png");
        if (bgFile.exists()) {
            ImageView bg = new ImageView(new Image(bgFile.toURI().toString()));
            bg.setPreserveRatio(false);
            bg.fitWidthProperty().bind(widthProperty());
            bg.fitHeightProperty().bind(heightProperty());
            this.getChildren().add(bg);
        }

        // centre vignette
        Region vig = new Region();
        vig.setStyle("-fx-background-color: radial-gradient("
                + "center 50% 50%, radius 50%,"
                + "rgba(0,0,0,0.5) 0%, transparent 100%);");
        vig.prefWidthProperty().bind(widthProperty());
        vig.prefHeightProperty().bind(heightProperty());
        this.getChildren().add(vig);

        // ── title — Press Start 2P via CSS ──
        Label title = new Label("RUNE CARD\nDUNGEON");
        if (PIXEL_FONT != null) title.setFont(Font.font(PIXEL_FAMILY, 26));
        title.setStyle(
            "-fx-font-family:'" + PIXEL_FAMILY + "';"
            + "-fx-font-size:26px;"
            + "-fx-text-fill:#ffd700;"
            + "-fx-text-alignment:center;"
            + "-fx-alignment:center;"
            + "-fx-effect:"
            + "  dropshadow(gaussian,rgba(255,140,0,0.95),20,0.55,0,3),"
            + "  dropshadow(gaussian,rgba(0,0,0,0.95),8,0.4,0,2);");
        title.setTextAlignment(TextAlignment.CENTER);
        title.setWrapText(true);

        // ── subtitle — Cinzel italic ──
        Label sub = new Label("— A dark card-crawling adventure —");
        if (CINZEL_FONT != null) sub.setFont(Font.font(CINZEL_FAMILY, 13));
        sub.setStyle(
            "-fx-font-family:'" + CINZEL_FAMILY + "';"
            + "-fx-font-size:13px;"
            + "-fx-font-style:italic;"
            + "-fx-text-fill:#aaddbb;"
            + "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.85),5,0.3,0,1);");

        // thin gold line
        Region div = new Region();
        div.setPrefHeight(1); div.setPrefWidth(260);
        div.setStyle("-fx-background-color:linear-gradient("
                + "from 0% 0% to 100% 0%,transparent,#c8a020,transparent);");

        // ── buttons — Cinzel via CSS ──
        Button startBtn    = btn("▶   Start Game",   "#1e5c1e", "#a8ffa8");
        Button levelBtn    = btn("☰   Select Level", "#1a2e5a", "#88c8ff");
        Button tutorialBtn = btn("?   How to Play",  "#4a3200", "#ffd060");
        Button exitBtn     = btn("✕   Exit",          "#5a1a1a", "#ffaaaa");

        startBtn.setOnAction(e    -> onStart.run());
        levelBtn.setOnAction(e    -> onSelectLevel.run());
        tutorialBtn.setOnAction(e -> onTutorial.run());
        exitBtn.setOnAction(e     -> onExit.run());

        VBox content = new VBox(16, title, sub, div,
                                startBtn, levelBtn, tutorialBtn, exitBtn);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(300);

        this.getChildren().add(content);
        this.setAlignment(Pos.CENTER);
    }

    private Button btn(String text, String bg, String fg) {
        Button b = new Button(text);
        b.setPrefWidth(260); b.setMaxWidth(260);

        // ← KEY FIX: font-family in CSS, not setFont()
        String base = "-fx-font-family:'" + CINZEL_FAMILY + "';"
                + "-fx-font-size:13px;-fx-font-weight:bold;"
                + "-fx-background-color:" + bg + "cc;"
                + "-fx-text-fill:" + fg + ";"
                + "-fx-background-radius:8;-fx-padding:10 0;-fx-cursor:hand;"
                + "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.6),6,0,0,2);";
        String hover = "-fx-font-family:'" + CINZEL_FAMILY + "';"
                + "-fx-font-size:13px;-fx-font-weight:bold;"
                + "-fx-background-color:" + bg + ";"
                + "-fx-text-fill:white;"
                + "-fx-background-radius:8;-fx-padding:10 0;-fx-cursor:hand;"
                + "-fx-effect:dropshadow(gaussian," + fg + ",14,0.5,0,0);";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e  -> b.setStyle(base));
        return b;
    }
}
