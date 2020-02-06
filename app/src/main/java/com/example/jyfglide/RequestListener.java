package com.example.jyfglide;

import android.graphics.Bitmap;

public interface RequestListener {
    void onSuccess(Bitmap bitmap);
    void onFail();
}
