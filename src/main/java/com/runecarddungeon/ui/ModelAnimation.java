package com.runecarddungeon.ui;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * 专为 JavaFX 设计的横条精灵图动画控制器
 */
public class ModelAnimation extends Transition {
    private final ImageView imageView; // 显示图片的组件
    private final int count;           // 动画总帧数
    private final int width;           // 单帧像素宽度
    private final int height;          // 单帧像素高度
    
    private int lastIndex = -1;

    public ModelAnimation(ImageView imageView, Duration duration, int count, int width, int height) {
        this.imageView = imageView;
        this.count = count;
        this.width = width;
        this.height = height;
        
        // animation time
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    /**
     * JavaFX 内部每帧会自动调用这个方法，frac 的值从 0.0 一直变到 1.0
     */
    @Override
    protected void interpolate(double frac) {
        // 根据当前的进度 frac，计算应该播放第几帧
        final int index = Math.min((int) Math.floor(frac * count), count - 1);
        
        // 只有当帧数发生改变时，才重新计算视口位置，优化渲染性能
        if (index != lastIndex) {
            // 计算当前帧在大图里的横坐标 X
            final int x = index * width;
            final int y = 0; // 因为我们的素材全是单行横条，所以 y 永远是 0
            
            // 移动剪裁窗口到新的帧
            imageView.setViewport(new Rectangle2D(x, y, width, height));
            lastIndex = index;
        }
    }
}