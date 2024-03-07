package com.example.loginapp.view.commonUI;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.loginapp.App;
import com.example.loginapp.R;
import com.google.android.material.tabs.TabLayout;

public final class AppAnimationState {

    public static void setBottomNavigationBarState(TabLayout bottomNavigationBar, Activity activity, Boolean show) {
        Animation animation = AnimationUtils.loadAnimation(activity, show ? R.anim.anim_bottom_navigation_up : R.anim.anim_bottom_navigation_down);
        bottomNavigationBar.startAnimation(animation);
        bottomNavigationBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public static void setUserViewState(ConstraintLayout constraintLayout, Boolean show) {
        Context context = App.getInstance();
        if (show) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_userview_down);
            constraintLayout.startAnimation(animation);
            constraintLayout.setVisibility(View.VISIBLE);
            new CountDownTimer(5000, 1000) { // Thời gian đếm ngược là 5 giây, cập nhật mỗi 1 giây
                public void onTick(long millisUntilFinished) {
                    // Không cần làm gì trong thời gian đếm ngược
                }

                public void onFinish() {
//                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_userview_up);
//                    constraintLayout.startAnimation(animation);
                    constraintLayout.setVisibility(View.GONE);
                    this.cancel();
                }
            }.start();
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
