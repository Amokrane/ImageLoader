package com.chentir.imageloader.library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.chentir.imageloader.library.cache.MemoryImageCache;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * A simple fixed size object pool that stores {@link Bitmap}s for later reuse to reduce memory footprint
 */
public class BitmapPool {
    /**
     * The default size of the pool. This is a very naive approach. Ideally, we would want to express the size of the
     * pool
     * in bytes and choose an appropriate size based on available memory. This would have been similar to {@link
     * MemoryImageCache}.
     */
    private static final int DEFAULT_POOL_CAPACITY = 10;

    /**
     * The pool is backed by a NavigableMap as it allows finding the highest entry of a given key efficiently (in
     * O(log(n)). This is useful, as a Bitmap can only be reused if its size in bytes is higher than or equal to the
     * size of the Bitmap that is needed.
     */
    private final NavigableMap<PoolKey, Bitmap> pool = new TreeMap<>();

    /**
     * Put a {@link Bitmap} back into the pool
     *
     * <p>
     * The bitmap would only be put into the pool if the size of the pool does not execeed {@link
     * #DEFAULT_POOL_CAPACITY}
     * </p>
     */
    public synchronized boolean putBack(@NonNull Bitmap bitmap) {
        Preconditions.checkNotNull(bitmap, "null Bitmaps cannot be put back to the BitmapPool");
        Preconditions.ensure(!bitmap.isRecycled(), "Recycled Bitmaps cannot be put back to the BitmapPool");

        if (pool.size() < DEFAULT_POOL_CAPACITY) {
            PoolKey poolKey = new PoolKey(calculateByteCount(bitmap));
            pool.put(poolKey, bitmap);
            return true;
        }

        return false;
    }

    /**
     * Take the next {@link Bitmap} available and suitable object from the pool.
     *
     * <p>
     * A {@link Bitmap} is suitable for reuse only its size in bytes is greater or equal that the size of the needed
     * {@link Bitmap}
     * </p>
     *
     * @return a {@link Bitmap}  that is suitable for reuse or {@code null} if no {@link Bitmap} was found
     */
    @Nullable
    public synchronized Bitmap take(BitmapFactory.Options targetOptions) {
        int desiredByteCount = calculateByteCount(targetOptions);
        PoolKey desiredPoolKey = new PoolKey(desiredByteCount);
        NavigableMap.Entry<PoolKey, Bitmap> poolEntry = pool.higherEntry(desiredPoolKey);
        return poolEntry != null ? poolEntry.getValue() : null;
    }

    /**
     * Clear and recycle all the {@link Bitmap}s in the pool
     */
    public synchronized void clear() {
        while (!pool.isEmpty()) {
            NavigableMap.Entry<PoolKey, Bitmap> entry = pool.pollFirstEntry();
            Bitmap bitmap = entry.getValue();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
        }

        pool.clear();
    }

    /**
     * @return the size of the {@link Bitmap} in bytes
     */
    private int calculateByteCount(@NonNull Bitmap bitmap) {
        Preconditions.checkNotNull(bitmap, "Cannot get the byte count on a null Bitmap");
        return calculateByteCount(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
    }

    private int calculateByteCount(@NonNull BitmapFactory.Options targetOptions) {
        Preconditions.checkNotNull(targetOptions, "Cannot get the byte count on null targetOptions");
        Bitmap.Config bitmapConfig;

        // Starting from Android Oreo, targetOptions.outConfig can be used to fetch the config
        // the decoded bitmap will have. Otherwise, the fallback strategy is to use targetOptions.inPreferredConfig.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bitmapConfig = targetOptions.outConfig;
        } else {
            bitmapConfig = targetOptions.inPreferredConfig;
        }

        return calculateByteCount(targetOptions.outWidth, targetOptions.outHeight, bitmapConfig);
    }

    private int calculateByteCount(int width, int height, @NonNull Bitmap.Config config) {
        Preconditions.checkNotNull(config, "Cannot get the byte count with a null Bitmap.Config");
        return width * height * getBytesPerPixel(config);
    }

    private int getBytesPerPixel(Bitmap.Config config) {
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }

        int bytesPerPixel;
        switch (config) {
            case ALPHA_8:
                bytesPerPixel = 1;
                break;
            case RGB_565:
            case ARGB_4444:
                bytesPerPixel = 2;
                break;
            case ARGB_8888:
            default:
                bytesPerPixel = 4;
                break;
        }
        return bytesPerPixel;
    }
}