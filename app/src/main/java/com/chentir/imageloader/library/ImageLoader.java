package com.chentir.imageloader.library;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import com.chentir.imageloader.library.cache.ImageCache;
import com.chentir.imageloader.library.cache.ImageStore;
import com.chentir.imageloader.library.retry.RetryStrategy;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * <code>
 * ImageLoader imageLoader = ImageLoader.getInstance(context).load(someImageView, "http://link_to_some_image_resource")
 * </code>
 * </pre>
 */
public class ImageLoader implements ImageLoadingCallback, ComponentCallbacks2 {

    /**
     * An {@link ExecutorService} used for submitting {@link DownloadImageTask}s.
     */
    private final ExecutorService workerExecutorService = Executors.newCachedThreadPool();
    private final Map<ImageView, Future> requestHistory = new ConcurrentHashMap<>();

    private final ScheduledExecutorService retryWorkerExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final Map<ImageView, ScheduledFuture> retryRequestHistory = new ConcurrentHashMap<>();

    private static volatile ImageLoader sInstance;


    public synchronized static ImageLoader getInstance(@NonNull Context context) {
        Preconditions.ensureMainThread();
        if (sInstance == null) {
            sInstance = new ImageLoader();
            context.registerComponentCallbacks(sInstance);
        }

        return sInstance;
    }

    /**
     * Asynchronously load an image defined by {@code imageUrl} into {@link ImageView}
     */
    public synchronized void load(@NonNull final ImageView imageView, @NonNull final String imageUrl) {
        Preconditions.ensureMainThread();

        // cancel and clear the previous request made on the same target ImageView
        cancelAndClearRequest(imageView);

        String uuid = UUID.randomUUID().toString();
        imageView.setTag(uuid);

        ImageLoadingRequest imageLoadingRequest = ImageLoadingRequest.createRequest(imageView, imageUrl, uuid);
        ImageStore imageStore = ImageLoaderModule.getInstance().getImageStore();
        Bitmap cachedBitmap = imageStore.fetchImage(imageUrl);

        if (cachedBitmap == null) {
            executeRequest(imageLoadingRequest);
        } else {
            imageView.setImageBitmap(cachedBitmap);
        }
    }

    private synchronized void executeRequest(@NonNull ImageLoadingRequest imageLoadingRequest) {
        DownloadImageTask downloadImageTask = new DownloadImageTask(imageLoadingRequest, this);
        Future<Void> future = workerExecutorService.submit(downloadImageTask);

        ImageView imageView = imageLoadingRequest.getImageView().get();
        if (imageView != null) {
            requestHistory.put(imageView, future);
        }
    }

    /**
     * Cancel the latest pending request made on the {@code imageView}
     */
    public synchronized void cancelRequest(@NonNull ImageView imageView) {
        cancelAndClearRequest(imageView);
    }

    public synchronized void cancelAllRequests() {
        cancelAllPendingRequests();
        cancelAllRetriedRequests();
        ImageLoaderModule.getInstance().getBitmapPool().clear();
    }

    private synchronized void cancelAllPendingRequests() {
        for (Map.Entry<ImageView, Future> entry : requestHistory.entrySet()) {
            Future future = entry.getValue();
            if (!future.isDone()) {
                future.cancel(true);
            }
        }
        requestHistory.clear();
    }

    private synchronized void cancelAllRetriedRequests() {
        for (Map.Entry<ImageView, ScheduledFuture> entry : retryRequestHistory.entrySet()) {
            ScheduledFuture scheduledFuture = entry.getValue();
            if (!scheduledFuture.isDone()) {
                scheduledFuture.cancel(true);
            }
        }
        retryRequestHistory.clear();
    }

    private synchronized void cancelAndClearRequest(@NonNull ImageView imageView) {
        Future future = requestHistory.get(imageView);
        if (future != null) {
            future.cancel(true);
            requestHistory.remove(imageView);
        }

        ScheduledFuture scheduledFuture = retryRequestHistory.get(imageView);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            retryRequestHistory.remove(imageView);
        }
    }

    @Override
    public void onSuccess(@NonNull final ImageLoadingRequest imageLoadingRequest, @NonNull final Bitmap bitmap) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    applyImageOnView(imageLoadingRequest, bitmap);

                    ImageStore imageStore = ImageLoaderModule.getInstance().getImageStore();
                    imageStore.putImage(imageLoadingRequest.getImageUrl(), bitmap);
                }
            }
        });
    }

    private synchronized void applyImageOnView(@NonNull final ImageLoadingRequest imageLoadingRequest,
        @NonNull final Bitmap bitmap) {
        WeakReference<ImageView> imageViewWeakReference = imageLoadingRequest.getImageView();
        ImageView imageView = imageViewWeakReference.get();

        // ensure that imageView is the target view for which loading the image request represented as imageLoadingReqeuest has been made
        if (imageView != null && imageLoadingRequest.getImageKey().equals(imageView.getTag())) {
            requestHistory.remove(imageView);
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onError(@NonNull final ImageLoadingRequest imageLoadingRequest, @NonNull Throwable throwable) {
        // only retry if the target ImageView is still weakly reachable
        if (imageLoadingRequest.getImageView() != null) {
            final RetryStrategy retryStrategy = ImageLoaderModule.getInstance().getRetryStrategy();
            final long nextTime = retryStrategy.calculateNextRetryTimeInMs(imageLoadingRequest);

            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    synchronized (this) {
                        ImageView imageView = imageLoadingRequest.getImageView().get();
                        if (imageView != null && imageLoadingRequest.getImageKey().equals(imageView.getTag())) {

                            requestHistory.remove(imageView);

                            DownloadImageTask downloadImageTask = new DownloadImageTask(imageLoadingRequest, sInstance);
                            ScheduledFuture scheduledFuture =
                                retryWorkerExecutorService.schedule(downloadImageTask, nextTime, TimeUnit.MILLISECONDS);
                            retryRequestHistory.put(imageView, scheduledFuture);
                        }
                    }
                }
            }, nextTime);
        }
    }

    /**
     * Adjust cache capacity based on system resource availabilities
     */
    @Override
    public void onTrimMemory(int i) {
        ImageStore imageStore = ImageLoaderModule.getInstance().getImageStore();

        if (i == TRIM_MEMORY_BACKGROUND || i == TRIM_MEMORY_RUNNING_MODERATE) {
            imageStore.adjustCapacity(ImageCache.SystemResourceAvailability.MEDIUM);
        } else if(i == TRIM_MEMORY_MODERATE || i == TRIM_MEMORY_RUNNING_LOW) {
            imageStore.adjustCapacity(ImageCache.SystemResourceAvailability.LOW);
            // TODO: clear the BitmapPool
        } else if(i == TRIM_MEMORY_COMPLETE || i == TRIM_MEMORY_RUNNING_CRITICAL) {
            imageStore.clearAll();
            // TODO: clear the BitmapPool
        }
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        // no-op
    }

    @Override
    public void onLowMemory() {
        ImageStore imageStore = ImageLoaderModule.getInstance().getImageStore();
        imageStore.clearAll();

        // TODO: clear the BitmapPool
    }
}