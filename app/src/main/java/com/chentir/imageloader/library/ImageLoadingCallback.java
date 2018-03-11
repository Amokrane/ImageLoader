package com.chentir.imageloader.library;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

public interface ImageLoadingCallback {
    void onSuccess(@NonNull ImageLoadingRequest imageLoadingRequest, Bitmap bitmap);

    void onError(@NonNull ImageLoadingRequest imageLoadingRequest, @NonNull Throwable throwable);
}