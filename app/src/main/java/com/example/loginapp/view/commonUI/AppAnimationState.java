package com.example.loginapp.view.commonUI;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.loginapp.App;
import com.example.loginapp.R;
import com.google.android.material.tabs.TabLayout;

public final class AppAnimationState {

    public static void setBottomNavigationBarState(TabLayout bottomNavigationBar, Activity activity, Boolean show) {
        if (show) {
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.anim_bottom_navigation_up);
            bottomNavigationBar.startAnimation(animation);
            bottomNavigationBar.setVisibility(View.VISIBLE);
        } else {
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.anim_bottom_navigation_down);
            bottomNavigationBar.startAnimation(animation);
            bottomNavigationBar.setVisibility(View.GONE);
        }
    }

    public static void setUserViewState(ConstraintLayout constraintLayout, Activity activity, Boolean show) {
        if (show) {
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.anim_userview_down);
            constraintLayout.startAnimation(animation);
            constraintLayout.setVisibility(View.VISIBLE);
        } else {
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.anim_userview_up);
            constraintLayout.startAnimation(animation);
            constraintLayout.setVisibility(View.GONE);
        }
    }

    public static void setCheckoutViewState(ConstraintLayout checkoutView, Boolean show) {
        Context context = App.getInstance();
        Animation animation;
        if (show) {
            animation = AnimationUtils.loadAnimation(context, R.anim.anim_bottom_navigation_up);
            checkoutView.setVisibility(View.VISIBLE);
        } else {
            animation = AnimationUtils.loadAnimation(context, R.anim.anim_bottom_navigation_down);
            checkoutView.setVisibility(View.GONE);
        }
        checkoutView.startAnimation(animation);
    }

    public static void setBottomActionView(ConstraintLayout bottomActionView, Boolean show) {
        Context context = App.getInstance();
        if (show) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_buy_view_appear);
            bottomActionView.startAnimation(animation);
        }
    }
}
