package com.example.sasha.okhear.camera;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sasha.okhear.Overlay_;
import com.example.sasha.okhear.R;
import com.example.sasha.okhear.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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

    private Timer timer;

    private volatile boolean startClicked = false;
    AtomicBoolean timerFinished = new AtomicBoolean(false);

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
                camera.startPreview();
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
        if (!startClicked) {
            imagesServerCommunication.setCallback(this);
            camera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] bytes, Camera camera) {
                    if (timer == null || timerFinished.get()) {
                        imagesServerCommunication.sendToServer(camera, bytes, iv);

                        if (timer == null) {
                            timer = new Timer();
                        }
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                timerFinished.set(true);
                            }
                        }, 1000);
                        timerFinished.set(false);
                    }

                    ////////////
//                    Camera.Parameters params = camera.getParameters();
//                    int w = params.getPreviewSize().width;
//                    int h = params.getPreviewSize().height;
//                    int format = params.getPreviewFormat();
//                    YuvImage image = new YuvImage(bytes, format, w, h, null);
//
//                    ByteArrayOutputStream out = new ByteArrayOutputStream();
//                    Rect area = new Rect(0, 0, w, h);
//                    image.compressToJpeg(area, 100, out);
//                    Bitmap bm = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size());
//                    iv.setImageBitmap(Utils.RotateBitmap(bm));
                    ////////////
                }
            });
        } else {
            timer.cancel();
            timer = null;
            camera.setOneShotPreviewCallback(null);
            imagesServerCommunication.setCallback(null);
        }
        startStartButtonAnimation();
        startClicked = !startClicked;
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
            JSONObject json = new JSONObject(response);
            startText.setText(String.valueOf(json.get("gesture")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
