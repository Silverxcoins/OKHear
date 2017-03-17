package com.example.sasha.okhear;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DrawableRes;

@EViewGroup(R.layout.overlay)
public class Overlay extends RelativeLayout {

    @DrawableRes(R.drawable.ic_hand)
    Drawable handIcon;

    @ViewById(R.id.left_main_button_icon)
    LinearLayout leftButtonIcon;

    @Click(R.id.left_main_button_icon)
    void onClickLeftButton() {
    }

    public Overlay(Context context) {
        super(context);
    }

    public Overlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Overlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
