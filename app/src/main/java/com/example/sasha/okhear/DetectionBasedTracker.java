package com.example.sasha.okhear;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;

public class DetectionBasedTracker {
    public DetectionBasedTracker(String cascadeName, int minHandSize) {
        mNativeObj = nativeCreateObject(cascadeName, minHandSize);
    }


    public void setMinHandSize(int size) {
        nativeSetFaceSize(mNativeObj, size);
    }

    public void release() {
        nativeDestroyObject(mNativeObj);
        mNativeObj = 0;
    }

    private long mNativeObj = 0;

    private static native long nativeCreateObject(String cascadeName, int minHandSize);

    private static native void nativeDestroyObject(long thiz);

    private static native void nativeStart(long thiz);

    private static native void nativeStop(long thiz);

    private static native void nativeSetFaceSize(long thiz, int size);

    private static native void nativeDetect(long thiz, long inputImage, long faces);
}
