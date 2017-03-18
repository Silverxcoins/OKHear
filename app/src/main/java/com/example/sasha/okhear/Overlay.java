package com.example.sasha.okhear;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.sasha.okhear.Utils.Preferences;
import com.example.sasha.okhear.Utils.StatusBarUtils;
import com.example.sasha.okhear.Utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.DrawableRes;

@EViewGroup
public class Overlay extends RelativeLayout {

    @ViewById(R.id.left_main_button_icon)
    LinearLayout leftButtonIcon;

    @ViewById(R.id.speak_front)
    View speakIconFront;

    @ViewById(R.id.search_bar)
    FrameLayout searchBar;

    @ViewById(R.id.search_bar_background)
    ImageView searchBarBackground;

    @ViewById(R.id.overlay_bottom_line)
    View overlayBottomLine;

    @ViewById(R.id.left_main_button)
    ImageView leftMainButton;

    @ViewById(R.id.right_main_button)
    ImageView rightMainButton;

    @DrawableRes(R.drawable.ic_hand)
    Drawable handIcon;

    @DrawableRes(R.drawable.ic_speak_back)
    Drawable speakIconBack;

    @ColorRes(R.color.colorPrimaryShow)
    int primaryShowColor;

    @ColorRes(R.color.colorPrimarySpeak)
    int primarySpeakColor;

    @Bean
    Preferences preferences;

    public Overlay(Context context) {
        super(context);
    }

    public Overlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Overlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    void init() {
        preferences.setSpeakOrShow(Preferences.SPEAK);
        setSpeakOrShow();
    }

    @Click(R.id.left_main_button)
    void onClickLeftButton() {
        preferences.changeSpeakOrShow();
        leftButtonIcon.setEnabled(false);
        startRotateAnimation();
        startColorAnimation();
    }

    private void startRotateAnimation() {
        ValueAnimator rotationAnimator = ValueAnimator.ofFloat(0, 180);
        rotationAnimator.setDuration(500);
        rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                if (value <= 90) {
                    leftButtonIcon.setRotationY(value);
                } else {
                    leftButtonIcon.setRotationY(180 - value);
                    setSpeakOrShowIcon();
                }
                if (value == 180) {
                    leftButtonIcon.setEnabled(true);
                }
            }
        });
        rotationAnimator.start();
    }

    private void startColorAnimation() {
        int speakOrShow = preferences.getSpeakOrShow();
        int startColor = (speakOrShow == Preferences.SPEAK) ? primaryShowColor : primarySpeakColor;
        int endColor = (speakOrShow == Preferences.SPEAK) ? primarySpeakColor : primaryShowColor;
        ValueAnimator colorAnimator = ValueAnimator.ofArgb(startColor, endColor);
        colorAnimator.setDuration(500);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int color = (Integer) valueAnimator.getAnimatedValue();
                setMainColor(color);
            }
        });
        colorAnimator.start();
    }

    private void setSpeakOrShow() {
        int speakOrShow = preferences.getSpeakOrShow();
        setSpeakOrShowIcon();
        setMainColor(speakOrShow == Preferences.SPEAK ? primarySpeakColor : primaryShowColor);
    }

    private void setSpeakOrShowIcon() {
        int speakOrShow = preferences.getSpeakOrShow();
        leftButtonIcon.setBackground(speakOrShow == Preferences.SPEAK ? speakIconBack : handIcon);
        Utils.setVisibility(speakIconFront, speakOrShow == Preferences.SPEAK);
    }

    private void setMainColor(int color) {
        searchBarBackground.setColorFilter(color);
        overlayBottomLine.setBackgroundColor(color);
        leftMainButton.setColorFilter(color);
        rightMainButton.setColorFilter(color);
        StatusBarUtils.setStatusBarColor((Activity) getContext(), color);
        ((MainActivity) getContext()).setCallButtonsColor(color);
    }
}
