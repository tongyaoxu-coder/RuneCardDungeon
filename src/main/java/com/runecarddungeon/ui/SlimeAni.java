package com.runecarddungeon.ui;

import javafx.scene.image.Image;
import javafx.util.Duration;

public class SlimeAni extends BaseAnimationSprite {
    // Distinct animation controllers for each core action
    private final ModelAnimation idleAnim;
    private final ModelAnimation attackAnim;
    private final ModelAnimation hurtAnim;
    private final ModelAnimation deathAnim;

    public SlimeAni() {
        super(); // Initializes the shared imageView from the base class
        
        // Parameters: ImageView, duration, frameCount, frameWidth, frameHeight
        // Configured based on the Slime sprite sheet data (44x18px)
        // Slime (44x18px): Idle (14), Attack (19), Hurt (3), Death (11)
        this.idleAnim   = new ModelAnimation(this.imageView, Duration.millis(1000), 14, 44, 18);
        this.attackAnim = new ModelAnimation(this.imageView, Duration.millis(800),  19, 44, 18);
        this.hurtAnim   = new ModelAnimation(this.imageView, Duration.millis(300),  3,  44, 18);
        this.deathAnim  = new ModelAnimation(this.imageView, Duration.millis(1200), 11, 44, 18);
        
        // Plays the idle animation by default upon initialization
        playIdle();
    }

    @Override
    public void playIdle() {
        this.imageView.setImage(new Image("file:assets/Slime/idle.png"));
        // Loops the idle animation indefinitely
        playAnimation(this.idleAnim, ModelAnimation.INDEFINITE);
    }

    @Override
    public void playAttack() {
        // Defaults to no callback execution
        this.playAttack(null);
    }

    @Override
    public void playHurt() {
        // Automatically returns to idle state after the hurt animation finishes
        this.playHurt(() -> playIdle());
    }

    @Override
    public void playDeath() {
        this.imageView.setImage(new Image("file:assets/Slime/death.png"));
        playAnimation(this.deathAnim, 1);
    }
    
    /**
     * Plays the attack animation with a specific callback.
     * onAttackFinished the game logic execution block triggered when the animation completes
     */
    public void playAttack(Runnable onAttackFinished) {
        this.imageView.setImage(new Image("file:assets/Slime/attack.png"));
        playAnimation(this.attackAnim, 1, onAttackFinished);
    }

    /**
     * Plays the hurt animation with a specific callback.
     * onHurtFinished the game logic execution block triggered when the animation completes
     */
    public void playHurt(Runnable onHurtFinished) {
        this.imageView.setImage(new Image("file:assets/Slime/hurt.png"));
        playAnimation(this.hurtAnim, 1, onHurtFinished);
    }
}