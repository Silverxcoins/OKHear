package com.example.sasha.okhear;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.sasha.okhear.Utils.Preferences;
import com.example.sasha.okhear.Utils.StatusBarUtils;
import com.example.sasha.okhear.camera.CameraFragment_;
import com.example.sasha.okhear.contacts.ContactsFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Bean
    Preferences preferences;

    @ViewById(R.id.overlay)
    Overlay_ overlay;

    ContactsFragment_ contactsFragment = new ContactsFragment_();
    CameraFragment_ cameraFragment = new CameraFragment_();

    boolean cameraFragmentActive = false;

    @AfterViews
    protected void init() {
        StatusBarUtils.setupFullscreenActivity(this);
        contactsFragment.setOverlay(overlay);
        setContactsFragment();
    }

    private void setContactsFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_fragment_container, contactsFragment);
        ft.commit();
    }

    public void setCameraFragment() {
        cameraFragmentActive = true;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_down_anim, R.anim.disappear_anim, R.anim.appear_anim, R.anim.slide_up_anim);
        ft.replace(R.id.main_fragment_container, cameraFragment);
        ft.addToBackStack(null);
        ft.commit();
        StatusBarUtils.setupFullscreenActivity(this);
    }

    public void setCallButtonsColor(int color) {
        contactsFragment.setCallButtonsColor(color);
    }

    public boolean isCameraFragmentActive() {
        return cameraFragmentActive;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (cameraFragmentActive) {
            cameraFragmentActive = false;
            overlay.startSlideSearchBarAnimation(false);
        }
    }
}
