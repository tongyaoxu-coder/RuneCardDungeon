package com.runecarddungeon.ui;

import javafx.scene.image.Image;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.util.Duration;

/*
 * Fire Worm a 51x41 pixels animated scale character with 9 sheets sprites

* Idle - 9 frames

* Attack - 16 frames
* Take Hit - 3 frames
* Death - 8 frames

*/

public class FireWormAni extends BaseAnimationSprite {
	// Fire worm movements
    private final ModelAnimation idleAnim;
    private final ModelAnimation shieldAnim;
    private final ModelAnimation attackAnim;
    private final ModelAnimation hurtAnim;
    private final ModelAnimation deathAnim;
    
    public FireWormAni() {
        super();
        
        this.idleAnim   = new ModelAnimation(this.imageView, Duration.millis(900),  9,  51, 41);
        this.shieldAnim = new ModelAnimation(this.imageView, Duration.millis(900),  9,  51, 41);
        this.attackAnim = new ModelAnimation(this.imageView, Duration.millis(1200), 16, 51, 41);
        this.hurtAnim   = new ModelAnimation(this.imageView, Duration.millis(300),  3,  51, 41);
        this.deathAnim  = new ModelAnimation(this.imageView, Duration.millis(800),  8,  51, 41);
        
        playIdle();
    }
    
    @Override
    public void playIdle() {
        this.imageView.setImage(new Image("file:assets/Fire_worm/Worm/Idle.png"));
        playAnimation(this.idleAnim, ModelAnimation.INDEFINITE);
    }
    
    @Override
    public void playHurt() {
        this.playHurt(() -> playIdle());
    }
    
    @Override
    public void playDeath() {
        this.imageView.setImage(new Image("file:assets/Fire_worm/Worm/Death.png"));
        playAnimation(this.deathAnim, 1);
    }
    
    @Override
    public void playAttack() {
        this.playAttack(null);
    }
    

    public void playShield() {
        this.playShield(() -> playIdle());
    }

    // Overloaded methods with Callbacks
    public void playHurt(Runnable onHurtFinished) {
        this.imageView.setImage(new Image("file:assets/Fire_worm/Worm/Get Hit.png"));
        playAnimation(this.hurtAnim, 1, onHurtFinished);
    }

    public void playAttack(Runnable onAttackFinished) {
        this.imageView.setImage(new Image("file:assets/Fire_worm/Worm/Attack.png"));
        playAnimation(this.attackAnim, 1, onAttackFinished);
    }

    public void playShield(Runnable onShieldFinished) {
    	//reuse Idle assets for defend
        this.imageView.setImage(new Image("file:assets/Fire_worm/Worm/Idle.png"));
        playAnimation(this.shieldAnim, 1, onShieldFinished);
    }

    // defend visual effects
    public void playDefendEffect() {
        playIdle();
        
        Glow glow = new Glow();
        glow.setLevel(0.8); // Glowing intensity
        
        ColorAdjust goldShield = new ColorAdjust();
        goldShield.setHue(0.2);
        glow.setInput(goldShield);
        
        this.imageView.setEffect(glow);
    }
    
    public void clearDefend() {
        this.imageView.setEffect(null);
    }
}