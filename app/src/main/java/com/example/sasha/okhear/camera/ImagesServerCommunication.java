package com.example.sasha.okhear.camera;

import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.sasha.okhear.utils.Http;
import com.example.sasha.okhear.utils.Utils;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SupposeBackground;
import org.androidannotations.annotations.UiThread;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
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

    public void sendToServerWithSocket(final Camera camera, final byte[] data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendToServerSocketInternal(camera, data);
            }
        }).start();
    }

    private void sendToServerSocketInternal(Camera camera, byte[] data) {
        byte[] jpegBytes = Utils.convertToJpeg(camera, data, null);
        byte[] endBytes = "\r\r\n".getBytes();
        byte[] result = new byte[jpegBytes.length + endBytes.length];
        for (int i = 0; i < result.length; ++i) {
            result[i] = i < jpegBytes.length ? jpegBytes[i] : endBytes[i - jpegBytes.length];
        }
        try {
            Socket socket = new Socket("62.109.1.48", 6000);

            try(InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream()) {

                out.write(jpegBytes);
                out.flush();

                byte[] responseBytes = new byte[1024 * 32];
                int readBytes = in.read(responseBytes);

                System.out.println("RESPONSE!!! " + new String(responseBytes, 0, readBytes));
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
