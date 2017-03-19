package com.example.sasha.okhear.camera;

import android.support.v4.app.Fragment;

import com.example.sasha.okhear.Overlay_;
import com.example.sasha.okhear.R;
import com.example.sasha.okhear.utils.Preferences;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_camera)
public class CameraFragment extends Fragment {

    @Bean
    Preferences preferences;

    Overlay_ overlay;

    public CameraFragment() {
    }

    @AfterViews
    void init() {
    }

    public void setOverlay(Overlay_ overlay) {
        this.overlay = overlay;
    }

}
