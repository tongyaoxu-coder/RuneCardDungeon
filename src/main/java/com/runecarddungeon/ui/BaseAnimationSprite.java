package com.runecarddungeon.ui;
// use for spirit diagram cut
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;

public abstract class BaseAnimationSprite {
    // show the animation image
    protected ImageView imageView;
    // hold the animation show currently
    protected ModelAnimation currentAnim;

    public BaseAnimationSprite() {
        this.imageView = new ImageView();
    }

    // Core method to play a specific animation.
    // anim: current animation playing
    // cycleCount: how many times the animation plays
    // onFinished: callbacks to execute when animation finished
    public void playAnimation(ModelAnimation anim, int cycleCount, Runnable onFinished) {
        if (currentAnim != null) currentAnim.stop();
        this.currentAnim = anim;
        this.currentAnim.setCycleCount(cycleCount);
        if (onFinished != null) this.currentAnim.setOnFinished(e -> onFinished.run());
        else this.currentAnim.setOnFinished(null);
        // Set frame 0 before play() so it will not expose the complete spirit graphs
        anim.applyFirstFrame();
        this.currentAnim.play();
    }

    // Overload for a version without callbacks
    public void playAnimation(ModelAnimation anim, int cycleCount) {
        playAnimation(anim, cycleCount, null);
    }

    public ImageView getImageView() { return imageView; }

     // Directly set the viewport to a single frame (frame 0) of the given size
    // guaranteeing only one character shows
    public void forceViewport(int frameW, int frameH) {
        imageView.setViewport(new Rectangle2D(0, 0, frameW, frameH));
    }

    // Trigger the four basic and neccessary animation for all combat entites
    // Derived class must implemented these methods
    public abstract void playIdle();
    public abstract void playAttack();
    public abstract void playHurt();
    public abstract void playDeath();
}
