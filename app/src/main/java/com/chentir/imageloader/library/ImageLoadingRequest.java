package com.chentir.imageloader.library;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import java.lang.ref.WeakReference;

/**
 * A request consists in loading an image from a URL and into an {@link ImageView}
 */
public class ImageLoadingRequest {
    private final WeakReference<ImageView> imageViewWeakReference;
    private final String imageUrl;
    private final String key;

    private ImageLoadingRequest(@NonNull WeakReference<ImageView> imageViewWeakReference, @NonNull String imageUrl, @NonNull String key) {
        this.imageViewWeakReference = imageViewWeakReference;
        this.imageUrl = imageUrl;
        this.key = key;
    }

    public static ImageLoadingRequest createRequest(@NonNull ImageView imageView, @NonNull String imageUrl, @NonNull String key) {
        Preconditions.checkNotNull(imageView,
            "The image locataed at: " + imageUrl + " cannot be loaded into a null ImageView");

        Preconditions.checkNotNull(imageUrl, "The image cannot be loaded from a null URL");

        return new ImageLoadingRequest(new WeakReference<>(imageView), imageUrl, key);
    }

    @Nullable
    public WeakReference<ImageView> getImageView() {
        return imageViewWeakReference;
    }

    @NonNull
    public String getImageUrl() {
        return imageUrl;
    }

    @NonNull
    public String getImageKey() {
        return key;
    }
}
