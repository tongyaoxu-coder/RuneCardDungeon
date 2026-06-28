package com.runecarddungeon.ui;

import java.util.function.IntConsumer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class LevelSelectPane extends StackPane {

    private static final String CINZEL;
    private static final String PIXEL;
    static {
        String c = "Georgia", p = "Georgia";
        try {
            java.io.File f1 = new java.io.File("assets/fonts/Cinzel-Regular.ttf");
            java.io.File f2 = new java.io.File("assets/fonts/PressStart2P-Regular.ttf");
            if (f1.exists()) { var fnt = javafx.scene.text.Font.loadFont(f1.toURI().toString(), 12); if (fnt != null) c = fnt.getFamily(); }
            if (f2.exists()) { var fnt = javafx.scene.text.Font.loadFont(f2.toURI().toString(), 12); if (fnt != null) p = fnt.getFamily(); }
        } catch (Exception ignored) {}
        CINZEL = c; PIXEL = p;
    }

    public LevelSelectPane(IntConsumer onSelectLevel, Runnable onBack) {
        this.setPrefSize(700, 500);

        // background
        java.io.File bgFile = new java.io.File("assets/bg_menu.png");
        if (bgFile.exists()) {
            ImageView bg = new ImageView(new Image(bgFile.toURI().toString()));
            bg.setPreserveRatio(false);
            bg.fitWidthProperty().bind(widthProperty());
            bg.fitHeightProperty().bind(heightProperty());
            this.getChildren().add(bg);
        } else {
            this.setStyle("-fx-background-color:#1a2e1a;");
        }
        Region overlay = new Region();
        overlay.setStyle("-fx-background-color:rgba(4,10,4,0.60);");
        overlay.prefWidthProperty().bind(widthProperty());
        overlay.prefHeightProperty().bind(heightProperty());
        this.getChildren().add(overlay);

        // title
        Label title = new Label("SELECT YOUR STARTING POINT");
        title.setStyle(
            "-fx-font-family:'" + PIXEL + "';" +
            "-fx-font-size:13px;-fx-text-fill:#f0e0a0;-fx-font-weight:bold;" +
            "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.9),8,0.4,0,2);");

        Label subtitle = new Label("Choose a level  —  upgrades between battles carry forward automatically");
        subtitle.setStyle(
            "-fx-font-family:'" + CINZEL + "';" +
            "-fx-font-size:11px;-fx-text-fill:#aabbaa;-fx-font-style:italic;");

        // flow row
        HBox flow = buildFlowRow(onSelectLevel);

        // back button
        Button back = new Button("<   Back to Menu");
        back.setPrefWidth(240);
        String backBase =
            "-fx-font-family:'" + CINZEL + "';" +
            "-fx-background-color:#1e4a10cc;-fx-text-fill:#c8f088;" +
            "-fx-font-size:13px;-fx-font-weight:bold;" +
            "-fx-background-radius:8;-fx-padding:10 0;-fx-cursor:hand;";
        String backHov =
            "-fx-font-family:'" + CINZEL + "';" +
            "-fx-background-color:#1e6a18;-fx-text-fill:white;" +
            "-fx-font-size:13px;-fx-font-weight:bold;" +
            "-fx-background-radius:8;-fx-padding:10 0;-fx-cursor:hand;" +
            "-fx-effect:dropshadow(gaussian,#c8f088,10,0.4,0,0);";
        back.setStyle(backBase);
        back.setOnMouseEntered(e -> back.setStyle(backHov));
        back.setOnMouseExited(e  -> back.setStyle(backBase));
        back.setOnAction(e -> onBack.run());

        VBox content = new VBox(20, title, subtitle, flow, back);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(36));

        this.getChildren().add(content);
        this.setAlignment(Pos.CENTER);
    }

    // ── flow row ──────────────────────────────────────────────────────────

    private HBox buildFlowRow(IntConsumer onSelectLevel) {
        HBox row = new HBox(0);
        row.setAlignment(Pos.CENTER);

        // Slime: 14 frames, 156x156 each
        StackPane n1 = battleNode("LV 1", "Slime House",
            "assets/Slime/idle.png", 156, 156,
            "Beginner", "#22cc55", "#0a2a10", 0, onSelectLevel);
        // Upgrade I
        StackPane u1 = upgradeNode("UPGRADE I");
        // Skeleton: 4 frames, 150x150 each
        StackPane n2 = battleNode("LV 2", "Skeleton Fortress",
            "assets/Skeleton/Idle.png", 150, 150,
            "Mid-game", "#cccccc", "#1a1a1a", 1, onSelectLevel);
        // Upgrade II
        StackPane u2 = upgradeNode("UPGRADE II");
        // FireWorm: 9 frames, 90x90 each
        StackPane n3 = bossNode("LV 3", "Fire Worm Elite",
            "assets/Fire_worm/Worm/Idle.png", 90, 90,
            "#ff5500", "#2a0800", 2, onSelectLevel);

        row.getChildren().addAll(n1, arrow(), u1, arrow(), n2, arrow(), u2, arrow(), n3);
        return row;
    }

    // ── battle node with sprite ───────────────────────────────────────────

    private StackPane battleNode(String lvl, String name,
                                  String spritePath, int fw, int fh,
                                  String difficulty, String accent, String darkBg,
                                  int index, IntConsumer onSelect) {

        // sprite — show first frame only
        ImageView sprite = loadSprite(spritePath, fw, fh, 130);

        // coloured top bar
        Label bar = new Label(lvl);
        bar.setStyle(
            "-fx-background-color:" + accent + ";" +
            "-fx-font-family:'" + PIXEL + "';" +
            "-fx-font-size:8px;-fx-text-fill:" + darkBg + ";-fx-font-weight:bold;" +
            "-fx-padding:4 0;-fx-pref-width:140;-fx-alignment:center;");

        // name label
        Label nameLbl = new Label(name);
        nameLbl.setStyle(
            "-fx-font-family:'" + CINZEL + "';" +
            "-fx-font-size:11px;-fx-font-weight:bold;-fx-text-fill:#e8e8d8;");
        nameLbl.setAlignment(Pos.CENTER);
        nameLbl.setWrapText(true);
        nameLbl.setMaxWidth(130);

        // difficulty tag
        Label diff = new Label(difficulty);
        diff.setStyle(
            "-fx-font-family:'" + CINZEL + "';" +
            "-fx-font-size:9px;-fx-text-fill:" + accent + ";-fx-font-style:italic;");

        // sprite container (clips to fixed size)
        StackPane spriteBox = new StackPane(sprite);
        spriteBox.setPrefSize(140, 100);
        spriteBox.setMaxSize(140, 100);
        spriteBox.setStyle("-fx-background-color:rgba(0,0,0,0.25);");

        VBox box = new VBox(0, bar, spriteBox, nameLbl, diff);
        box.setAlignment(Pos.TOP_CENTER);
        box.setSpacing(5);
        box.setPadding(new Insets(0, 6, 10, 6));
        box.setPrefSize(140, 220);
        box.setMinSize(140, 220);
        box.setMaxSize(140, 220);

        String base =
            "-fx-background-color:" + darkBg + "dd;" +
            "-fx-border-color:" + accent + ";-fx-border-width:2;" +
            "-fx-background-radius:12;-fx-border-radius:12;-fx-cursor:hand;";
        String hover =
            "-fx-background-color:" + darkBg + ";" +
            "-fx-border-color:white;-fx-border-width:2.5;" +
            "-fx-background-radius:12;-fx-border-radius:12;-fx-cursor:hand;" +
            "-fx-effect:dropshadow(gaussian," + accent + ",28,0.65,0,0);";
        box.setStyle(base);
        box.setOnMouseEntered(e -> box.setStyle(hover));
        box.setOnMouseExited(e  -> box.setStyle(base));
        box.setOnMouseClicked(e -> onSelect.accept(index));

        StackPane wrap = new StackPane(box);
        wrap.setPrefSize(148, 228);
        return wrap;
    }

    // ── boss node ─────────────────────────────────────────────────────────

    private StackPane bossNode(String lvl, String name,
                                String spritePath, int fw, int fh,
                                String accent, String darkBg,
                                int index, IntConsumer onSelect) {

        ImageView sprite = loadSprite(spritePath, fw, fh, 130);

        Label bar = new Label(lvl + "  ★ BOSS ★");
        bar.setStyle(
            "-fx-background-color:" + accent + ";" +
            "-fx-font-family:'" + PIXEL + "';" +
            "-fx-font-size:7px;-fx-text-fill:#1a0400;-fx-font-weight:bold;" +
            "-fx-padding:5 0;-fx-pref-width:148;-fx-alignment:center;");

        Label nameLbl = new Label(name);
        nameLbl.setStyle(
            "-fx-font-family:'" + CINZEL + "';" +
            "-fx-font-size:11px;-fx-font-weight:bold;-fx-text-fill:#ffccaa;");
        nameLbl.setAlignment(Pos.CENTER);
        nameLbl.setWrapText(true);
        nameLbl.setMaxWidth(136);

        Label diff = new Label("Challenge");
        diff.setStyle(
            "-fx-font-family:'" + CINZEL + "';" +
            "-fx-font-size:9px;-fx-text-fill:#ff8844;-fx-font-style:italic;");

        StackPane spriteBox = new StackPane(sprite);
        spriteBox.setPrefSize(148, 100);
        spriteBox.setMaxSize(148, 100);
        spriteBox.setStyle("-fx-background-color:rgba(40,0,0,0.35);");

        VBox box = new VBox(0, bar, spriteBox, nameLbl, diff);
        box.setAlignment(Pos.TOP_CENTER);
        box.setSpacing(5);
        box.setPadding(new Insets(0, 6, 10, 6));
        box.setPrefSize(148, 220);
        box.setMinSize(148, 220);
        box.setMaxSize(148, 220);

        String base =
            "-fx-background-color:" + darkBg + "ee;" +
            "-fx-border-color:" + accent + ";-fx-border-width:2.5;" +
            "-fx-background-radius:12;-fx-border-radius:12;-fx-cursor:hand;";
        String hover =
            "-fx-background-color:" + darkBg + ";" +
            "-fx-border-color:#ffaa66;-fx-border-width:3;" +
            "-fx-background-radius:12;-fx-border-radius:12;-fx-cursor:hand;" +
            "-fx-effect:dropshadow(gaussian,#ff6600,32,0.75,0,0);";
        box.setStyle(base);
        box.setOnMouseEntered(e -> box.setStyle(hover));
        box.setOnMouseExited(e  -> box.setStyle(base));
        box.setOnMouseClicked(e -> onSelect.accept(index));

        StackPane wrap = new StackPane(box);
        wrap.setPrefSize(156, 228);
        return wrap;
    }

    // ── upgrade node ──────────────────────────────────────────────────────

    private StackPane upgradeNode(String label) {
        Label lbl = new Label(label);
        lbl.setStyle(
            "-fx-font-family:'" + CINZEL + "';" +
            "-fx-font-size:10px;-fx-text-fill:#ccaa44;-fx-font-weight:bold;");
        lbl.setAlignment(Pos.CENTER);
        lbl.setWrapText(true);
        lbl.setMaxWidth(68);

        Label auto = new Label("auto");
        auto.setStyle(
            "-fx-font-family:'" + CINZEL + "';" +
            "-fx-font-size:9px;-fx-text-fill:#887733;-fx-font-style:italic;");

        VBox box = new VBox(6, lbl, auto);
        box.setAlignment(Pos.CENTER);
        box.setPrefSize(76, 120);
        box.setMinSize(76, 120);
        box.setStyle(
            "-fx-background-color:rgba(50,38,0,0.60);" +
            "-fx-border-color:#887744;-fx-border-width:1.5;" +
            "-fx-border-style:dashed;" +
            "-fx-background-radius:10;-fx-border-radius:10;-fx-padding:10 6;");

        StackPane wrap = new StackPane(box);
        wrap.setPrefSize(84, 228);
        StackPane.setAlignment(box, Pos.CENTER);
        return wrap;
    }

    // ── arrow ─────────────────────────────────────────────────────────────

    private StackPane arrow() {
        Label l = new Label("--->");
        l.setStyle("-fx-font-size:13px;-fx-text-fill:#887744;-fx-font-weight:bold;");
        StackPane wrap = new StackPane(l);
        wrap.setPrefSize(40, 228);
        StackPane.setAlignment(l, Pos.CENTER);
        return wrap;
    }

    // ── sprite loader: crops first frame ─────────────────────────────────

    private ImageView loadSprite(String path, int fw, int fh, double displaySize) {
        ImageView iv = new ImageView();
        java.io.File f = new java.io.File(path);
        if (f.exists()) {
            Image img = new Image(f.toURI().toString());
            iv.setImage(img);
            // show only the first frame
            iv.setViewport(new Rectangle2D(0, 0, fw, fh));
        }
        // scale to fit displaySize, preserving aspect
        double scale = displaySize / Math.max(fw, fh);
        iv.setFitWidth(fw * scale);
        iv.setFitHeight(fh * scale);
        iv.setPreserveRatio(false);
        iv.setSmooth(false); // pixel art — no blur
        return iv;
    }
}
