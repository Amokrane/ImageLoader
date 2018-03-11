package com.chentir.imageloader.library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BitmapPool {
    private static final int DEFAULT_POOL_CAPACITY= 20;

    private final BlockingQueue<Bitmap> pool = new ArrayBlockingQueue<>(DEFAULT_POOL_CAPACITY);

    private volatile int liveCount = 0;

    public void add(@NonNull Bitmap bitmap) {
        synchronized (this) {
            if (pool.size() < DEFAULT_POOL_CAPACITY) {
                try {
                    liveCount++;
                    pool.put(bitmap);
                } catch (InterruptedException e) {
                    // FIXME: is this good?
                    // don't do anything
                }
            }
        }
    }

    @Nullable
    public Bitmap take() {
        synchronized (this) {
            try {
                return pool.take();
            } catch (InterruptedException e) {
                // FIXME: is this good?
                // don't do anything
            }
        }

        return null;
    }

    public boolean hasBeenInitialized() {
        return liveCount >= DEFAULT_POOL_CAPACITY;
    }

    public boolean canUseForInBitmap(
        Bitmap candidate, BitmapFactory.Options targetOptions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // From Android 4.4 (KitKat) onward we can re-use if the byte size of
            // the new bitmap is smaller than the reusable bitmap candidate
            // allocation byte count.
            int width = targetOptions.outWidth / targetOptions.inSampleSize;
            int height = targetOptions.outHeight / targetOptions.inSampleSize;
            int byteCount = width * height * getBytesPerPixel(candidate.getConfig());

            try {
                return byteCount <= candidate.getAllocationByteCount();
            } catch (NullPointerException e) {
                return byteCount <= candidate.getHeight() * candidate.getRowBytes();
            }
        }
        // On earlier versions, the dimensions must match exactly and the inSampleSize must be 1
        return candidate.getWidth() == targetOptions.outWidth
            && candidate.getHeight() == targetOptions.outHeight
            && targetOptions.inSampleSize == 1;
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