package me.lingxiao.exam.util;

import android.graphics.Bitmap;

public interface HttpCallbackListener {
    void onFinish(String response);

    void onFinish(Bitmap bitmap);

    void onError(Exception e);
}
