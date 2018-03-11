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

    @GuardedBy("this") private final DiskImageCache diskImageCache;

    @GuardedBy("this") private final MemoryImageCache memoryImageCache;

    public ImageStore() {
        this(new DiskImageCache(), new MemoryImageCache());
    }

    @VisibleForTesting
    ImageStore(DiskImageCache diskImageCache, MemoryImageCache memoryImageCache) {
        this.diskImageCache = diskImageCache;
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
        // TODO: add pre-conditions
        // TODO: add an AssertBackgroundThread
        // FIXME: you cannot just read image from disk image on UI thread. Either do it correctly or remove disk caching entirely
        return memoryImageCache.fetch(url) == null ? memoryImageCache.fetch(url) : diskImageCache.fetch(url);
    }

    @WorkerThread
    public synchronized void putImage(@NonNull String url, @NonNull Bitmap bitmap) {
        // TODO: add an AssertBackgroundThread
        // TODO: add pre-conditions
        // TODO: define whether you want a write back or write through caching strategy
    }

    /**
     * Clear all caches. This method should be called when the system is running low on memory.
     */
    public synchronized void clearAll() {
        memoryImageCache.clear();
        diskImageCache.clear();
    }

    public synchronized void adjustCapacity(@NonNull ImageCache.SystemResourceAvailability systemResourceAvailability) {
        memoryImageCache.onAdjustCapacity(systemResourceAvailability);
        diskImageCache.onAdjustCapacity(systemResourceAvailability);
    }
}