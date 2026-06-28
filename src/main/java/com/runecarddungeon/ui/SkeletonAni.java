package com.runecarddungeon.ui;

import javafx.scene.image.Image;
import javafx.util.Duration;

// Skeleton sprite sheet: 150x150px per frame — this was already correct
// Idle(4) Attack(8) TakeHit(4) Shield(4) Death(4)
public class SkeletonAni extends BaseAnimationSprite {
    private final ModelAnimation idleAnim;
    private final ModelAnimation attackAnim;
    private final ModelAnimation hurtAnim;
    private final ModelAnimation deathAnim;
    private final ModelAnimation shieldAnim;

    public SkeletonAni() {
        super();
        this.idleAnim   = new ModelAnimation(this.imageView, Duration.millis(600),  4, 150, 150);
        this.attackAnim = new ModelAnimation(this.imageView, Duration.millis(800),  8, 150, 150);
        this.hurtAnim   = new ModelAnimation(this.imageView, Duration.millis(300),  4, 150, 150);
        this.shieldAnim = new ModelAnimation(this.imageView, Duration.millis(600),  4, 150, 150);
        this.deathAnim  = new ModelAnimation(this.imageView, Duration.millis(800),  4, 150, 150);
        playIdle();
    }

    private static String uri(String p) { return new java.io.File(p).toURI().toString(); }

    @Override public void playIdle() {
        this.imageView.setImage(new Image(uri("assets/Skeleton/Idle.png")));
        playAnimation(this.idleAnim, ModelAnimation.INDEFINITE);
    }
    @Override public void playAttack() { playAttack(null); }
    public void playAttack(Runnable cb) {
        this.imageView.setImage(new Image(uri("assets/Skeleton/Attack.png")));
        playAnimation(this.attackAnim, 1, () -> {
            playIdle();
            if (cb != null) cb.run();
        });
    }
    @Override public void playHurt() {
        this.imageView.setImage(new Image(uri("assets/Skeleton/Take Hit.png")));
        playAnimation(this.hurtAnim, 1, () -> playIdle());
    }
    @Override public void playDeath() {
        this.imageView.setImage(new Image(uri("assets/Skeleton/Death.png")));
        playAnimation(this.deathAnim, 1);
    }
    public void playShield() {
        this.imageView.setImage(new Image(uri("assets/Skeleton/Shield.png")));
        playAnimation(this.shieldAnim, 1, () -> playIdle());
    }
}
