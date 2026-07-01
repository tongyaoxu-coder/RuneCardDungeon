package com.runecarddungeon.ui;

import javafx.scene.image.Image;
import javafx.util.Duration;

// Knight sprite sheets —  frames and sizes 96*84 
//   IDLE 7 frames
//   HURT  4 frames
//   DEATH  12 frames
//   DEFEND  6 frames
//   ATTACK 1  8 frames (72*84)
//   ATTACK 3(used as "strike")  6 frames
public class KnightAni extends BaseAnimationSprite {
    private final ModelAnimation idleAnim;
    private final ModelAnimation hurtAnim;
    private final ModelAnimation deathAnim;
    private final ModelAnimation shieldAnim;
    private final ModelAnimation attackAnim;
    private final ModelAnimation strikeAnim;

    public KnightAni() {
        super();
        // Mirror X: knight faces towards enemy
        // in the original asset, knight face in the same direction with enemy
        this.imageView.setScaleX(-1); 
        // all frames are 96*84 (except ATTACK 72px)
        // duration; frames number; sizes: length*width
        this.idleAnim   = new ModelAnimation(this.imageView, Duration.millis(700),  7,  96, 84);
        this.hurtAnim   = new ModelAnimation(this.imageView, Duration.millis(300),  4,  96, 84);
        this.deathAnim  = new ModelAnimation(this.imageView, Duration.millis(1200), 12, 96, 84);
        this.shieldAnim = new ModelAnimation(this.imageView, Duration.millis(600),  6,  96, 84);
        this.attackAnim = new ModelAnimation(this.imageView, Duration.millis(500),  8,  72, 84);
        this.strikeAnim = new ModelAnimation(this.imageView, Duration.millis(500),  6,  96, 84);
        playIdle();
    }

    private static String uri(String p) { return new java.io.File(p).toURI().toString(); }

    @Override 
    public void playIdle() {
        this.imageView.setImage(new Image(uri("assets/Knight/IDLE.png")));
        playAnimation(this.idleAnim, ModelAnimation.INDEFINITE);
    }
    
    @Override 
    public void playDeath() {
        this.imageView.setImage(new Image(uri("assets/Knight/DEATH.png")));
        playAnimation(this.deathAnim, 1);
    }
    @Override 
    public void playHurt()   
    { 
        playHurt(() -> playIdle()); 
    }
    public void playHurt(Runnable cb) {
        this.imageView.setImage(new Image(uri("assets/Knight/HURT.png")));
        playAnimation(this.hurtAnim, 1, cb);
    }

    @Override
    public void playAttack() { 
        playAttack(null); 
    }
    public void playAttack(Runnable cb) {
        this.imageView.setImage(new Image(uri("assets/Knight/ATTACK 1.png")));
        playAnimation(this.attackAnim, 1, () -> { if (cb != null) cb.run(); playIdle(); });
    }
    
    public void playStrike(){
        playStrike(null); 
    }
    public void playStrike(Runnable cb) {
        this.imageView.setImage(new Image(uri("assets/Knight/ATTACK 3.png")));
        playAnimation(this.strikeAnim, 1, () -> { if (cb != null) cb.run(); playIdle(); });
    }

    public void playShield() {
        playShield(null);
    }
    public void playShield(Runnable cb) {
        this.imageView.setImage(new Image(uri("assets/Knight/DEFEND.png")));
        playAnimation(this.shieldAnim, 1, () -> { if (cb != null) cb.run(); playIdle(); });
    }
}
