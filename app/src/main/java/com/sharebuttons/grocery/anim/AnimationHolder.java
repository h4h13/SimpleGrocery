package com.sharebuttons.grocery.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

/**
 * Created by Monkey D Luffy on 7/19/2015.
 */
public class AnimationHolder {

    public static void animate(RecyclerView.ViewHolder holder, boolean getDown) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(holder.itemView, "translationY", getDown ? 500 : -500, 0);
        objectAnimator.setDuration(1000);
        objectAnimator.start();
    }

    public static void animateFab(ImageView fab) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(fab, "scaleX", 0, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(fab, "scaleY", 0, 1);

        scaleX.setDuration(500);
        scaleY.setDuration(500);

        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }


}
