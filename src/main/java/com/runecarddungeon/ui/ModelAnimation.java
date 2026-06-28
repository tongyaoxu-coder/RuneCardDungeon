package com.runecarddungeon.ui;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class ModelAnimation extends Transition {
    private final ImageView imageView;
    private final int count;
    private final int width;
    private final int height;
    private int lastIndex = -1;

    public ModelAnimation(ImageView imageView, Duration duration, int count, int width, int height) {
        this.imageView = imageView;
        this.count = count;
        this.width = width;
        this.height = height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected void interpolate(double frac) {
        final int index = Math.min((int) Math.floor(frac * count), count - 1);
        if (index != lastIndex) {
            imageView.setViewport(new Rectangle2D(index * width, 0, width, height));
            lastIndex = index;
        }
    }

    /** Immediately show frame 0 — prevents the full sheet from flashing. */
    public void applyFirstFrame() {
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
        lastIndex = 0;
    }
}
