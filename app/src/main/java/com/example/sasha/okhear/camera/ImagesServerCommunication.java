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

    public void sendToServer(final Camera camera, final byte[] bytes, final ImageView iv) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String response = sendToServerInternal(camera, bytes, iv);
                notifyGetResponse(response);
            }
        });
    }

    private String sendToServerInternal(Camera camera, byte[] bytes, ImageView iv) {
        byte[] jpegBytes = Utils.convertToJpeg(camera, bytes, iv);
        return Http.sendMultiPartPostRequest("", jpegBytes);
    }

    @UiThread
    void notifyGetResponse(String response) {
        if (callback != null) {
            callback.onResponse(response);
        }
    }
}
