package com.codepath.android.lollipopexercise.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.view.View;
import android.view.ViewAnimationUtils;

public class ViewUtils {
    public static void enterReveal(final Activity activity,
                                   final View v,
                                   final Transition.TransitionListener enterTransitionListener) {
        // get the center for the clipping circle
        int cx = v.getMeasuredWidth() / 2;
        int cy = v.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(v.getWidth(), v.getHeight()) / 2;

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        v.setVisibility(View.VISIBLE);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                activity.getWindow().getEnterTransition().removeListener(enterTransitionListener);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }
    
    public static void exitReveal(final AppCompatActivity activity, final View v) {
        // get the center for the clipping circle
        int cx = v.getMeasuredWidth() / 2;
        int cy = v.getMeasuredHeight() / 2;

        // get the initial radius for the clipping circle
        int initialRadius = v.getWidth() / 2;

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(v, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.INVISIBLE);
                activity.supportFinishAfterTransition();
            }
        });

        // start the animation
        anim.start();
    }
}
