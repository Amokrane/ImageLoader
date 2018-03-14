package com.chentir.imageloader.library;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/**
 * Callback that will be called used by any task responsible for loading images to notify {@link ImageLoader} that the
 * job is done
 */
public interface ImageLoadingCallback {
    void onSuccess(@NonNull ImageLoadingRequest imageLoadingRequest, Bitmap bitmap);

    void onError(@NonNull ImageLoadingRequest imageLoadingRequest, @NonNull Throwable throwable);
}