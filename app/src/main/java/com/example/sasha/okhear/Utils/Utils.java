package com.example.sasha.okhear.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Utils {
    public static void setVisibility(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    public static byte[] convertToJpeg(Camera camera, byte[] bytes, ImageView iv) {
        Camera.Parameters parameters = camera.getParameters();
        int format = parameters.getPreviewFormat();

        int w = parameters.getPreviewSize().width;
        int h = parameters.getPreviewSize().height;

        YuvImage yuvImage = new YuvImage(bytes, format, w, h, null);
        Rect rect = new Rect(0, 0, w, h);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(rect, 50, out);
        byte[] jpegBytes = out.toByteArray();
        return jpegBytes;
    }
}
