package com.example.sasha.okhear;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.sasha.okhear.camera.CameraScreen_;
import com.example.sasha.okhear.utils.Preferences;
import com.example.sasha.okhear.utils.StatusBarUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.DimensionPixelSizeRes;

@EViewGroup
public class Overlay extends RelativeLayout {

    @ViewById(R.id.left_main_button_icon)
    View leftButtonIcon;

    @ViewById(R.id.search_bar)
    FrameLayout searchBar;

    @ViewById(R.id.search_bar_background)
    ImageView searchBarBackground;

    @ViewById(R.id.overlay_bottom_line)
    View overlayBottomLine;

    @ViewById(R.id.left_main_button)
    FrameLayout leftMainButton;

    @ViewById(R.id.left_main_button_background)
    ImageView leftMainButtonBackground;

    @ViewById(R.id.right_main_button)
    FrameLayout rightMainButton;

    @ViewById(R.id.right_main_button_background)
    ImageView rightMainButtonBackground;

    @ViewById(R.id.status_bar_background)
    View statusBarBackground;

    @ViewById(R.id.search_bar_with_status_background)
    LinearLayout searchBarWithStatusBackground;

    @ViewById(R.id.right_main_button_icon)
    View rightButtonIcon;

    @DimensionPixelSizeRes(R.dimen.search_bar_height)
    int searchBarHeight;

    @DimensionPixelSizeRes(R.dimen.main_button_diameter)
    int mainButtonDiameter;

    @ColorRes(R.color.colorPrimaryShow)
    int primaryShowColor;

    @ColorRes(R.color.colorPrimarySpeak)
    int primarySpeakColor;

    @Bean
    Preferences preferences;

    private CameraScreen_ cameraScreen;

    private volatile boolean controlsHidden = false;

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
        statusBarBackground.getLayoutParams().height = StatusBarUtils.getStatusBarHeight(getContext());
        statusBarBackground.invalidate();
        preferences.setSpeakOrShow(Preferences.SPEAK);
        setSpeakOrShow();
    }

    @Click(R.id.left_main_button)
    void onClickLeftButton() {
        preferences.changeSpeakOrShow();
        startRotateAnimation(leftMainButton, leftButtonIcon, getLeftButtonIconRes());
        startColorAnimation();
    }

    @Click(R.id.right_main_button)
    void onClickRightButton() {
        if (!getMainActivity().isCameraScreenActive()) {
            getMainActivity().setCameraScreen(true);
            startCameraDownAnimations(true);
            leftMainButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    cameraScreen.swapCamera();
                }
            });
        } else {
            getMainActivity().setCameraScreen(false);
            startCameraDownAnimations(false);
            leftMainButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickLeftButton();
                }
            });
        }
    }

    public void startSlideSearchBarAnimation(final boolean down) {
        ValueAnimator slideSearchBarAnimator = (down ? ValueAnimator.ofInt(0, getBottom()) : ValueAnimator.ofInt(getBottom(), 0));
        slideSearchBarAnimator.setDuration(400);
        slideSearchBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                searchBarWithStatusBackground.setY(value);
            }
        });
        slideSearchBarAnimator.start();
    }

    public void showControls(boolean show) {
        if (show == controlsHidden) {
            controlsHidden = !controlsHidden;

            int startSearchBarValue = (int) searchBarWithStatusBackground.getTranslationY();
            int endSearchBarValue = (show ? 0 : 0 - searchBarHeight - StatusBarUtils.getStatusBarHeight(getContext()));
            ValueAnimator searchBarAnimator = ValueAnimator.ofInt(startSearchBarValue, endSearchBarValue);
            searchBarAnimator.setDuration(400);
            searchBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int value = (Integer) valueAnimator.getAnimatedValue();
                    searchBarWithStatusBackground.setTranslationY(value);
                }
            });

            int startMainButtonsValue = (int) leftMainButton.getTranslationX();
            int endMainButtonsValue = (show ? 0 : 0 - mainButtonDiameter);
            ValueAnimator mainButtonsAnimator = ValueAnimator.ofInt(startMainButtonsValue, endMainButtonsValue);
            mainButtonsAnimator.setDuration(400);
            mainButtonsAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int value = (Integer) valueAnimator.getAnimatedValue();
                    leftMainButton.setTranslationX(value);
                    leftMainButton.setTranslationY(0 - value);
                    rightMainButton.setTranslationX(0 - value);
                    rightMainButton.setTranslationY(0 - value);
                }
            });

            searchBarAnimator.start();
            mainButtonsAnimator.start();
        }
    }

    public void setCameraScreen(CameraScreen_ cameraScreen) {
        this.cameraScreen = cameraScreen;
    }

    public void startCameraDownAnimations(boolean down) {
        startSlideSearchBarAnimation(down);
        startRotateAnimation(leftMainButton, leftButtonIcon, getLeftButtonIconRes());
        startRotateAnimation(rightMainButton, rightButtonIcon, getRightButtonIconRes());
    }

    private void startRotateAnimation(final View button, final View buttonIconView, final @DrawableRes int iconRes) {
        button.setEnabled(false);
        ValueAnimator rotationAnimator = ValueAnimator.ofFloat(0, 180);
        rotationAnimator.setDuration(500);
        rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                if (value <= 90) {
                    buttonIconView.setRotationY(value);
                } else {
                    buttonIconView.setRotationY(180 - value);
                    buttonIconView.setBackgroundResource(iconRes);
                }
                if (value == 180) {
                    button.setEnabled(true);
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
        leftButtonIcon.setBackgroundResource(getLeftButtonIconRes());
        setMainColor(speakOrShow == Preferences.SPEAK ? primarySpeakColor : primaryShowColor);
    }

    private
    @DrawableRes
    int getLeftButtonIconRes() {
         if (!getMainActivity().isCameraScreenActive()) {
             int speakOrShow = preferences.getSpeakOrShow();
             return (speakOrShow == Preferences.SPEAK ? R.drawable.ic_speak : R.drawable.ic_hand);
         } else {
             return R.drawable.ic_swap;
         }
    }

    private
    @DrawableRes
    int getRightButtonIconRes() {
        boolean isCameraScreenActive = getMainActivity().isCameraScreenActive();
        return (isCameraScreenActive ? R.drawable.ic_contacts : R.drawable.ic_camera);
    }

    private void setMainColor(int color) {
        statusBarBackground.setBackgroundColor(color);
        searchBarBackground.setColorFilter(color);
        overlayBottomLine.setBackgroundColor(color);
        leftMainButtonBackground.setColorFilter(color);
        rightMainButtonBackground.setColorFilter(color);
        ((MainActivity_) getContext()).setCallButtonsColor(color);
    }

    private MainActivity_ getMainActivity() {
        return (MainActivity_) getContext();
    }

}
