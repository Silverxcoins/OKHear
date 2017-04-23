package com.example.sasha.okhear.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.view.View;

import com.example.sasha.okhear.camera.FrameManager;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;

@SuppressWarnings("deprecation")
public class Utils {
    public static void setVisibility(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    public static Mat convertToGrayColors(Mat src) {
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_RGB2GRAY, 4);
        return gray;
    }

    public static FrameManager.BitmapWithCoords cropBitmap(Bitmap bitmap, Rect rect) {
        int height = rect.height + rect.height;
        int width = height * 576 / 1024;
        int y = rect.y - rect.height * 2 / 3;
        if (y < 0) {
            y = 0;
        }
        int x = rect.x - (width - rect.width) / 2;
        if (x < 0) {
            x = 0;
        }
        if (x + width > bitmap.getWidth()) {
            width = bitmap.getWidth() - x;
        }
        if (y + height > bitmap.getHeight()) {
            height = bitmap.getHeight() - y;
        }
        return new FrameManager.BitmapWithCoords(
                Bitmap.createBitmap(bitmap, x, y, width, height),
                x, y, width, height
        );
    }

    public static Bitmap frameBytesToBitmap(Camera camera, byte[] bytes, boolean isFrontCamera) {
        Camera.Parameters parameters = camera.getParameters();
        int format = parameters.getPreviewFormat();
        int width = parameters.getPreviewSize().width;
        int height = parameters.getPreviewSize().height;
        Bitmap bitmap = bytesToBitmap(bytes, format, width, height);
        bitmap = Bitmap.createScaledBitmap(bitmap, width / 2, height / 2, true);

        return rotateBitmap(bitmap, isFrontCamera);
    }

    public static byte[] getSmallBitmapBytes(Bitmap bitmap) {
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 32, 32, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private static Bitmap bytesToBitmap(byte[] bytes, int format, int width, int height) {
        YuvImage yuvImage = new YuvImage(bytes, format, width, height, null);
        android.graphics.Rect rect = new android.graphics.Rect(0, 0, width, height);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(rect, 50, out);
        byte[] jpegBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(jpegBytes, 0, out.size());
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, boolean isFrontCamera) {
        Matrix matrix = new Matrix();
        matrix.postRotate(isFrontCamera ? 270 : 90);
        if (isFrontCamera) {
            matrix.postScale(-1, 1, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
