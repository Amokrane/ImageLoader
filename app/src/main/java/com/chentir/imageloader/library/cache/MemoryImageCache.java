package com.chentir.imageloader.library.cache;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;

class MemoryImageCache implements ImageCache {

    /**
     * Represent the initial ratio of the VM heap budget used by the memory cache
     */
    private static final double VM_BUDGET_INITIAL_RATIO = 0.15;

    private static final double VM_BUDGET_MEDIUM_RATIO = 0.1;

    private static final double VM_BUDGET_LOW_RATIO = 0.05;

    private static final double VM_BUDGET_CRITICAL_RATIO = 0;

    private static final int INITIAL_CACHE_SIZE_IN_MB =
        (int) (VM_BUDGET_INITIAL_RATIO * Runtime.getRuntime().maxMemory() * 1024 * 1024);

    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(INITIAL_CACHE_SIZE_IN_MB) {
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount();
        }
    };

    @Override
    @Nullable
    public Bitmap fetch(String url) {
        return cache.get(url);
    }

    @Override
    public void put(@NonNull String url, @NonNull Bitmap bitmap) {
        cache.put(url, bitmap);
    }

    @Override
    public void clear() {
        cache.evictAll();
    }

    @Override
    public void onAdjustCapacity(@NonNull SystemResourceAvailability systemResourceAvailability) {
        switch(systemResourceAvailability) {
            case CRITICAL:
                cache.resize((int) (VM_BUDGET_CRITICAL_RATIO * Runtime.getRuntime().maxMemory() * 1024 * 1024));
                break;

            case MEDIUM:
                cache.resize((int) (VM_BUDGET_MEDIUM_RATIO * Runtime.getRuntime().maxMemory() * 1024 * 1024));
                break;

            case LOW:
                cache.resize((int) (VM_BUDGET_LOW_RATIO * Runtime.getRuntime().maxMemory() * 1024 * 1024));
                break;
        }
    }
}