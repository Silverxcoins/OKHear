package com.example.sasha.okhear.camera;

import android.hardware.Camera;
import android.widget.ImageView;

import com.example.sasha.okhear.utils.Http;
import com.example.sasha.okhear.utils.Utils;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@EBean(scope = EBean.Scope.Singleton)
public class ImagesServerCommunication {

    public interface Callback {
        void onResponse(String response);
    }

    private final Executor executor = Executors.newSingleThreadExecutor();

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void sendToServer(final Camera camera, final byte[] bytes) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String response = sendToServerInternal(camera, bytes);
                notifyGetResponse(response);
            }
        });
    }

    private String sendToServerInternal(Camera camera, byte[] bytes) {
        byte[] jpegBytes = Utils.convertToJpeg(camera, bytes, null);
        return Http.sendMultiPartPostRequest("", jpegBytes);
    }

    @UiThread
    void notifyGetResponse(String response) {
        if (callback != null) {
            callback.onResponse(response);
        }
    }
}
