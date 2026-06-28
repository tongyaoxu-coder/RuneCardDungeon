package com.runecarddungeon.ui;

import com.runecarddungeon.battle.BattleManager;
import com.runecarddungeon.battle.BattleState;
import com.runecarddungeon.model.Card;
import com.runecarddungeon.model.Enemy;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class BattlePane extends StackPane {

    private static final javafx.scene.text.Font CINZEL;
    private static final String CINZEL_FAM;
    static {
        javafx.scene.text.Font f = null;
        java.io.File cf = new java.io.File("assets/fonts/Cinzel-Regular.ttf");
        if (cf.exists()) f = javafx.scene.text.Font.loadFont(cf.toURI().toString(), 12);
        CINZEL = f;
        CINZEL_FAM = f != null ? f.getFamily() : "Georgia";
    }


    // ═══════════════════════════════════════════════════════════════════════
    //  Sprite position & size — change these constants and run directly, no Clean needed.
    //
    //  SPRITE_Y_FRAC : The fraction of the window height where sprite "feet" land.
    //                  0.72 = upper-middle of the grass (recommended), 0.80 = lower
    //  KNIGHT_SIZE   : Display box size for the knight (px)
    //  ENEMY_SIZE    : Display box size for enemies (px)
    //  SPRITE_X_PAD  : Distance from left/right edges (px); smaller = closer to edge
    private static final double SPRITE_Y_FRAC = 0.72;
    private static final double KNIGHT_SIZE   = 360.0;
    private static final double ENEMY_SIZE    = 460.0;
    private static final double SPRITE_X_PAD  = 80.0;
    // ═══════════════════════════════════════════════════════════════════════

    // Transparent bottom padding fraction per sprite (used for foot alignment).
    // emptyFrac = transparent pixels below feet / frame height
    private static final double KNIGHT_EMPTY_FRAC = 0.26;
    private static final double SLIME_EMPTY_FRAC  = 0.44;
    private static final double SKEL_EMPTY_FRAC   = 0.33;
    private static final double WORM_EMPTY_FRAC   = 0.36;

    // Sprite frame dimensions (measured from actual sprite sheets)
    private static final int KNIGHT_FW = 96,  KNIGHT_FH = 84;
    private static final int SLIME_FW  = 156, SLIME_FH  = 156;
    private static final int SKEL_FW   = 150, SKEL_FH   = 150;
    private static final int WORM_FW   = 90,  WORM_FH   = 90;

    private final BattleManager battleManager;
    private final Runnable      onBattleEnd;

    private Label       enemyNameLabel, enemyHpLabel, enemyIntentLabel;
    private ProgressBar enemyHpBar;
    private Label       playerHpLabel, playerBlockLabel, energyLabel;
    private ProgressBar playerHpBar;
    private HBox        handBox;
    private final VBox  pauseLayer;

    private final KnightAni          knightSprite;
    private final BaseAnimationSprite enemySprite;

    // Battle log — shows recent combat messages on screen
    private VBox  logBox;
    private final java.util.Deque<String> logMessages = new java.util.ArrayDeque<>();

    // ── constructor ───────────────────────────────────────────────────────

    public BattlePane(BattleManager battleManager,
                      Runnable onBattleEnd,
                      Runnable onRestart,
                      Runnable onBackToMenu) {

        this.battleManager = battleManager;
        this.onBattleEnd   = onBattleEnd;

        knightSprite = new KnightAni();
        enemySprite  = pickEnemySprite(battleManager.getEnemy());

        // Force single frame before any rendering
        knightSprite.forceViewport(KNIGHT_FW, KNIGHT_FH);
        setEnemyInitialViewport(enemySprite);

        pauseLayer = buildPauseLayer(onRestart, onBackToMenu);
        pauseLayer.setVisible(false);

        this.getChildren().addAll(
            buildBackground(),
            buildSpriteLayer(),
            buildLogLayer(),
            buildUiOverlay(),
            pauseLayer
        );

        // Mirror System.out battle messages onto the on-screen log
        installLogCapture();

        refresh();

        // Restart idle loops after the scene is ready so animations play smoothly
        Platform.runLater(() -> {
            knightSprite.playIdle();
            enemySprite.playIdle();
        });
    }

    // ── background ────────────────────────────────────────────────────────

    private StackPane buildBackground() {
        StackPane bg = new StackPane();
        java.io.File f = new java.io.File("assets/bg_play.png");
        if (f.exists()) {
            ImageView iv = new ImageView(new Image(f.toURI().toString()));
            iv.setPreserveRatio(false);
            iv.fitWidthProperty().bind(widthProperty());
            iv.fitHeightProperty().bind(heightProperty());
            bg.getChildren().add(iv);
        } else {
            bg.setStyle("-fx-background-color:linear-gradient(from 0% 0% to 0% 100%,#5ab8d4,#4a9a3a);");
        }
        return bg;
    }

    // ── battle log overlay (top-right, below HUD) ────────────────────────

    private Pane buildLogLayer() {
        logBox = new VBox(3);
        logBox.setAlignment(Pos.TOP_RIGHT);
        logBox.setPickOnBounds(false);
        logBox.setPadding(new Insets(8, 16, 0, 0));
        logBox.setMaxWidth(340);

        Pane wrap = new Pane(logBox);
        wrap.setPickOnBounds(false);
        logBox.layoutXProperty().bind(wrap.widthProperty().subtract(340));
        logBox.setLayoutY(120); // below HUD (~80px) + gap
        return wrap;
    }

    /**
     * Redirects System.out so that battle messages printed by the model
     * (Skeleton, Dragon, Player, BattleManager, etc.) also appear on screen.
     * The original console output is preserved.
     */
    private void installLogCapture() {
        final java.io.PrintStream original = System.out;
        System.setOut(new java.io.PrintStream(original, true) {
            @Override public void println(String s) {
                original.println(s);          // keep console output
                if (s != null && !s.isBlank())
                    javafx.application.Platform.runLater(() -> pushLog(s.trim()));
            }
        });
    }

    /** Adds a message to the on-screen log (keeps the latest 5). */
    private void pushLog(String msg) {
        logMessages.addLast(msg);
        while (logMessages.size() > 5) logMessages.removeFirst();
        if (logBox == null) return;
        logBox.getChildren().clear();
        for (String m : logMessages) {
            Label l = new Label(m);
            l.setStyle("-fx-font-family:'" + CINZEL_FAM + "';"
                    + "-fx-font-size:11px;-fx-text-fill:#f0e8d0;"
                    + "-fx-background-color:rgba(0,0,0,0.45);"
                    + "-fx-background-radius:4;-fx-padding:2 8;"
                    + "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.8),3,0.3,0,1);");
            l.setWrapText(true);
            l.setMaxWidth(350);
            logBox.getChildren().add(l);
        }
    }

    // ── sprite layer ──────────────────────────────────────────────────────

    private Pane buildSpriteLayer() {
        StackPane enemyBox  = spriteBox(enemySprite,  ENEMY_SIZE);
        StackPane playerBox = spriteBox(knightSprite, KNIGHT_SIZE);

        Pane layer = new Pane(enemyBox, playerBox);
        layer.setPickOnBounds(false);

        // Enemy: left side — foot-aligned to SPRITE_Y_FRAC
        double enemyEmptyFrac = getEmptyFrac(enemySprite);
        int    enemyFH        = getFrameH(enemySprite);
        int    enemyFW        = getFrameW(enemySprite);
        double enemyScale     = ENEMY_SIZE / Math.max(enemyFW, enemyFH);
        double enemyEmptyPx   = enemyFH * enemyEmptyFrac * enemyScale; // transparent below feet

        enemyBox.setLayoutX(SPRITE_X_PAD);
        // box bottom = SPRITE_Y_FRAC * height → layoutY = that - ENEMY_SIZE
        // then shift down by emptyPx so the ACTUAL feet (not box bottom) hit the line
        enemyBox.layoutYProperty().bind(
            layer.heightProperty().multiply(SPRITE_Y_FRAC)
                 .subtract(ENEMY_SIZE)
                 .add(enemyEmptyPx));

        // Player (Knight): right side
        double knightEmptyPx = KNIGHT_FH * KNIGHT_EMPTY_FRAC * (KNIGHT_SIZE / Math.max(KNIGHT_FW, KNIGHT_FH));

        playerBox.layoutXProperty().bind(
            layer.widthProperty().subtract(KNIGHT_SIZE + SPRITE_X_PAD));
        playerBox.layoutYProperty().bind(
            layer.heightProperty().multiply(SPRITE_Y_FRAC)
                 .subtract(KNIGHT_SIZE)
                 .add(knightEmptyPx));

        return layer;
    }

    /**
     * Builds the sprite display box using the sprite's LIVE animated ImageView.
     * Now that frame counts/sizes are correct, the idle animation loops cleanly
     * (one character that breathes/bobs) instead of scrolling through the sheet.
     */
    private StackPane spriteBox(BaseAnimationSprite sprite, double size) {
        int fw = getFrameW(sprite);
        int fh = getFrameH(sprite);

        ImageView iv = sprite.getImageView();
        iv.setViewport(new javafx.geometry.Rectangle2D(0, 0, fw, fh));

        double scale = size / Math.max(fw, fh);
        iv.setFitWidth(fw * scale);
        iv.setFitHeight(fh * scale);
        iv.setPreserveRatio(false);
        iv.setSmooth(false);

        StackPane box = new StackPane(iv);
        StackPane.setAlignment(iv, Pos.BOTTOM_CENTER);
        box.setPrefSize(size, size);
        box.setMinSize(size, size);
        box.setMaxSize(size, size);
        return box;
    }

    private int    getFrameW(BaseAnimationSprite s) {
        if (s instanceof KnightAni)   return KNIGHT_FW;
        if (s instanceof SkeletonAni) return SKEL_FW;
        if (s instanceof FireWormAni) return WORM_FW;
        return SLIME_FW;
    }
    private int    getFrameH(BaseAnimationSprite s) {
        if (s instanceof KnightAni)   return KNIGHT_FH;
        if (s instanceof SkeletonAni) return SKEL_FH;
        if (s instanceof FireWormAni) return WORM_FH;
        return SLIME_FH;
    }
    private double getEmptyFrac(BaseAnimationSprite s) {
        if (s instanceof KnightAni)   return KNIGHT_EMPTY_FRAC;
        if (s instanceof SkeletonAni) return SKEL_EMPTY_FRAC;
        if (s instanceof FireWormAni) return WORM_EMPTY_FRAC;
        return SLIME_EMPTY_FRAC;
    }
    private void setEnemyInitialViewport(BaseAnimationSprite sprite) {
        if      (sprite instanceof SkeletonAni) sprite.forceViewport(SKEL_FW, SKEL_FH);
        else if (sprite instanceof FireWormAni) sprite.forceViewport(WORM_FW, WORM_FH);
        else                                    sprite.forceViewport(SLIME_FW, SLIME_FH);
    }

    // ── UI overlay: HUD top → hand below HUD → spacer → (sprites show through) ──

    private VBox buildUiOverlay() {
        VBox overlay = new VBox();
        overlay.setPickOnBounds(false);
        overlay.getChildren().add(buildHud());
        overlay.getChildren().add(buildHandArea());   // cards RIGHT below the HUD
        Region spacer = new Region();
        spacer.setPickOnBounds(false);
        VBox.setVgrow(spacer, Priority.ALWAYS);
        overlay.getChildren().add(spacer);
        return overlay;
    }

    // ── HUD ───────────────────────────────────────────────────────────────
    // Layout: enemy info (left) | energy (centre) | player info (right)
    // Fonts and HP bars are enlarged; overall height is approximately 80px

    private HBox buildHud() {
        // ── pause button ──
        Button pauseBtn = new Button("❚❚");
        pauseBtn.setStyle(
            "-fx-font-family:'" + CINZEL_FAM + "';" +
            "-fx-background-color:rgba(0,0,0,0.6);-fx-text-fill:#cccccc;" +
            "-fx-font-size:14px;-fx-background-radius:6;" +
            "-fx-padding:6 12;-fx-cursor:hand;");
        pauseBtn.setOnAction(e -> pauseLayer.setVisible(true));

        // ── enemy side (left) ──
        enemyNameLabel = lbl("", "#ff8888", 15, true);
        enemyHpBar     = fatBar("#cc2222", 200);
        enemyHpLabel   = lbl("", "#dddddd", 12, false);
        enemyIntentLabel = lbl("", "#ffcc44", 12, false);
        enemyIntentLabel.setWrapText(true);
        enemyIntentLabel.setMaxWidth(250);

        VBox enemySide = new VBox(4,
            enemyNameLabel,
            enemyHpBar,
            enemyHpLabel,
            enemyIntentLabel);
        enemySide.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(enemySide, Priority.ALWAYS);

        // ── energy area (centre) ──
        energyLabel = lbl("", "#f5d020", 18, true);
        Label energyTitle = lbl("ENERGY", "#aaaaaa", 10, false);
        VBox mid = new VBox(2, energyTitle, energyLabel);
        mid.setAlignment(Pos.TOP_CENTER);
        mid.setPadding(new Insets(0, 20, 0, 20));
        mid.setMinWidth(140);

        // ── player side (right) ──
        playerHpBar      = fatBar("#22aa55", 200);
        playerHpLabel    = lbl("", "#dddddd", 12, false);
        playerBlockLabel = lbl("", "#6699ff", 12, false);
        Label heroTitle  = lbl("⚔  HERO", "#88ccff", 15, true);

        VBox playerSide = new VBox(4,
            heroTitle,
            playerHpBar,
            playerHpLabel,
            playerBlockLabel);
        playerSide.setAlignment(Pos.TOP_RIGHT);
        HBox.setHgrow(playerSide, Priority.ALWAYS);

        // ── assemble HUD ──
        HBox hud = new HBox(12, pauseBtn, enemySide, mid, playerSide);
        hud.setAlignment(Pos.CENTER_LEFT);
        hud.setPadding(new Insets(10, 16, 10, 16));
        hud.setStyle(
            "-fx-background-color:rgba(0,0,15,0.75);" +
            "-fx-border-color:rgba(255,220,100,0.18);" +
            "-fx-border-width:0 0 2 0;");
        return hud;
    }

    // ── hand ──────────────────────────────────────────────────────────────

    private VBox buildHandArea() {
        handBox = new HBox(14);
        handBox.setAlignment(Pos.CENTER);
        handBox.setPadding(new Insets(6, 0, 2, 0));

        Button endBtn = new Button("End Turn  →");
        endBtn.setStyle("-fx-font-family:'" + CINZEL_FAM + "';"
                + "-fx-background-color:#c8a020;-fx-text-fill:#0a0a00;"
                + "-fx-font-size:13px;-fx-font-weight:bold;"
                + "-fx-background-radius:8;-fx-padding:9 26;-fx-cursor:hand;");
        endBtn.setOnAction(e -> onEndTurn());

        VBox hand = new VBox(8, handBox, endBtn);
        hand.setAlignment(Pos.CENTER);
        hand.setStyle("-fx-background-color:transparent;-fx-padding:10 14;");
        return hand;
    }

    // ── card: pure image, no text overlay ────────────────────────────────

    private StackPane buildCardView(Card card) {
        final double W = 120, H = 180;

        ImageView art = new ImageView();
        art.setFitWidth(W);
        art.setFitHeight(H);
        art.setPreserveRatio(false);
        art.setSmooth(true);
        loadImage(art, cardImagePath(card));

        Rectangle clip = new Rectangle(W, H);
        clip.setArcWidth(16); clip.setArcHeight(16);
        art.setClip(clip);

        StackPane cv = new StackPane(art);
        cv.setPrefSize(W, H); cv.setMaxSize(W, H); cv.setMinSize(W, H);

        String glow = cardGlow(card);
        String norm = "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.65),10,0,0,3);-fx-cursor:hand;";
        String hov  = "-fx-effect:dropshadow(gaussian," + glow + ",22,0.6,0,0);"
                + "-fx-cursor:hand;-fx-translate-y:-12;";
        cv.setStyle(norm);
        cv.setOnMouseEntered(e -> cv.setStyle(hov));
        cv.setOnMouseExited(e  -> cv.setStyle(norm));
        cv.setOnMouseClicked(e -> onPlayCard(card));
        return cv;
    }

    private String cardImagePath(Card card) {
        String n = card.getName().toLowerCase();
        boolean up = n.contains("+");
        String base = n.contains("attack") || n.contains("strike") ? "attack"
                    : n.contains("block")  || n.contains("shield")  ? "shield"
                    : n.contains("heal")   || n.contains("potion")  ? "heal"
                    : n.contains("curse")  || n.contains("weaken")  ? "curse"
                    : "attack";
        if (up && new java.io.File("assets/cards/" + base + "_upgraded.png").exists())
            return "assets/cards/" + base + "_upgraded.png";
        return "assets/cards/" + base + ".png";
    }

    private String cardGlow(Card card) {
        String n = card.getName().toLowerCase();
        if (n.contains("attack") || n.contains("strike")) return "#ff6633";
        if (n.contains("block")  || n.contains("shield"))  return "#4488ff";
        if (n.contains("heal")   || n.contains("potion"))  return "#44cc66";
        if (n.contains("curse")  || n.contains("weaken"))  return "#aa44ee";
        return "#ffffff";
    }

    private void loadImage(ImageView iv, String path) {
        java.io.File f = new java.io.File(path);
        if (f.exists()) iv.setImage(new Image(f.toURI().toString()));
    }

    // ── sprite selection ──────────────────────────────────────────────────

    private BaseAnimationSprite pickEnemySprite(Enemy enemy) {
        if (enemy == null) return new SlimeAni();
        String n = enemy.getName().toLowerCase();
        if (n.contains("skelet"))                                  return new SkeletonAni();
        if (n.contains("dragon") || n.contains("worm") || n.contains("fire"))
                                                                   return new FireWormAni();
        return new SlimeAni();
    }

    // ── game flow ─────────────────────────────────────────────────────────

    private void onEndTurn() {
        Enemy enemy = battleManager.getEnemy();
        if (enemy != null && enemy.getHp() > 0) {
            // Enemy attacks first, then knight plays hurt once enemy is done
            if (enemySprite instanceof SlimeAni)
                ((SlimeAni) enemySprite).playAttack(() -> { knightSprite.playHurt(); enemySprite.playIdle(); });
            else if (enemySprite instanceof SkeletonAni)
                ((SkeletonAni) enemySprite).playAttack(() -> { knightSprite.playHurt(); enemySprite.playIdle(); });
            else if (enemySprite instanceof FireWormAni)
                ((FireWormAni) enemySprite).playAttack(() -> { knightSprite.playHurt(); enemySprite.playIdle(); });
            else {
                enemySprite.playAttack();
                knightSprite.playHurt();
            }
        }
        battleManager.endPlayerTurn();
        refresh();
    }

    private void onPlayCard(Card card) {
        // Check energy before playing — show a message if insufficient
        if (card.getEnergyCost() > battleManager.getPlayer().getEnergy()) {
            pushLog("⚡ Not enough Energy to play " + card.getName() + "!");
            return;
        }

        String n = card.getName().toLowerCase();
        if (n.contains("attack") || n.contains("strike") || n.contains("slash")) {
            knightSprite.playAttack(() -> {
                enemySprite.playHurt();
                battleManager.playCard(card);
                Platform.runLater(this::refresh);
            });
        } else if (n.contains("block") || n.contains("shield") || n.contains("defend")) {
            knightSprite.playShield(() -> {
                battleManager.playCard(card);
                Platform.runLater(this::refresh);
            });
        } else {
            battleManager.playCard(card);
            refresh();
        }
    }

    // ── refresh ───────────────────────────────────────────────────────────

    private void refresh() {
        Enemy enemy = battleManager.getEnemy();
        if (enemy != null) {
            // Choose icon based on enemy type
            String icon = enemy.getName().toLowerCase().contains("skelet") ? "💀"
                        : enemy.getName().toLowerCase().contains("dragon")
                          || enemy.getName().toLowerCase().contains("worm") ? "🔥"
                        : "🟢";
            enemyNameLabel.setText(icon + "  " + enemy.getName());
            enemyHpLabel.setText("HP: " + enemy.getHp() + " / " + enemy.getMaxHp());
            enemyHpBar.setProgress(
                enemy.getMaxHp() > 0 ? (double) enemy.getHp() / enemy.getMaxHp() : 0);
            enemyIntentLabel.setText("Intent: " + enemy.getCurrentIntent());
        }
        var p = battleManager.getPlayer();
        playerHpLabel.setText("HP: " + p.getHp() + " / " + p.getMaxHp());
        playerHpBar.setProgress(
            p.getMaxHp() > 0 ? (double) p.getHp() / p.getMaxHp() : 0);
        playerBlockLabel.setText("🛡  Block: " + p.getBlock());

        int en  = p.getEnergy();
        int max = p.getMaxEnergy();
        StringBuilder sb = new StringBuilder("⚡  ");
        for (int i = 0; i < en; i++)  sb.append("● ");
        for (int i = en; i < max; i++) sb.append("○ ");
        energyLabel.setText(sb.toString().trim());

        handBox.getChildren().clear();
        for (Card card : battleManager.getHand().getCards())
            handBox.getChildren().add(buildCardView(card));

        BattleState state = battleManager.getState();
        if (state == BattleState.VICTORY) {
            // play death animation then transition
            enemySprite.playDeath();
            javafx.animation.PauseTransition delay =
                new javafx.animation.PauseTransition(javafx.util.Duration.millis(1200));
            delay.setOnFinished(e -> onBattleEnd.run());
            delay.play();
        } else if (state == BattleState.DEFEAT) {
            onBattleEnd.run();
        }
    }

    // ── pause overlay ─────────────────────────────────────────────────────

    private VBox buildPauseLayer(Runnable onRestart, Runnable onBackToMenu) {
        Label title = lbl("⏸  Paused", "white", 28, true);
        Button resume  = oBtn("▶  Resume",       "#1e5c1e");
        Button restart = oBtn("↺  Restart",      "#1e2e5c");
        Button menu    = oBtn("⬅  Back to Menu", "#5c1e1e");
        resume.setOnAction(e  -> pauseLayer.setVisible(false));
        restart.setOnAction(e -> onRestart.run());
        menu.setOnAction(e    -> onBackToMenu.run());
        VBox layer = new VBox(20, title, resume, restart, menu);
        layer.setAlignment(Pos.CENTER);
        layer.setStyle("-fx-background-color:rgba(0,0,0,0.72);");
        return layer;
    }

    // ── helpers ───────────────────────────────────────────────────────────

    private Label lbl(String t, String fill, int size, boolean bold) {
        Label l = new Label(t);
        l.setStyle("-fx-font-family:'" + CINZEL_FAM + "';"
                + "-fx-text-fill:" + fill + ";-fx-font-size:" + size + "px;"
                + (bold ? "-fx-font-weight:bold;" : "")
                + "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.8),3,0.3,0,1);");
        return l;
    }

    private ProgressBar bar(String accent) {
        ProgressBar pb = new ProgressBar(1.0);
        pb.setPrefWidth(160);
        pb.setStyle("-fx-accent:" + accent + ";-fx-pref-height:9;");
        return pb;
    }

    // Thicker progress bar used in the HUD
    private ProgressBar fatBar(String accent, double width) {
        ProgressBar pb = new ProgressBar(1.0);
        pb.setPrefWidth(width);
        pb.setStyle("-fx-accent:" + accent + ";-fx-pref-height:14;-fx-background-radius:4;-fx-border-radius:4;");
        return pb;
    }

    private Button oBtn(String text, String bg) {
        Button b = new Button(text);
        b.setPrefWidth(240);
        b.setStyle("-fx-font-family:'" + CINZEL_FAM + "';"
                + "-fx-background-color:" + bg + ";-fx-text-fill:white;"
                + "-fx-font-size:14px;-fx-font-weight:bold;"
                + "-fx-background-radius:8;-fx-padding:10 0;");
        return b;
    }

    // ── font helper ───────────────────────────────────────────────────────
    private static Font gameFont(double size) {
        java.io.File f = new java.io.File("assets/fonts/Cinzel-Regular.ttf");
        if (f.exists()) return Font.loadFont(f.toURI().toString(), size);
        return Font.font("Georgia", size);  // graceful fallback
    }

}
