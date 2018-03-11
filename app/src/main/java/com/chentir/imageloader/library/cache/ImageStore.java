package com.chentir.imageloader.library.cache;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.WorkerThread;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * @impl the image is first fetched from the memory store then from the remote store
 */
@ThreadSafe public class ImageStore {

    @GuardedBy("this") private final MemoryImageCache memoryImageCache;

    public ImageStore() {
        this(new MemoryImageCache());
    }

    @VisibleForTesting
    ImageStore(MemoryImageCache memoryImageCache) {
        this.memoryImageCache = memoryImageCache;
    }

    /**
     * Fetch the image from one of the cache stores (if available)
     *
     * @return a {@link Bitmap} representing the image to be downloaded or {@code null} if the image has never been
     * cached in the store before
     */
    @Nullable
    public synchronized Bitmap fetchImage(@NonNull String url) {
        return memoryImageCache.fetch(url);
    }

    @WorkerThread
    public synchronized void putImage(@NonNull String url, @NonNull Bitmap bitmap) {
        memoryImageCache.put(url, bitmap);
    }

    /**
     * Clear all caches. This method should be called when the system is running low on memory.
     */
    public synchronized void clearAll() {
        memoryImageCache.clear();
    }

    public synchronized void adjustCapacity(@NonNull ImageCache.SystemResourceAvailability systemResourceAvailability) {
        memoryImageCache.onAdjustCapacity(systemResourceAvailability);
    }
}