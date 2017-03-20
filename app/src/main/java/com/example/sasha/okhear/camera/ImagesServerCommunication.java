package com.example.sasha.okhear.camera;

import com.example.sasha.okhear.utils.Http;

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

    public void sendToServer(final byte[] bytes) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String response = sendToServerInternal(bytes);
                notifyGetResponse(response);
            }
        });
    }

    private String sendToServerInternal(byte[] bytes) {
        return Http.sendMultiPartPostRequest("", bytes);
    }

    @UiThread
    void notifyGetResponse(String response) {
        if (callback != null) {
            callback.onResponse(response);
        }
    }
}
