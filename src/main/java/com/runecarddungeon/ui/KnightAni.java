package com.runecarddungeon.ui;

import javafx.scene.image.Image;
import javafx.util.Duration;

/*
 * Knight: 10 animations!

* Attack 1 – 6 frames -> attack

* Attack 3 - 6 frames -> strike
* Idle - 3 frames
* Walk - 8 frames
* Defend - 6 frames
* Hurt – 3 frames
* Death - 12 frames
* Sprite size: 32x32 px.
 */

public class KnightAni extends BaseAnimationSprite {
	// knight (player) movements
    private final ModelAnimation idleAnim;
    private final ModelAnimation hurtAnim;
    private final ModelAnimation deathAnim;
    private final ModelAnimation shieldAnim;
    private final ModelAnimation attackAnim;
    private final ModelAnimation strikeAnim;
    
    public KnightAni() {
        super();
        
        this.idleAnim   = new ModelAnimation(this.imageView, Duration.millis(500),  3,  32, 32);
        this.hurtAnim   = new ModelAnimation(this.imageView, Duration.millis(300),  3,  32, 32);
        this.deathAnim  = new ModelAnimation(this.imageView, Duration.millis(1200), 12, 32, 32);
        this.shieldAnim = new ModelAnimation(this.imageView, Duration.millis(600),  6,  32, 32);
        this.attackAnim = new ModelAnimation(this.imageView, Duration.millis(500),  6,  32, 32);
        this.strikeAnim = new ModelAnimation(this.imageView, Duration.millis(500),  6,  32, 32);
        
        playIdle();
    }
    
    @Override
    public void playIdle() {
        this.imageView.setImage(new Image("file:assets/Knight/IDLE.png"));
        playAnimation(this.idleAnim, ModelAnimation.INDEFINITE);
    }
    
    @Override
    public void playHurt() {
        // Automatically return to idle state after taking damage
        this.playHurt(() -> playIdle());
    }
    
    @Override
    public void playDeath() {
        this.imageView.setImage(new Image("file:assets/Knight/DEATH.png"));
        playAnimation(this.deathAnim, 1);
    }
    
    @Override
    public void playAttack() {
        this.playAttack(null);
    }
    
    public void playStrike() {
        this.playStrike(null);
    }
    
    public void playShield() {
    	// Automatically returns to idle state after the shied animation finishes
        this.playShield(() -> playIdle());
    }

    // Overloaded methods with Callbacks

    public void playHurt(Runnable onHurtFinished) {
        this.imageView.setImage(new Image("file:assets/Knight/HURT.png"));
        playAnimation(this.hurtAnim, 1, onHurtFinished);
    }

    public void playAttack(Runnable onAttackFinished) {
        this.imageView.setImage(new Image("file:assets/Knight/ATTACK1.png"));
        playAnimation(this.attackAnim, 1, onAttackFinished);
    }

    public void playStrike(Runnable onStrikeFinished) {
        this.imageView.setImage(new Image("file:assets/Knight/ATTACK3.png"));
        playAnimation(this.strikeAnim, 1, onStrikeFinished);
    }

    public void playShield(Runnable onShieldFinished) {
        this.imageView.setImage(new Image("file:assets/Knight/DEFEND.png"));
        playAnimation(this.shieldAnim, 1, onShieldFinished);
    }
}