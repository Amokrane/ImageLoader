package com.chentir.imageloader.library;

import android.support.annotation.NonNull;
import com.chentir.imageloader.library.cache.ImageStore;
import com.chentir.imageloader.library.retry.ExponentialBackoff;
import com.chentir.imageloader.library.retry.RetryStrategy;
import okhttp3.OkHttpClient;

/**
 * A Singleton class that provides all the dependencies to the classes of the library
 */
class ImageLoaderModule {

    private static volatile ImageLoaderModule sInstance;

    private final ImageStore imageStore;

    private final OkHttpClient okHttpClient;

    private final RetryStrategy retryStrategy;

    private final BitmapPool bitmapPool;

    private ImageLoaderModule() {
        imageStore = new ImageStore();
        okHttpClient = new OkHttpClient();
        retryStrategy = new ExponentialBackoff();
        bitmapPool = new BitmapPool();
    }

    @NonNull
    public static synchronized ImageLoaderModule getInstance() {
        if (sInstance == null) {
            sInstance = new ImageLoaderModule();
        }
        return sInstance;
    }

    @NonNull
    public synchronized ImageStore getImageStore() {
        return imageStore;
    }

    @NonNull
    public synchronized OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    @NonNull
    public synchronized RetryStrategy getRetryStrategy() {
        return retryStrategy;
    }

    @NonNull
    public synchronized BitmapPool getBitmapPool() {
        return bitmapPool;
    }
}
