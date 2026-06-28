package com.runecarddungeon.ui;

import javafx.scene.image.Image;
import javafx.util.Duration;

// Slime sprite sheet: each frame is 156x156px
// idle(14) attack(19) hurt(3) death(11)
public class SlimeAni extends BaseAnimationSprite {
    private final ModelAnimation idleAnim;
    private final ModelAnimation attackAnim;
    private final ModelAnimation hurtAnim;
    private final ModelAnimation deathAnim;

    public SlimeAni() {
        super();
        // FIXED: frame size is 156x156, not 44x18
        this.idleAnim   = new ModelAnimation(this.imageView, Duration.millis(1000), 14, 156, 156);
        this.attackAnim = new ModelAnimation(this.imageView, Duration.millis(800),  19, 156, 156);
        this.hurtAnim   = new ModelAnimation(this.imageView, Duration.millis(300),   3, 156, 156);
        this.deathAnim  = new ModelAnimation(this.imageView, Duration.millis(1200), 11, 156, 156);
        playIdle();
    }

    private static String uri(String p) { return new java.io.File(p).toURI().toString(); }

    @Override public void playIdle() {
        this.imageView.setImage(new Image(uri("assets/Slime/idle.png")));
        playAnimation(this.idleAnim, ModelAnimation.INDEFINITE);
    }
    @Override public void playAttack() { playAttack(null); }
    @Override public void playHurt()   { playHurt(() -> playIdle()); }
    @Override public void playDeath() {
        this.imageView.setImage(new Image(uri("assets/Slime/death.png")));
        playAnimation(this.deathAnim, 1);
    }
    public void playAttack(Runnable cb) {
        this.imageView.setImage(new Image(uri("assets/Slime/attack.png")));
        playAnimation(this.attackAnim, 1, () -> {
            playIdle();
            if (cb != null) cb.run();
        });
    }
    public void playHurt(Runnable cb) {
        this.imageView.setImage(new Image(uri("assets/Slime/hurt.png")));
        playAnimation(this.hurtAnim, 1, cb);
    }
}
