package com.example.sasha.okhear.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Http {

    private static final String URL = "http://62.109.1.48:5000/api/photo";

    private static final MediaType JPEG = MediaType.parse("image/jpeg");

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager =
                ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static String sendMultiPartPostRequest(String url, byte[] bytes) {
        RequestBody formBody = null;
        try {
            formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "file", RequestBody.create(JPEG, bytes))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder()
                .url(URL)
                .post(formBody)
                .build();
        try {
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String sendGetRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            return null;
        }
    }
}