package com.runecarddungeon.ui;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.util.Duration;

// Fire worm sprite sheet detail
// FireWorm sprite sheet size: 90*90px
// frames: Idle(9) Attack(16) GetHit(3) Death(8)
public class FireWormAni extends BaseAnimationSprite {
    private final ModelAnimation idleAnim;
    private final ModelAnimation attackAnim;
    private final ModelAnimation hurtAnim;
    private final ModelAnimation deathAnim;

    public FireWormAni() {
        super();
        // idle: duartion: 900ms, frames: 9, sizes: 90*90
        this.idleAnim   = new ModelAnimation(this.imageView, Duration.millis(900),   9, 90, 90);
        // attack: duartion: 1200ms, frames: 16, sizes: 90*90
        this.attackAnim = new ModelAnimation(this.imageView, Duration.millis(1200), 16, 90, 90);
        // take hit: duartion: 300ms, frames: 3, sizes: 90*90
        this.hurtAnim   = new ModelAnimation(this.imageView, Duration.millis(300),   3, 90, 90);
        // death: duartion: 800ms, frames: 8, sizes: 90*90
        this.deathAnim  = new ModelAnimation(this.imageView, Duration.millis(800),   8, 90, 90);
        playIdle();
    }

    private static String uri(String p) { return new java.io.File(p).toURI().toString(); }

    @Override 
    public void playIdle() {
        this.imageView.setImage(new Image(uri("assets/Fire_worm/Worm/Idle.png")));
        playAnimation(this.idleAnim, ModelAnimation.INDEFINITE);
    }
    
    @Override 
    public void playHurt(){ 
        // default: after get hurt animation played, fire worm should return to defaut state: idle
        playHurt(() -> playIdle()); 
    }
    public void playHurt(Runnable cb) {
        // receive outside logic when needed
        // e.g. after a hit, the fire worm dies, then the animation playing logic: playHurt->playDeath
        this.imageView.setImage(new Image(uri("assets/Fire_worm/Worm/Get Hit.png")));
        playAnimation(this.hurtAnim, 1, cb);
    }
    
    @Override 
    public void playDeath() {
        this.imageView.setImage(new Image(uri("assets/Fire_worm/Worm/Death.png")));
        // death animation played, 1 refer to 1 time
        playAnimation(this.deathAnim, 1);
    }
    @Override 
    public void playAttack() { playAttack(null); }

    public void playAttack(Runnable cb) {
        this.imageView.setImage(new Image(uri("assets/Fire_worm/Worm/Attack.png")));
        playAnimation(this.attackAnim, 1, () -> {
            playIdle();
            if (cb != null) cb.run();
        });
    }

    public void playShield(Runnable cb) {
        this.imageView.setImage(new Image(uri("assets/Fire_worm/Worm/Idle.png")));
        playAnimation(this.idleAnim, 1, cb);
    }
    // block effect
    public void playDefendEffect() {
        playIdle();
        Glow glow = new Glow(0.8);
        ColorAdjust gold = new ColorAdjust(); gold.setHue(0.2);
        glow.setInput(gold);
        this.imageView.setEffect(glow);
    }
    public void clearDefend() { this.imageView.setEffect(null); }
}
