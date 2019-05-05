package utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class Animations {

    // Alpha
    // rotate
    // scale
    // translate

    public static Animation slideInTop() {

        AnimationSet set = new AnimationSet(true);
        Animation animation;

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(300);
        set.addAnimation(animation);

        return set;
    }

    public static Animation slideOutBottom() {

        AnimationSet set = new AnimationSet(true);
        Animation animation;

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f);
        animation.setDuration(300);
        set.addAnimation(animation);

        return set;
    }

    public static Animation slideInBottom() {

        AnimationSet set = new AnimationSet(true);
        Animation animation;

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(300);
        set.addAnimation(animation);

        return set;
    }

    public static Animation slideOutTop() {

        AnimationSet set = new AnimationSet(true);
        Animation animation;

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.0f);
        animation.setDuration(300);
        set.addAnimation(animation);

        return set;
    }

    public static Animation slideInRight() {

        AnimationSet set = new AnimationSet(true);
        Animation animation;

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(300);
        set.addAnimation(animation);

        return set;
    }

    public static Animation slideInLeft() {

        AnimationSet set = new AnimationSet(true);
        Animation animation;

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(300);
        set.addAnimation(animation);

        return set;
    }

    public static Animation slideOutRight() {

        AnimationSet set = new AnimationSet(true);
        Animation animation;

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(300);
        set.addAnimation(animation);

        return set;
    }

    public static Animation slideOutLeft() {

        AnimationSet set = new AnimationSet(true);
        Animation animation;

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(300);
        set.addAnimation(animation);

        return set;
    }

    public static AnimationSet shakeHorizontal(int intensity) {

        float strength = (float) intensity / 100;

        AnimationSet animationSet = new AnimationSet(true);
        Animation animation;

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -strength, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(50);
        animation.setStartOffset(0);
        animationSet.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -strength, Animation.RELATIVE_TO_SELF, strength, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(100);
        animation.setStartOffset(50);
        animationSet.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, strength, Animation.RELATIVE_TO_SELF, -strength, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(100);
        animation.setStartOffset(150);
        animationSet.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -strength, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(50);
        animation.setStartOffset(250);
        animationSet.addAnimation(animation);

        return animationSet;
    }

    public static AnimationSet shakeVertical(int intensity) {

        float strength = (float) intensity / 100;

        AnimationSet animationSet = new AnimationSet(true);
        Animation animation;

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -strength);
        animation.setDuration(50);
        animation.setStartOffset(0);
        animationSet.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -strength, Animation.RELATIVE_TO_SELF, strength);
        animation.setDuration(100);
        animation.setStartOffset(50);
        animationSet.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, strength, Animation.RELATIVE_TO_SELF, -strength);
        animation.setDuration(100);
        animation.setStartOffset(150);
        animationSet.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -strength, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(50);
        animation.setStartOffset(250);
        animationSet.addAnimation(animation);

        return animationSet;
    }

    public static AnimationSet zoomIn(int speed) {
        AnimationSet animationSet = new AnimationSet(true);
        Animation animation;

        animation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(speed);
        animation.setStartOffset(0);
        animationSet.addAnimation(animation);

        animation = new AlphaAnimation(0, 1);
        animation.setDuration(speed);
        animation.setStartOffset(0);
        animationSet.addAnimation(animation);

        return animationSet;
    }

    public static AnimationSet rotate() {
        AnimationSet animationSet = new AnimationSet(true);
        Animation animation;

        animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        animation.setStartOffset(0);
        animationSet.addAnimation(animation);

        return animationSet;
    }
}
