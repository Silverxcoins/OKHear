package com.example.sasha.okhear;

import android.animation.ValueAnimator;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.sasha.okhear.camera.CameraScreen;
import com.example.sasha.okhear.contacts.ContactsFragment;
import com.example.sasha.okhear.contacts.ContactsFragment_;
import com.example.sasha.okhear.utils.Preferences;
import com.example.sasha.okhear.utils.BarsUtil;
import com.example.sasha.okhear.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Bean
    Preferences preferences;

    @ViewById(R.id.overlay)
    Overlay overlay;

    CameraScreen cameraScreen;

    private ContactsFragment contactsFragment = ContactsFragment_.builder().build();

    private volatile boolean cameraScreenActive = false;

    @AfterViews
    protected void init() {
        cameraScreen = (CameraScreen) findViewById(R.id.camera_screen);
        BarsUtil.setupFullscreenActivity(this);
        contactsFragment.setOverlay(overlay);
        overlay.setCameraScreen(cameraScreen);
        setContactsFragment();
    }

    private void setContactsFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_fragment_container, contactsFragment);
        ft.commit();
    }

    public void setCameraScreen(final boolean set) {
        Utils.setVisibility(cameraScreen, true);
        this.cameraScreenActive = set;
        final int height = cameraScreen.getHeight();
        final int startValue = set ? 0 - height : 0;
        final int endValue = set ? 0 : 0 - height;
        ValueAnimator animator =  ValueAnimator.ofInt(startValue, endValue);
        animator.setDuration(400);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                cameraScreen.setTranslationY(value);
            }
        });
        cameraScreen.onShowCamera(set);
        animator.start();
    }

    public void setCallButtonsColor(int color) {
        contactsFragment.setCallButtonsColor(color);
    }

     public boolean isCameraScreenActive() {
        return cameraScreenActive;
    }

    @Override
    public void onBackPressed() {
        if (cameraScreenActive) {
            setCameraScreen(false);
            overlay.startCameraDownAnimations(false);
        } else {
            super.onBackPressed();
        }
    }
}
