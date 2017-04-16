package com.example.sasha.okhear.camera;

import android.hardware.Camera;

import com.example.sasha.okhear.utils.Ui;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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
//        byte[] jpegBytes = Utils.frameBytesToBitmap(camera, bytes);
//        return Http.sendMultiPartPostRequest("", jpegBytes);
        return "";
    }

    @UiThread
    void notifyGetResponse(String response) {
        if (callback != null) {
            callback.onResponse(response);
        }
    }

    public void sendToServerWithSocket(final byte[] data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendToServerSocketInternal(data);
            }
        }).start();
    }

    private void sendToServerSocketInternal(byte[] data) {
        byte[] endBytes = "\r\r\n".getBytes();
        byte[] result = new byte[data.length + endBytes.length];
        for (int i = 0; i < result.length; ++i) {
            result[i] = i < data.length ? data[i] : endBytes[i - data.length];
        }
        try {
            Socket socket = new Socket("62.109.1.48", 9000);

            try(InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream()) {

                out.write(result);
                out.flush();

                final byte[] responseBytes = new byte[1024 * 32];
                final int readBytes = in.read(responseBytes);

                if (callback != null) {
                    Ui.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResponse(new String(responseBytes, 0, readBytes));
                        }
                    });
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
