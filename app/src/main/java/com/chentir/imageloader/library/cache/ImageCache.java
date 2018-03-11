package com.chentir.imageloader.library.cache;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface ImageCache {

    /**
     * Coarse levels of cache resource availability
     */
    enum SystemResourceAvailability {
        MEDIUM,
        LOW,
        CRITICAL
    }

    @Nullable
    Bitmap fetch(String url);

    void clear();

    /**
     * Adjust the capacity of the cache given the availability of the used system resource
     * @param systemResourceAvailability
     */
    void onAdjustCapacity(@NonNull SystemResourceAvailability systemResourceAvailability);
}
