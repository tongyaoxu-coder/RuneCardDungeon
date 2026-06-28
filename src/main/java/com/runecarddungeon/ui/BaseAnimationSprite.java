package com.runecarddungeon.ui;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;

public abstract class BaseAnimationSprite {
    protected ImageView imageView;
    protected ModelAnimation currentAnim;

    public BaseAnimationSprite() {
        this.imageView = new ImageView();
    }

    public void playAnimation(ModelAnimation anim, int cycleCount, Runnable onFinished) {
        if (currentAnim != null) currentAnim.stop();
        this.currentAnim = anim;
        this.currentAnim.setCycleCount(cycleCount);
        if (onFinished != null) this.currentAnim.setOnFinished(e -> onFinished.run());
        else this.currentAnim.setOnFinished(null);
        // Set frame 0 BEFORE play() so the full sprite sheet never flashes
        anim.applyFirstFrame();
        this.currentAnim.play();
    }

    public void playAnimation(ModelAnimation anim, int cycleCount) {
        playAnimation(anim, cycleCount, null);
    }

    public ImageView getImageView() { return imageView; }

    /**
     * Directly set the viewport to a single frame (frame 0) of the given size.
     * Used by BattlePane to lock the static pose immediately on creation,
     * guaranteeing only ONE character shows (not the whole sprite sheet).
     */
    public void forceViewport(int frameW, int frameH) {
        imageView.setViewport(new Rectangle2D(0, 0, frameW, frameH));
    }

    public abstract void playIdle();
    public abstract void playAttack();
    public abstract void playHurt();
    public abstract void playDeath();
}
