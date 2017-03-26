package com.example.sasha.okhear.camera;

import android.animation.ValueAnimator;
import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sasha.okhear.Overlay_;
import com.example.sasha.okhear.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

@EViewGroup
public class CameraScreen extends FrameLayout implements ImagesServerCommunication.Callback{

    @ViewById(R.id.camera_view)
    SurfaceView cameraView;

    @ViewById(R.id.start_button)
    View startButton;

    @ViewById(R.id.start_text)
    TextView startText;

    @ViewById(R.id.iv)
    ImageView iv;

    @Bean
    ImagesServerCommunication imagesServerCommunication;

    private Overlay_ overlay;

    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private int cameraId;

    private Timer timer = new Timer();

    private volatile boolean startClicked = false;
    AtomicBoolean readyToSend = new AtomicBoolean(true);

    public CameraScreen(Context context) {
        super(context);
    }

    public CameraScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    public void init() {
        surfaceHolder = cameraView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceCreated(final SurfaceHolder holder) {
                if (camera == null) {
                    camera = Camera.open();
                }
                cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                try {
                    camera.setPreviewDisplay(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Camera.Size previewSize = camera.getParameters().getPreviewSize();
                float aspect = (float) previewSize.width / previewSize.height;
                int previewSurfaceHeight = cameraView.getHeight();
                camera.setDisplayOrientation(90);
                cameraView.getLayoutParams().height = previewSurfaceHeight;
                cameraView.getLayoutParams().width = (int) (previewSurfaceHeight / aspect);

                Camera.Parameters params = camera.getParameters();
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                camera.setParameters(params);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (camera != null) {
                    camera.setPreviewCallback(null);
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                }
            }
        });
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setOverlay(Overlay_ overlay) {
        this.overlay = overlay;
    }

    @Click(R.id.start_button)
    void onStartButtonClick() {
        startSendingFrames(!startClicked);
        startStartButtonAnimation();
        startClicked = !startClicked;
    }

    private void startSendingFrames(boolean start) {
        if (start) {
            imagesServerCommunication.setCallback(this);
            readyToSend.set(true);
            camera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] bytes, final Camera camera) {
                    if (readyToSend.get()) {
                        readyToSend.set(false);
                        imagesServerCommunication.sendToServerWithSocket(camera, bytes);
                    }
                }
            });
        } else {
            camera.setOneShotPreviewCallback(null);
            imagesServerCommunication.setCallback(null);
        }
    }

    private void startStartButtonAnimation() {
        ValueAnimator animator;
        if (!startClicked) {
            startText.setText("");
            animator = ValueAnimator.ofFloat(1, 5);
        } else {
            startText.setText("Start");
            animator = ValueAnimator.ofFloat(5, 1);
        }
        animator.setDuration(400);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                startButton.setScaleX(value);
                startButton.setScaleY(value);
            }
        });
        animator.start();
    }

    @Override
    public void onResponse(String response) {
        try {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    readyToSend.set(true);
                }
            }, 400);
            JSONObject json = new JSONObject(response);
            startText.setText(String.valueOf(json.get("gesture")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onShowCamera(boolean show) {
        if (show) {
            camera.startPreview();
        } else {
            camera.stopPreview();
        }
    }

    public void swapCamera() {
        if (startClicked) {
            startSendingFrames(false);
        }
        camera.stopPreview();
        camera.release();

        if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
            cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        else {
            cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        camera = Camera.open(cameraId);
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
        if (startClicked) {
            startSendingFrames(true);
        }
    }
}
