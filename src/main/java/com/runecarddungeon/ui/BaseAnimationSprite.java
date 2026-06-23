package com.runecarddungeon.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public abstract class BaseAnimationSprite {
	protected ImageView imageView;          
    protected ModelAnimation currentAnim;   
    
    public BaseAnimationSprite() {
        this.imageView = new ImageView();
    }
    
    public void playAnimation(ModelAnimation anim, int cycleCount, Runnable onFinished) {
    	if(currentAnim!=null) {
    		currentAnim.stop();
    	}
    	this.currentAnim = anim;
        this.currentAnim.setCycleCount(cycleCount);
        
        if(onFinished != null) {
        	this.currentAnim.setOnFinished(e -> onFinished.run());
        }else {
        	this.currentAnim.setOnFinished(null);
        }
        
        
        this.currentAnim.play();
    }
    
    public void playAnimation(ModelAnimation anim, int cycleCount) {
        playAnimation(anim, cycleCount, null);
    }

    public ImageView getImageView() {
        return imageView;
    }

    // core movement
    public abstract void playIdle();
    public abstract void playAttack();
    public abstract void playHurt();
    public abstract void playDeath();

    
}
