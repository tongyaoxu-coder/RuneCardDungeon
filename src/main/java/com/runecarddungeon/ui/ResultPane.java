package com.runecarddungeon.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

//shows win / lose / clear screen
public class ResultPane extends StackPane {

    private static final Font PIXEL;
    private static final Font CINZEL;
    private static final String PIXEL_FAM;
    private static final String CINZEL_FAM;
    static {
        Font cf = null, pf = null;
        java.io.File f1 = new java.io.File("assets/fonts/Cinzel-Regular.ttf");
        java.io.File f2 = new java.io.File("assets/fonts/PressStart2P-Regular.ttf");
        if (f1.exists()) cf = Font.loadFont(f1.toURI().toString(), 14);
        if (f2.exists()) pf = Font.loadFont(f2.toURI().toString(), 14);
        CINZEL = cf; CINZEL_FAM = cf != null ? cf.getFamily() : "Georgia";
        PIXEL  = pf; PIXEL_FAM  = pf != null ? pf.getFamily() : "Georgia";
    }

    public ResultPane(String message, String buttonLabel, Runnable onButton) {

        // ── background ──
        java.io.File bg = new java.io.File("assets/bg_menu.png");
        if (bg.exists()) {
            ImageView bgImg = new ImageView(new Image(bg.toURI().toString()));
            bgImg.setPreserveRatio(false);
            bgImg.fitWidthProperty().bind(widthProperty());
            bgImg.fitHeightProperty().bind(heightProperty());
            this.getChildren().add(bgImg);
        } else {
            this.setStyle("-fx-background-color:#1a2a1a;");
        }

        Region overlay = new Region();
        overlay.setStyle("-fx-background-color:rgba(0,0,0,0.62);");
        overlay.prefWidthProperty().bind(widthProperty());
        overlay.prefHeightProperty().bind(heightProperty());
        this.getChildren().add(overlay);

        // ── detect result type ──
        String lc = message.toLowerCase();
        boolean victory   = lc.contains("victory") || lc.contains("cleared")
                         || lc.contains("congrat") || lc.contains("🏆");
        boolean gameClear = lc.contains("all level") || lc.contains("🏆");

        String icon       = gameClear ? "👑" : victory ? "⚔" : "💀";
        String titleText  = gameClear ? "GAME CLEAR" : victory ? "VICTORY" : "DEFEATED";
        String titleColor = gameClear ? "#ffd700" : victory ? "#7fff7f" : "#ff5555";
        String glowColor  = gameClear ? "gold"     : victory ? "#00ff44" : "#ff0000";

        // ── icon ──
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size:80px;"
                + "-fx-effect:dropshadow(gaussian," + glowColor + ",24,0.5,0,0);");

        // ── ART-STYLE TITLE (big pixel font + controlled glow) ──
        Label title = new Label(titleText);
        title.setFont(Font.font(PIXEL_FAM, 40));
        title.setTextAlignment(TextAlignment.CENTER);
        // Use a tighter glow radius so text stays legible, plus a dark shadow for contrast
        title.setStyle(
            "-fx-font-family:'" + PIXEL_FAM + "';"
            + "-fx-font-size:40px;"
            + "-fx-text-fill:" + titleColor + ";"
            + "-fx-effect:"
            + "dropshadow(gaussian,rgba(0,0,0,1.0),4,0.8,0,2),"   // dark outline first
            + "dropshadow(gaussian," + glowColor + ",12,0.5,0,0);" // then a subtle glow
        );

        // ── subtitle (Cinzel italic) ──
        String subText = gameClear ? "You conquered the entire dungeon!"
                       : victory   ? "The enemy has fallen before you."
                                    : "The dungeon claims another soul...";
        Label subtitle = new Label(subText);
        subtitle.setFont(Font.font(CINZEL_FAM, 15));
        subtitle.setStyle(
            "-fx-font-family:'" + CINZEL_FAM + "';"
            + "-fx-font-size:15px;-fx-font-style:italic;-fx-text-fill:#dddddd;"
            + "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.9),5,0.3,0,1);");

        // ── divider ──
        Region divider = new Region();
        divider.setPrefHeight(2); divider.setPrefWidth(320);
        divider.setStyle("-fx-background-color:linear-gradient("
                + "from 0% 0% to 100% 0%,transparent," + titleColor + ",transparent);");

        // ── button ──
        String btnBg = (victory || gameClear) ? "#1e5c1e" : "#5c1e1e";
        Button btn = new Button(buttonLabel);
        btn.setPrefWidth(240);
        String btnBase = "-fx-font-family:'" + CINZEL_FAM + "';"
                + "-fx-font-size:14px;-fx-font-weight:bold;"
                + "-fx-background-color:" + btnBg + "dd;-fx-text-fill:#eeeeee;"
                + "-fx-background-radius:8;-fx-padding:12 0;-fx-cursor:hand;"
                + "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.5),6,0,0,2);";
        String btnHover = "-fx-font-family:'" + CINZEL_FAM + "';"
                + "-fx-font-size:14px;-fx-font-weight:bold;"
                + "-fx-background-color:" + btnBg + ";-fx-text-fill:white;"
                + "-fx-background-radius:8;-fx-padding:12 0;-fx-cursor:hand;"
                + "-fx-effect:dropshadow(gaussian," + titleColor + ",14,0.5,0,0);";
        btn.setStyle(btnBase);
        btn.setOnMouseEntered(e -> btn.setStyle(btnHover));
        btn.setOnMouseExited(e  -> btn.setStyle(btnBase));
        btn.setOnAction(e -> onButton.run());

        VBox content = new VBox(18, iconLabel, title, subtitle, divider, btn);
        content.setAlignment(Pos.CENTER);

        this.getChildren().add(content);
        this.setAlignment(Pos.CENTER);
    }
}
